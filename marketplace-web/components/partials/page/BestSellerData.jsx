import React, { useEffect } from 'react';
import { useState } from 'react';
import useGetProducts from '~/hooks/useGetProducts';
import ProductItems from '../product/ProductItems';

const BestSellerData  =({slug})=>{
    const { productItems, loading, getProductsByCollection } = useGetProducts();
    const [productArray, setproductArray]=useState(null);
    var array=new Array();

    useEffect(() => {
        getProductsByCollection(slug);
    }, []);

    return(
        <div className="ps-section--custom">
            {productItems?<ProductItems columns={5} products={productItems} />:'product not found!!'}
        </div>
    );
};

export default BestSellerData;
