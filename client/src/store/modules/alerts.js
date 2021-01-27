export const alertsStore = {
    namespaced: true,
    state: {
        fetchError: null,
        accessError: null
    },
    getters: {
        accessErrorView(state) {
            return state.accessError.view
        }
    },
    mutations: {
        setFetchError(state, error) {
            state.fetchError = error
        },
        setAccessError(state, error) {
            state.accessError = error
        }
    },
}