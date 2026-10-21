import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from '@bradgarropy/next-seo';
import Cancellation_And_Refund_Policy from '~/components/partials/page/Cancellation_And_Refund_Policy';

const return_policy = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Cancellation & Refund Policy',
        },
    ];

    return (
        <PageContainer
            footer={<FooterDefault />}
            title="Cancellation & Refund Policy"
        >

            <SEO
                title={`${process.env.NEXT_PUBLIC_WEBSITE_NAME} | Cancellation & Refund Policy`}
                description="Cancellation and Refund Policy"
                keywords={[
                    process.env.NEXT_PUBLIC_WEBSITE_NAME,
                    'Cancellation Policy',
                    'Refund Policy',
                ]}
            />

            <div className="ps-page--single">
                <BreadCrumb
                    breacrumb={breadCrumb}
                    layout="fullwidth"
                />

                <Cancellation_And_Refund_Policy />
            </div>

            <Newletters layout="container" />

        </PageContainer>
    );
};

export default return_policy;