import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from "@bradgarropy/next-seo"
import Add_Affilate_Accnt from '~/components/partials/page/Add_Affiliate_Accnt';


const return_policy = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Create Affilate account',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Nuvio Associates affiliate marketing program.">
            <SEO  description="The Nuvio affiliate program gives partners an opportunity to earn commissions by promoting Nuvio through affiliate links. Affiliate Program Steps." keywords={["Nuvio, nuvio seller, affiliate,Affiliate Program Steps,Affiliate Program, affiliate marketing"]} />
            <div className="ps-page--single">
                <BreadCrumb breacrumb={breadCrumb} layout="fullwidth" />
                <Add_Affilate_Accnt />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default return_policy;