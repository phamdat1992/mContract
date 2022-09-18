import { ToolAPI } from '@Utils/axios';
class ToolUsb {
  static getToken = () => {
    const url = '/get-token';
    return ToolAPI.get(url);
  };

  static getCheckUsb = token => {
    const url = '/api/check-usb';
    return ToolAPI.get(url, {
      headers: {
        Authorization: `${token}`,
      },
    });
  };

  static getCertificate = token => {
    const url = '/api/get-certificate';
    return ToolAPI.get(url, {
      headers: {
        Authorization: `${token}`,
      },
    });
  };

  static signContract = (token, data) => {
    const url = `/api/sign`;
    return ToolAPI.post(url, data, {
      headers: {
        Authorization: `${token}`,
      },
    });
  };
  static getDataToSign = token => {
    const url = '/api/data-to-sign';
    return ToolAPI.get(url, {
      headers: {
        Authorization: `${token}`,
      },
    });
  }
}
export default ToolUsb;
