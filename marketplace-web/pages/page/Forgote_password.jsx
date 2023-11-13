import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FaqsContent from '~/components/partials/page/FaqsContent';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import PageContainer from '~/components/layouts/PageContainer';
import Newletters from '~/components/partials/commons/Newletters';
import ForgotePassword1UI from '~/components/partials/page/ForgotePassword1UI';

const ForgotePassword = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Forgote password',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Forgote password page">
            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} />
                <div className="container">
                    <ForgotePassword1UI/>
                </div>
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default ForgotePassword;
