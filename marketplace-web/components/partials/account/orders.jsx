import React, { Component, useContext, useEffect, useState } from 'react';
import AccountMenuSidebar from './modules/AccountMenuSidebar';
import TableInvoices from './modules/TableInvoices';
import Link from 'next/link';
import { getToken, logOut, userIsLogin } from '../../../store/auth/action';
import { userData } from '~/repositories/UserDeatils';
import { AuthContext } from '~/context/loginContext';
import { useDispatch } from 'react-redux';
import { useRouter } from 'next/router';
import axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';
import { Modal } from 'antd';
import useEcomerce from '~/hooks/useEcomerce';

function Invoices() {
    /* constructor(props) {
         super(props);
         this.state = {
             user:null
         };
     }
 */
    const [currentUser, setUser] = useState([]);

    const dispatch = useDispatch();
    const { removeItems } = useEcomerce();
    const [reset,setReset] =useState(false);

    const router = useRouter();
    const { flag } = router.query;
    const { actionType } = router.query;
    const { txid } = router.query;
    
    console.warn("flag = "+flag+" txid = "+txid);

    console.warn(JSON.stringify(router.query));
    if (flag == 1 || flag === 1) {
        console.log("inside flag condition.........")
        const data={
            "txid" : txid
        }
        
        axios.post(`${marketplaceUrl}/updateOrder`,data,{
            headers: {
                Authorization: "Bearer " + getToken(),
            }
        }).then(
            async (response) => {
                if(response.data==0){
                    const modal = Modal.success({
                        centered: true,
                        title: 'Order ID : '+txid,
                        content: `You'r order place successfully, thanks for order the product.`,
                    });
                    removeItems("cart")
                    //window.location.assign("/account/orders")
                    setReset(true);
                    modal.update;
                }else if(response.data==1){
                    const modal = Modal.error({
                        centered: true,
                        title: 'Order ID : '+txid,
                        content: `payment failed`,
                    });
                    //window.location.assign("/account/orders")
                    modal.update;
                }

            },
            (error) => {
                //order details is not save to database
                alert("Something went wrong! "+JSON.stringify(error));
                console.log("error : " + JSON.stringify(error));
            }
        )
    }

    //render() {
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
            active: true,
        },
        {
            text: 'Address',
            url: '/account/addresses',
            icon: 'icon-papers',
        },
        {
            text: 'Recent Viewed Product',
            url: '/account/recent-viewed-product',
            icon: 'icon-papers',
        },
        {
            text: 'Wishlist',
            url: '/account/wishlist',
            icon: 'icon-papers',
        },
    ];

    // const {currentUser}= useContext(AuthContext);

    //getUserDetails();
    useEffect(async () => {
        setUser(await userData())
    }, [reset]);


    return (
        <section className="ps-my-account ps-page--account">
            {
                userIsLogin() ?
                    <div className="container">
                        <div className="row">
                            <div className="col-lg-4">
                                <div className="ps-page__left">
                                    <aside className="ps-widget--account-dashboard">
                                        <div className="ps-widget__header">
                                            <img src="/static/img/users/3.jpg" />
                                            <figure>
                                                <figcaption>Hello <span className='text-capitalize'>{currentUser.firstName}</span></figcaption>
                                                <p>{currentUser.email}</p>
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
                                                        router.push('/account/login');
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
                                <div className="ps-page__content">
                                    <div className="ps-section--account-setting">
                                        <div className="ps-section__header">
                                            <h3>Orders</h3>
                                        </div>
                                        <div className="ps-section__content">
                                            <TableInvoices data={currentUser} />
                                            
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
                           // Router.push('/account/login');
                           window.location.assign("/account/login")
                        }} className='btn btn-lg btn-warning text-center'>Login Here !!</a>
                    </div>
            }
        </section>
    );
    //  }
}

export default Invoices;
