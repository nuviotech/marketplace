import React, { useState } from 'react';
import { Button, Modal, Rate } from 'antd';
import Rating from '~/components/elements/Rating';
import Axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';
import { getToken, userIsLogin } from '~/store/auth/action';
import { Router } from 'next/router';

const PartialReview = ({ product }) => {
    var userLoginStatus = userIsLogin();
    const [rate, setRate] = useState(1);
    const [msg, setMsg] = useState('');
    const [name, setName] = useState();
    const [email, setEmail] = useState();

    //alert(JSON.stringify(product))
    const handleSubmit = () => {

        const obj = {
            rate: rate,
            review: msg,
            productId: product?.id,
            sellerId: product?.userId
        }
        if (rate >= 1) {
            Axios.post(`${marketplaceUrl}/saveProductRating`, obj, {
                headers: {
                    Authorization: "Bearer " + getToken(),
                }
            }).then(
                async (response) => {
                    if(response.data.status==0){
                        const modal = Modal.success({
                            centered: true,
                            title: 'Success!',
                            content: `Thank you for rate the product.`,
                        });
                    }else{
                        const modal = Modal.error({
                            centered: true,
                            title: 'Error!',
                            content: `Rating not save!!`,
                        });
                    }
                },
                (error) => {
                    alert("Some thing wrong, try later.")
                    console.log(error)
                }
            )

        }

    }

    const goTOLogin = () => {
        window.sessionStorage.setItem("action", window?.location?.href);
        window.location.assign("/account/login");
    }

    return (
        <div className="row">
            <div className="col-xl-5 col-lg-5 col-md-12 col-sm-12 col-12 ">
                <div className="ps-block--average-rating">
                    
                    {(product?.totalRatingRation != null && product?.totalRatingRation?.split(":")[0] > 0) &&
                        <>
                            <div className="ps-block__header">
                                <h3>{product?.totalRatingRation?.split(":")[0]}</h3>
                            </div>
                            <div className='mb-2 mt-0 pt-0'>
                                <Rate disabled defaultValue={product?.totalRatingRation?.split(":")[0]} /><br />
                                <span>review {product?.totalRatingRation?.split(":")[1]}</span>
                            </div>
                        </>
                    }

                    <div className="ps-block__star">
                        <span>5 Star</span>
                        <div className="ps-progress" data-value="100">
                            <span></span>
                        </div>
                        <span>100%</span>
                    </div>
                    <div className="ps-block__star">
                        <span>4 Star</span>
                        <div className="ps-progress" data-value="0">
                            <span></span>
                        </div>
                        <span>0</span>
                    </div>
                    <div className="ps-block__star">
                        <span>3 Star</span>
                        <div className="ps-progress" data-value="0">
                            <span></span>
                        </div>
                        <span>0</span>
                    </div>
                    <div className="ps-block__star">
                        <span>2 Star</span>
                        <div className="ps-progress" data-value="0">
                            <span></span>
                        </div>
                        <span>0</span>
                    </div>
                    <div className="ps-block__star">
                        <span>1 Star</span>
                        <div className="ps-progress" data-value="0">
                            <span></span>
                        </div>
                        <span>0</span>
                    </div>
                </div>
            </div>
            <div className="col-xl-7 col-lg-7 col-md-12 col-sm-12 col-12 ">

                <h4>Submit Your Review</h4>
                <p>
                    Your email address will not be published. Required fields
                    are marked
                    <sup>*</sup>
                </p>
                <div className="form-group form-group__rating">
                    <label className='mr-2'>Your rating of this product</label>
                    <Rate allowHalf defaultValue={1} onChange={(e) => { setRate(e) }} />
                </div>
                <div className="form-group">
                    <textarea
                        className="form-control"
                        rows="6"
                        onChange={(e) => { setMsg(e.target.value) }}
                        placeholder="Write your review here"></textarea>
                </div>
                {/* <div className="row">
                    <div className="col-xl-6 col-lg-6 col-md-6 col-sm-12  ">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="text"
                                required
                                onChange={(e) => { setName(e.target.value) }}
                                placeholder="Your Name"
                            />
                        </div>
                    </div>
                    <div className="col-xl-6 col-lg-6 col-md-6 col-sm-12  ">
                        <div className="form-group">
                            <input
                                className="form-control"
                                type="email"
                                required
                                onChange={(e) => { setEmail(e.target.value) }}
                                placeholder="Your Email"
                            />
                        </div>
                    </div>
                </div> */}
                {
                    userLoginStatus ?

                        <div className="form-group submit">
                            <button onClick={handleSubmit} className="ps-btn">Submit Review</button>
                        </div>
                        :
                        <button onClick={() => { goTOLogin() }} className="ps-btn"  >Login First </button>
                }

            </div>
        </div>
    );
};

export default PartialReview;
