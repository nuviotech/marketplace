import React, { Component, useContext, useEffect, useState } from 'react';
import Link from 'next/link';
import { logOut, userIsLogin, getToken } from '../../../store/auth/action';
import { userData } from '~/repositories/UserDeatils';
import { AuthContext } from '~/context/loginContext';
import { useDispatch } from 'react-redux';
import { useRouter } from 'next/router';
import axios from 'axios';
import { marketplaceUrl } from '~/repositories/Repository';

function RecentViewedProducts() {
    const dispatch = useDispatch();
    const Router = useRouter();
    const { currentUser } = useContext(AuthContext);
    const [recentlyViewedProducts, setRecentlyViewedProducts] = useState([]);
    const [loading, setLoading] = useState(true);

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

    // Fetch recently viewed products on component mount
    useEffect(() => {
        const fetchRecentlyViewed = async () => {
            if (userIsLogin()) {
                try {
                    const response = await axios.get(`${marketplaceUrl}/getRecentlyViewedProducts`, {
                        headers: {
                            Authorization: "Bearer " + getToken(),
                        }
                    });
                    
                    if (response.data && response.data.data) {
                        setRecentlyViewedProducts(response.data.data);
                    }
                } catch (error) {
                    console.error("Error fetching recently viewed products:", error);
                } finally {
                    setLoading(false);
                }
            } else {
                setLoading(false);
            }
        };

        fetchRecentlyViewed();
    }, []);

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
                                    <div className="ps-section__header">
                                        <h3>Recently Viewed Products</h3>
                                    </div>
                                    <div className="ps-section__content">
                                        {loading ? (
                                            <p>Loading...</p>
                                        ) : recentlyViewedProducts && recentlyViewedProducts.length > 0 ? (
                                            <div className="table-responsive">
                                                <table className="table ps-table--shopping-cart">
                                                    <thead>
                                                        <tr>
                                                            <th>Product Name</th>
                                                            <th>Price</th>
                                                            <th>Viewed Date</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        {recentlyViewedProducts.map((product) => (
                                                            <tr key={product.id}>
                                                                <td>
                                                                    <strong>
                                                                        {product.productName && product.productName.length > 50
                                                                            ? product.productName.substring(0, 50) + "..."
                                                                            : product.productName}
                                                                    </strong>
                                                                </td>
                                                                <td className="price">
                                                                    ₹{product.productPrice}
                                                                </td>
                                                                <td>
                                                                    {new Date(product.viewedDate).toLocaleDateString()}
                                                                </td>
                                                                <td>
                                                                    <Link href={`/product/${product.productId}`}>
                                                                        <a className="btn btn-sm btn-outline-primary">View Product</a>
                                                                    </Link>
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </table>
                                            </div>
                                        ) : (
                                            <p>No recently viewed products.</p>
                                        )}
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                    :
                    <div className='text-center'>
                        <h3 className='text-danger text-center'>Please login first to access this page !!</h3>
                        <a onClick={() => {
                           window.location.assign("/account/login")
                        }} className='btn btn-lg btn-warning text-center'>Login Here !!</a>
                    </div>
            }
        </section>
    );
}

export default RecentViewedProducts;
