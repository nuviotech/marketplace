import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import Return_policy from '~/components/partials/page/Return_policy';
import SEO from "@bradgarropy/next-seo"


const return_policy = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Return Policy',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Return Policy">
            <SEO title="Return Policy" description="Return Policy" keywords={["Nuvio, nuvio seller, Return Policy"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Return_policy />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default return_policy;
