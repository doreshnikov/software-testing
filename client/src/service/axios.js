import axios from 'axios'

axios.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded'
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*'

export const $axios = axios.create({
    baseURL: 'http://localhost:3000'
})
