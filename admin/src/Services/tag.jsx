import { DefaultAPI } from "@Utils/axios";
import { TAGS } from "@Enums/endpoints";
class TagService {
    static list = (params) => {
        const url = TAGS.LIST_TAG;
        return DefaultAPI.get(url, { params });
    }
    static add = (data) => {
        const url = TAGS.ADD_TAG;
        return DefaultAPI.post(url, data);
    }
    static edit = ( data) => {
        const url = TAGS.EDIT_TAG;
        return DefaultAPI.put(url, data);
    }
    static delete = (id) => {
        const url = TAGS.DELETE_TAG.replace('{id}', id);
        return DefaultAPI.delete(url);
    }
    static detail = (id) => {
        const url = TAGS.DETAIL_TAG.replace('{id}', id);
        return DefaultAPI.get(url);
    }

    static contractTag = (id) => {
        const url = TAGS.LIST_CONTRACT_TAG.replace('{contractId}', id);
        return DefaultAPI.get(url);
    }

   

}
export default TagService;