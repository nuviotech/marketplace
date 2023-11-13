import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import PrivacyPolicy from '~/components/partials/page/PrivacyPolicy';
import SEO from "@bradgarropy/next-seo"

const Privacy_policy = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Privacy Policy',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Privacy Policy">
            <SEO title="Privacy Policy" description="Privacy Policy" keywords={["Nuvio, nuvio seller, Privacy Policy"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <PrivacyPolicy/>
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default Privacy_policy;
