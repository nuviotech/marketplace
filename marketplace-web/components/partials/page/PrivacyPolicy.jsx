import Axios from 'axios';
import React, { useEffect, useState } from 'react';
import { marketplaceUrl } from '~/repositories/Repository';

const PrivacyPolicy = () => {
    const [policy, setPolicy] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPolicy = async () => {
            try {
                const result = await Axios.get(`${marketplaceUrl}/getPolicy/marketplace_privacy_policy_for_website`);
                setPolicy(result.data);
            } catch (error) {
                console.error("Related product(error): ", error);
                setError("An error occurred while fetching the policy.");
            }
        };

        fetchPolicy();
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            {policy ? (
                <div className="ps-section--custom">
                    <div className="container">
                        <div className="ps-section__header">
                            <h1>Privacy Policy</h1>
                        </div>
                        <div>{policy.split("##").map(point=>{
                            return <div className='my-2'>{point}</div>
                        })}</div>
                    </div>
                </div>
            ) : (
                <div>Loading...</div>
            )}
        </div>
    );


};

export default PrivacyPolicy;
