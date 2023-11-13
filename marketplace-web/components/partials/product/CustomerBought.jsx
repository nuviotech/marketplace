import React, { useEffect, useState } from 'react';
import Slider from 'react-slick';
import {
    carouselFullwidth,
    carouselStandard,
} from '~/utilities/carousel-helpers';
import Product from '~/components/elements/products/Product';
import { getProductsByCollectionHelper } from '~/utilities/strapi-fetch-data-helpers';
import useGetProducts from '~/hooks/useGetProducts';
import Link from 'next/link';
import { generateTempArray } from '~/utilities/common-helpers';
import SkeletonProduct from '~/components/elements/skeletons/SkeletonProduct';
import ProductDealOfDay from '~/components/elements/products/ProductDealOfDay';

const CustomerBought = ({ collectionSlug, boxed, layout }) => {
    const { productItems, loading, getProductsByCollection } = useGetProducts();

    useEffect(() => {
        getProductsByCollection(collectionSlug);
    }, [collectionSlug]);

    // Views
    let productItemsView;
    if (!loading) {
        if (productItems && productItems.length > 0) {
            const slideItems = productItems.map((item) => {
                if(item!==null)
                    {return <ProductDealOfDay product={item} key={item?.product_ref_id} />}
        });
            productItemsView = (
                <Slider {...carouselFullwidth} className="ps-carousel outside">
                    {slideItems}
                </Slider>
            );
         //   console.log("Slider : "+ JSON.stringify(productItemsView));
        } else {
            productItemsView = <p>No product(s) found.</p>;
        }
    } else {
        const skeletons = generateTempArray(6).map((item) => (
            <div className="col-xl-2 col-lg-3 col-sm-3 col-6" key={item}>
                <SkeletonProduct />
            </div>
        ));
        productItemsView = <div className="row">{skeletons}</div>;
    }

    return (
        <div className="ps-deal-of-day">
            <div className="ps-container">
                <div className="ps-section__header">
                    <div className="ps-block--countdown-deal">
                        <div className="ps-block__left">
                            <h3>Customers who bought this item also bought</h3>
                        </div>
                        
                    </div>
                    <Link href="/shop">
                        <a>View all</a>
                    </Link>
                </div>
                <div className="ps-section__content">{productItemsView}</div>
            </div>
        </div>
    );
};

export default CustomerBought;
