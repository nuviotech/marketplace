import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from "@bradgarropy/next-seo"
import Terms_and_conditions from '~/components/partials/page/Terms_and_conditions';
import Affiliate_Terms_and_conditions from '~/components/partials/page/Affiliate_Terms_and_conditions';

const affiliate_marketing_terms_and_conditions = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'affiliate program terms and conditions',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Terms Of Services">
            <SEO title="Affiliate Marketing Terms and conditions" description="These Affiliate Program Terms and Conditions govern your participation in the affiliate program offered by Nuvio Technologies Pvt Ltd." keywords={["Nuvio, nuvio seller, terms of services"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Affiliate_Terms_and_conditions />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default affiliate_marketing_terms_and_conditions;