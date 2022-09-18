importScripts('https://www.gstatic.com/firebasejs/7.14.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.14.2/firebase-messaging.js');

const config = {
  apiKey: 'AIzaSyBNHEzJV3xMH3AfUytHtPKsIX7yyazllx0',
  authDomain: 'digital-signature-a7a17.firebaseapp.com',
  projectId: 'digital-signature-a7a17',
  storageBucket: 'digital-signature-a7a17.appspot.com',
  messagingSenderId: '983735603663',
  databaseUrl: 'https://digital-signature-a7a17.firebaseio.com',
  appId: '1:983735603663:web:4616a4c0b50c4d615942ba',
  measurementId: 'G-LDPFS1LM90',
};

firebase.initializeApp(config);
const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function (payload) {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);
  const notificationTitle = payload.data.title;
  const notificationOptions = {
    body: payload.data.body,
    icon: '/firebase-logo.png',
  };
  return self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener('notificationclick', event => {
  console.log(event);
  return event;
});
