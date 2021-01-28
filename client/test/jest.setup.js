import Vue from 'vue'
import Vuex from 'vuex'
import VueRouter from 'vue-router'
import VueMaterial from 'vue-material'
import {$axios} from '@/service/axios'

Vue.use(Vuex)
Vue.use(VueRouter)
Vue.use(VueMaterial)

Vue.prototype.$http = $axios

jest.setTimeout(30000)