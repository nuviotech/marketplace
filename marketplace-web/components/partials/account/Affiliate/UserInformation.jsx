import React, { Component, useContext, useEffect, useState } from 'react';
import Link from 'next/link';
import FormChangeUserInformation from '~/components/shared/FormChangeUserInformation';
import { userIsLogin, logOut } from '../../../../store/auth/action';
import { userData } from '~/repositories/UserDeatils';
import { AuthContext } from '~/context/loginContext';
import { useDispatch } from 'react-redux';
import { useRouter } from 'next/router';
import { getAffiliateAccountDetails } from './action';
import { FacebookIcon, FacebookShareButton, TelegramIcon, TelegramShareButton, WhatsappIcon, WhatsappShareButton } from 'react-share';
import { notification } from 'antd';

const UserInformation = () => {

    const dispatch = useDispatch();
    const Router = useRouter();
    const [user, setUser] = useState(null);

    const accountLinks = [
        {
            text: 'Account Information',
            url: '/account/user-information',
            icon: 'icon-user',
            active: true,
        }
    ];

    //Views
    const accountLinkView = accountLinks.map((item) => (
        <li key={item.text} className={item.active ? 'active' : ''}>
            <Link href={item.url}>
                <a>
                    <i className={item.icon}></i>
                    {item.text}
                </a>
            </Link>
        </li>
    ));



    //const { currentUser } = useContext(AuthContext);
    //alert("current user is :"+JSON.stringify(currentUser));
    /*
     getUserDetails();
      const [user,setUser]=useState([]);
      useEffect(async()=>{
        setUser( await userData())
      },[]);
    
    */

    const copyToClipboard = () => {
        const textToCopy = `https://nuvio.in/account/register?affiliate_account_id=${user?.reportingAccountId}`;
        navigator.clipboard.writeText(textToCopy)
            .then(() => {
                notification["success"]({
                    message: 'Link coppied!!',
                });
            })
            .catch((error) => {
                console.error("Unable to copy text: ", error);
            });
    };


    const share = <>
        <FacebookShareButton url={`https://nuvio.in/account/register?affiliate_account_id=${user?.reportingAccountId}`}>
            <FacebookIcon size={33} logoFillColor='white' >

            </FacebookIcon>
        </FacebookShareButton>

        <WhatsappShareButton url={`https://nuvio.in/account/register?affiliate_account_id=${user?.reportingAccountId}`}>
            <WhatsappIcon size={33} logoFillColor='white'></WhatsappIcon>
        </WhatsappShareButton>

        <TelegramShareButton url={`https://nuvio.in/account/register?affiliate_account_id=${user?.reportingAccountId}`}>
            <TelegramIcon size={33} logoFillColor='white'>
            </TelegramIcon>
        </TelegramShareButton>

    </>

    useEffect(async () => {
        setUser(await getAffiliateAccountDetails())
    }, []);

    return (
        <section className="ps-my-account ps-page--account">
            {
                userIsLogin() ?
                    <div className="container">

                        <div className="row text-left">
                            <div className="col-lg-3">
                                <div className="ps-section__left">
                                    <aside className="ps-widget--account-dashboard">
                                        <div className="ps-widget__header">
                                            <img src="/static/img/users/1.png" />
                                            <figure>
                                                <figcaption>Account ID: <span className='text-capitalize'>{user?.reportingAccountId}</span></figcaption>
                                                <p>{user?.email}</p>
                                            </figure>
                                        </div>
                                        <div className="ps-widget__content">
                                            <ul className="ps-list--user-links">
                                                {accountLinks.map((link) => (
                                                    <li
                                                        key={link.text}
                                                        className={
                                                            link.active ? 'active' : ''
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
                                                        Router.push('/page/add_affiliate_accnt');
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
                            <div className="col-lg-9">
                                <div className="ps-page__content">
                                    <div className="ps-form--account-setting">
                                        <div className="ps-form__header">
                                            <h3>Account Information</h3>
                                        </div>
                                        <div className="ps-form__content">
                                            <h4>Account Name : {user?.reportingAccountName}</h4>
                                            <h4>Account Id : {user?.reportingAccountId}</h4>
                                            <h4>Created Date : {user?.createdDtTime}</h4>
                                            <h4>Share Link : {share}</h4>
                                            <h4><button className='btn btn-md btn-warning'  onClick={copyToClipboard}>Copy Share Link</button></h4>
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
                            window.location.assign("/page/add_affiliate_accnt")
                        }} className='btn btn-lg btn-warning text-center'>Login Here !!</a>
                    </div>
            }
        </section>
    );
};

export default UserInformation;
