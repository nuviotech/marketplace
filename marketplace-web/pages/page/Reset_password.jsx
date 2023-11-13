import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FaqsContent from '~/components/partials/page/FaqsContent';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import PageContainer from '~/components/layouts/PageContainer';
import Newletters from '~/components/partials/commons/Newletters';
import ForgotePassword1UI from '~/components/partials/page/ForgotePassword1UI';
import ResetPasswordUi from '~/components/partials/page/ResetPasswordUi';

const ResetPassword = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Reset password',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Reset password page">
            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} />
                <div className="container">
                    <ResetPasswordUi/>
                </div>
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default ResetPassword;
