import React from 'react';
import SiteFeatures from '~/components/partials/homepage/home-default/SiteFeatures';
import HomeAdsColumns from '~/components/partials/homepage/home-default/HomeAdsColumns';
import HomeAds from '~/components/partials/homepage/home-default/HomeAds';
import NewArrivals from '~/components/partials/homepage/home-default/NewArrivals';
import Newletters from '~/components/partials/commons/Newletters';
import OurPartners from '~/components/partials/commons/OurPartners';
import HomeDefaultDealOfDay from '~/components/partials/homepage/home-default/HomeDefaultDealOfDay';
import HomeDefaultTopCategories from '~/components/partials/homepage/home-default/HomeDefaultTopCategories';
import HomeDefaultProductListing from '~/components/partials/homepage/home-default/HomeDefaultProductListing';
import HomeDefaultBanner from '~/components/partials/homepage/home-default/HomeDefaultBanner';
import PageContainer from '~/components/layouts/PageContainer';

const HomepageDefaultPage = () => {

    return (
        <PageContainer title="Multipurpose Marketplace ">
            
            
            <main id="homepage-1">
                <HomeDefaultBanner />
                <SiteFeatures />
                <HomeDefaultDealOfDay collectionSlug="deal-of-the-day" />
                <HomeAdsColumns />
                <HomeDefaultTopCategories />
                <HomeDefaultProductListing
                    collectionSlug="consumer-electronics"
                    title="Consumer Electronics"
                    bestSellerSlug="fullwidth-consumer-electronic-best-seller"
                    mostPopularSlug="fullwidth-consumer-electronic-most-popular"
                />
                <HomeDefaultProductListing
                    collectionSlug="clothings"
                    title="Clothings"
                    bestSellerSlug="fullwidth-clothings-best-seller"
                    mostPopularSlug="fullwidth-clothings-most-popular"
                />
                <HomeDefaultProductListing
                    collectionSlug="garden-and-kitchen"
                    title="Garden & Kitchen"
                    bestSellerSlug="fullwidth-garden-and-kitchen-best-seller"
                    mostPopularSlug="fullwidth-garden-and-kitchen-most-popular"
                />
                <HomeAds />
                <NewArrivals collectionSlug="new-arrivals-products" />
                <Newletters />
                <OurPartners />
            </main>
        
        </PageContainer>
    );
};

export default HomepageDefaultPage;
