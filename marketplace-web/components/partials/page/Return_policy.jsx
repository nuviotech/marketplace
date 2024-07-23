import Axios from 'axios';
import React from 'react';
import { useEffect } from 'react';
import { useState } from 'react';
import { marketplaceUrl } from '~/repositories/Repository';

const BlankContent = () => {
    const [policy, setPolicy] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPolicy = async () => {
            try {
                const result = await Axios.get(`${marketplaceUrl}/getPolicy/marketplace_return_policy`);
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
                            <h1>RETURN/REPLACEMENT/EXCHANGE POLICY</h1>
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

export default BlankContent;
