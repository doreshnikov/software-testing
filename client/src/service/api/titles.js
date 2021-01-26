import {$axios} from '@/service/axios'

const endpoint = 'titles'

export default {
    loadTitles() {
        return $axios.get(`/${endpoint}`)
    },
    loadTitle(titleId) {
        return $axios.get(`/${endpoint}/${titleId}`)
    },
    loadTitleFrom(server, titleLocator) {
        return $axios.get(`/${endpoint}/${server}/${titleLocator}`)
    },
    addTitle(title) {
        return $axios.put(`/${endpoint}`, title)
    },
    removeTitle(titleId) {
        return $axios.delete(`/${endpoint}/${titleId}`)
    }
}