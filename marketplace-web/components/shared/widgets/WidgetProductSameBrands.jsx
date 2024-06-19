import React, { useEffect, useState } from 'react';
import { getProductsByCollectionHelper } from '~/utilities/strapi-fetch-data-helpers';
import Product from '~/components/elements/products/Product';
import { generateTempArray } from '~/utilities/common-helpers';
import SkeletonProduct from '~/components/elements/skeletons/SkeletonProduct';
import ProductRepository from '~/repositories/ProductRepository';
import { useRouter } from 'next/router';

const WidgetProductSameBrands = ({ collectionSlug }) => {

    const [productItems, setProductItems] = useState(null);
    const [loading, setLoading] = useState(true);
    const Router = useRouter();

    async function getProducts() {
        setLoading(true);
       
        const responseData = await ProductRepository.getProductsByBrand(collectionSlug+"&limit=5");
        if (responseData) {
            setProductItems(responseData);
            setTimeout(
                function () {
                    setLoading(false);
                }.bind(this),
                250
            );
        }
    }

    useEffect(() => {
        getProducts();
    },[collectionSlug] );

    // Views
    let productItemsView;
    if (!loading) {
        if (productItems && productItems?.length > 0) {
            productItemsView = productItems.map((item) => (
                <Product product={item} key={item.id} />
            ));
        } else {
            productItemsView = <p>No product found.</p>;
        }
    } else {
        productItemsView = generateTempArray(3).map((item) => (
            <SkeletonProduct key={item} />
        ));
    }

    function handleSelectBrand(e) {
        Router.push(`/brand/${e}`);
    }

    return (
        <aside className="widget widget_same-brand">
            <h3>Same Brand</h3>
            <div className="widget__content">{productItemsView}</div>
            <div style={{cursor:"pointer"}} onClick={()=>{handleSelectBrand(collectionSlug)}} className='text-muted text-center'>All Records</div>
        </aside>
    );
};

export default WidgetProductSameBrands;
