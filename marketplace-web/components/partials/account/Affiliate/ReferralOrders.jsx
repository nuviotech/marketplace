import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { userIsLogin, logOut } from '../../../../store/auth/action';
import { useDispatch } from 'react-redux';
import { useRouter } from 'next/router';
import { getAffiliateAccountReferralOrders } from './action';
import { Pagination, Table } from 'antd';


const ReferralOrders = () => {

    const dispatch = useDispatch();
    const Router = useRouter();
    const [referralOrders, setReferralOrders] = useState([]);

    // State to handle pagination
    const [page, setPage] = useState(0);

    const accountLinks = [
        {
            text: 'Account Information',
            url: '/account/Affiliate_marketing/user-information',
            icon: 'icon-user',
            active: false,
        },
        {
            text: 'Referral Orders',
            url: '/account/Affiliate_marketing/referral-orders',
            icon: 'icon-user',
            active: true,
        }
    ];

    useEffect(async () => {
        setReferralOrders(await getAffiliateAccountReferralOrders(page))
    }, [page]);

    // Table columns configuration
    const columns = [
        { title: 'ID', dataIndex: 'referralOrderId', key: 'referralOrderId' },
        { title: 'Buyer Id', dataIndex: 'buyerId', key: 'buyerId' },
        { title: 'Order Id', dataIndex: 'orderId', key: 'orderId' },
        {
            title: 'Commission Rate', dataIndex: 'commissionRate', key: 'commissionRate',
            render: (text) => `${text}%`, // Custom render function
        },
        {
            title: 'Purchased Amount', dataIndex: 'purchasedAmount', key: 'purchasedAmount',
            render: (text) => `${text} ₹`, // Custom render function
        },
        {
            title: 'Payment Distributed', dataIndex: 'paymentSettlement', key: 'paymentDistribute',
            render:(text)=>{
                if(text==='Y')
                    return 'YES'
                else
                    return 'NO' 
            }
        },
    ];



    return (
        <section className="ps-my-account ps-page--account">
            {
                userIsLogin() ?
                    <div className="container-fluid">
                        <div className="row text-left">
                            <div className="col-lg-3">
                                <div className="ps-section__left">
                                    <aside className="ps-widget--account-dashboard">

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
                                            <h3>Referral Orders</h3>
                                        </div>
                                        <div className="ps-form__content">
                                            {/* tables */}
                                            <Table
                                                dataSource={referralOrders?.content}
                                                columns={columns}
                                                pagination={false}
                                            />
                                            <Pagination pageSize={referralOrders?.pageable?.pageSize || 0} onChange={(e) => { setPage(e - 1) }} defaultCurrent={1} total={referralOrders?.totalPages} />
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

export default ReferralOrders;
