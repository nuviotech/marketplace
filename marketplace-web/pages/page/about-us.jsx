import React from 'react';

import BreadCrumb from '~/components/elements/BreadCrumb';
import OurTeam from '~/components/partials/page/about-us/OurTeam';
import AboutAwards from '~/components/partials/page/about-us/AboutAwards';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import PageContainer from '~/components/layouts/PageContainer';
import SEO from "@bradgarropy/next-seo"

const AboutUsPage = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'About Us',
        },
    ];
    return (

        <PageContainer footer={<FooterDefault />} title="About Us">
        <SEO title="About Us" description="About Us" keywords={["Nuvio, nuvio seller, About us"]} />

            <div className="ps-page--single">
                <img src="/static/img/bg/about-us.jpg" alt="" />
                <BreadCrumb breacrumb={breadCrumb} />
                <OurTeam />
                
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};
export default AboutUsPage;
