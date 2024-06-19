import { marketplaceUrl } from "~/repositories/Repository";
const baseUrl = "https://nuvio.in";
const EXTERNAL_DATA_URL = "https://nuvio.in/product";
const currentTime = new Date().toISOString();

function generateSiteMap(items) {
return `<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:news="http://www.google.com/schemas/sitemap-news/0.9" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:mobile="http://www.google.com/schemas/sitemap-mobile/1.0" xmlns:image="http://www.google.com/schemas/sitemap-image/1.1" xmlns:video="http://www.google.com/schemas/sitemap-video/1.1">
<url><loc>${baseUrl}</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/addresses</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/checkout</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/compare</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/edit-address</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/orders</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/login</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/my-account</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/my-account-2</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/notifications</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/order-tracking</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/payment</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/payment-success</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/recent-viewed-product</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/register</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/shipping</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/shopping-cart</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/user-information</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/account/wishlist</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/blog</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/blog/blog-left-sidebar</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/blog/blog-right-sidebar</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/blog/blog-small-thumbnail</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/auto-part</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/electronic</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/furniture</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/market-place</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/market-place-2</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/market-place-3</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/market-place-4</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/organic</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/home/technology</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/about-us</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/blank</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/contact-us</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/faqs</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/page-404</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/policy</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/privacy_policy</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/return_policy</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/page/terms_of_service</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/post/default</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/post/detail-2</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/post/detail-3</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/search</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/shop</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/shop/shop-carousel</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/shop/shop-categories</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/shop/shop-fullwidth</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/shop/shop-sidebar</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/shop/shop-sidebar-without-banner</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/sitemap.xml</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/stores</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>${baseUrl}/vendor/become-a-vendor</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>

    ${items.map(pid => {
        const id =pid.replace(/&/g, "&amp;");
        return `
        <url><loc>${EXTERNAL_DATA_URL}/${id}</loc><lastmod>${currentTime}</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
        `;

    }).join('')}
   </urlset>
 `;
}

function SiteMap() {
    // getServerSideProps will do the heavy lifting
}

export async function getServerSideProps({ res }) {
    // We make an API call to gather the URLs for our site
    const request = await fetch(`${marketplaceUrl}/getAllProducts`);
    const data = await request.json();

    // We generate the XML sitemap with the posts data
    const sitemap = generateSiteMap(data);
    res.setHeader('Content-Type', 'text/xml');
    // we send the XML to the browser
    res.write(sitemap);
    res.end();

    return {
        props: {},
    };
}

export default SiteMap;
