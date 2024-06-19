import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from "@bradgarropy/next-seo"
import Terms_and_conditions from '~/components/partials/page/Terms_and_conditions';

const terms_and_conditions = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'user_terms_and_conditions',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Terms Of Services">
            <SEO title="Terms and conditions" description="buyer user terms and conditions" keywords={["Nuvio, nuvio seller, terms of services"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Terms_and_conditions />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default terms_and_conditions;