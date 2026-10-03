import axios from 'axios';

const isServer = typeof window === "undefined";

// Use localhost when running on the server (SSR)
// Use public HTTPS URL in the browser
const baseDomain = isServer
    ? "http://127.0.0.1:1337"
    : process.env.NEXT_PUBLIC_STRAPI_PANEL_URL;

export const basePostUrl = baseDomain;
export const baseStoreURL = baseDomain;

export const marketplaceAdminUrl = isServer
    ? "http://127.0.0.1:9091"
    : process.env.NEXT_PUBLIC_MARKETPLACE_ADMIN_BASE_URL;

export const policyMakerUrl =
    process.env.NEXT_PUBLIC_POLICYMAKER_API_BASE_URL;

export const marketplaceUrl = isServer
    ? "http://127.0.0.1:8084"
    : process.env.NEXT_PUBLIC_MARKETPLACE_API_BASE_URL;

export const policyMakerKey =
    process.env.NEXT_PUBLIC_PUBLICAPI_KEY;

export const customHeaders = {
    Accept: "application/json",
};

export const baseUrl = baseDomain;

export default axios.create({
    baseURL: baseUrl,
    headers: customHeaders,
});

export const serializeQuery = (query) => {
    return Object.keys(query)
        .map(
            (key) =>
                `${encodeURIComponent(key)}=${encodeURIComponent(query[key])}`
        )
        .join("&");
};