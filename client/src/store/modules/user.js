import {extractError, processRequest} from '@/service/utils'
import userService from '@/service/api/user'
import {$axios} from '@/service/axios'

const service = userService($axios)

export const userStore = {
    namespaced: true,
    state: {
        login: null
    },
    getters: {
        isLoggedIn(state) {
            return state.login !== null
        }
    },
    mutations: {
        login(state, login) {
            state.login = login
        },
        logout(state) {
            state.login = null
        }
    },
    actions: {
        login(context, formData) {
            return processRequest(
                service.authenticate(formData),
                r => {
                    context.commit('login', r.data.login)
                    localStorage.setItem('accessToken', r.data.accessToken)
                    localStorage.setItem('refreshToken', r.data.refreshToken)
                    return r.data
                },
                e => Promise.reject(extractError(e))
            )
        },
        logout(context) {
            return processRequest(
                service.logout(localStorage.getItem('refreshToken')),
                () => {
                    context.commit('logout')
                    localStorage.removeItem('refreshToken')
                    localStorage.removeItem('accessToken')
                },
                e => Promise.reject(extractError(e))
            )
        }
    }
}