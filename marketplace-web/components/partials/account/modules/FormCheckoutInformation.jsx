import React, { Component, useContext, useEffect, useState } from 'react';
import { connect } from 'react-redux';
import axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';
import Link from 'next/link';
import { Form, Input, Modal } from 'antd';
import useEcomerce from '~/hooks/useEcomerce';
import { calculateAmount } from '~/utilities/ecomerce-helpers';
import { userIsLogin, getToken } from '~/store/auth/action';
import { useRouter } from 'next/router';
import { AuthContext } from '~/context/loginContext';
import { userData } from '~/repositories/UserDeatils';

const FormCheckoutInformation = ({ ecomerce }) => {
    var userLoginStatus = userIsLogin();
    const { currentUser } = useContext(AuthContext);
    console.warn(currentUser);
    //const [currentUser, setCurrentUser] = useState();
    const [loader, setLoader] = useState(false)

    //console.log("userloginstatus : "+userLoginStatus);
    //console.log("eccomerce : " + JSON.stringify(ecomerce.cartItems));
    let totalAmount = 0;
    const [review, setReview] = useState({state:"select state"});

    const setDefaultValues = () => {
        if (currentUser?.shippingAddress) {
            setReview({
                fname: currentUser?.shippingAddress?.firstName,
                last_name: currentUser?.shippingAddress?.lastName,
                address: currentUser?.shippingAddress?.streetAddress,
                city: currentUser?.shippingAddress?.city,
                contact: currentUser?.phone,
                state: currentUser?.shippingAddress?.state,
                postal_code:currentUser?.shippingAddress?.pincode,
            })
        }
    }

    const { products, getProducts } = useEcomerce();
    const router = useRouter();
    useEffect(async () => {
        if (ecomerce.cartItems) {
           // setCurrentUser(await userData())

            getProducts(ecomerce.cartItems, 'cart');
            setDefaultValues();
        }
    }, [ecomerce, currentUser]);

    if (products && products.length > 0) {
        totalAmount = calculateAmount(products);
    }

    const handleLoginSubmit = () => {
        if(review.state==="select state"){
            alert("select state!!");
            return;
        }
        const orderInformation = {
            "fname": review.fname,
            "lname": review.last_name,
            "shippingAddressPrimary": review.address,
            "landmark": review.apprtment_name,
            "city": review.city,
            "postalCode": review.postal_code,
            "products": ecomerce.cartItems,
            "contactNumber": review.contact,
            "totalBill": totalAmount,
            "token": getToken(),
            "state": review.state,
        }
        
        //  console.log(orderInformation);
        setLoader(true);
        placeOrder(orderInformation);
        //  Router.push('/account/shipping');

    };
    const loadScript = (src) => {
        return new Promise((resolve) => {
            const script = document.createElement('script')
            script.src = src;
            script.crossOrigin = 'anonymous'
            script.onload = () => {
                resolve(true);
            }
            script.onerror = () => {
                resolve(false);
            }
            document.body.appendChild(script);
        })
    }

    const goToLogin = () => {
        sessionStorage.setItem("action", "checkout");
        router.push("/account/login");
    }

    const placeOrder = (data) => {
        axios.post(`${marketplaceUrl}/saveOrderDetails`, data, {
            headers: {
                Authorization: "Bearer " + getToken(),
            }
        }).then(
            async (response) => {


                // console.warn(JSON.stringify(response));

                if (response.data.success) {
                    //var merchantId = response.data.data.merchantId;
                    // var transactionId = response.data.data.merchantTransactionId
                    router.push(response.data.data.instrumentResponse.redirectInfo.url + "");
                } else {
                    setLoader(false);
                    alert("Something went wrong !!!");
                }

                //razorpay payment integration
                /*
                const result = await loadScript("https://checkout.razorpay.com/v1/checkout.js");
                if (!result) {
                    alert("network issue...");
                    return;
                }
                const amt = totalAmount;
               // console.log("amt " + JSON.stringify(response.data));
                const Razorpay_payment = response.data.RZ_order;
                const order_ref_id = response.data.order_id;
                //console.log("order id : "+Razorpay_payment.id);
                const options = {
                    key: response.data.rzKey,
                    currency: "INR",
                    name: "Payment gateway.",
                    description: "Test Wallet Transaction",
                    image: "/static/img/nuvioseller.png",
                    order_id: Razorpay_payment.id,
                    handler: function (response) {

                        const payment_id = response.razorpay_payment_id;
                        const rz_order_id = response.razorpay_order_id;
                        const signature = response.razorpay_signature;

                        if (response.razorpay_payment_id) {
                            //update the states of order payment
                            const paymentObject = {
                                "rzPaymentId": payment_id,
                                "rzOrderId": rz_order_id,
                                "rzSignature": signature,
                                "orderID": order_ref_id
                            }
                            axios.post(`${marketplaceUrl}/updateOrder`, paymentObject, {
                                headers: {
                                    Authorization: "Bearer "+getToken(),
                                }
                            }).then(
                                (response) => {
                                    // alert("payment is success full");
                                    const modal = Modal.success({
                                        centered: true,
                                        title: 'Payment Success!',
                                        content: `Thank you! Your order is ` + order_ref_id,
                                    });
                                    modal.update;
                                    review.fname = "";
                                },
                                (error) => {
                                    alert("error happning in update payment status...")
                                }
                            )

                        }
                    },
                    prefill: {
                        name: review.fname + " " + review.last_name,
                        email: "xyz@gmail.com",
                        contact: review.contact,
                    },
                    theme: {
                        color: "#fcb800"
                    }
                };

                const paymentObject = new window.Razorpay(options);
                paymentObject.open();



                //order details is save to database
                //console.log("Razor pay details : "+JSON.stringify(response));
                // alert(" Order are successfully save to store. ");

                //Router.push('/account/payment');
            */




                /*
                    //Paytm payment integration
                    const result = await loadScript("https://securegw.paytm.in/merchantpgpui/checkoutjs/merchants/NhwQUF41773981847093.js");
                    if (!result) {
                        alert("network issue...");
                        return;
                    }
                    console.log("Res : " + response);
                    var config = {
                        "root": "",
                        "flow": "DEFAULT",
                        "data": {
                            "orderId": response.paytm_orderId, // update order id 
                            "token": "", // update token value 
                            "tokenType": "TXN_TOKEN",
                            "amount": response.amount // update amount 
                        },
                        "merchant": {
                            mid: "NhwQUF41773981847093"
                        },
                        "handler": {
                            "notifyMerchant": function (eventName, data) {
                                console.log("notifyMerchant handler function called");
                                console.log("eventName => ", eventName);
                                console.log("data => ", data);
                            },
                            "transactionStatus": function (data) {
                                console.log("transaction complete")
                                console.log(data);
    
                                if (data.STATUS = "TXN_FAILURE") {
                                    alert("transaction fail");
                                } else if (data.STATUS = "TXN_SUCCESS") {
                                    alert("transaction success");
                                    const paytm_order_id = data.paytm_orderId;
                                    const order_ref_id = data.order_id;
    
                                    //update the states of order payment
                                    const paymentObject = {
                                        "rzPaymentId": paytm_order_id,
                                        "orderID": order_ref_id
                                    }
                                    axios.post(`${marketplaceUrl}/updateOrder`, paymentObject, {
                                        headers: {
                                            Authorization: "Bearer " + getToken(),
                                        }
                                    }).then(
                                        (response) => {
                                            // alert("payment is success full");
                                            const modal = Modal.success({
                                                centered: true,
                                                title: 'Payment Success!',
                                                content: `Thank you! Your order is ` + order_ref_id,
                                            });
                                            modal.update;
                                            review.fname = "";
                                        },
                                        (error) => {
                                            alert("error happning in update payment status...")
                                        }
                                    )
    
    
    
                                } else {
                                    alert("something wrong");
                                }
                            }
                        }
                    };
                    if (window.Paytm && window.Paytm.CheckoutJS) {
                        window.Paytm.CheckoutJS.onLoad(function excecuteAfterCompleteLoad() {
                            // initialze configuration using init method
                            window.Paytm.CheckoutJS.init(config).then(function onSuccess() {
                                // after successfully updating configuration, invoke JS Checkout
                                window.Paytm.CheckoutJS.invoke();
                            }).catch(function onError(error) {
                                console.log("error => ", error);
                            });
                        });
                    }
                    //paytm
                    */


            },
            (error) => {
                //order details is not save to database
                //alert("Something went wrong! ");
                setLoader(false)
                console.log("error : " + JSON.stringify(error));
            }
        )
    }


    //const{fname}=this.state;
    return (

        <Form
            className="ps-form__billing-info"
            onFinish={() => { handleLoginSubmit() }}>
            {userLoginStatus ?
                <div>

                    <h3 className="ps-form__heading">Contact information</h3>
                    <div className="form-group">
                        <input type="text"
                            className="form-control"
                            value={review.contact}
                            placeholder='Enter the contact...'
                            required="true"
                            pattern="[7-9]{1}[0-9]{9}"
                            onChange={(e) => {
                                setReview({ ...review, contact: e.target.value })
                            }}
                        />
                        {/*<Form.Item
                            name="name"
                            rules={[
                                {
                                    required: true,
                                    pattern: "[7-9]{1}[0-9]{9}$",
                                    message:
                                        'Enter your contact number!!',
                                    
                                },
                            ]}>
                        

                            <Input
                                className="form-control"
                                type="text"
                                placeholder={review.contact}
                                name="contact"
                                value={review.contact}
                                onChange={(e) => {
                                    setReview({ ...review, contact: e.target.value })
                                }}
                            />
                        </Form.Item>*/}
                    </div>
                    {/*<div className="form-group">
                    <div className="ps-checkbox">
                        <input
                            className="form-control"
                            type="checkbox"
                            id="keep-update"

                        />
                        <label htmlFor="keep-update">
                            Keep me up to date on news and exclusive offers?
                        </label>
                    </div>
                </div>*/}
                    <h3 className="ps-form__heading">Fill The Information</h3>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="form-group">
                                <input type="text"
                                    className="form-control"
                                    value={review.fname}
                                    placeholder='First Name'
                                    required="true"
                                    onChange={(e) => {
                                        setReview({ ...review, fname: e.target.value })
                                    }}
                                />
                                {/*
                                <Form.Item
                                    name="firstName"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'Enter your first name!',
                                        },
                                    ]}>
                                    <Input
                                        className="form-control"
                                        type="text"
                                        placeholder="First Name"
                                        name="fname"
                                        onChange={(e) => {
                                            setReview({ ...review, fname: e.target.value })
                                        }}
                                    />
                                </Form.Item>
                                    */}
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="form-group">
                                <input type="text"
                                    className="form-control"
                                    value={review.last_name}
                                    placeholder='First Name'
                                    required="true"
                                    onChange={(e) => {
                                        setReview({ ...review, last_name: e.target.value })
                                    }}
                                />
                                {/*
                                <Form.Item
                                    name="lastName"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'Enter your last name!!',
                                        },
                                    ]}>
                                    <Input
                                        className="form-control"
                                        type="text"
                                        value={currentUser.lastName}
                                        placeholder="Last Name"
                                        name="last_name"
                                        onChange={(e) => {
                                            setReview({ ...review, last_name: e.target.value })
                                        }}
                                    />
                                </Form.Item>
                                    */}
                            </div>
                        </div>
                    </div>
                    <div className="form-group">
                        <input type="text"
                            className="form-control"
                            value={review.address}
                            placeholder='Address'
                            required="true"
                            onChange={(e) => {
                                setReview({ ...review, address: e.target.value })
                            }}
                        />
                        {/* <Form.Item
                            name="address"
                            rules={[
                                {
                                    required: true,
                                    message: 'Enter an address!',
                                },
                            ]}>
                            <Input
                                className="form-control"
                                type="text"
                                placeholder="Address"
                                name="address"
                                onChange={(e) => {
                                    setReview({ ...review, address: e.target.value })
                                }}
                            />
                        </Form.Item>
                            */}
                    </div>
                    <div className="form-group">
                        <input type="text"
                            className="form-control"
                            placeholder='Apartment, suite, etc. (optional)'
                            onChange={(e) => {
                                setReview({ ...review, apprtment_name: e.target.value })
                            }}
                        />
                        {/*<Form.Item
                            name="apartment"
                            rules={[
                                {
                                    required: true,
                                    message: 'Enter an Apartment!',
                                },
                            ]}>
                            <Input
                                className="form-control"
                                type="text"
                                placeholder="Apartment, suite, etc. (optional)"
                                name="apprtment_name"
                                onChange={(e) => {
                                    setReview({ ...review, apprtment_name: e.target.value })
                                }}
                            />
                        </Form.Item>
                            */}
                    </div>
                    <div className="row">
                        <div className="col-sm-4">
                            <div className="form-group">
                                <input type="text"
                                    className="form-control"
                                    value={review.city}
                                    required="true"
                                    placeholder='City '
                                    onChange={(e) => {
                                        setReview({ ...review, city: e.target.value })
                                    }}
                                />
                                {/*
                                <Form.Item
                                    name="city"
                                    rules={[
                                        {
                                            required: true,
                                            message: 'Enter a city!',
                                        },
                                    ]}>
                                    <Input
                                        className="form-control"
                                        type="city"
                                        placeholder="City"
                                        name="city"
                                        onChange={(e) => {
                                            setReview({ ...review, city: e.target.value })
                                        }}
                                    />
                                </Form.Item>
                                    */}
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="form-group">
                                <select onChange={(event) => { setReview({ ...review, state: event.target.value }) }} className='form-control'>
                                    <option>{review.state}</option>
                                    <option>Andhra Pradesh</option>
                                    <option>Arunachal Pradesh</option>
                                    <option>Assam</option>
                                    <option>Bihar</option>
                                    <option>Chhattisgarh</option>
                                    <option>Goa</option>
                                    <option>Gujarat</option>
                                    <option>Haryana</option>
                                    <option>Himachal Pradesh</option>
                                    <option>Jammu and Kashmir</option>
                                    <option>Jharkhand</option>
                                    <option>Karnataka</option>
                                    <option>Kerala</option>
                                    <option>Madhya Pradesh</option>
                                    <option>Maharashtra</option>
                                    <option>Manipur</option>
                                    <option>Meghalaya</option>
                                    <option>Mizoram</option>
                                    <option>Nagaland</option>
                                    <option>Orissa</option>
                                    <option>Punjab</option>
                                    <option>Rajasthan</option>
                                    <option>Sikkim</option>
                                    <option>Tamil Nadu</option>
                                    <option>Tripura</option>
                                    <option>Uttarakhand</option>
                                    <option>Uttar Pradesh</option>
                                    <option>West Bengal</option>
                                    <option>Tamil Nadu</option>
                                    <option>Tripura</option>
                                    <option>Andaman and Nicobar Islands</option>
                                    <option>Chandigarh</option>
                                    <option>Dadra and Nagar Haveli</option>
                                    <option>Daman and Diu</option>
                                    <option>Delhi</option>
                                    <option>Lakshadweep</option>
                                    <option>Pondicherry</option>
                                </select>
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="form-group">
                                <input type="text"
                                    className="form-control"
                                    value={review.postal_code}
                                    placeholder='Postal Code '
                                    required="true"
                                    pattern='[0-9]{6}'
                                    onChange={(e) => {
                                        setReview({ ...review, postal_code: e.target.value })
                                    }}
                                />
                                {/*<Form.Item
                                    name="postalCode"
                                    rules={[
                                        {
                                            required: true,
                                            pattern: "[0-9]{6}",
                                            message: 'Enter a postal code!',
                                        },
                                    ]}>
                                    <Input
                                        className="form-control"
                                        type="postalCode"
                                        placeholder="Postal Code"
                                        name="postal_code"
                                        onChange={(e) => {
                                            setReview({ ...review, postal_code: e.target.value })
                                        }}
                                    />
                                </Form.Item>
                                    */}
                            </div>
                        </div>
                    </div>
                    {/*
                <div className="form-group">
                    <div className="ps-checkbox">
                        <input
                            className="form-control"
                            type="checkbox"
                            id="save-information"
                        />
                        <label htmlFor="save-information">
                            Save this information for next time
                        </label>
                    </div>
                </div>*/}
                    <div className="ps-form__submit">
                        <Link href="/shop">
                            <a>
                                <i className="icon-arrow-left mr-2"></i>
                                Return to shopping cart
                            </a>
                        </Link>
                        <div className="ps-block__footer">


                            {loader ? (
                                <i>please wait <span className='spinner-border'></span> </i>
                            ) : (

                                <button className="ps-btn">Continue to shipping</button>
                            )
                            }
                        </div>
                    </div>

                </div>
                :
                <div className='mb-5'>
                    <h3 class="" className="">Please Login First, For Place The Order...</h3>
                    <button class="ps-btn" onClick={() => { goToLogin() }}><i className="icon-arrow-left mr-2"></i> Go To Login</button>
                </div>
            }
        </Form>

    );
}
export default connect((state) => state)(FormCheckoutInformation);
//export default FormCheckoutInformation;
