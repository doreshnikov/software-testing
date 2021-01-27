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