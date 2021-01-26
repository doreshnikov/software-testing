import Vue from 'vue'

import VueMaterial from 'vue-material'

import 'vue-material/dist/vue-material.min.css'
import 'vue-material/dist/theme/default.css'
import 'vue-material-design-icons/styles.css'

Vue.use(VueMaterial)

import store from '@/store/store'
import router from '@/routes'
import {$axios} from '@/service/axios'

Vue.prototype.$http = $axios
Vue.config.productionTip = false

import App from './components/App.vue'

new Vue({
    render: h => h(App),
    store: store,
    router: router
}).$mount('#app')
