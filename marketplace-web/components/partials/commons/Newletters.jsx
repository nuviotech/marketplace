import { Modal, notification } from 'antd';
import Axios from 'axios';
import React, { useState } from 'react';
import { marketplaceUrl } from '~/repositories/Repository';

const Newsletters = ({ layout }) => {

    const [email, setEmail] = useState('');

    const saveSubscriber = async () => {
        if (email != '') {
            const data = await Axios.post(`${marketplaceUrl}/saveSubscriberEmail?subscriberEmail=` + email).then(
                (response) => {
                    return response.data;
                },
                (error) => {
                    alert(error);
                    return error;
                }
            ).catch(err => {
                return (err)
            });
            if (data == '0') {
                const modal = Modal.success({
                    centered: true,
                    title: 'Email save.',
                    content: `your email is save successfully !!`,
                });
                modal.update;
            }
            if (data == "1") {
                const modal = Modal.info({
                    centered: true,
                    title: 'Email alredy present.',
                    content: `your email is save already!!`,
                });
                modal.update;
            }

            if (data == '-1') {
                const modal=Modal.error({
                    centered: true,
                    title: 'Something went wrong!!',
                });
                modal.update;

            }
        }
    }

    return (
        <section className="ps-newsletter">
            <div className={layout && layout === 'container' ? ' container' : 'ps-container'}>

                <div className="row">
                    <div className="col-xl-5 col-lg-12 col-md-12 col-sm-12 col-12 ">
                        <div className="ps-form__left">
                            <h3>Newsletter</h3>
                            <p>Subcribe to get information about products and coupons</p>
                        </div>
                    </div>
                    <div className="col-xl-7 col-lg-12 col-md-12 col-sm-12 col-12 ">
                        <div className="ps-form__right">
                            <div className="form-group--nest">
                                <input
                                    className="form-control"
                                    type="email"
                                    placeholder="Email address"
                                    onChange={(e) => { setEmail(e.target.value) }}
                                />
                                <button onClick={() => { saveSubscriber() }} className="ps-btn">Subscribe</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </section>
    );
}

export default Newsletters;
