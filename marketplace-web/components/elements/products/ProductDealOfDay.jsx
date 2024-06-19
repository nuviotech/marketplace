import React from 'react';
import { connect } from 'react-redux';
import Link from 'next/link';
import Rating from '../Rating';
import { StrapiProductPriceExpanded } from '~/utilities/product-helper';
import ModuleProductActions from '~/components/elements/products/modules/ModuleProductActions';
import ModuleProductProgressbar from '~/components/elements/products/modules/ModuleProductProgressbar';
import useProduct from '~/hooks/useProduct';
import { Rate } from 'antd';

const ProductDealOfDay = ({ product }) => {
    const { thumbnailImage, badge, title } = useProduct();
    return (
        <div className="ps-product ps-product--inner">
            <div className="ps-product__thumbnail" style={{ maxWidth: "100%", height: "205px", overflow: "hidden" }}>
                <Link href="/product/[pid]" as={`/product/${product?.title.replaceAll("/", " | ").replaceAll(" ", "-")}&pid=${product?.id}`}>
                    <a>{thumbnailImage(product)}</a>
                </Link>
                {badge(product)}
                <ModuleProductActions product={product} />
            </div>
            <div className="ps-product__container">
                <Link href="/shop">
                    <a className="ps-product__vendor">{product?.brand}</a>
                </Link>
                <div className="ps-product__content">
                    {StrapiProductPriceExpanded(product)}
                    
                    {
                        (product?.totalRatingRation != null && product?.totalRatingRation?.split(":")[0] > 0) &&
                        <Rate allowHalf defaultValue={product?.totalRatingRation?.split(":")[0]} disabled />
                    }
                    {/*
                    <div className="ps-product__rating">
                        <Rating />
                        <span>{product.ratingCount}</span>
                    </div>
                    */}
                    {title(product)}
                    <ModuleProductProgressbar product={product} />
                </div>
            </div>
        </div>
    );
};

export default connect()(ProductDealOfDay);
