import React from 'react';
import Link from 'next/link';

const ContactInfo = () => (
    <div className="ps-contact-info">
        <div className="container">
            <div className="ps-section__header">
                <h3>Contact Us For Any Questions</h3>
            </div>
            <div className="ps-section__content">
                <div className="row">
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12 ">
                        <div className="ps-block--contact-info">
                            <h4>Contact Directly</h4>
                            <p>
                                <a href={`mailto:${process.env.NEXT_PUBLIC_EMAIL}`}>
                                    {process.env.NEXT_PUBLIC_EMAIL}
                                </a>
                                <span><b>{process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}</b></span>
                                <a
                                    href={`https://wa.me/+91${process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}`}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                >
                                    <i class="fa fa-whatsapp whatsapp-icon fa-3x text-success"  ></i>
                                </a>
                            </p>
                        </div>
                    </div>
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12 ">
                        <div className="ps-block--contact-info">
                            <h4>Registered Address</h4>
                            <p>
                                <span>
                                   {process?.env?.NEXT_PUBLIC_ADDRESS}
                                </span><br />
                            </p>
                        </div>
                    </div>
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12 ">
                        <div className="ps-block--contact-info">
                            <h4>Work With Us</h4>
                            <p>
                                <span>Send your CV to our email:</span>
                                <a href={`mailto:${process.env.NEXT_PUBLIC_EMAIL}`}><b>{process.env.NEXT_PUBLIC_EMAIL}</b></a>
                            </p>
                        </div>
                    </div>
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12 ">
                        <div className="ps-block--contact-info">
                            <h4>Customer Service</h4>
                            <p>
                                <a href={`mailto:${process.env.NEXT_PUBLIC_EMAIL}`}>{process.env.NEXT_PUBLIC_EMAIL}</a>
                                <span><b>+91 {process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}</b></span>
                                <a
                                    href={`https://wa.me/+91${process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}`}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                >
                                    <i class="fa fa-whatsapp whatsapp-icon fa-3x text-success"  ></i>
                                </a>
                            </p>
                        </div>
                    </div>
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12 ">
                        <div className="ps-block--contact-info">
                            <h4>Media Relations</h4>
                            <p>
                                <a href={`mailto:${process.env.NEXT_PUBLIC_EMAIL}`}>support@nuvio.in</a>
                                <span>+91 {process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}</span>
                            </p>
                        </div>
                    </div>
                    <div className="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12 ">
                        <div className="ps-block--contact-info">
                            <h4>Vendor Support</h4>
                            <p>
                                <a href={`mailto:${process.env.NEXT_PUBLIC_EMAIL}`}> {process.env.NEXT_PUBLIC_EMAIL}</a>
                                <span>+91 {process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}</span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
);

export default ContactInfo;
