import React from 'react';

const PartialVendor = ({product}) => (
    <section>
        <h4>Shop Name : {JSON?.parse(JSON?.parse(JSON?.stringify(product?.sellerDetails)))?.companyName}</h4>
        <h4>Contact : {JSON?.parse(JSON?.parse(JSON?.stringify(product?.sellerDetails)))?.phone}</h4>
        <h4>Email : {JSON?.parse(JSON?.parse(JSON?.stringify(product?.sellerDetails)))?.email}</h4>
        <h4>Address : {JSON?.parse(JSON?.parse(JSON?.stringify(product?.sellerDetails)))?.address}</h4>
        <a href="/shop">More Products from {product?.companyName}</a>
    </section>
);

export default PartialVendor;
