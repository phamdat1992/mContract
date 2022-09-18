import AuthService from "@Services/auth";
import { get } from '@Utils/cookie';

class RefreshToken {
  request;

  start(){
    this.request = AuthService.refreshToken({ refreshToken: get('digital_signature_refresh_token')});
  }

  end() {
    this.promise = null;
  }

  refreshToken() {
    return new Promise((resolve, reject) => {
      if(!this.request){
        this.start();
      }
      this.request.then(res => {
        resolve(res);
      }).catch(err => {
        reject(err);
      })
    }); 
  }
}

export default new RefreshToken();