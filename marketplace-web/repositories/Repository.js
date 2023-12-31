import axios from 'axios';


const baseDomain = process.env.NEXT_PUBLIC_STRAPI_PANEL_URL // API for products
export const basePostUrl =  process.env.NEXT_PUBLIC_STRAPI_PANEL_URL; // API for post
export const baseStoreURL =  process.env.NEXT_PUBLIC_STRAPI_PANEL_URL; // API for vendor(store)

export const policyMakerUrl = "http://localhost:9001";//change the name policy maker  url
export const marketplaceUrl = process.env.NEXT_PUBLIC_MARKETPLACE_API_BASE_URL;//marketplace api url


//export const policyMakerKey = process.env.API_KEY;
export const policyMakerKey = process.env.NEXT_PUBLIC_PUBLICAPI_KEY;


export const customHeaders = {
    Accept: 'application/json',
};


export const baseUrl = `${baseDomain}`;


export default axios.create({
    baseUrl,
    headers: customHeaders,
});
export const serializeQuery = (query) => {
    return Object.keys(query)
        .map(
            (key) =>
                `${encodeURIComponent(key)}=${encodeURIComponent(query[key])}`
        )
        .join('&');
};
