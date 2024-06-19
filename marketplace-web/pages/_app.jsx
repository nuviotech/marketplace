import React, { useEffect } from 'react';
import { wrapper } from '~/store/store';
import { CookiesProvider } from 'react-cookie';
import MasterLayout from '~/components/layouts/MasterLayout';
import '~/public/static/fonts/Linearicons/Font/demo-files/demo.css';
//import '~/public/static/fonts/font-awesome/css/font-awesome.min.css';
import '~/public/static/css/bootstrap.min.css';
import '~/public/static/css/slick.min.css';
import '~/public/static/css/style.css'
import '~/scss/style.scss';
import '~/scss/home-default.scss';
import '~/scss/market-place-1.scss';
import '~/scss/market-place-2.scss';
import '~/scss/market-place-3.scss';
import '~/scss/market-place-4.scss';
import '~/scss/electronic.scss';
import '~/scss/furniture.scss';
import '~/scss/organic.scss';
import '~/scss/technology.scss';
import '~/scss/autopart.scss';
import '~/scss/electronic.scss';
import Head from 'next/head';
import { AuthContext, AuthContextProvider } from '~/context/loginContext';
import { useContext } from 'react';
import SEO from "@bradgarropy/next-seo"

function App({ Component, pageProps }) {
    useEffect(() => {
        setTimeout(function () {
            document.getElementById('__next').classList.add('loaded');
        }, 100);
    });


    return (
        <>
            <AuthContextProvider>
                {/*<Head>
                    <title>Nuvio Sellers</title>
                    <meta httpEquiv="X-UA-Compatible" content="IE=edge" />
                    <meta
                        name="viewport"
                        content="width=device-width, initial-scale=1.0"
                    />
                    <meta name="format-detection" content="telephone=no" />
                    <meta name="apple-mobile-web-app-capable" content="yes" />
                   {/* <meta name="author" content="nuvio" />
                    <meta
                        name="keywords"
                        content="Nuvio,nuvio sellers , eCommerce, Template"
                    />
                    <meta
                        name="description"
                        content="Nuvio sellers | eCommerce"
                    />
    </Head>*/}

                <Head>

                    <meta httpEquiv="X-UA-Compatible" content="IE=edge" />
                    <meta name="author" content="nuvio" />
                    <meta
                        name="viewport"
                        content="width=device-width, initial-scale=1.0"
                    />
                    <meta name="format-detection" content="telephone=no" />
                    <meta name="apple-mobile-web-app-capable" content="yes" />
                </Head>
                <SEO title="Nuvio Sellers" description="Nuvio sellers | eCommerce" keywords={["Nuvio", "nuvio sellers", "ecommerce"]} />
                <CookiesProvider>
                    <MasterLayout>

                        <Component {...pageProps} />
                    </MasterLayout>
                </CookiesProvider>
            </AuthContextProvider>

            <script src="https://smtpjs.com/v3/smtp.js"></script>

            <link rel='preload' as="script" href="https://www.googletagmanager.com/gtag/js?id=G-B7SC7YX3EL"></link>
            <script>
                window.dataLayer = window.dataLayer || [];
                {function gtag() {
                    dataLayer.push(arguments);
                }}

                gtag('js', new Date());

                gtag('config', 'G-B7SC7YX3EL');
            </script>
        </>
    );
}

export default wrapper.withRedux(App);
