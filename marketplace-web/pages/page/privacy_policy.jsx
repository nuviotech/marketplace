import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from '@bradgarropy/next-seo';
import Privacy_Policy from '~/components/partials/page/Privacy_Policy';

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
        <PageContainer
            footer={<FooterDefault />}
            title="Privacy Policy"
        >

            <SEO
                title={`${process.env.NEXT_PUBLIC_WEBSITE_NAME} | Privacy Policy`}
                description="Privacy Policy"
                keywords={[
                    process.env.NEXT_PUBLIC_WEBSITE_NAME,
                    'Privacy Policy',
                ]}
            />

            <div className="ps-page--single">
                <BreadCrumb
                    breacrumb={breadCrumb}
                    layout="fullwidth"
                />

                <Privacy_Policy />
            </div>

            <Newletters layout="container" />

        </PageContainer>
    );
};

export default Privacy_policy;