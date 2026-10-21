import React from 'react';

const OurPartners = () => {
    const partners = [
        {
            id: 1,
            logo: '/static/partners/unnamed.png',
            url: 'https://manageecom.com'
        },
        {
            id: 2,
            logo: '/static/partners/qikdlogo.jpg',
            url: 'https://qikd.in'
        },
        {
            id: 3,
            logo: '/static/partners/nuvio2.webp',
            url: 'https://nuvio.in'
        },
        {
            id: 4,
            logo: '/static/partners/nuvio.webp',
            url: 'https://nuviosellers.com'
        }
    ];

    return (
        <section className="ps-partners">
            <div className="ps-container">
                <h3 className="ps-partners__title">
                    OUR <span>PARTNERS</span>
                </h3>

                <div className="ps-partners__line"></div>

                <div className="ps-partners__grid">
                    {partners.map((partner) => (
                        <a
                            key={partner.id}
                            href={partner.url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="ps-partners__item"
                        >
                            <img
                                src={partner.logo}
                                alt="Partner"
                                loading="lazy"
                                className="ps-partners__logo"
                            />
                        </a>
                    ))}
                </div>
            </div>
        </section>
    );
};

export default OurPartners;