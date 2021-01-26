export const alertsStore = {
    namespaced: true,
    state: {
        fetchError: null,
        accessError: null
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