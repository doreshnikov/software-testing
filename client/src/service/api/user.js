import {$axios} from '@/service/axios'

const endpoint = 'user'

export default {
    authenticate(user) {
        return $axios.post(`/${endpoint}/authenticate`, user)
    },
    logout(refreshToken) {
        return $axios.post(`/${endpoint}/logout`, {refreshToken: refreshToken})
    },
    refresh(refreshToken) {
        return $axios.post(`/${endpoint}/refresh`, {refreshToken: refreshToken})
    }
}