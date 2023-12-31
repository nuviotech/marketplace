import React, { Component, useContext, useEffect, useState } from 'react';
import AccountMenuSidebar from './modules/AccountMenuSidebar';
import TableNotifications from './modules/TableNotifications';
import Link from 'next/link';
import ProductCart from '../../elements/products/ProductCart';
import { AuthContext } from '~/context/loginContext';
import { useRouter } from 'next/router';
import { Button, Form, Input, Modal, Select } from 'antd';
import { Option } from 'antd/lib/mentions';

const InvoiceDetail = () => {

    const { currentUser } = useContext(AuthContext);
    const router = useRouter();
    const [orderDetails, setOrderDetails] = useState([]);

    const [state, setState] = useState({
        action: 'N',
        open: false,
        email: currentUser?.email,
        reason: 'none',
        orderProductId: '',
        categoryId: 0,
        paymentId: 0,
        amount: 0,
        productName: ''
    });

    const { odId } = router.query;


    console.warn(currentUser)
    //main returnOrder function
    const returnOrder = async () => {
        if (state.orderProductId == '' || state.categoryId == 0 || state.paymentId == 0 || state.amount == 0) {
            alert("something went wrong !!");
            setState({ open: false });
            return;
        }

        const data = {
            email: state.email,
            reason: state.reason,
            paymentId: state.paymentId,
            amt: state.amount,
            orderProductId: state.orderProductId,
            categoryId: state.categoryId,
            userId: currentUser.userId,
            productName: state.productName
        }
        // alert(JSON.stringify(data))
        //this.setState({ action: await returnPolicyByUser(data) });

        if (state.action == '0') {
            Modal.success({
                centered: true,
                title: 'Success Information..',
                content: `Now your order is under return policy, please wait for some time...`,
            });
            setState({ open: false });
        } else if (state.action == "1") {
            Modal.warning({
                centered: true,
                title: 'Opps, too late !!',
                content: `Your return policy period is expired !!`,
            });
            setState({ open: false });
        } else if (state.action == "-1") {
            Modal.error({
                centered: true,
                title: 'Opps, Something wrong !!',
                content: `Server is down , please try later.`,
            });
            setState({ open: false });
        }
    }

    const openModel = (oid, catId, payId, amt, title) => {
        setState({ orderProductId: oid });
        setState({ categoryId: catId });
        setState({ paymentId: payId });
        setState({ amount: amt });
        setState({ productName: title })
        setState({ open: true });
    }




    function check() {
        if (odId == null) {
            const modal = Modal.error({
                centered: true,
                title: 'Invalid Action',
                content: `Order id is not present, please try with correct order id...`,
            });
            modal.update;
            router.push("/account/orders");
        } else {

            currentUser.orders?.map((data) => {
                if (data.orderId === odId) {
                    setOrderDetails(data);
                }
            })
            console.warn("__________________________");
            console.warn(orderDetails.invoices)
        }
    }

    useEffect(async () => {
        check();
    }, []);
    // return {
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
    /*const invoiceProducts = [
        {
            id: '6',
            thumbnail: '/static/img/products/shop/5.jpg',
            title: 'Grand Slam Indoor Of Show Jumping Novel',
            vendor: "Robert's Store",
            sale: true,
            price: '32.99',
            salePrice: '41.00',
            rating: true,
            ratingCount: '4',
            badge: [
                {
                    type: 'sale',
                    value: '-37%',
                },
            ],
        },
        {
            id: '7',
            thumbnail: '/static/img/products/shop/6.jpg',
            title: 'Sound Intone I65 Earphone White Version',
            vendor: 'Youngshop',
            sale: true,
            price: '100.99',
            salePrice: '106.00',
            rating: true,
            ratingCount: '5',
            badge: [
                {
                    type: 'sale',
                    value: '-5%',
                },
            ],
        },
    ];*/
    return (
        <section className="ps-my-account ps-page--account">
            <div className="container">
                <div className="row">
                    <div className="col-lg-4">
                        <div className="ps-page__left">
                            <AccountMenuSidebar data={accountLinks} />
                        </div>
                    </div>
                    <div className="col-lg-8">
                        <div className="ps-page__content">
                            <div className="ps-section--account-setting">
                                <div className="ps-section__header">
                                    <h3>
                                        Order ID - {odId}
                                        
                                    </h3>
                                </div>
                                <div className="ps-section__content">
                                    <div className="row">
                                        <div className="col-md-4 col-12">
                                            <figure className="ps-block--invoice">
                                                <figcaption>
                                                    Address
                                                </figcaption>
                                                <div className="ps-block__content">
                                                    <strong>
                                                        {orderDetails?.fname} {orderDetails?.lname}
                                                    </strong>
                                                    <p>
                                                        {orderDetails?.shippingAddressPrimary}<br />
                                                        {orderDetails?.landmark}
                                                        {orderDetails?.city}&nbsp; ,{orderDetails?.postalCode}
                                                    </p>
                                                    <p>
                                                        Phone: {orderDetails?.contactNumber}
                                                    </p>
                                                </div>
                                            </figure>
                                        </div>
                                        <div className="col-md-4 col-12">
                                            <figure className="ps-block--invoice">
                                                <figcaption>
                                                    Shipping Fee
                                                </figcaption>
                                                <div className="ps-block__content">
                                                    <p>
                                                        Shipping Fee: Free
                                                    </p>
                                                </div>
                                            </figure>
                                        </div>
                                        <div className="col-md-4 col-12">
                                            <figure className="ps-block--invoice">
                                                <figcaption>
                                                    Payment
                                                </figcaption>
                                                <div className="ps-block__content">
                                                    <p>
                                                        Total Bill : {orderDetails?.totalBill}<br />
                                                        Payment Method: -
                                                    </p>
                                                </div>
                                            </figure>
                                        </div>
                                    </div>
                                    <div className="table-responsive">
                                        <table className="table ps-table--shopping-cart">
                                            <thead>
                                                <tr>
                                                    <th>Product</th>
                                                    <th>Price</th>
                                                    <th>Quantity</th>
                                                    <th>Status</th>
                                                    <th>Action</th>

                                                </tr>
                                            </thead>
                                            <tbody>
                                                {orderDetails?.invoices?.map((invoice)=> (


                                                    
                                                        invoice?.items?.map(
                                                            product => (
                                                                <tr
                                                                    key={
                                                                        product?.orderProductId
                                                                    }>
                                                                    <td>
                                                                        {
                                                                            product?.product_name?.length > 83 ?
                                                                                product?.product_name?.substring(0, 83) + "..." :
                                                                                product?.product_name

                                                                        }
                                                                    </td>
                                                                    <td className="price">
                                                                        ₹
                                                                        {
                                                                            product?.sole_price
                                                                        }
                                                                    </td>

                                                                    <td>{product?.quantity}</td>
                                                                    <td className="status">

                                                                        {
                                                                            product?.status
                                                                        }
                                                                    </td>
                                                                    <td className='text-center'>
                                                                        {

                                                                        }
                                                                    </td>
                                                                </tr>
                                                            )
                                                        )
                                                    


                                                ))}

                                            </tbody>
                                        </table>
                                    </div>
                                    <Link href="/account/orders">
                                        <a className="ps-btn ps-btn--sm ">
                                            Back to orders
                                        </a>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <Modal
                open={state.open}
                title="Return policy"

                onCancel={() => { setState({ open: false }) }}
                footer={[

                    <Button key="submit" type="primary" onClick={() => { returnOrder() }}>
                        Submit
                    </Button>,
                    <Button key="close" type="secondary" onClick={() => { setState({ open: false }) }}>
                        Close
                    </Button>
                ]}
            >
                <Form

                    name="control-hooks"

                    style={{ maxWidth: 600 }}
                >
                    <Form.Item name="note" label="Email" rules={[{ required: true }]}>
                        <Input defaultValue={state.email} onChange={(event) => { setState({ email: event.target.value }) }} />
                    </Form.Item>

                    <Form.Item name="gender" label="Reason" rules={[{ required: true }]}>
                        <Select
                            placeholder="Select a option and change input text above"
                            onChange={(event) => { setState({ reason: event }) }}
                            allowClear
                        >
                            <Option value="Damage product arrived.">Damage product arrived.</Option>
                            <Option value="Package is open">Package is open</Option>
                            <Option value="other">other</Option>
                        </Select>
                    </Form.Item>
                </Form>
            </Modal>

        </section>
    );

}

export default InvoiceDetail;
