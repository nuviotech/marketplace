import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import BlankContent from '~/components/partials/page/Blank';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import Shipping_And_Delivery from '~/components/partials/page/shipping_and_delivery';
import SEO from "@bradgarropy/next-seo"

const Shipping_Delivery = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'shipping and delivery',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="shipping and delivery">
            <SEO  description="shipping and delivery" keywords={["Nuvio, nuvio seller,shipping and delivery"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Shipping_And_Delivery />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default Shipping_Delivery;
