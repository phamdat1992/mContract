const API_URL = `${process.env.REACT_APP_API_URL}`

const fetchMiddleware = (store) => (next) => (action) => {
    if (!action || !action.request) {
        return next(action)
    }

    createFetchRequest(action)
        .then((response) => {
            if (!response.ok && response.status >= 500 && response.statusText) {
                throw Error(response.statusText)
            }
            return response.json()
        })
        .then((data) => {
            store.dispatch({
                type: `${action.type}_OK`,
                meta: action.meta,
                data,
            })
        })
        .catch((error) => {
            store.dispatch({
                type: `${action.type}_ERROR`,
                meta: action.meta,
                error,
            })
        })

    return next(action)
}

const createFetchRequest = (action) => {
    const { url = '/', method = 'GET', headers = {}, params } = action.request
    const body = ['POST', 'PUT'].indexOf(method) >= 0 && params ? JSON.stringify(params) : undefined

    const queryParams = ['GET'].indexOf(method) >= 0 && params ? `?${getEncodedUrlParams(params)}` : ''
    const fetchRequest = fetch(`${getApiUrl(url)}${queryParams}`, {
        method: method,
        headers: {
            'Content-Type': 'application/json; charset=utf-8',
            ...headers,
        },
        mode: 'cors',
        credentials: 'include',
        referrer: 'strict-origin-when-cross-origin',
        body: body,
    })
    return fetchRequest
}

const getApiUrl = (url) => {
    if (/^http[s]*:\/\/.+/.test(url)) {
        return url
    }
    return `${API_URL}/${url}`
}

const getEncodedUrlParams = (params) =>
    Object.keys(params)
        .map((key) =>
            params[key] !== undefined && params[key] !== null
                ? `${encodeURIComponent(key)}=${encodeURIComponent(`${params[key]}`)}`
                : '',
        )
        .filter((param) => param !== '')
        .join('&')

export default fetchMiddleware
