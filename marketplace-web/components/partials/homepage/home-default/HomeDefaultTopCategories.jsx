import React, { useEffect, useState } from 'react';
import MediaRepository from '~/repositories/MediaRepository';

const HomeDefaultTopCategories = () => {
    const [categories, setCategories] = useState([]);
    const API_URL = process.env.NEXT_PUBLIC_STRAPI_PANEL_URL;

    async function getCategories() {
        const responseData =
            await MediaRepository.getPromotionsBySlug('categories');

        console.log(
            'CATEGORIES RESPONSE:',
            JSON.stringify(responseData, null, 2)
        );

        if (responseData) {
            setCategories(responseData);
        }
    }

    useEffect(() => {
        getCategories();
    }, []);

    return (
        <div className="ps-top-categories">
            <div className="ps-container">
                <h3>Top Categories</h3>

                <div className="row">
                    {categories.map((category) => {
                        console.log(
                            'CATEGORY ITEM:',
                            JSON.stringify(category, null, 2)
                        );

                        return (
                            <div
                                key={category.id}
                                className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6"
                            >
                                <div
                                    style={{ height: '252px' }}
                                    className="ps-block--category"
                                >
                                    <a
                                        href={category.link}
                                        className="ps-block__overlay"
                                    />

                                    <img
                                        src={`${API_URL}${category.image?.url}`}
                                        alt={category.name}
                                        style={{
                                            width: '170px',
                                            height: '170px',
                                            objectFit: 'contain',
                                        }}
                                    />

                                    <p>{category.name}</p>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
};

export default HomeDefaultTopCategories;