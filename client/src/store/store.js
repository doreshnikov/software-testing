import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

import {alertsStore} from '@/store/modules/alerts'
import {titlesStore} from '@/store/modules/titles'
import {userStore} from '@/store/modules/user'

export default new Vuex.Store({
    modules: {
        alerts: alertsStore,
        titles: titlesStore,
        user: userStore
    },
    plugins: [
        createPersistedState({paths: ['user', 'titles']})
    ]
})