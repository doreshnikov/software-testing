import Vuex from 'vuex'

import {alertsStore} from '@/store/modules/alerts'
import {titlesStore} from '@/store/modules/titles'
import {userStore} from '@/store/modules/user'
import {$axios} from '@/service/axios'
import store from '@/store/store'

jest.mock('../../src/service/axios')

describe('alerts store', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store({modules: {alerts: alertsStore}})
    })
    afterEach(() => {
        alertsStore.state.fetchError = null
        alertsStore.state.accessError = null
    })

    it('responds to fetch error set', () => {
        expect(() => store.commit('alerts/setFetchError', 'errormessage')).not.toThrow()
        expect(store.state.alerts['fetchError']).toBe('errormessage')
    })
    it('returns correct access error view', () => {
        expect(() => store.commit('alerts/setAccessError', {'error': null, 'view': 'errorview'})).not.toThrow()
        expect(store.getters['alerts/accessErrorView']).toBe('errorview')
    })
})

describe('titles store', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store({modules: {titles: titlesStore}})
    })
    afterEach(() => {
        titlesStore.state.titles = []
    })

    it('responds to titles set', () => {
        expect(() => store.commit('titles/setTitles', [{}, {}])).not.toThrow()
        expect(store.state.titles['titles'].length).toBe(2)
    })
    it('responds to title added', () => {
        expect(() => store.commit('titles/addTitle', {id: 1})).not.toThrow()
        expect(store.state.titles['titles'][0].id).toBe(1)
    })
    it('responds to title removed', () => {
        store.commit('titles/setTitles', [{id: 1}, {id: 2}])
        expect(() => store.commit('titles/removeTitle', 1)).not.toThrow()
        expect(store.state.titles['titles'].length).toBe(1)
        expect(store.state.titles['titles'][0].id).toBe(2)
    })
})

describe('user store', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store({modules: {user: userStore}})
    })
    afterEach(() => {
        alertsStore.state.login = null
    })

    it('responds to login set/unset', () => {
        expect(() => store.commit('user/login', 'doreshnikov')).not.toThrow()
        expect(store.state.user['login']).toBe('doreshnikov')
        expect(() => store.commit('user/logout')).not.toThrow()
        expect(store.state.user['login']).toBeNull()
    })
    it('returns correct status', () => {
        expect(store.getters['user/isLoggedIn']).toBeFalsy()
        store.commit('user/login', '')
        expect(store.getters['user/isLoggedIn']).toBeTruthy()
    })
})

describe('integrity', () => {
    const localStore = store
    afterEach(() => {
        localStore.commit('titles/setTitles', [])
        localStore.commit('user/logout')
        localStore.commit('alerts/setFetchError', null)
    })

    describe('titles store (actions)', () => {
        it('commits new title on add', async () => {
            $axios.put.mockImplementation(() => Promise.resolve({data: {id: 1}}))
            await expect(localStore.dispatch('titles/addTitle', {}))
                .resolves.toEqual(1)
            expect(localStore.state.titles['titles'][0].id).toBe(1)
        })
        it('ignores new title on fail', async () => {
            $axios.put.mockImplementation(() => Promise.reject({toString: () => 'errormessage'}))
            await expect(localStore.dispatch('titles/addTitle', {}))
                .rejects.toEqual({error: 'errormessage'})
            expect(localStore.state.titles['titles'].length).toBe(0)
        })
    })
    describe('user store (actions)', () => {
        it('saves to local storage on auth', async () => {
            $axios.post.mockImplementation(() => Promise.resolve({
                data: {login: 'y', accessToken: 'a', refreshToken: 'b'}
            }))
            await expect(localStore.dispatch('user/login', {login: 'x'}))
                .resolves.toHaveProperty('accessToken')
            expect(localStore.state.user['login']).toBe('y')
            expect(localStorage.getItem('accessToken')).toBe('a')
            expect(localStorage.getItem('refreshToken')).toBe('b')
        })
        it('keeps tokens on logout fail', async () => {
            $axios.post.mockImplementation(() => Promise.reject({toString: () => 'errormessage'}))
            localStorage.setItem('accessToken', 't')
            await expect(localStore.dispatch('user/logout'))
                .rejects.toEqual({error: 'errormessage'})
            expect(localStorage.getItem('accessToken')).toBe('t')
        })
    })
})