import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import ProductRepository from '~/repositories/ProductRepository';
import SkeletonProductDetail from '~/components/elements/skeletons/SkeletonProductDetail';
import BreadCrumb from '~/components/elements/BreadCrumb';
import ProductWidgets from '~/components/partials/product/ProductWidgets';
import ProductDetailFullwidth from '~/components/elements/detail/ProductDetailFullwidth';
import CustomerBought from '~/components/partials/product/CustomerBought';
import RelatedProduct from '~/components/partials/product/RelatedProduct';
import HeaderProduct from '~/components/shared/headers/HeaderProduct';
import HeaderDefault from '~/components/shared/headers/HeaderDefault';
import PageContainer from '~/components/layouts/PageContainer';
import Newletters from '~/components/partials/commons/Newletters';
import HeaderMobileProduct from '~/components/shared/header-mobile/HeaderMobileProduct';
import SEO from "@bradgarropy/next-seo"
import axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';
import ResponseCache from 'next/dist/server/response-cache';
import Head from 'next/head';

const ProductDefaultPage = ({ responseData }) => {

    const router = useRouter();
    const { pid } = router.query;

    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(false);

    const title = responseData?.title;
    const keyWords = [];
    responseData?.keywords?.split(",").map((item) => keyWords.push(item))
    //console.warn(pid);
    // const description = responseData?.title + " | price : " + responseData?.sale_price;
    const description = "price : " + responseData?.sale_price;

    async function getProduct() {
        setLoading(true);
        //const responseData = await ProductRepository.getProductsById(pid);
        if (responseData) {
            setProduct(responseData);
            setTimeout(
                function () {
                    setLoading(false);
                }.bind(this),
                250
            );
        }
    }

    useEffect(() => {
        getProduct();
    }, [responseData]);

    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Shop',
            url: '/shop',
        },
        {
            text: product ? product.title : 'Loading...',
        },
    ];
    // Views
    // alert(pid)
    let productView, headerView;
    if (!loading) {
        if (product) {
            productView = <ProductDetailFullwidth pid={pid} product={product} />;
            headerView = (
                <>
                    <HeaderProduct product={product} />
                    <HeaderMobileProduct />
                </>
            );
        } else {
            headerView = (
                <>
                    <HeaderDefault />
                    <HeaderMobileProduct />
                </>
            );
        }
    } else {
        productView = <SkeletonProductDetail />;
    }

    return (
        <>
            <Head>
                <link rel="shortcut icon" href={responseData?.images[0].url} />
                <link
                    rel="icon"
                    href={responseData?.images[0].url}
                    sizes="192x192"
                />
                <link
                    rel="apple-touch-icon-precomposed"
                    href={responseData?.images[0].url}
                />
            </Head>
            <SEO
                title={title}
                description={description}
                keywords={keyWords}
                icon={responseData?.images[0].url}
                facebook={{
                    image: responseData?.images[0].url,
                    url: `https://nuvio.in/product/${pid}`,
                    type: "website",
                }}
                twitter={{
                    image: responseData?.images[0].url,
                    site: "nuvio.in",
                    card: "summary",
                }}

            />

            <PageContainer
                header={headerView}
                title={responseData ? responseData.title : 'Loading...'}>
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />

                <div className="ps-page--product">
                    <div className="ps-container">
                        <div className="ps-page__container">
                            <div className="ps-page__left">{productView}</div>
                            <div className="ps-page__right">
                                <ProductWidgets brand={product?.brand} />
                            </div>
                        </div>

                        <CustomerBought
                            layout="fullwidth"
                            collectionSlug="deal-of-the-day"
                        />
                        <RelatedProduct keywords={keyWords} collectionSlug="shop-recommend-items" />
                    </div>
                </div>
                <Newletters />
            </PageContainer>
        </>
    );
};


export async function getStaticPaths() {
    try {
        //getting urls
        const responseData = await axios.get(
            `${marketplaceUrl}/getAllProducts`
        )
            .then((response) => {
                return response.data;
            })
            .catch((error) => ({ error: JSON.stringify(error) }));

        const data = responseData;
        //console.log("is Array : "+Array.isArray(data));
        //console.log("Length Of Array : "+data.length);
        // console.log("DATA :->: " + JSON.stringify(responseData));
        var paths;
        //console.log("**********************************************");
        if (data == null || data.length == 0) {
            console.info("empty product array !!!");
            paths =[]
        } else {
            if (data.error) {
                console.warn("ERROR ::=> "+data.error);
                paths = []
            } else {
                paths = data?.map((item) => {
                    console.log("******");
                    return {
                        params: {
                            pid: item.toString()
                        }
                    }
                })
                console.log(paths);
            }
        }
        return {
            paths,
            fallback: false
        }
    } catch (error) {
        console.log("!!!! ERROR HAPPEN : " + error);
    }

}

export async function getStaticProps(context) {
    const { params } = context;
    // const responseData = await ProductRepository.getProductsById(params.pid);
    const pid = params.pid.split("&pid=");
    //console.log("!@#$ ::::: "+pid[1])
    const responseData = await ProductRepository.getProductsById(pid[1] + "");
    return {
        props: {
            responseData
        }
    }
}



export default ProductDefaultPage;
