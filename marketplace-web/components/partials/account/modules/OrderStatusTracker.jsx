import React from 'react';
import { Steps, Tag, Alert } from 'antd';

const { Step } = Steps;

const getStatusInfo = (statusRaw) => {
    const status = (statusRaw || 'PENDING').trim().toUpperCase();
    if (status === 'CANCELLED') {
        return { isCancelled: true, isReturned: false, step: -1, statusLabel: 'CANCELLED' };
    }
    if (status === 'RETURNED') {
        return { isCancelled: false, isReturned: true, step: -1, statusLabel: 'RETURNED' };
    }

    let step = 0; // Order Placed
    if (status === 'PROCESSING') step = 1;
    else if (status === 'PACKED') step = 2;
    else if (status === 'SHIPPED') step = 3;
    else if (status === 'DELIVERED') step = 4;

    return { isCancelled: false, isReturned: false, step, statusLabel: status };
};

const OrderStatusTracker = ({ product }) => {
    const { isCancelled, isReturned, step, statusLabel } = getStatusInfo(product?.status);

    const timelineSteps = [
        { title: 'Order Placed', description: 'Order received' },
        { title: 'Processing', description: 'Under processing' },
        { title: 'Packed', description: 'Packed & ready' },
        { title: 'Shipped', description: 'In transit' },
        { title: 'Delivered', description: 'Delivered' }
    ];

    return (
        <div className="ps-block--order-tracker mb-4 p-4 border rounded bg-white shadow-sm" style={{ borderRadius: '8px', border: '1px solid #eaeaea' }}>
            <div className="d-flex justify-content-between align-items-center mb-3 pb-2 border-bottom">
                <div>
                    <h5 style={{ margin: 0, fontSize: '15px', fontWeight: '600' }}>
                        Item: <span className="text-primary">{product?.product_name}</span>
                    </h5>
                </div>
                <div>
                    {isCancelled ? (
                        <Tag color="red" style={{ fontSize: '13px', padding: '3px 10px', fontWeight: '600' }}>CANCELLED</Tag>
                    ) : isReturned ? (
                        <Tag color="orange" style={{ fontSize: '13px', padding: '3px 10px', fontWeight: '600' }}>RETURNED</Tag>
                    ) : (
                        <Tag color="blue" style={{ fontSize: '13px', padding: '3px 10px', fontWeight: '600' }}>{statusLabel}</Tag>
                    )}
                </div>
            </div>

            {isCancelled ? (
                <Alert
                    message="Order Cancelled"
                    description="This order item has been cancelled."
                    type="error"
                    showIcon
                    style={{ marginTop: '10px' }}
                />
            ) : isReturned ? (
                <Alert
                    message="Order Returned"
                    description="This order item has been marked as returned."
                    type="warning"
                    showIcon
                    style={{ marginTop: '10px' }}
                />
            ) : (
                <div style={{ marginTop: '20px', padding: '10px 0' }}>
                    <Steps current={step} responsive size="small" style={{ width: '100%' }}>
                        {timelineSteps.map((item, index) => (
                            <Step
                                key={item.title}
                                title={
                                    <span style={{
                                        fontWeight: index <= step ? '600' : 'normal',
                                        color: index === step ? '#fcb800' : index < step ? '#28a745' : '#888'
                                    }}>
                                        {item.title}
                                    </span>
                                }
                                description={<span style={{ fontSize: '11px', color: '#999' }}>{item.description}</span>}
                            />
                        ))}
                    </Steps>
                </div>
            )}
        </div>
    );
};

export default OrderStatusTracker;
