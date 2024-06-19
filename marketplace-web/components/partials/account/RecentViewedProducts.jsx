import React, { Component, useContext, useEffect, useState } from 'react';
import Link from 'next/link';
import { logOut, userIsLogin } from '../../../store/auth/action';
import { userData } from '~/repositories/UserDeatils';
import { AuthContext } from '~/context/loginContext';
import { useDispatch } from 'react-redux';
import { useRouter } from 'next/router';

function RecentViewedProducts() {
    /* constructor(props) {
         super(props);
         this.state = {};
     }*/
    const dispatch = useDispatch();
    const Router = useRouter();

    //  render() {
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
        },
        {
            text: 'Recent Viewed Product',
            url: '/account/recent-viewed-product',
            active: true,
            icon: 'icon-store',
        },
        {
            text: 'Wishlist',
            url: '/account/wishlist',
            icon: 'icon-heart',
        },
    ];

    const { currentUser } = useContext(AuthContext);
    /*
    //getUserDetails();
    const [user,setUser]=useState([]);
    useEffect(async()=>{
        setUser(await userData())
    },[]);
    */


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
                                            <img src="/static/img/users/1.png" />
                                            <figure>
                                                <figcaption>Hello, <span className='text-capitalize'>{currentUser.firstName}</span></figcaption>
                                                <p>{currentUser.email}</p>
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
                                <section className="ps-section--account-setting">
                                    <div className="ps-section__content">
                                        <p>No product here.</p>
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                    :
                    <div className='text-center'>
                        <h3 className='text-danger text-center'>Please login first to access this page !!</h3>
                        <a onClick={() => {
                           // Router.push('/account/login');
                           window.location.assign("/account/login")
                        }} className='btn btn-lg btn-warning text-center'>Login Here !!</a>
                    </div>
            }
        </section>
    );
    // }
}

export default RecentViewedProducts;
