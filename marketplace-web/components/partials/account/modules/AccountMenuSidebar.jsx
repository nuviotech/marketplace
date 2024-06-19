import React, { useContext } from 'react';
import Link from 'next/link';
import { useDispatch } from 'react-redux';
import { useRouter } from 'next/router';
import { logOut } from '~/store/auth/action';
import { AuthContext } from '~/context/loginContext';

const AccountMenuSidebar = ({ data }) => {
    const dispatch=useDispatch();
    const Router= useRouter();
    const {currentUser}= useContext(AuthContext);

    return(
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
                {data.map(link => (
                    <li key={link.text} className={link.active ? 'active' : ''}>
                        <Link href={link.url}>
                            <a>
                                <i className={link.icon}></i>
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
    );
}

export default AccountMenuSidebar;
