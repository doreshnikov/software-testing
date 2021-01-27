import Vuex from 'vuex'

export default {

    testTitle: {
        name: 'test name',
        description: 'test description',
        chapters: 100,
        image: 'http://test.image'
    },

    store(state = {}, getters = {}, mutations = {}, actions = {}) {
        const keys = new Set()
        for (let t of [state, getters, mutations, actions]) {
            for (let k of Object.keys(t)) {
                keys.add(k.split('/')[0])
            }
        }

        function transform(data, k) {
            const res = {}
            Object.entries(data)
                .filter(e => e[0].startsWith(k))
                .forEach(e => res[e[0].split('/')[1]] = e[1])
            return res
        }

        const modules = {}
        Array.from(keys).forEach(k => modules[k] = {
            namespaced: true,
            state: transform(state, k),
            getters: transform(getters, k),
            mutations: transform(mutations, k),
            actions: transform(actions, k)
        })
        return new Vuex.Store({modules: modules})
    }

}