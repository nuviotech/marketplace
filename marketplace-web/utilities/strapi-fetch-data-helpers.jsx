/*
 * React template helpers
 * Author: Nouthemes
 * Developed: diaryforlife
 * */

import React from 'react';
import CollectionRepository from '~/repositories/CollectionRepository';
import ProductRepository from '~/repositories/ProductRepository';

const STRAPI_URL =
    process.env.NEXT_PUBLIC_STRAPI_PANEL_URL || 'http://localhost:1337';

/**
 * Get products belonging to a Strapi collection.
 *
 * Strapi stores only the product_ref_id.
 *
 * Example response:
 *
 * {
 *   items: [
 *      {
 *          product_ref_id: "152a8c3f81604ba38578a8dffee19346"
 *      }
 *   ]
 * }
 */
export async function getProductsByCollectionHelper(slug) {
    try {
        if (!slug) {
            console.error(
                '### STRAPI COLLECTION ERROR ### Missing collection slug'
            );

            return {
                items: [],
            };
        }

        const url =
            `${STRAPI_URL}/collections` +
            `?slug=${encodeURIComponent(slug)}` +
            `&_limit=1`;

        console.log(
            '### STRAPI COLLECTION REQUEST ###',
            url
        );

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(
                `Strapi request failed: ${response.status}`
            );
        }

        const data = await response.json();

        console.log(
            '### STRAPI COLLECTION RESPONSE ###',
            data
        );

        if (!Array.isArray(data) || data.length === 0) {
            console.log(
                '### STRAPI COLLECTION ### Collection not found:',
                slug
            );

            return {
                items: [],
            };
        }

        const collection = data[0];

        const products = Array.isArray(collection.products)
            ? collection.products
            : [];

        const items = products
            .map((product) => {
                if (!product) {
                    return null;
                }

                /*
                 * Strapi may return:
                 *
                 * {
                 *     id: 1,
                 *     title: "...",
                 *     product_ref_id: "..."
                 * }
                 *
                 * We only need product_ref_id.
                 */

                if (
                    product.product_ref_id !== undefined &&
                    product.product_ref_id !== null &&
                    product.product_ref_id !== ''
                ) {
                    return {
                        product_ref_id:
                            product.product_ref_id,
                    };
                }

                return null;
            })
            .filter(Boolean);

        console.log(
            '### STRAPI COLLECTION PRODUCTS ###',
            slug,
            items
        );

        return {
            items,
        };

    } catch (error) {
        console.error(
            '### STRAPI COLLECTION ERROR ###',
            error
        );

        return {
            items: [],
        };
    }
}


/**
 * Get products by category.
 *
 * Keep this function if your existing application
 * uses category-based product listings.
 */
export async function getProductsByCategoriesHelper(payload) {
    try {
        if (!payload) {
            return {
                items: [],
            };
        }

        const url =
            `${STRAPI_URL}/categories` +
            `?slug=${encodeURIComponent(payload)}`;

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(
                `Strapi category request failed: ${response.status}`
            );
        }

        const data = await response.json();

        if (!Array.isArray(data) || data.length === 0) {
            return {
                items: [],
            };
        }

        const category = data[0];

        return {
            items: Array.isArray(category.products)
                ? category.products
                : [],
        };

    } catch (error) {
        console.error(
            '### STRAPI CATEGORY ERROR ###',
            error
        );

        return {
            items: [],
        };
    }
}