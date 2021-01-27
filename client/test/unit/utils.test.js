import common from '../common'

describe('store', () => {
    let store
    beforeEach(() => {
        store = common.store(
            {'user/key1': 'value1'},
            {'user/key2': state => state.key1, 'titles/key1': () => 'value3'},
            {'alerts/key1': () => null},
            {'user/key3': (context, val) => context.state.key1 = val}
        )
    })

    it('has all modules', () => {
        for (let module of ['user', 'titles', 'alerts']) {
            expect(store.hasModule(module)).toBeTruthy()
        }
    })
    it('has state', () => {
        expect(store.state['user']['key1']).toBe('value1')
    })
    it('responds to getters', () => {
        expect(store.getters['user/key2']).toBe('value1')
        expect(store.getters['titles/key1']).toBe('value3')
    })
    it('processes calls', () => {
        expect(() => store.commit('alerts/key1')).not.toThrow()
        expect(() => store.dispatch('user/key3', 'value2')).not.toThrow()
        expect(store.getters['user/key2']).toBe('value2')
    })
})