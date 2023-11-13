import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { connect } from 'react-redux';
import useEcomerce from '~/hooks/useEcomerce';
import { calculateAmount } from '~/utilities/ecomerce-helpers';

const ModulePaymentOrderSummary = ({ ecomerce, shipping }) => {
    //const[tax,setTax] =useState(0);
    const { products, getProducts } = useEcomerce();
    useEffect(() => {
        if (ecomerce.cartItems) {
            getProducts(ecomerce.cartItems, 'cart');

        }
    }, [ecomerce]);

    // view
    let listItemsView, shippingView, totalView;
    let amount;
    //let tax=0;
    if (products && products.length > 0) {
        amount = calculateAmount(products);

        listItemsView = products.map((item) => {
            // tax +=(item.sale_price * item.hsnRate / (100 + (item.hsnRate*1))) * item.quantity

            return (
                <Link href="/" key={item.id}>

                    <a>
                        <strong>
                            {item.title}
                            <span>x{item.quantity}</span>
                        </strong>
                        <small>{item.quantity * item.sale_price} <del>₹{item.quantity * item.price}</del></small>
                    </a>
                </Link>
            );


        });
    } else {
        listItemsView = <p>No Product.</p>;
    }
    if (shipping === true) {
        shippingView = (
            <figure>
                <figcaption>
                    <strong>Shipping Fee</strong>
                    <small>₹20.00</small>
                </figcaption>
            </figure>
        );
        totalView = (
            <figure className="ps-block__total">
                <h3>
                    Total
                    <strong>₹{parseInt(amount) + 20}.00</strong>
                </h3>
            </figure>
        );
    } else {
        totalView = (
            <figure className="ps-block__total">
                <h3>
                    Total
                    <strong>₹{parseInt(amount)}</strong>
                </h3>
            </figure>
        );
    }
    return (
        <div className="ps-block--checkout-order">
            <div className="ps-block__content">
                <figure>
                    <figcaption>
                        <strong>Product</strong>
                        <strong>total</strong>
                    </figcaption>
                </figure>
                <figure className="ps-block__items">{listItemsView}</figure>
                {/*<figure>
                    <figcaption className='text-danger'>
                        <strong>Total Tax </strong>
                        <small>₹{tax.toFixed(2)}</small>
                    </figcaption>
                </figure>*/}
                {shippingView}
                {totalView}

            </div>
        </div>

    );
};
export default connect((state) => state)(ModulePaymentOrderSummary);