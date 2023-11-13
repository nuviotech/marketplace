import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import PageContainer from '~/components/layouts/PageContainer';
import Newletters from '~/components/partials/commons/Newletters';
import BestSellerData from '~/components/partials/page/BestSellerData';

const GardenAndKitchenBestSeller = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Best Seller',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Best seller">
            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} />
                <div className="container">
                    <BestSellerData slug="fullwidth-garden-and-kitchen-best-seller"/>
                </div>
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default GardenAndKitchenBestSeller;