const endpoint = 'user'

export default function ($axios) {
    return {
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
}