import React, { Component } from 'react'
import PropTypes from 'prop-types'

/**
 * Utils
 */
const removeElementByIds = ((ids: any) => {
    ids.forEach((id: any) => {
        const element = document.getElementById(id)
        if (element && element.parentNode) {
            element.parentNode.removeChild(element)
        }
    })
})

/**
 * Messenger Chat Plugin
 */
class MessengerChat extends Component {
    static propTypes = {
        pageId: PropTypes.string.isRequired,
        // appId:                    PropTypes.string.isRequired,
        shouldShowDialog: PropTypes.bool,
        htmlRef: PropTypes.string,
        minimized: PropTypes.bool,
        themeColor: PropTypes.string,
        loggedInGreeting: PropTypes.string,
        loggedOutGreeting: PropTypes.string,
        greetingDialogDisplay: PropTypes.oneOf(['show', 'hide', 'fade']),
        greetingDialogDelay: PropTypes.number,
        autoLogAppEvents: PropTypes.bool,
        xfbml: PropTypes.bool,
        version: PropTypes.string,
        // language:                 PropTypes.string,
        onCustomerChatDialogShow: PropTypes.func,
        onCustomerChatDialogHide: PropTypes.func,
    }

    static defaultProps = {
        shouldShowDialog: false,
        htmlRef: undefined,
        minimized: undefined,
        themeColor: undefined,
        loggedInGreeting: undefined,
        loggedOutGreeting: undefined,
        greetingDialogDisplay: undefined,
        greetingDialogDelay: undefined,
        autoLogAppEvents: true,
        xfbml: true,
        version: '4.0',
        onCustomerChatDialogShow: undefined,
        onCustomerChatDialogHide: undefined,
    }

    constructor(props: any) {
        super(props)

        this.setFbAsyncInit = this.setFbAsyncInit.bind(this)
        this.reloadSDKAsynchronously = this.reloadSDKAsynchronously.bind(this)
        this.loadSDKAsynchronously = this.loadSDKAsynchronously.bind(this)
        this.createMarkup = this.createMarkup.bind(this)
        this.controlPlugin = this.controlPlugin.bind(this)
        this.subscribeEvents = this.subscribeEvents.bind(this)
        this.removeFacebookSDK = this.removeFacebookSDK.bind(this)

        this.state = {
            fbLoaded: false,
            shouldShowDialog: undefined,
        }
    }

    componentDidMount() {
        this.setFbAsyncInit()
        this.reloadSDKAsynchronously()
    }

    componentDidUpdate(prevProps: any) {
        if (
            prevProps.pageId !== (this.props as any).pageId ||
            // prevProps.appId !== this.props.appId ||
            prevProps.shouldShowDialog !== (this.props as any).shouldShowDialog ||
            prevProps.htmlRef !== (this.props as any).htmlRef ||
            prevProps.minimized !== (this.props as any).minimized ||
            prevProps.themeColor !== (this.props as any).themeColor ||
            prevProps.loggedInGreeting !== (this.props as any).loggedInGreeting ||
            prevProps.loggedOutGreeting !== (this.props as any).loggedOutGreeting ||
            prevProps.greetingDialogDisplay !== (this.props as any).greetingDialogDisplay ||
            prevProps.greetingDialogDelay !== (this.props as any).greetingDialogDelay ||
            prevProps.autoLogAppEvents !== (this.props as any).autoLogAppEvents ||
            prevProps.xfbml !== (this.props as any).xfbml ||
            prevProps.version !== (this.props as any).version ||
            prevProps.language !== (this.props as any).language
        ) {
            this.setFbAsyncInit()
            this.reloadSDKAsynchronously()
        }
    }

    setFbAsyncInit() {
        const { autoLogAppEvents, xfbml, version } = this.props as any

        (window as any).fbAsyncInit = () => {
            (window as any).FB.init({
                // appId,
                autoLogAppEvents,
                xfbml,
                version: `v${version}`,
            })

            this.setState({ fbLoaded: true })
        }
    }

