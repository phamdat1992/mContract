import "bootstrap/dist/css/bootstrap.min.css";
import "../styles/globals.scss";
import type { AppProps } from "next/app";
import { Provider } from "react-redux";
import { store } from "@/redux/store";
import withRedux from "next-redux-wrapper";
import MessengerChat from "@/components/MessengerChat/index";
import SimpleBar from "simplebar-react";
import * as AOS from 'aos';
import React, { Ref, useEffect } from "react";
import Head from "next/head";
import { useRouter } from "next/router";
import NProgress from "nprogress";
import Router from "next/router";
function MyApp({ Component, pageProps }: AppProps) {
   const ref: Ref<any> = React.createRef();
   const router = useRouter();
   const domain = "https://mcontract.vn";
   const thumbnail = 'https://mcontract.vn/uploads/Image-thumb-website.png';

   useEffect(() => {
      ref.current.scrollTo(0, 0);
      // router.pathname
   }, [router]);

   function onScroll() {
      AOS.refresh();
   }

   useEffect(() => {
      const handleStart = (url: string) => {
         NProgress.start()
      }
      const handleStop = () => {
         NProgress.done()
      }

      router.events.on('routeChangeStart', handleStart)
      router.events.on('routeChangeComplete', handleStop)
      router.events.on('routeChangeError', handleStop)

      return () => {
         router.events.off('routeChangeStart', handleStart)
         router.events.off('routeChangeComplete', handleStop)
         router.events.off('routeChangeError', handleStop)
      }
   }, [router]);

   const handleRouteChange = (url: any) => {
      window.gtag('config', 'G-5SJT33B64K', {
         page_path: url,
      });
   };

   useEffect(() => {
      router.events.on('routeChangeComplete', handleRouteChange);
      return () => {
         router.events.off('routeChangeComplete', handleRouteChange);
      };
   }, [router.events]);

   useEffect(() => {
      ref.current.addEventListener('scroll', () => {
         onScroll();
      });
   }, []);
   return (
      <Provider store={store}>
         <Head>
            <link rel="icon" href="/favicon.ico" />
            <base href="/" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <meta name="keywords" content="mcontract, chữ ký số" />
            <meta name="description"
               content="MContract là giải pháp ký số hàng đầu cho doanh nghiệp. Ký số không giấy tờ - Ký mọi nơi không gián đoạn - Không gặp mặt, không phát sinh chi phí" />
            <meta name="subject" content="Ký số không giấy tờ - Ký mọi nơi không gián đoạn - Không gặp mặt, không phát sinh chi phí " />
            <meta name="language" content="EN" />
            <meta name="robots" content="index,follow" />
            <meta name="Classification" content="" />
            <meta name="author" content="" />
            <meta name="copyright" content="MContract" />
            <meta name="reply-to" content="" />
            <meta name="url" content={domain} />
            <meta name="identifier-URL" content={domain} />
            {/* <!-- OpenGraph Meta Tags --> */}
            <meta property="og:title" content="MContract" />
            <meta property="og:type" content="Article" />
            <meta property="og:url" content={domain} />
            <meta property="og:image" content={`${thumbnail}`} />
            <meta property="og:site_name" content="MContract" />
            <meta property="fb:app_id" content="293230826077455" />
            <meta property="og:description"
               content="MContract là giải pháp ký số hàng đầu cho doanh nghiệp. Ký số không giấy tờ - Ký mọi nơi không gián đoạn - Không gặp mặt, không phát sinh chi phí" />
            <script async src="https://www.googletagmanager.com/gtag/js?id=G-5SJT33B64K"></script>
            <script
               dangerouslySetInnerHTML={{
                  __html: `
              window.dataLayer = window.dataLayer || [];
              function gtag(){dataLayer.push(arguments);}
              gtag('js', new Date());
              gtag('config', 'G-5SJT33B64K', { page_path: window.location.pathname });
            `,
               }}
            />
         </Head>
         <SimpleBar style={{ height: '100vh' }} scrollableNodeProps={{ ref: ref }}>
            <Component {...pageProps} />
         </SimpleBar>
         <MessengerChat pageId="101821908954037" htmlRef="fb-msgr" />
      </Provider>
   );
}

const getInitialProps = async ({ Component, ctx }: any) => {
   const pageProps = Component.getInitialProps ? await Component.getInitialProps(ctx) : {};
   //Anything returned here can be accessed by the client
   return { pageProps: pageProps };
};
const makeStore = () => store;
export { getInitialProps };
export default withRedux(makeStore)(MyApp);
