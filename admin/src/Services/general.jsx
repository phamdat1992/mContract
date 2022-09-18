import { DefaultAPI } from "@Utils/axios";
import { COMMON, FILE } from "@Enums/endpoints";
class GeneralService {
    static getProvinces = (params) => {
        const url = COMMON.PROVINCE_LIST;
        return DefaultAPI.get(url, { params });
    }
    static getDistricts = (id) => {
        const url = COMMON.PROVINCE_DISTRICT.replace('{id}', id);
        return DefaultAPI.get(encodeURI(url));
    }
    static getWards = (id) => {
        const url = COMMON.DISTRICT_WARD.replace('{id}', id);
        return DefaultAPI.get(url);
    }
    static uploadFile = (data) => {
        const url = FILE.UPLOAD_FILE;
        return DefaultAPI.post(url, data);
    }
    static getLetterName = (name) => {
        if(name) {
            const arr = name.split(' ');
            return ((arr[arr.length - 1]).slice(0,1))
        } 
        return null;
    }
}
export default GeneralService;