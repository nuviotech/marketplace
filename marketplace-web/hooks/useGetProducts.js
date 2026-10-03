import { useState } from 'react';

import {
    getProductsByCategoriesHelper,
    getProductsByCollectionHelper,
} from '~/utilities/strapi-fetch-data-helpers';

import ProductRepository from '~/repositories/ProductRepository';


// ---------------------------------------------------------
// Helper
// ---------------------------------------------------------

function safeArray(data) {
    if (Array.isArray(data)) {
        return data;
    }

    if (typeof data === 'string') {
        try {
            const parsed = JSON.parse(data);

            return Array.isArray(parsed)
                ? parsed
                : [];
        } catch (e) {
            console.error(
                '### SAFE ARRAY JSON ERROR ###',
                e
            );

            return [];
        }
    }

    return [];
}


// ---------------------------------------------------------
// Hook
// ---------------------------------------------------------

export default function useGetProducts() {

    const [loading, setLoading] = useState(false);

    const [productItems, setProductItems] =
        useState(null);

    const [product, setProduct] =
        useState(null);


    return {

        loading,

        productItems,

        product,


        // -------------------------------------------------
        // Set Products
        // -------------------------------------------------

        setProductItems: (payload) => {
            setProductItems(payload);
        },


        // -------------------------------------------------
        // Set Loading
        // -------------------------------------------------

        setLoading: (payload) => {
            setLoading(payload);
        },


        // =================================================
        // GET PRODUCTS BY COLLECTION
        // =================================================

        getProductsByCollection: async (payload) => {

            setLoading(true);

            setProductItems(null);


            try {

                if (!payload) {

                    console.error(
                        '### COLLECTION ERROR ### Missing slug'
                    );

                    setLoading(false);

                    return;
                }


                console.log(
                    '### GET COLLECTION ###',
                    payload
                );


                // -----------------------------------------
                // STEP 1
                // Get collection from Strapi
                // -----------------------------------------

                const responseData =
                    await getProductsByCollectionHelper(
                        payload
                    );


                console.log(
                    '### COLLECTION DATA ###',
                    responseData
                );


                if (
                    !responseData ||
                    !Array.isArray(responseData.items)
                ) {

                    console.log(
                        '### COLLECTION ### No items'
                    );

                    setProductItems(null);

                    setLoading(false);

                    return;
                }


                // -----------------------------------------
                // STEP 2
                // Extract product_ref_id
                // -----------------------------------------

                const collectionItems =
                    responseData.items;


                const productIds =
                    collectionItems
                        .map(
                            (item) =>
                                item?.product_ref_id
                        )
                        .filter(
                            (id) =>
                                id !== undefined &&
                                id !== null &&
                                id !== ''
                        );


                console.log(
                    '### COLLECTION PRODUCT IDS ###',
                    productIds
                );


                if (productIds.length === 0) {

                    console.log(
                        '### COLLECTION ### No product IDs'
                    );

                    setProductItems(null);

                    setLoading(false);

                    return;
                }


                // -----------------------------------------
                // STEP 3
                // Get actual ManageEcom products
                // -----------------------------------------

                const products =
                    await ProductRepository.getProductsByIds(
                        productIds
                    );


                const items =
                    safeArray(products);


                console.log(
                    '### MANAGEECOM PRODUCTS ###',
                    items
                );


                // -----------------------------------------
                // STEP 4
                // Put actual products into state
                // -----------------------------------------

                if (items.length > 0) {

                    setProductItems(items);

                } else {

                    setProductItems(null);
                }


            } catch (error) {

                console.error(
                    '### GET PRODUCTS BY COLLECTION ERROR ###',
                    error
                );

                setProductItems(null);

            } finally {

                setLoading(false);

            }
        },


        // =================================================
        // GET PRODUCTS BY CATEGORY
        // =================================================

        getProductsByCategory: async (payload) => {

            setLoading(true);

            try {

                const responseData =
                    await getProductsByCategoriesHelper(
                        payload
                    );


                if (responseData) {

                    const items =
                        Array.isArray(responseData.items)
                            ? responseData.items
                            : safeArray(responseData);


                    if (items.length > 0) {

                        setProductItems(items);

                    } else {

                        setProductItems(null);
                    }

                } else {

                    setProductItems(null);

                }

            } catch (error) {

                console.error(
                    '### GET PRODUCTS BY CATEGORY ERROR ###',
                    error
                );

                setProductItems(null);

            } finally {

                setLoading(false);

            }
        },


        // =================================================
        // GET NORMAL PRODUCTS
        // =================================================

        getProducts: async (payload) => {

            setLoading(true);

            try {

                let responseData;


                if (payload) {

                    responseData =
                        await ProductRepository.getProducts(
                            payload
                        );

                } else {

                    const queries = {
                        _limit: 12,
                    };

                    responseData =
                        await ProductRepository.getProducts(
                            queries
                        );
                }


                const items =
                    safeArray(responseData);


                if (items.length > 0) {

                    setProductItems(items);

                } else {

                    setProductItems(null);

                }

            } catch (error) {

                console.error(
                    '### GET PRODUCTS ERROR ###',
                    error
                );

                setProductItems(null);

            } finally {

                setLoading(false);

            }
        },


        // =================================================
        // GET SINGLE PRODUCT
        // =================================================

        getProductById: async (payload) => {

            setLoading(true);

            try {

                const responseData =
                    await ProductRepository.getProductsById(
                        payload
                    );


                if (responseData) {

                    let product =
                        responseData;


                    if (
                        typeof product === 'string'
                    ) {

                        try {

                            product =
                                JSON.parse(product);

                        } catch (e) {

                            console.error(
                                '### PRODUCT JSON ERROR ###',
                                e
                            );

                        }
                    }


                    setProduct(product);

                } else {

                    setProduct(null);

                }

            } catch (error) {

                console.error(
                    '### GET PRODUCT BY ID ERROR ###',
                    error
                );

                setProduct(null);

            } finally {

                setLoading(false);

            }
        },
    };
}