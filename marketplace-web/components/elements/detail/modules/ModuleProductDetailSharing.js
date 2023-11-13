import React from 'react';
import { FacebookIcon, FacebookShareButton, TelegramIcon, TelegramShareButton, WhatsappIcon, WhatsappShareButton } from 'react-share';

const ModuleProductDetailSharing = ({pid}) => (
    <div className="ps-product__sharing">
        <FacebookShareButton  url={`https://nuvio.in/product/${pid}`}>
            <FacebookIcon size={50} logoFillColor='white' >

            </FacebookIcon>
        </FacebookShareButton>
        
        <WhatsappShareButton url={`https://nuvio.in/product/${pid}`}>
            <WhatsappIcon size={50} logoFillColor='white'></WhatsappIcon>
        </WhatsappShareButton>

        <TelegramShareButton url={`https://nuvio.in/product/${pid}`}>
            <TelegramIcon size={50} logoFillColor='white'>
            </TelegramIcon>
        </TelegramShareButton>
        {/*
        <a className="facebook" target='_blank' href="https://www.facebook.com/Nuvio.in">
            <i className="fa fa-facebook"></i>
        </a>
        <a className="twitter" target='_blank' href="https://twitter.com/Nuvio_Sellers">
            <i className="fa fa-twitter"></i>
        </a>
       
        <a className="linkedin" target='_blank' href="https://in.linkedin.com/company/nuvio-technologies-pvt-ltd">
            <i className="fa fa-linkedin"></i>
        </a>
        <a className="bg-warning" target='_blank' href="https://www.instagram.com/nuvio.in/">
            <i className="fa fa-instagram"></i>
        </a>*/}
    </div>
);

export default ModuleProductDetailSharing;