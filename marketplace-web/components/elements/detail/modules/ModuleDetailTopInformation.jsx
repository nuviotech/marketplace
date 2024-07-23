import React, { Component } from 'react';
import Link from 'next/link';
import Rating from '~/components/elements/Rating';
import SEO from "@bradgarropy/next-seo"
import Head from 'next/head';
import { Rate } from 'antd';

const ModuleDetailTopInformation = ({props ,product }) => {
    // Views
    let priceView;

    var description = process.env.NEXT_PUBLIC_WEBSITE_NAME+" | "+product.title+" | price : "+product.sale_price;

    
        priceView = (
            <h4 className="ps-product__price sale">
                <del className="mr-2">&#8377;{product.price}</del>&#8377;
                {product.sale_price}
            </h4>
        );

    
    return (
        <>
            
        <header>
            <h1>{product.title}</h1>
            <div className="ps-product__meta">
                <p>
                    Brand:
                    <Link href="/shop">
                        <a className="ml-2 text-capitalize">{product.brand}</a>
                    </Link>
                </p>
                {
                   ( product?.totalRatingRation!=null && product?.totalRatingRation?.split(":")[0]>0 ) &&
                   <Rate allowHalf defaultValue={product?.totalRatingRation?.split(":")[0]} disabled />
                }
               
                {/*                 
                <div className="ps-product__rating">
                    <Rating />
                    <span>(2 review)</span>
                </div>
                */}

            </div>

            <div> 
                    {priceView} 
            </div>
        </header>
        </>
    );
};

export default ModuleDetailTopInformation;
