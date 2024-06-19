import React from 'react';
import Link from 'next/link';

const WidgetSaleOnSite = () => {
    return (
        <aside className="widget widget_sell-on-site">
            <p>
                <i className="icon-store"></i> Sell on Nuvio?
                <Link href="https://nuviosellers.com/signUp">
                    <a> Register Now !!</a>
                </Link>
            </p>
        </aside>
    );
};

export default WidgetSaleOnSite;
