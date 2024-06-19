import React, { useState } from 'react';
import FormCheckoutInformation from './modules/FormCheckoutInformation';
import ModulePaymentOrderSummary from '~/components/partials/account/modules/ModulePaymentOrderSummary';
import { getDataFromLocalStorage } from '~/repositories/WebHelper';
import { userIsLogin } from '~/store/auth/action';
const Checkout = () => {
    const [coupon, setCoupon] = useState(() => {
        // Try to  retrieve coupon details from localStorage on initial render
        return getDataFromLocalStorage('c_code');

    });
    const handleSetCoupon = async (couponCode) => {
        setCoupon(couponCode);
    }

    return (
        <div className="ps-checkout ps-section--shopping">
            <div className="container">
                <div className="ps-section__content px-md-5 mx-md-5">
                    <div className="ps-form--checkout">
                        {/* this for laptop sreen */}
                        <div className="ps-form__content d-md-block d-none">
                            <div className="row ">
                                <div className="col-xl-8 col-lg-8 col-md-12 col-sm-12">
                                    <FormCheckoutInformation coupon={coupon} />
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-12 col-sm-12  ps-block--checkout-order">
                                    <div className="ps-form__orders">
                                        <h3>Your Order</h3>
                                        <ModulePaymentOrderSummary handleSetCoupon={handleSetCoupon} />
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* this for mobile sreen */}
                        <div className="ps-form__content d-block d-md-none ">
                            {
                                <>
                                    {userIsLogin() &&
                                        <div className='alert alert-info'>✅ Provide Delivery Information</div>
                                    }
                                    <div className="row ">
                                        <div className="col-xl-4 col-lg-4 col-md-12 col-sm-12  ps-block--checkout-order">
                                            <div className="ps-form__orders">
                                                <h3>Your Order</h3>
                                                <ModulePaymentOrderSummary handleSetCoupon={handleSetCoupon} />
                                            </div>
                                        </div>
                                        <div className="col-xl-8 col-lg-8 col-md-12 col-sm-12">
                                            <FormCheckoutInformation coupon={coupon} />
                                        </div>
                                    </div>
                                </>
                            }
                        </div>

                    </div>
                </div>
            </div>
        </div>
    );
};

export default Checkout;
