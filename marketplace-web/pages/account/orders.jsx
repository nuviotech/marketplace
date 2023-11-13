import React from 'react';

import BreadCrumb from '~/components/elements/BreadCrumb';
import Invoices from '~/components/partials/account/orders';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';

const InvoicePage = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Orders',
        },
    ];
    return (
        <>
            <PageContainer footer={<FooterDefault />} title="orders">
                <div className="ps-page--my-account">
                    <BreadCrumb breacrumb={breadCrumb} />
                    <Invoices />
                </div>
                <Newletters layout="container" />
            </PageContainer>
        </>
    );
};

export default InvoicePage;
