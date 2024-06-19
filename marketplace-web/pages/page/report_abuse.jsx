import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from "@bradgarropy/next-seo"
import Report_abuse from '~/components/partials/page/Report_abuse';

const report_abuse = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'report abuse',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Report abuse">
            <SEO title="Report abuse" description="Please use the form below to report any abuse or suspicious activity on our platform. Your report helps us maintain a safe and secure environment for all users." keywords={["Nuvio, nuvio seller,Report abuse,suspicious activity"]} />

            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Report_abuse />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default report_abuse;