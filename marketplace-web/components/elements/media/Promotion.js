import React from 'react';
import Link from 'next/link';
import { baseUrl } from '~/repositories/Repository';

const Promotion = ({ link, image }) => {
    if (image) {
        return (
            <Link href={link}>
                <a className="ps-collection">
                    <img  src={`${baseUrl}${image.url}`} alt="nuvio " />
                </a>
            </Link>
        );
    } else {
        return (
            <Link href={link ? link : '/shop'}>
                <a className="ps-collection">
                    <img style={{width:"100%",height:"auto"}} src="/static/img/not-found.jpg" alt="nuvio" />
                </a>
            </Link>
        );
    }
};

export default Promotion;
