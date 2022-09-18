const BASE_URL = 'https://mcontract.vn/api/otps/subscribe';
const NOTIFICATION_EVENT = 'notify';

interface EventData {
  contractId: number;
  contractName: number;
  type: string; // Phân biệt các loại notification 
  detail: any; // Dữ liệu tùy theo type
}

class Event {
  connection: EventSource;
  constructor(token: string) {
    this.connection = new EventSource(`${BASE_URL}/${token}`);
  }

  onConnect = (callback: (data: any) => void) => {
    this.connection.onopen = callback;
  }

  onNotify = (callback: (data: any) => void) => {
    this.connection.addEventListener(NOTIFICATION_EVENT, (e: any) => {
      const data: EventData = JSON.parse(e.data);
      callback(data);
    });
  }

}

export { Event };

