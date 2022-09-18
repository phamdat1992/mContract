declare var io: any;

const BASE_URL = 'https://mcontract.vn/notify';
// const BASE_URL = 'wss://digital-signature.api.demo-amit.com/notify';
const NOTIFICATION_EVENT = 'notify';

// interface EventData {
//   contractId: number;
//   contractName: number;
//   type: string; // Phân biệt các loại notification
//   detail: any; // Dữ liệu tùy theo type
// }

class Socket {
  connection: any;

  startConnect(token: string, callback = () => {}) {
    if (this.connection && this.connection.connected) {
      callback();
    } else {
      this.connect(token, callback);
    }
  }

  connect(token: string, callback = () => {}) {
    this.connection = null;
    const url = `${BASE_URL}?token=${token}`;
    this.connection = io(url, {
      transports: ['websocket'],
      reconnection: true,
      reconnectionAttempts: Infinity,
      reconnectionDelay: 100,
      reconnectionDelayMax: 500
    });
    this.connection.on('connect', () => {
      callback();
    });
  }

  onTimeout = (callback: (data: any) => void) => {
    this.connection.on('timeout', (e: any) => {
      callback(e);
    });
  };

  onNotify = (callback: (data: any) => void) => {
    this.connection.on(NOTIFICATION_EVENT, (e: any) => {
      if (e.type) {
        callback(e);
      }
    });
  };

  offNotify = () => {
    this.connection.off(NOTIFICATION_EVENT, () => {});
  };
}

export default new Socket();
