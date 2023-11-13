import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import Cancellation_And_Refund_Policy from '~/components/partials/page/Cancellation_&_Refund_Policy';
import SEO from "@bradgarropy/next-seo"


const return_policy = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Cancellation And Refund Policy',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Cancellation And Refund Policy">
            <SEO  description="Cancellation And Refund Policy" keywords={["Nuvio, nuvio seller, Cancellation And Refund Policy"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Cancellation_And_Refund_Policy />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default return_policy;
