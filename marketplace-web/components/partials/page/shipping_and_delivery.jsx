import Axios from 'axios';
import React, { useEffect, useState } from 'react';
import { marketplaceUrl } from '~/repositories/Repository';

const Shipping_And_Delivery = () => {
    const [policy, setPolicy] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPolicy = async () => {
            try {
                const result = await Axios.get(`${marketplaceUrl}/getPolicy/marketplace_shipping_and_delivery_policy_for_website`);
                setPolicy(result.data);
            } catch (error) {
                console.error("Related product(error): ", error);
                setError("An error occurred while fetching the policy.");
            }
        };

        fetchPolicy();
    }, []);

    return (
        <div>
            {policy ? (
                <div className="ps-section--custom">
                    <div className="container">
                        <div className="ps-section__header">
                            <h1>Shipping And Delivery</h1>
                        </div>
                        <div>{policy.split("##").map(point => {
                            return <div className='my-2'>{point}</div>
                        })}</div>
                    </div>
                </div>
            ) : (
                <div>Loading...</div>
            )}
        </div>
    );

}
export default Shipping_And_Delivery;
