import React from 'react';
import Link from 'next/link';
import useProduct from '~/hooks/useProduct';

const ProductOnCart = ({ product, children }) => {
    const { thumbnailImage, title } = useProduct();
    return (
        <div className="ps-product--cart-mobile">
            <div className="ps-product__thumbnail">
                <Link href="/product/[pid]" as={`/product/${product?.title?.replaceAll("/"," | ").replaceAll(" ","-")}&pid=${product?.id}`}>
                    <a>{thumbnailImage(product)}</a>
                </Link>
            </div>
            <div className="ps-product__content">
                {title(product)}
                <p>
                    <small>
                    <del style={{fontSize:"10px"}} className='text-danger mr-2'>₹{product?.price} </del> ₹{product?.sale_price} x {product?.quantity}
                    </small>
                </p>{' '}
                {children}
            </div>
        </div>
    );
};

export default ProductOnCart;
