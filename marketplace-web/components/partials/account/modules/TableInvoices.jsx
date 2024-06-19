import React, { Component } from 'react';
import { Table, Divider, Tag, Button, Modal, Form, Input, Select } from 'antd';
import Link from 'next/link';
import { getToken, userIsLogin } from '~/store/auth/action';
import { marketplaceUrl } from '~/repositories/Repository';
import axios from 'axios';
import { returnPolicyByUser } from '~/repositories/UserDeatils';


class TableInvoices extends Component {
    constructor(props) {
        super(props);
        //alert(JSON.stringify(props.data.orders))
        /*
        this.state = {
            action: 'N',
            open: false,
            email: this.props.data.email,
            reason: 'none',
            orderProductId: '',
            categoryId: 0,
            paymentId: 0,
            amount: 0,
            productName:''
        };*/


    }
    render() {

        const tableData = [];


        const strAscending = this.props.data.orders?.sort((a, b) =>
            a.orderDate < b.orderDate ? 1 : -1,
        );
        strAscending?.map((data) => {
            //  var r=data.razorpayOrderDetails?.replace("\\","");
            //console.log(data.paymentDetails.rzPaymentId);
            /* data.products.map((item)=>{
                 var obj = {
                     id: item.orderProductId,
                     categoryId: item.categoryId,
                     paymentId: data.paymentDetails?.rzPaymentId,
                     invoiceId: item.orderProductId,
                     razorpayId: JSON.parse(data.razorpayOrderDetails)?.id,
                     title: item.product_name,
                     dateCreate: "" + new Date(data.orderDate).toISOString().split('T')[0],
                     amount: item.sole_price,
                     paymentStatus: data.paymentStatus,
                     status: data.orderStatus,
                     under_return_policy:item.underReturnPolicy
                 }
                 tableData.push(obj);
             })
             */
            var obj = {
                id: data.orderId,
                razorpayId: JSON.parse(data.razorpayOrderDetails)?.id,
                dateCreate: "" + new Date(data.orderDate).toISOString().split('T')[0],
                amount: data.totalBill,
                paymentStatus: data.paymentStatus,
                status: data.paymentStatus,
            }
            tableData.push(obj);
        })
        /*
                //main returnOrder function
                const returnOrder = async () => {
                    if (this.state.orderProductId == '' || this.state.categoryId == 0 || this.state.paymentId == 0 || this.state.amount == 0) {
                        alert("something went wrong !!");
                        this.setState({ open: false });
                        return;
                    }
        
                    const data = {
                        email: this.state.email,
                        reason: this.state.reason,
                        paymentId: this.state.paymentId,
                        amt: this.state.amount,
                        orderProductId: this.state.orderProductId,
                        categoryId: this.state.categoryId,
                        userId: this.props.data.userId,
                        productName:this.state.productName
                    }
                   // alert(JSON.stringify(data))
                    //this.setState({ action: await returnPolicyByUser(data) });
        
                    if (this.state.action == '0') {
                        Modal.success({
                            centered: true,
                            title: 'Success Information..',
                            content: `Now your order is under return policy, please wait for some time...`,
                        });
                        this.setState({ open: false });
                    } else if (this.state.action == "1") {
                        Modal.warning({
                            centered: true,
                            title: 'Opps, too late !!',
                            content: `Your return policy period is expired !!`,
                        });
                        this.setState({ open: false });
                    } else if (this.state.action == "-1") {
                        Modal.error({
                            centered: true,
                            title: 'Opps, Something wrong !!',
                            content: `Server is down , please try later.`,
                        });
                        this.setState({ open: false });
                    }
                }
        
                const openModel = (oid, catId, payId, amt,title) => {
                    this.setState({ orderProductId: oid });
                    this.setState({ categoryId: catId });
                    this.setState({ paymentId: payId });
                    this.setState({ amount: amt });
                    this.setState({productName: title})
                    this.setState({ open: true });
                }
        */
        /*
            //payment code here
            const loadScript = (src) => {
                return new Promise((resolve) => {
                    const script = document.createElement('script')
                    script.src = src;
                    script.onload = () => {
                        resolve(true);
                    }
                    script.onerror = () => {
                        resolve(false);
                    }
                    document.body.appendChild(script);
                })
            }
        
        const payment= async (orderId,totalAmount,rzOrderId)=>{
                alert(orderId+" | "+totalAmount+" | "+rzOrderId);
                
                const result = await loadScript("https://checkout.razorpay.com/v1/checkout.js");
                if (!result) {
                    alert("network issue...");
                    return;
                }
                const amt = totalAmount;
                console.log("amt " + amt)
                const Razorpay_payment = rzOrderId;
                const order_ref_id = orderId;
                const options = {
                    key: "rzp_test_qN46tu51pwl8nZ",//arange the key dynamicaly
                    currency: "INR",
                    name: "Payment gateway.",
                    description: "Test Wallet Transaction",
                    image: "/static/img/nuvioseller.png",
                    order_id: Razorpay_payment.id,
                    handler: function (response) {
    
                        const payment_id = response.razorpay_payment_id;
                        const rz_order_id = response.razorpay_order_id;
                        const signature = response.razorpay_signature;
    
                        if (response.razorpay_payment_id) {
                            //update the states of order payment
                            const paymentObject = {
                                "rzPaymentId": payment_id,
                                "rzOrderId": rz_order_id,
                                "rzSignature": signature,
                                "orderID": order_ref_id
                            }
                            axios.post(`${marketplaceUrl}/updateOrder`, paymentObject, {
                                headers: {
                                    Authorization: "Bearer "+getToken(),
                                }
                            }).then(
                                (response) => {
                                    // alert("payment is success full");
                                    const modal = Modal.success({
                                        centered: true,
                                        title: 'Payment Success!',
                                        content: `Thank you! Your order is ` + order_ref_id,
                                    });
                                    modal.update;
                                    review.fname = "";
                                },
                                (error) => {
                                    alert("error happning in update payment status...")
                                }
                            )
    
                        }
                    },
                    prefill: {
                        name: this.props.data.firstName + " " + this.props.data.lastName,
                        email: this.props.data.email,
                        contact: this.props.data.phone,
                    },
                    theme: {
                        color: "#fcb800"
                    }
                };
    
                const paymentObject = new window.Razorpay(options);
                paymentObject.open();
    
                
    
                //order details is save to database
                //console.log("Razor pay details : "+JSON.stringify(response));
                // alert(" Order are successfully save to store. ");
    
                //Router.push('/account/payment');
            }
            //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
           const tableData = [
                {
                    id: '1',
                    invoiceId: '500884010',
                    title: 'Marshall Kilburn Portable Wireless Speaker',
                    dateCreate: '20-1-2020',
                    amount: '42.99',
                    status: 'Successful delivery',
                }
            ];
    */
        //const tableData = this.props.data.orders;
        //alert(JSON.stringify(tableData));
        const tableColumn = [
            // {
            //     title: 'Id',
            //     dataIndex: 'id',
            //     rowKey: 'id',
            //     key: 'id',
            //     width: '30px',
            //     render: (text, record) => (
            //         <>
            //             <Link href={`/account/orderDetails?odId=${record.id}`}>
            //                 {
            //                     <span className='d-none d-md-block'>{record.id}</span>
            //                 }
            //             </Link>
            //         </>
            //     ),
            // },
            
            /*{
                title: 'Title',
                dataIndex: 'title',
                rowKey: 'title',
                width:'80px',
                key: 'title',
            },*/
            {
                title: 'Date',
                rowKey: 'dateCreate',
                dataIndex: 'dateCreate',
                key: 'dateCreate',
                width: '120px',
            },
            {
                title: 'Amount',
                rowKey: 'amount',
                dataIndex: 'amount',
                key: 'amount',
                width: '72px',
                render: (text, record) => (
                    <span className="text-right">₹{record.amount}</span>
                ),
            },
            {
                title: 'Status',
                key: 'status',
                dataIndex: 'status',
                rowKey: 'status',
                width: '60px',
                render: (text, record) => (
                    record.status == 'success' ?
                        <span className='alert-success px-4 py-2'>PAID</span>
                        :
                        <span className='alert-danger p-2'>UNPAID</span>

                ),
            },
            {
                title: 'Action',
                key: 'status',
                dataIndex: 'status',
                rowKey: 'status',
                width: '60px',
                render: (text, record) => (
                    <span className="text-right">
                        {

                            (record.paymentStatus == "unpaid" && record.paymentType == 'Online Payment') ?
                                <button onClick={() => { removeOrder(record.id) }} className="btn btn-outline-danger">Remove</button>
                                : (record.under_return_policy === "0") ?
                                    <button onClick={() => { openModel(record.id, record.categoryId, record.paymentId, record.amount, record.title) }} className="btn btn-outline-warning">Return</button>
                                    :
                                    <Link href={`/account/orderDetails?odId=${record.id}`} ><button className='btn btn-outline-info'>View Details</button></Link>
                        }
                    </span>
                ),
            },

        ];
        return (
            <>
                {/* <Modal
                    open={this.state.open}
                    title="Return policy"

                    onCancel={() => { this.setState({ open: false }) }}
                    footer={[

                        <Button key="submit" type="primary" onClick={() => { returnOrder() }}>
                            Submit
                        </Button>,
                        <Button key="close" type="secondary" onClick={() => { this.setState({ open: false }) }}>
                            Close
                        </Button>
                    ]}
                >
                    <Form

                        name="control-hooks"

                        style={{ maxWidth: 600 }}
                    >
                        <Form.Item name="note" label="Email" rules={[{ required: true }]}>
                            <Input defaultValue={this.state.email} onChange={(event) => { this.setState({ email: event.target.value }) }} />
                        </Form.Item>

                        <Form.Item name="gender" label="Reason" rules={[{ required: true }]}>
                            <Select
                                placeholder="Select a option and change input text above"
                                onChange={(event) => { this.setState({ reason: event }) }}
                                allowClear
                            >
                                <Option value="Damage product arrived.">Damage product arrived.</Option>
                                <Option value="Package is open">Package is open</Option>
                                <Option value="other">other</Option>
                            </Select>
                        </Form.Item>


                    </Form>

                </Modal>
                */}
                <Table

                    columns={tableColumn}
                    dataSource={tableData}
                    rowKey={record => record.id}
                />
            </>
        );
    }
}

export default TableInvoices;
