import React, { useEffect, useState } from 'react';
import FormCheckoutInformation from './modules/FormCheckoutInformation';
import ModulePaymentOrderSummary from '~/components/partials/account/modules/ModulePaymentOrderSummary';
import useEcomerce from '~/hooks/useEcomerce';
const Checkout = () => {
    const [coupon,setCoupon]=useState(null);
    const [orderTotalAmt,setOrderTotalAmt]=useState(); 
    const handleSetCoupon=(couponCode,amt)=>{
        setCoupon(couponCode);
        setOrderTotalAmt(amt);
    }
    return (
        <div className="ps-checkout ps-section--shopping">
            <div className="container">
                <div className="ps-section__header">
                    <h1>Checkout Information</h1>
                </div>
                <div className="ps-section__content">
                    <div className="ps-form--checkout">
                        <div className="ps-form__content">
                            <div className="row">
                                <div className="col-xl-8 col-lg-8 col-md-12 col-sm-12">
                                    <FormCheckoutInformation coupon={coupon} orderTotalAmt={orderTotalAmt} />
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-12 col-sm-12  ps-block--checkout-order">
                                    <div className="ps-form__orders">
                                        <h3>Your order</h3>
                                        <ModulePaymentOrderSummary handleSetCoupon={handleSetCoupon}/>
                                    </div>

                                    

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Checkout;
