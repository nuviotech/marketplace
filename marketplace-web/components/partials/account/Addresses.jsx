import React, { Component, useContext, useEffect, useState } from 'react';
import Link from 'next/link';
import { logOut, userIsLogin } from '../../../store/auth/action';
import { userData } from '~/repositories/UserDeatils';
import { AuthContext } from '~/context/loginContext';
import { useRouter } from 'next/router';
import { useDispatch } from 'react-redux';

function Addresses() {
    /* constructor(props) {
         super(props);
         this.state = {};
     }*/
    const [currentUser, setCurrentUser] = useState();

    const dispatch = useDispatch();
    const Router = useRouter();
    // render() {
    const accountLinks = [
        {
            text: 'Account Information',
            url: '/account/user-information',
            icon: 'icon-user',
        },
        {
            text: 'Notifications',
            url: '/account/notifications',
            icon: 'icon-alarm-ringing',
        },
        {
            text: 'Orders',
            url: '/account/orders',
            icon: 'icon-papers',
        },
        {
            text: 'Address',
            url: '/account/addresses',
            icon: 'icon-map-marker',
            active: true,
        },
        {
            text: 'Recent Viewed Product',
            url: '/account/recent-viewed-product',
            icon: 'icon-store',
        },
        {
            text: 'Wishlist',
            url: '/account/wishlist',
            icon: 'icon-heart',
        },
    ];

    // const { currentUser } = useContext(AuthContext);


    //getUserDetails();
    // const [user,setUser]=useState([]);
    useEffect(async () => {
        setCurrentUser(await userData())
    }, []);
    console.warn(currentUser);
    return (
        <section className="ps-my-account ps-page--account">

            {
                userIsLogin() ?
                    <div className="container">
                        <div className="row">
                            <div className="col-lg-4">
                                <div className="ps-section__left">
                                    <aside className="ps-widget--account-dashboard">
                                        <div className="ps-widget__header">
                                            <img src="/static/img/users/3.jpg" />
                                            <figure>
                                                <figcaption>Hello, <span className='text-capitalize'>{currentUser?.firstName}</span></figcaption>
                                                <p>{currentUser?.email}</p>
                                            </figure>
                                        </div>
                                        <div className="ps-widget__content">
                                            <ul>
                                                {accountLinks.map(link => (
                                                    <li
                                                        key={link.text}
                                                        className={
                                                            link.active
                                                                ? 'active'
                                                                : ''
                                                        }>
                                                        <Link href={link.url}>
                                                            <a>
                                                                <i
                                                                    className={
                                                                        link.icon
                                                                    }></i>
                                                                {link.text}
                                                            </a>
                                                        </Link>
                                                    </li>
                                                ))}
                                                <li>

                                                    <a onClick={() => {
                                                        dispatch(logOut());
                                                        Router.push('/account/login');
                                                    }}>
                                                        <i className="icon-power-switch"></i>
                                                        Logout
                                                    </a>

                                                </li>
                                            </ul>
                                        </div>
                                    </aside>
                                </div>
                            </div>
                            <div className="col-lg-8">
                                <div className="ps-section--account-setting">
                                    <div className="ps-section__content">
                                        <div className="row">
                                            <div className="col-md-6 col-12">
                                                <figure className="ps-block--address">
                                                    <figcaption>
                                                        Billing address
                                                    </figcaption>
                                                    <div className="ps-block__content">
                                                        <p>
                                                            {
                                                                currentUser?.billingAddress ?
                                                                <>
                                                                    {currentUser?.billingAddress?.firstName} {currentUser?.billingAddress?.lastName} <br />
                                                                    {currentUser?.billingAddress?.streetAddress}<br />
                                                                    {currentUser?.billingAddress?.city}, {currentUser?.billingAddress?.state} {currentUser?.billingAddress?.country}<br />
                                                                    {currentUser?.billingAddress?.pincode}
                                                                </>
                                                                :
                                                                <>
                                                                    <p>Add billing address !!</p>
                                                                </>
                                                            }

                                                        </p>
                                                        <Link href="/account/edit-address?action=billing-address">
                                                            <a>Edit</a>
                                                        </Link>
                                                    </div>
                                                </figure>
                                            </div>
                                            <div className="col-md-6 col-12">
                                                <figure className="ps-block--address">
                                                    <figcaption>
                                                        Shipping address
                                                    </figcaption>
                                                    <div className="ps-block__content">
                                                        <p>
                                                            {
                                                                currentUser?.shippingAddress ?
                                                                <>
                                                                    {currentUser?.shippingAddress?.firstName} {currentUser?.shippingAddress?.lastName} <br />
                                                                    {currentUser?.shippingAddress?.streetAddress}<br />
                                                                    {currentUser?.shippingAddress?.city}, {currentUser?.shippingAddress?.state} {currentUser?.shippingAddress?.country}<br />
                                                                    {currentUser?.shippingAddress?.pincode}
                                                                </>
                                                                :
                                                                <>
                                                                    <p>Add shipping address !!</p>
                                                                </>
                                                            }
                                                        </p>
                                                        <Link href="/account/edit-address?action=shipping-address">
                                                            <a>Edit</a>
                                                        </Link>
                                                    </div>
                                                </figure>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    :
                    <div className='text-center'>
                        <h3 className='text-danger text-center'>Please login first to access this page !!</h3>
                        <a onClick={() => {
                            //Router.push('/account/login');
                            window.location.assign("/account/login")
                        }} className='btn btn-lg btn-warning text-center'>Login Here !!</a>
                    </div>
            }

        </section>
    );
    // }
}

export default Addresses;
