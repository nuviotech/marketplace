import Axios from 'axios';
import React from 'react';
import { useEffect } from 'react';
import { useState } from 'react';
import { marketplaceUrl } from '~/repositories/Repository';

const Cancellation_And_Refund_Policy = () => {
    const [policy, setPolicy] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPolicy = async () => {
            try {
                const result = await Axios.get(`${marketplaceUrl}/getPolicy/marketplace_cancellation_and_refund_policy_for_website`);
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
            {
                policy ? (
                    <div className="ps-section--custom" >
                        <div className="container">
                            <div className="ps-section__header">
                                <h1>Cancellation And Refund Policy</h1>
                            </div>
                            <div>{policy.split("##").map(point => {
                                return <div className='my-2'>{point}</div>
                            })}</div>
                        </div>
                    </div >
                ) : (
                    <div>Loading...</div>
                )}
        </div >
    );
}

export default Cancellation_And_Refund_Policy;
