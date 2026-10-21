import React from 'react';

const DownloadAppSection = ({ link = '#' }) => {
    return (
        <div className="ps-download-app-banner">
            <a
                href={link}
                className="ps-download-app-banner__wrapper"
                aria-label="Download Our App">
                <div className="ps-download-app-banner__content">
                    <div className="ps-download-app-banner__icon">
                        <i className="icon-smartphone"></i>
                    </div>
                    <div className="ps-download-app-banner__text-group">
                        <h4 className="ps-download-app-banner__title">
                            Download Our App
                        </h4>
                        <p className="ps-download-app-banner__subtext">
                            Shop faster and easier on our mobile app.
                        </p>
                    </div>
                </div>
                <div className="ps-download-app-banner__action">
                    <span className="ps-download-app-banner__btn">
                        Download App
                    </span>
                </div>
            </a>
        </div>
    );
};

export default DownloadAppSection;
