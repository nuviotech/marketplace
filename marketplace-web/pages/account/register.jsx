import React from 'react';

import BreadCrumb from '~/components/elements/BreadCrumb';
import Register from '~/components/partials/account/Register';
import PageContainer from '~/components/layouts/PageContainer';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import { useRouter } from 'next/router';

const RegisterPage = () => {

    const router = useRouter();
    const { affiliate_account_id } = router.query;

    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Register an account',
        },
    ];



    return (
        <>
            <PageContainer footer={<FooterDefault />} title="Register">
                <div className="ps-page--my-account">
                    <BreadCrumb breacrumb={breadCrumb} />
                    <Register affiliate_account_id={affiliate_account_id} />
                </div>
                <Newletters layout="container" />
            </PageContainer>
        </>
    );
};

export default RegisterPage;