    loadSDKAsynchronously() {
        // const { language } = this.props
        /* eslint-disable */
        (function (d, s, id) {
            var js,
                fjs = d.getElementsByTagName(s)[0]
            if (d.getElementById(id)) {
                return;
            }
            js = d.createElement(s);
            js.id = id;
            (js as any).src = `https://connect.facebook.net/en_GB/sdk/xfbml.customerchat.js`;
            (fjs as any).parentNode.insertBefore(js, fjs)
        })(document, 'script', 'facebook-jssdk')
        /* eslint-enable */
    }

    removeFacebookSDK() {
        removeElementByIds(['facebook-jssdk', 'fb-root'])
        delete (window as any).FB
    }

    reloadSDKAsynchronously() {
        this.removeFacebookSDK()
        this.loadSDKAsynchronously()
    }

    controlPlugin() {
        const { shouldShowDialog } = this.props as any

        if (shouldShowDialog) {
            (window as any).FB.CustomerChat.showDialog()
        } else {
            (window as any).FB.CustomerChat.hideDialog()
        }
    }

    subscribeEvents() {
        const { onCustomerChatDialogShow, onCustomerChatDialogHide } = this.props as any;

        if (onCustomerChatDialogShow) {
            (window as any).FB.Event.subscribe(
                'customerchat.dialogShow',
                onCustomerChatDialogShow
            )
        }

        if (onCustomerChatDialogHide) {
            (window as any).FB.Event.subscribe(
                'customerchat.dialogHide',
                onCustomerChatDialogHide
            )
        }
    }

    createMarkup() {
        const {
            pageId,
            htmlRef,
            minimized,
            themeColor,
            loggedInGreeting,
            loggedOutGreeting,
            greetingDialogDisplay,
            greetingDialogDelay,
        } = this.props as any

        const refAttribute = htmlRef !== undefined ? `ref="${htmlRef}"` : ''

        const minimizedAttribute =
            minimized !== undefined ? `minimized="${minimized}"` : ''

        const themeColorAttribute =
            themeColor !== undefined ? `theme_color="${themeColor}"` : ''

        const loggedInGreetingAttribute =
            loggedInGreeting !== undefined
                ? `logged_in_greeting="${loggedInGreeting}"`
                : ''

        const loggedOutGreetingAttribute =
            loggedOutGreeting !== undefined
                ? `logged_out_greeting="${loggedOutGreeting}"`
                : ''

        const greetingDialogDisplayAttribute =
            greetingDialogDisplay !== undefined
                ? `greeting_dialog_display="${greetingDialogDisplay}"`
                : ''

        const greetingDialogDelayAttribute =
            greetingDialogDelay !== undefined
                ? `greeting_dialog_delay="${greetingDialogDelay}"`
                : ''

        return {
            __html: `<div
             class="fb-customerchat"
             page_id="${pageId}"
             ${refAttribute}
             ${minimizedAttribute}
             ${themeColorAttribute}
             ${loggedInGreetingAttribute}
             ${loggedOutGreetingAttribute}
             ${greetingDialogDisplayAttribute}
             ${greetingDialogDelayAttribute}
           ></div>`,
        }
    }

    render() {
        const { fbLoaded, shouldShowDialog } = this.state as any

        if (fbLoaded && shouldShowDialog !== (this.props as any).shouldShowDialog) {
            document.addEventListener(
                'DOMNodeInserted',
                event => {
                    const element = event.target as any
                    if (
                        element.className &&
                        typeof element.className === 'string' &&
                        element.className.includes('fb_dialog')
                    ) {
                        this.controlPlugin()
                    }
                },
                false
            );
            this.subscribeEvents()
        }
        // Add a random key to rerender. Reference:
        // https://stackoverflow.com/questions/30242530/dangerouslysetinnerhtml-doesnt-update-during-render
        return <div key={Date()} dangerouslySetInnerHTML={this.createMarkup()} />
    }
}

export default MessengerChat