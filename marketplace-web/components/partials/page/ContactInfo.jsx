import React from 'react';
import { WhatsappIcon } from 'react-share';
import ReactWhatsapp from 'react-whatsapp';

const ContactInfo = () => {
    const email = process.env.NEXT_PUBLIC_EMAIL;
    const whatsappNumber = process.env.NEXT_PUBLIC_WHATSAPP_NUMBER;
    const address = process.env.NEXT_PUBLIC_ADDRESS;

    return (
        <section className="ps-contact-info">
            <div className="container">
                <div className="contact-intro">
                    <span className="contact-eyebrow">GET IN TOUCH</span>
                    <h3>We’re Here to Help</h3>
                    <p>
                        Have a question about your order, products, vendors,
                        or anything else? Our team is ready to assist you.
                    </p>
                </div>

                <div className="contact-primary-grid">
                    {/* Customer Support */}
                    <div className="contact-card">
                        <div className="contact-card__icon">
                            <i className="fa fa-headphones" />
                        </div>

                        <div className="contact-card__content">
                            <span className="contact-card__label">
                                CUSTOMER SUPPORT
                            </span>

                            <h4>Need Help?</h4>

                            <p>
                                Our support team is available to help with
                                orders, products and general queries.
                            </p>

                            <a href={`mailto:${email}`} className="contact-link">
                                {email}
                                <i className="fa fa-arrow-right" />
                            </a>
                        </div>
                    </div>

                    {/* WhatsApp */}
                    <div className="contact-card contact-card--whatsapp">
                        <div className="contact-card__icon">
                            <i className="fa fa-whatsapp" />
                        </div>

                        <div className="contact-card__content">
                            <span className="contact-card__label">
                                WHATSAPP
                            </span>

                            <h4>Chat With Us</h4>

                            <p>
                                Get quick assistance directly through
                                WhatsApp.
                            </p>

                            <ReactWhatsapp
                                number={`91${whatsappNumber}`}
                                message="Hi, I need assistance."
                                element="div">
                                <button className="contact-whatsapp-btn">
                                    Start a conversation
                                    <i className="fa fa-arrow-right" />
                                </button>
                            </ReactWhatsapp>
                        </div>
                    </div>

                    {/* Email */}
                    <div className="contact-card">
                        <div className="contact-card__icon">
                            <i className="fa fa-envelope-o" />
                        </div>

                        <div className="contact-card__content">
                            <span className="contact-card__label">
                                EMAIL
                            </span>

                            <h4>Send Us an Email</h4>

                            <p>
                                For detailed queries, reach out to us through
                                email and we’ll get back to you.
                            </p>

                            <a href={`mailto:${email}`} className="contact-link">
                                Send an email
                                <i className="fa fa-arrow-right" />
                            </a>
                        </div>
                    </div>
                </div>

                {/* Office */}
                <div className="contact-office">
                    <div className="contact-office__icon">
                        <i className="fa fa-map-marker" />
                    </div>

                    <div className="contact-office__content">
                        <span className="contact-card__label">
                            OUR OFFICE
                        </span>

                        <h4>Registered Address</h4>

                        <p>{address}</p>
                    </div>
                </div>

                {/* Other Enquiries */}
                <div className="contact-other">
                    <div className="contact-other__header">
                        <span className="contact-eyebrow">
                            OTHER ENQUIRIES
                        </span>
                        <h3>Looking for Something Else?</h3>
                    </div>

                    <div className="contact-other__grid">
                        <div className="contact-other__item">
                            <i className="fa fa-briefcase" />
                            <div>
                                <h4>Work With Us</h4>
                                <p>
                                    Interested in joining our team? Send your
                                    CV to us.
                                </p>
                                <a href={`mailto:${email}`}>
                                    Send your CV
                                </a>
                            </div>
                        </div>

                        <div className="contact-other__item">
                            <i className="fa fa-newspaper-o" />
                            <div>
                                <h4>Media Relations</h4>
                                <p>
                                    For media and press-related enquiries,
                                    contact our team.
                                </p>
                                <a href={`mailto:${email}`}>
                                    Contact Media
                                </a>
                            </div>
                        </div>

                        <div className="contact-other__item">
                            <i className="fa fa-shopping-bag" />
                            <div>
                                <h4>Vendor Support</h4>
                                <p>
                                    Need assistance as a seller or vendor?
                                    We’re here to help.
                                </p>
                                <a href={`mailto:${email}`}>
                                    Contact Vendor Support
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ContactInfo;