import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import PolicyContent from '~/components/partials/page/policy';

const PolicyPage = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'policy',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Policy">
            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <PolicyContent />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default PolicyPage;
