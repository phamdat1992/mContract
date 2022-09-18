import { DefaultAPI } from "@Utils/axios";
import { AUTH, COMPANY } from "@Enums/endpoints";
class ProfileService {
    static getUser = () => { 
        const url = AUTH.GET_USER;
        return DefaultAPI.get(url);
    }
    static updateUser = (data) => { 
        const url = AUTH.UPDATE_USER;
        return DefaultAPI.put(url, data);
    }
    static getCompany = () => {
        const url = COMPANY.GET;
        return DefaultAPI.get(url);
    }
    static updateCompany = (data) => {
        const url = COMPANY.UPDATE;
        return DefaultAPI.put(url, data);
    }
}
export default ProfileService;