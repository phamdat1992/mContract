import {MENU_ACTION} from '@Consts/action';
import {MOCK_MENU} from '@Mocks/menu';
function load(state){
    return {
        ...state,
        menu: MOCK_MENU,
    }
}
const initialState = {
    menu: MOCK_MENU,
}
function MenuReducer(state = initialState, action) {
    switch (action.type) {
        case MENU_ACTION.LOAD:
            return load(state);
    
        default:
            return state;
    }
}
export default MenuReducer;