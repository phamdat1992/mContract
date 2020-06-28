import {applyMiddleware, compose, createStore} from 'redux'
import thunk from 'redux-thunk'

import reducers from './reducers'
import fetchMidleware from '~/middlewares/fetch'

const enhancer = compose(applyMiddleware(thunk, fetchMidleware))

export default createStore(reducers, enhancer)
