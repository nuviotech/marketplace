import React, { useEffect, useState } from 'react';
import { DatePicker, Form, Input, Modal, Radio } from 'antd';
import { updateUserDetails } from '~/repositories/UserDeatils';
import { logOut } from '~/store/auth/action';

const FormChangeUserInformation = (data) => {
    const user = data.data;

    const [update, setUpdate] = useState({
        email: null,
        firstName: null,
        lastName: null,
        phone: null,
       // shippingAddress: null,
        //city: null,
       // country: null,
        password: null,
       // state: "Nan"
    });


    const updateData = async () => {
        await updateUserDetails(update);
        const modal = Modal.success({
            centered: true,
            title: 'information is updated Successfully! ',
            content: `your are logout at this time for reflect the changes on web, please login your new credentials if update .`,
            okText: "Logout",
            onOk: () => {
                logOut();
                window.location.assign("/account/login")
            }
        });
        modal.update();
    }

    function Action() {
        const modal = Modal.confirm({
            title: 'Confirmation!!',
            content: 'Are you shure to update this data ?',
            okText: 'Yes',
            cancelText: 'No',
            onOk: async () => {
                await updateData();
            },
        });
        modal.update()
    }


    return (

        <div className="ps-form--account-setting">
            <div className="ps-form__header">
                <h3>Account Information</h3>
            </div>
            <div className="ps-form__content">
                <div className="form-group">
                    <input
                        disabled="true"
                        className="form-control"
                        type="text"
                        placeholder="email address"
                        defaultValue={user.email}
                        onChange={(event) => { setUpdate({ ...update, email: event.target.value }) }}

                    />
                </div>
                <div className="row">
                    <div className="col-sm-6">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                placeholder="First name"
                                defaultValue={user.firstName}
                                onChange={(event) => { setUpdate({ ...update, firstName: event.target.value }) }}

                            />
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                placeholder="Last name"
                                defaultValue={user.lastName}
                                onChange={(event) => { setUpdate({ ...update, lastName: event.target.value }) }}

                            />
                        </div>
                    </div>

                    <div className="col-sm-6">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                placeholder="Phone Number"
                                defaultValue={user.phone}
                                onChange={(event) => { setUpdate({ ...update, phone: event.target.value }) }}

                            />
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="password"
                                placeholder="password"
                                defaultValue={"**********"}
                                onChange={(event) => { setUpdate({ ...update, password: event.target.value }) }}

                            />
                        </div>
                    </div>
                    {/*
                    <div className="col-sm-12">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                placeholder="Address"
                                defaultValue={user.shippingAddress}
                                onChange={(event) => { setUpdate({ ...update, shippingAddress: event.target.value }) }}

                            />
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                placeholder="City"
                                defaultValue={user.city}
                                onChange={(event) => { setUpdate({ ...update, city: event.target.value }) }}

                            />
                        </div>
                    </div>
                    <div className="col-sm-4">
                        <div className="form-group">
                            <select onChange={(event) => { setUpdate({ ...update, state: event.target.value }) }} className='form-control'>
                                <option>{user.state}</option>
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
                            <input
                                className="form-control"
                                type="text"
                                placeholder="Country"
                                defaultValue={user.country}
                                onChange={(event) => { setUpdate({ ...update, country: event.target.value }) }}
                            />
                        </div>
                    </div>*/}

                </div>

                <div >
                    <button className="ps-btn" onClick={() => { Action() }} >Update profile</button>

                </div>
            </div>
        </div>
    );
};

export default FormChangeUserInformation;
