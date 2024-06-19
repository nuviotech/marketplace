import React from 'react';

const PartialVendor = ({product}) => (
    <section>
        <h4>{product?.companyName}</h4>
        <p>
           
        </p>
        <a href="/shop">More Products from {product?.companyName}</a>
    </section>
);

export default PartialVendor;
