import axios from 'axios'
import store from '@/store/store'
import userService from '@/service/api/user'

axios.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded'
axios.defaults.headers.common['Access-Control-Allow-Origin'] = '*'

export const $axios = axios.create({
    baseURL: 'http://localhost:3000'
})

export function processRequest(request, onResult, onError) {
    return request.then(r => {
        return onResult(r)
    }).catch(e => {
        return onError(e)
    })
}

export function extractError(error) {
    return error.response ? error.response.data : {error: error.toString()}
}

$axios.interceptors.request.use(
    r => {
        let accessToken = localStorage.getItem('accessToken')
        if (accessToken) {
            r.headers['Authorization'] = `Bearer ${accessToken}`
        }
        return r
    },
    e => Promise.reject(e)
)

$axios.interceptors.response.use(
    r => r,
    async e => {
        if (e.response) {
            const refreshToken = localStorage.getItem('refreshToken')
            switch (e.response.status) {
                case 401:
                    if (e.response.config.url.endsWith('/refresh')) {
                        localStorage.removeItem('accessToken')
                        localStorage.removeItem('refreshToken')
                        store.commit('alerts/setAccessError', {
                            error: e.response.data.error,
                            view: 'Token refresh failed, please re-login manually'
                        })
                        store.commit('user/logout')
                    }
                    break
                case 403:
                    if (refreshToken) {
                        await userService.refresh(refreshToken).then(r => {
                            localStorage.setItem('accessToken', r.data.accessToken)
                            localStorage.setItem('refreshToken', r.data.refreshToken)
                        })
                        return new Promise((resolve, reject) =>
                            $axios.request(e.config).then(resolve).catch(reject)
                        )
                    }
                    store.commit('alerts/setAccessError', e.response.data)
                    store.commit('user/logout')
                    break
                default:
                    break
            }
        }
        return Promise.reject(e)
    }
)