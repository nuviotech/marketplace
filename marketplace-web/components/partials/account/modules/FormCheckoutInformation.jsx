import React, {  useEffect, useState } from 'react';
import { connect } from 'react-redux';
import axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';
import Link from 'next/link';
import { Form, Modal } from 'antd';
import useEcomerce from '~/hooks/useEcomerce';
import { calculateAmount } from '~/utilities/ecomerce-helpers';
import { userIsLogin, getToken } from '~/store/auth/action';
import Router, { useRouter } from 'next/router';
import { identifyCodStatus, userData } from '~/repositories/UserDeatils';
import GuestUserForm from './GuestUserForm';

const FormCheckoutInformation = ({ ecomerce, coupon }) => {
    var userLoginStatus = userIsLogin();
    
    //const { currentUser } = useContext(AuthContext);
    const [currentUser, setCurrentUser] = useState({});
    const [loader, setLoader] = useState(false)
    const [codIsEnabled, setCodIsEnabled] = useState(false);
    const [state, setState] = useState({});
    const [review, setReview] = useState({ state: "select state" });
    const { removeItems } = useEcomerce();

    const [inputErrors, setInputErrors] = useState({ isError: false, type: null, message: null });
   // var flag = ecomerce + currentUser;
    //no use guestUserData function yet 
    const guestUserData = (data) => {
        setReview({
            fname: data?.firstName,
            last_name: data?.lastName,
            contact: data?.phone,
            state: "select state",
            address: '',
            city: '',
            postal_code: '',
        })
    }

    const numberCheck = (e) => {
        
        const phoneRegex = /^[789]\d{9}$/;
        if (phoneRegex.test(e)) {
            setInputErrors({ isError: false, type: null, message: null });
        } else {
            setInputErrors({ isError: true, type: "number", message: "Invalid mobile number. Please enter 10 digits only." });
        }
    }

    let totalAmount = 0;

    const setDefaultValues = () => {
        if (currentUser?.shippingAddress) {
            setReview({
                fname: currentUser?.shippingAddress?.firstName,
                last_name: currentUser?.shippingAddress?.lastName,
                address: currentUser?.shippingAddress?.streetAddress,
                city: currentUser?.shippingAddress?.city,
                contact: currentUser?.phone,
                state: currentUser?.shippingAddress?.state,
                postal_code: currentUser?.shippingAddress?.pincode,
            })
        } else {
            setReview({
                fname: currentUser?.firstName,
                last_name: currentUser?.lastName,
                contact: currentUser?.phone,
                state: "select state",
                address: '',
                city: '',
                postal_code: ''
            })
        }
    }

    const { products, getProducts } = useEcomerce();
    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            if (ecomerce.cartItems) {
                const user = await userData();
                setCurrentUser(user);
                const codStatus = await identifyCodStatus();
                setCodIsEnabled(codStatus);
                getProducts(ecomerce.cartItems, 'cart');
                setDefaultValues();
            }
        };
        fetchData(); // Call the async function

    }, [ecomerce]);


    // useEffect(async () => {
    //     if (ecomerce.cartItems) {
    //         setCurrentUser(await userData())
    //         setCodIsEnabled(await identifyCodStatus());
    //         getProducts(ecomerce.cartItems, 'cart');
    //         setDefaultValues();
    //     }
    // }, [ecomerce, currentUser]);
    //, currentUser
    if (products && products.length > 0) {
        totalAmount = calculateAmount(products);
    }

    const handleLoginSubmit = (event) => {
        event.preventDefault();
        const dt = new FormData(event.currentTarget);
        const orderInformation = {
            "fname": dt.get("firstName"),
            "lname": dt.get("lastName"),
            "shippingAddressPrimary": dt.get("address"),
            "landmark": dt.get("apprt"),
            "city": dt.get("city"),
            "postalCode": dt.get("pincode"),
            "products": ecomerce.cartItems,
            "contactNumber": dt.get("phone"),
            "totalBill": totalAmount,
            "token": getToken(),
            "state": dt.get("state"),
            "couponCode": coupon,
        //    "orderTotalAmtBeforeApplyingCoupon": orderTotalAmt,
            "paymentType": dt.get("paymentType")
        }

        
        if (orderInformation.products.length == 0) {
            Modal.error({
                centered: true,
                title: 'your shopping cart is empty!!',
            });
        } else if (orderInformation.fname.length == 0 || orderInformation.lname.length == 0) {
            Modal.error({
                centered: true,
                title: 'first name or last name is required!!',
            });
        } else if (orderInformation.shippingAddressPrimary.length == 0) {
            Modal.error({
                centered: true,
                title: 'address is required!!',
            });
        } else if (orderInformation.city.length == 0) {
            Modal.error({
                centered: true,
                title: 'city is required!!',
            });
        } else if (orderInformation.state.length == 0 || orderInformation.state == "select state" || orderInformation.state === "select state") {
            Modal.error({
                centered: true,
                title: 'select the state !!',
            });
        } else if (orderInformation.postalCode.length == 0) {
            Modal.error({
                centered: true,
                title: 'pincode is required!!',
            });
        } else {
            setLoader(true);
            placeOrder(orderInformation);
        }
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
                if (response.data.success) {
                    //var merchantId = response.data.data.merchantId;
                    // var transactionId = response.data.data.merchantTransactionId
                    router.push(response.data.data.instrumentResponse.redirectInfo.url + "");
                } else {
                    setLoader(false);
                    if (response.data === 'InvalidCouponCode') {
                        Modal.error({
                            centered: true,
                            title: 'Sorry, Invalid coupon code !!',
                        });
                    } else if (response.data === 'user_alredy_use_coupon') {
                        Modal.error({
                            centered: true,
                            title: 'Sorry, This Token Has Already Been Used !!',
                        });
                    } else if (response.data === 'cod_order_save') {
                        removeItems('cart');
                        window.location.assign("/account/orders?orId=OD-nuvio-2024&cod=true");
                    } else if (response.data === 'cash_on_delivery_not_available') {
                        Modal.error({
                            centered: true,
                            title: 'Sorry, Cash on Delivery not available!!',
                        });
                    } else {
                        const modal = Modal.error({
                            centered: true,
                            title: 'Something went wrong on server!!',
                            content: `Oops! It seems that something unexpected occurred on our end. We apologize for any inconvenience this may have caused. Our team has been notified and is working diligently to fix the issue. Please try again later, or feel free to contact our support team if you need immediate assistance. Thank you for your understanding.`,
                        });
                        modal.update;
                    }

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

        <form
            className="ps-form__billing-info"
            onSubmit={(e) => { handleLoginSubmit(e) }}>
            {userLoginStatus ?
                <div>
                    <div className="mx-2">
                        <h2>Delivery Information</h2>
                    </div>
                    <p className='mt-4'><b>✅ Your account is ready to go. please fill below delivery information to complete your order !! </b></p>
                    <h4 className="ps-form__heading">Self Information</h4>

                    <div className="row">
                        <div className="col-sm-6">
                            <div className="form-group">
                                <small className='text-muted'>first name <span className="text-danger">*</span></small>
                                <input type="text"
                                    name="firstName"
                                    className="form-control"
                                    defaultValue={currentUser?.firstName}
                                    placeholder='First Name'
                                    required="true"
                                // onChange={(e) => {
                                //     setReview({ ...review, fname: e.target.value })
                                // }}
                                />

                            </div>
                        </div>

                        <div className="col-sm-6">
                            <div className="form-group">
                                <small className='text-muted'>last name <span className="text-danger">*</span></small>
                                <input type="text"
                                    name="lastName"
                                    className="form-control"
                                    defaultValue={currentUser?.lastName}
                                    placeholder='Last Name'
                                    required="true"
                                // onChange={(e) => {
                                //     setReview({ ...review, last_name: e.target.value })
                                // }}
                                />

                            </div>
                        </div>
                    </div>

                    <div className="form-group">
                        <small className='text-muted'>Contact number <span className="text-danger">*</span></small>
                        <input type="tel"
                            className="form-control"
                            defaultValue={currentUser?.phone}
                            placeholder='Enter the contact number.'
                            required="true"
                            name="phone"
                            pattern="[7-9]{1}[0-9]{9}"
                            onChange={(e) => {
                                numberCheck(e.target.value)
                            }}
                        />
                        {
                            (inputErrors?.isError && inputErrors?.type == 'number') &&
                            <label className='text-danger' >{inputErrors.message}</label>
                        }


                    </div>
                    <h4 className="ps-form__heading">Address Information</h4>

                    <div className="form-group">
                        {/* <small className='text-muted'>Address <span className="text-danger">*</span></small> */}
                        <input type="text"
                            id='addressI'
                            className="form-control"
                            defaultValue={review?.address}
                            placeholder='Address'
                            name="address"
                            required="true"
                        // onChange={(e) => {
                        //     setReview({ ...review, address: e.target.value })
                        // }}
                        />

                    </div>
                    <div className="form-group">
                        {/* <small className='text-muted'>Apartment, suite, etc. </small> */}
                        <input type="text"
                            className="form-control"
                            name="apprt"
                            placeholder='Apartment, suite, etc. (optional)'
                        // onChange={(e) => {
                        //     setReview({ ...review, apprtment_name: e.target.value })
                        // }}
                        />

                    </div>
                    <div className="row">
                        <div className="col-sm-4">
                            <div className="form-group">
                                {/* <small className='text-muted'>City<span className="text-danger">*</span></small> */}
                                <input type="text"
                                    className="form-control"
                                    defaultValue={review?.city}
                                    required="true"
                                    placeholder='City '
                                    name="city"
                                // onChange={(e) => {
                                //     setReview({ ...review, city: e.target.value })
                                // }}
                                />
                            </div>
                        </div>
                        <div className="col-sm-4">
                            <div className="form-group">
                                {/* <small className='text-muted'>Select state<span className="text-danger">*</span></small> */}
                                <select name="state" onChange={(event) => { setReview({ ...review, state: event.target.value }) }} className='form-control'>
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
                                    <option>Telangana</option>
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
                                {/* <small className='text-muted'>Pincode <span className="text-danger">*</span></small> */}
                                <input type="text"
                                    className="form-control"
                                    defaultValue={review.postal_code}
                                    placeholder='Postal Code '
                                    required="true"
                                    name="pincode"
                                    pattern='[0-9]{6}'
                                // onChange={(e) => {
                                //     setReview({ ...review, postal_code: e.target.value })
                                // }}
                                />

                            </div>
                        </div>
                    </div>
                    <h4 className="ps-form__heading">Payment Modes</h4>
                    <div className="w-100">
                        <div className="form-group">
                            <select placeholder='select payment mode' name="paymentType" onChange={(event) => { setReview({ ...review, paymentType: event.target.value }) }} className='form-control'>
                                <option>Online Payment</option>
                                {codIsEnabled == 'enabled' &&
                                    <option>Cash on Delivery</option>
                                }
                            </select>
                        </div>
                    </div>

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

                                <button className="ps-btn">Place Order</button>
                            )
                            }
                        </div>
                    </div>

                </div>
                :
                <div className='mb-5'>
                    <GuestUserForm guestUserData={guestUserData} coupon={coupon} />
                    <h4 class="" className="">Already Registered? <span onClick={() => { goToLogin() }} style={{ cursor: "pointer" }}><u>Login Here</u></span></h4>
                </div>
            }

        </form>

    );
}
export default connect((state) => state)(FormCheckoutInformation);
//export default FormCheckoutInformation;
