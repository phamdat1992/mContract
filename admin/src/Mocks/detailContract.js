const DETAIL_CONTRACT = {
    id: 1,
    createdTime: null,
    expirationTime: null,
    title: '',
    content: '',
    fileName: '',
    filePath: '',
    userCreate: {},
    signerDtos: [
        {
            id: null,
            fullName: '',
            email: '',
            taxCode: '',
            companyName: '',
            status: null,
            signedTime: null,
            x: null,
            y: null,
            page: null,

        }
    ],
    tagDtos: [
        {
            id: null,
            name: '',
            colorCode: ''
        }
    ],
    commentDtos: [
        {
            id: null,
            createdTime: null,
            content: '',
            user: null,
            signer: null,
            childCommentDtos: []

        }
    ]

}
export default DETAIL_CONTRACT;