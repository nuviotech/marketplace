import React from 'react';

const FooterCopyright = () => (
    <div className=" text-center">
        <p>&copy; {new Date().getFullYear()} {process.env.NEXT_PUBLIC_FOOTER_WEBSITE_NAME } All Rights Reserved</p>
    </div>
);

export default FooterCopyright;
