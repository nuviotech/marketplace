import { marketplaceUrl } from "~/repositories/Repository";
const EXTERNAL_DATA_URL = "https://nuvio.in/product";

function generateSiteMap(items) {

    return `<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:news="http://www.google.com/schemas/sitemap-news/0.9" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:mobile="http://www.google.com/schemas/sitemap-mobile/1.0" xmlns:image="http://www.google.com/schemas/sitemap-image/1.1" xmlns:video="http://www.google.com/schemas/sitemap-video/1.1">
<url><loc>https://nuvio.in</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/addresses</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/checkout</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/compare</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/edit-address</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/orders</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/login</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/my-account</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/my-account-2</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/notifications</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/order-tracking</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/payment</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/payment-success</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/recent-viewed-product</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/register</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/shipping</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/shopping-cart</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/user-information</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/account/wishlist</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/blog</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/blog/blog-left-sidebar</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/blog/blog-right-sidebar</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/blog/blog-small-thumbnail</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/auto-part</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/electronic</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/furniture</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/market-place</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/market-place-2</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/market-place-3</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/market-place-4</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/organic</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/home/technology</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/about-us</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/blank</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/contact-us</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/faqs</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/page-404</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/policy</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/privacy_policy</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/return_policy</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/page/terms_of_service</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/post/default</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/post/detail-2</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/post/detail-3</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/search</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/shop</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/shop/shop-carousel</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/shop/shop-categories</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/shop/shop-fullwidth</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/shop/shop-sidebar</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/shop/shop-sidebar-without-banner</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/sitemap.xml</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/stores</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>
<url><loc>https://nuvio.in/vendor/become-a-vendor</loc><lastmod>2023-03-25T13:31:56.849Z</lastmod><changefreq>daily</changefreq><priority>0.7</priority></url>



     ${items
            .map((id) => {
                return `
       <url>
           <loc>${`${EXTERNAL_DATA_URL}/${id}`}</loc>
       </url>
     `;
            })
            .join('')}
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
