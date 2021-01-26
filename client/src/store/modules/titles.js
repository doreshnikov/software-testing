import {extractError, processRequest} from '@/service/axios'
import service from '@/service/api/titles'

export const titlesStore = {
    namespaced: true,
    state: {
        titles: []
    },
    mutations: {
        setTitles(state, titles) {
            state.titles = titles
        },
        removeTitle(state, titleId) {
            state.titles.splice(
                state.titles.findIndex(t => t.id === titleId),
                1
            )
        },
        addTitle(state, title) {
            state.titles.push(title)
        }
    },
    actions: {
        loadTitles(context) {
            return processRequest(
                service.loadTitles(),
                r => {
                    context.commit('setTitles', r.data)
                    context.commit('alerts/setFetchError', null, {root: true})
                },
                e => {
                    let error = extractError(e)
                    context.commit('alerts/setFetchError', error, {root: true})
                    return Promise.reject(error)
                }
            )
        },
        removeTitle(context, titleId) {
            return processRequest(
                service.removeTitle(titleId),
                () => {
                    context.commit('removeTitle', titleId)
                },
                e => {
                    let error = extractError(e)
                    console.log(`Error on 'removeTitle': ${error.error}`)
                    return Promise.reject(error)
                }
            )
        },
        addTitle(context, title) {
            return processRequest(
                service.addTitle(title),
                r => {
                    title.id = r.data.id
                    context.commit('addTitle', title)
                },
                e => {
                    let error = extractError(e)
                    console.log(`Error on 'addTitle': ${error.error}`)
                    return Promise.reject(error)
                }
            )
        }
    }
}