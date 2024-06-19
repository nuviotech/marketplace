import React from 'react';
import Link from 'next/link';

const HomeDefaultTopCategories = () => (
    <div className="ps-top-categories">
        <div className="ps-container">
            <h3>Top categories</h3>
            <div className="row">
                <div  className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/category/17?page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{height:"170px"}} src="/static/img/categories/7.jpg" alt="nuvio" />
                        <p>Electronics Gadgets</p>
                    </div>
                </div>
                <div  className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/category/7?page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"100px",height:"170px"}} src="/static/img/categories/image.jfif" alt="nuvio" />
                        <p>Clothings</p>
                    </div>
                </div>
                <div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/search?keyword=laptop&page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"170px",height:"170px"}} src="/static/img/categories/c.jfif" alt="nuvio" />
                        <p>Computers Accessories</p>
                    </div>
                </div>
                <div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/category/8?page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"140px",height:"170px"}} src="/static/img/categories/s.jfif" alt="nuvio" />
                        <p>Home Improments</p>
                    </div>
                </div>
                {/*<div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div className="ps-block--category">
                        <Link href="/shop">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"170px",height:"170px"}} src="/static/img/categories/5.jpg" alt="nuvio" />
                        <p>Health & Beauty</p>
                    </div>
                </div>*/}
                <div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/search?keyword=wall%20clock&page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"150px",height:"170px"}} src="/static/img/categories/clock.jfif" alt="nuvio" />
                        <p>Wall Clocks/Watches</p>
                    </div>
                </div>
                <div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/search?keyword=Jewellery&page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"170px",height:"170px"}} src="/static/img/categories/jwl.png" alt="nuvio" />
                        <p>Jewellery</p>
                    </div>
                </div>
                <div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/search?keyword=Office&page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"170px",height:"170px"}} src="/static/img/categories/office.jfif" alt="nuvio" />
                        <p>Office Accessories</p>
                    </div>
                </div>
                <div className="col-xl-2 col-lg-3 col-md-4 col-sm-4 col-6 ">
                    <div style={{height:"252px"}} className="ps-block--category">
                        <Link href="/category/8?page=1&price_lt=0&price_gt=0">
                            <a className="ps-block__overlay"></a>
                        </Link>
                        <img style={{width:"170px",height:"170px"}} src="/static/img/categories/k.png" alt="nuvio" />
                        <p>Home & Kitchen</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
);

export default HomeDefaultTopCategories;
