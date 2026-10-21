import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import PolicyContent from '~/components/partials/page/PolicyContent';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from '@bradgarropy/next-seo';

const PolicyPage = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Policy',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Policy">
            <SEO
                title={
                    process.env.NEXT_PUBLIC_WEBSITE_NAME + ' | Policy'
                }
                description="Nuvio eCommerce Policies"
                keywords={[
                    process.env.NEXT_PUBLIC_WEBSITE_NAME,
                    'Policy',
                    'Privacy Policy',
                    'Shipping Policy',
                    'Return Policy',
                    'Payment Policy',
                    'Security Policy',
                ]}
            />

            <div className="ps-page--single">
                <BreadCrumb
                    breacrumb={breadCrumb}
                    layout="fullwidth"
                />

                <PolicyContent />
            </div>

            <Newletters layout="container" />
        </PageContainer>
    );
};

export default PolicyPage;