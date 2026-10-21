import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from '@bradgarropy/next-seo';
import Return_Policy from '~/components/partials/page/Return_Policy';

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

            <SEO
                title={`${process.env.NEXT_PUBLIC_WEBSITE_NAME} | Return Policy`}
                description="Return Policy"
                keywords={[
                    process.env.NEXT_PUBLIC_WEBSITE_NAME,
                    'Return Policy',
                ]}
            />

            <div className="ps-page--single">
                <BreadCrumb
                    breacrumb={breadCrumb}
                    layout="fullwidth"
                />

                <Return_Policy />
            </div>

            <Newletters layout="container" />

        </PageContainer>
    );
};

export default return_policy;