import { Modal } from 'antd';
import Axios from 'axios';
import { Router, useRouter } from 'next/router';
import React, { Component, useContext, useEffect, useState } from 'react';
import { AuthContext } from '~/context/loginContext';
import { marketplaceUrl } from '~/repositories/Repository';
import { getToken, userIsLogin } from '~/store/auth/action';

function FormEditAddress({ type }) {
    const { currentUser } = useContext(AuthContext);
    const [firstName, setFirstName] = useState();
    const [lastName, setLastName] = useState();
    const [country, setCountry] = useState();
    const [streetAddress, setStreetAddress] = useState();
    const [state, setState] = useState("Maharashtra");
    const [pincode, setPincode] = useState();
    const [city, setCity] = useState(null);
    const [email, setEmail] = useState();

    const Router = useRouter();
    if (!userIsLogin()) {
        //window.location.assign("/account/login")
        Router.push("/account/login");
    }
    const setBillingAddressDefaultValues = () => {
        setFirstName(currentUser?.billingAddress?.firstName);
        setLastName(currentUser?.billingAddress?.lastName);
        setCity(currentUser?.billingAddress?.city);
        setStreetAddress(currentUser?.billingAddress?.streetAddress);
        setCountry(currentUser?.billingAddress?.country);
        setEmail(currentUser?.billingAddress?.email);
        setPincode(currentUser?.billingAddress?.pincode);
        setState(currentUser?.billingAddress?.state);
    }

    const setShippingAddressDefaultValues = () => {
        setFirstName(currentUser?.shippingAddress?.firstName);
        setLastName(currentUser?.shippingAddress?.lastName);
        setCity(currentUser?.shippingAddress?.city);
        setStreetAddress(currentUser?.shippingAddress?.streetAddress);
        setCountry(currentUser?.shippingAddress?.country);
        setEmail(currentUser?.shippingAddress?.email);
        setPincode(currentUser?.shippingAddress?.pincode);
        setState(currentUser?.shippingAddress?.state);
    }
    useEffect(() => {
        if (type === "billing-address") {
            setBillingAddressDefaultValues();
        } else if (type === "shipping-address") {
            setShippingAddressDefaultValues();
        } else {
            Router.push("/account/addresses");
        }
    }, []);

    const saveAddress = () => {
        const data = {
            firstName: firstName,
            lastName: lastName,
            country: country,
            streetAddress: streetAddress,
            state: state,
            email: email,
            city: city,
            pincode: pincode,
            loginUserEmail: currentUser.email,
            type: type
        }

        Axios.post(`${marketplaceUrl}/saveAddress`, data, {
            headers: {
                Authorization: "Bearer " + getToken(),
            }
        }).then(
            async (response) => {
                if (response.data === "done") {
                    const modal = Modal.success({
                        centered: true,
                        title: 'Address save!',
                        content: `Your ` + type + " is changed successfully.",
                    });
                    modal.update;
                    Router.push("/account/addresses");
                }
            },
            (error) => {
                //order details is not save to database
                alert("Something went wrong! ");

                console.log("error : " + JSON.stringify(error));
            }
        )

    }

    return (
        <form onSubmit={()=>{saveAddress()}} className="ps-form--edit-address">
            <div className="ps-form__header">
                {type === "billing-address" && <h3>Billing address</h3>}
                {type === "shipping-address" && <h3>Shipping address</h3>}
            </div>
            <div className="ps-form__content">
                <div className="form-group">
                    <label>
                        FirstName <sup>*</sup>
                    </label>
                    <input required="true" value={firstName} onChange={(event) => { setFirstName(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group">
                    <label>
                        Lastname <sup>*</sup>
                    </label>
                    <input required="true" value={lastName} onChange={(event) => { setLastName(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group">
                    <label>
                        City
                    </label>
                    <input value={city} onChange={(event) => { setCity(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group">
                    <label>
                        Country <sup>*</sup>
                    </label>
                    <input required="true" value={country} onChange={(event) => { setCountry(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group">
                    <label>
                        Street Address <sup>*</sup>
                    </label>
                    <input required="true" value={streetAddress} onChange={(event) => { setStreetAddress(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group">
                    <label>
                        State <sup>*</sup>
                    </label>

                    <select onChange={(event) => { setState(event.target.value) }} className='form-control'>
                        <option>{state}</option>
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
                <div className="form-group">
                    <label>
                        Pincode <sup>*</sup>
                    </label>
                    <input required="true" value={pincode} onChange={(event) => { setPincode(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group">
                    <label>
                        Email address <sup>*</sup>
                    </label>
                    <input required="true" value={email} onChange={(event) => { setEmail(event.target.value) }} type="text" placeholder="" className="form-control" />
                </div>
                <div className="form-group submit">
                    <button className="ps-btn">Save Address</button>
                </div>
            </div>
        </form>
    );

}

export default FormEditAddress;
