const MOCK_MENU = [
    {
        id: 0,
        title: 'Tất cả hợp đồng',
        icon: 'DOCUMENTS',
        status: "TOTAL",
        path: '/tat-ca-hop-dong'
    },
    {
        id: 1,
        title: 'Chờ xử lí',
        icon: 'CLOCK',
        status: "PROCESSING",
        path: '/cho-xu-ly'
    },
    {
        id: 2,
        title: 'Chờ đối tác',
        icon: 'HOURCLASS',
        status: "WAITINGFORPARTNER",
        path: '/cho-doi-tac'
    },
    {
        id: 3,
        title: 'Sai xác thực',
        icon: 'FAIL_SHIELD',
        status: "AUTHENTICATIONFAIL",
        path: '/sai-xac-thuc'
    },
    {
        id: 4,
        title: 'Hoàn thành',
        icon: 'CHECK_DOCUMENT',
        status: "COMPLETE",
        path: '/hoan-thanh'
    },
    {
        id: 5,
        title: 'Hết hạn',
        icon: 'FAIL_DOCUMENT',
        status: "EXPIRED",
        path: '/het-han'
    },
    {
        id: 6,
        title: 'Hủy bỏ',
        icon: 'CANCEL_DOCUMENT',
        status: "CANCEL",
        path: '/huy-bo'
    }
];

export {MOCK_MENU};