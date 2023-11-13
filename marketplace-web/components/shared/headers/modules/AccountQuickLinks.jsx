import React from 'react';
import { connect, useDispatch } from 'react-redux';
import Link from 'next/link';
import { logOut,userIsLogin,login } from '~/store/auth/action';
import { useRouter } from 'next/router';
import Login from '~/components/partials/account/Login';
//import { login,saveToken} from '../../../store/auth/action';
import LoginLogout from './LoginLogoutManager';
const AccountQuickLinks = (props) => {
   // alert(JSON.stringify(props))
    const dispatch = useDispatch();
    const Router = useRouter();
    
    const handleLogout = (e) => {
        e.preventDefault();
        dispatch(logOut());
        Router.push('/account/login');

    };
    const accountLinks = [
        {
            text: 'Account Information',
            url: '/account/user-information',
        },
        {
            text: 'Notifications',
            url: '/account/notifications',
        },
        {
            text: 'Orders',
            url: '/account/orders',
        },
        {
            text: 'Address',
            url: '/account/addresses',
        },
        {
            text: 'Recent Viewed Product',
            url: '/account/recent-viewed-product',
        },
        {
            text: 'Wishlist',
            url: '/account/wishlist',
        },
    ];

    Login.test;
    const { isLoggedIn } = props;
   // props.dispatch(login());

    //const isLoggedIn  = userIsLogin();
    /*if(userIsLogin()){
        props.isLoggedIn=true,
        props.auth.isLoggedIn=true,
        props.setting.currency.symbol='RS',
        props.setting.currency.text='INR',
        props.app.isShowDemoPanel=true
    }*/
    //alert(isLoggedIn)
    // View
    const linksView = accountLinks.map((item) => (
        <li key={item.text}>
            <Link href={item.url}>
                <a>{item.text}</a>
            </Link>
        </li>
    ));

    return (
        <div>
            <LoginLogout />
           {
           
    isLoggedIn?
            <div className="ps-block--user-account">
                
                <i className="icon-user"></i>
                <div className="ps-block__content">
                    <ul className="ps-list--arrow">
                        {linksView}
                        <li className="ps-block__footer">
                            <a href="#" onClick={(e) => handleLogout(e)}>
                                Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            :
            <div className="ps-block--user-header">
                                

                <div className="ps-block__left">
                    <i className="icon-user"></i>
                </div>
                <div className="ps-block__right">
                    <Link href="/account/login">
                        <a>Login</a>
                    </Link>
                    <Link href="/account/register">
                        <a>Register</a>
                    </Link>
                </div>
            </div>
            
            }
            
        </div>
        
    )
};

export default connect((state) => state)(AccountQuickLinks);
