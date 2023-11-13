import React from 'react';
import Link from 'next/link';

const ModuleProductDetailDescription = ({ product }) => (
    
    <div className="ps-product__desc">
        <p>
            Sold By :
            <Link href="/shop">
                <a>
                    <strong> {product?.companyName}</strong>
                </a>
            </Link>
        </p>
        <ul className="ps-list--dot">
            {product?.bulletpoints?.split("##")?.map(item => {return(<li>{item}</li>)})}
        </ul>
    </div>
);

export default ModuleProductDetailDescription;
