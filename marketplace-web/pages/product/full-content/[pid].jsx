import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import ProductRepository from '~/repositories/ProductRepository';
import SkeletonProductDetail from '~/components/elements/skeletons/SkeletonProductDetail';
import BreadCrumb from '~/components/elements/BreadCrumb';
import ProductDetailFullContent from '~/components/elements/detail/ProductDetailFullContent';
import CustomerBought from '~/components/partials/product/CustomerBought';
import RelatedProduct from '~/components/partials/product/RelatedProduct';
import PageContainer from '~/components/layouts/PageContainer';
import axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';
import { userIsLogin, getToken } from '~/store/auth/action';

const ProductDefaultPage = () => {
    const router = useRouter();
    const { pid } = router.query;
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(false);

    async function getProduct(pid) {
        setLoading(true);
        const responseData = await ProductRepository.getProductsById(pid);
        if (responseData) {
            setProduct(responseData);
            trackProductView(responseData);
            setTimeout(
                function () {
                    setLoading(false);
                }.bind(this),
                250
            );
        }
    }

    const trackProductView = (productData) => {
        if (!userIsLogin() || !productData) return;
        try {
            axios.post(
                `${marketplaceUrl}/addRecentlyViewedProduct`,
                {
                    productId: productData.id,
                    productName: productData.title,
                    productPrice: productData.sale_price || productData.price || 0,
                    productImage: productData.images?.[0]?.url || '',
                    categoryId: productData.category_id || 0,
                },
                {
                    headers: { Authorization: "Bearer " + getToken() },
                    timeout: 5000,
                }
            ).catch((error) => {
                console.debug("Recently viewed product tracking failed (non-critical)", error.message);
            });
        } catch (error) {
            console.debug("Recently viewed product tracking error (non-critical)", error.message);
        }
    };

    useEffect(() => {
        getProduct(pid);
    }, [pid]);

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
    let productView;
    if (!loading) {
        if (product) {
            productView = <ProductDetailFullContent product={product} />;
        } else {
        }
    } else {
        productView = <SkeletonProductDetail />;
    }
    return (
        <PageContainer title={product ? product.title : 'Loading...'}>
            <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
            <div className="ps-page--product">
                <div className="container">
                    {productView}
                    <CustomerBought
                        layout="fullwidth"
                        collectionSlug="deal-of-the-day"
                    />
                    <RelatedProduct collectionSlug="shop-recommend-items" />
                </div>
            </div>
        </PageContainer>
    );
};

export default ProductDefaultPage;
