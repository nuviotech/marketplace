import React from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import ContactInfo from '~/components/partials/page/ContactInfo';
import ContactForm from '~/components/partials/page/ContactForm';
import ContactMap from '~/components/partials/page/ContactMap';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import PageContainer from '~/components/layouts/PageContainer';
import Newletters from '~/components/partials/commons/Newletters';
import SEO from "@bradgarropy/next-seo"

const ContactUsPage = () => {
    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'ContactUs',
        },
    ];

    return (
        <PageContainer footer={<FooterDefault />} title="Contact Us">
            <SEO title="Contact Us" description="Contact Us" keywords={["Nuvio, nuvio seller,Contact us"]} />

            <div className="ps-page--single" id="contact-us">
                <BreadCrumb breacrumb={breadCrumb} />
                
                <ContactInfo />
                <ContactMap />
                <ContactForm />
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};

export default ContactUsPage;
