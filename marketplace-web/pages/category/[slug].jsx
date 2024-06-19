import React, { useEffect, useState } from 'react';
import BreadCrumb from '~/components/elements/BreadCrumb';
import WidgetShopCategories from '~/components/shared/widgets/WidgetShopCategories';
import WidgetShopBrands from '~/components/shared/widgets/WidgetShopBrands';
import WidgetShopFilterByPriceRange from '~/components/shared/widgets/WidgetShopFilterByPriceRange';
import ProductRepository from '~/repositories/ProductRepository';
import { useRouter } from 'next/router';
import ProductItems from '~/components/partials/product/ProductItems';
import PageContainer from '~/components/layouts/PageContainer';
import FooterDefault from '~/components/shared/footers/FooterDefault';
import Newletters from '~/components/partials/commons/Newletters';
import { Pagination } from 'antd';


const ProductCategoryScreen = () => {
    const Router = useRouter();
    const { slug, page } = Router.query;
    const { query } = Router;
    const [pageSize] = useState(20);
    const [total, setTotal] = useState(0);
    const [category, setCategory] = useState(null);
    const [loading, setLoading] = useState(false);

    async function getCategry(queries) {
        setLoading(true);
        if (slug) {
            const responseData = await ProductRepository.getProductsByCategory(queries);
            if (responseData !== null) {
                setCategory(responseData);
                setTimeout(
                    function () {
                        setLoading(false);
                    }.bind(this),
                    250
                );
            } else {
                setCategory(null)
                setTimeout(
                    function () {
                        setLoading(false);
                    }.bind(this),
                    250
                );
            }
        }
    }

    function handlePagination(page, pageSize) {
        Router.push(`?page=${page}&price_lt=${query?.price_lt}&price_gt=${query?.price_gt}&slug=${query?.slug}`);
    }

    async function getTotalRecords(params) {
        var responseData;
        if (params) {
            responseData = await ProductRepository.getTotalRecords(params);
        } else {
            responseData = await ProductRepository.getTotalRecords();
        }
        setTotal(responseData);
    }

    useEffect(() => {
        if (query && query.slug) {
            var params = {
                key: 'categories',
                value: query.slug,
                price_lt: query.price_lt,
                price_gt: query.price_gt
            };

            const queries = {
                _start: page,
                _limit: pageSize,
                slug: query.slug,
                price_lt: query.price_lt,
                price_gt: query.price_gt
            };
            
            getTotalRecords(params);
            getCategry(queries);
        }
    }, [query]);

    const breadCrumb = [
        {
            text: 'Home',
            url: '/',
        },
        {
            text: 'Shop',
            url: '/',
        },
        {
            text: category ? category.name : 'Product categories',
        },
    ];

    //Views
    let productItemsViews;

    if (!loading) {
        if (category && category.length > 0) {
            productItemsViews = (
                <ProductItems columns={4} products={category} total={total} />
            );
        } else {
            productItemsViews = <p>No Product found</p>;
        }
    } else {
        productItemsViews = <p>Loading...</p>;
    }

    return (
        <PageContainer
            footer={<FooterDefault />}
            title={category ? category.name : 'Category'}
            boxed={true}>
            <div className="ps-page--shop">
                <BreadCrumb breacrumb={breadCrumb} />
                <div className="container">
                    <div className="ps-layout--shop ps-shop--category">
                        <div className="ps-layout__left">
                            <WidgetShopCategories />
                            <WidgetShopBrands />
                            <WidgetShopFilterByPriceRange />
                        </div>
                        <div className="ps-layout__right">
                            <h3 className="ps-shop__heading">
                                {category && category.name}
                            </h3>
                            {productItemsViews}
                            <div className="ps-pagination my-3">
                                <Pagination
                                    total={total}
                                    pageSize={pageSize}
                                    responsive={true}
                                    showSizeChanger={false}
                                    current={page !== undefined ? parseInt(page) : 1}
                                    onChange={(e) => handlePagination(e)}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Newletters layout="container" />
        </PageContainer>
    );
};
export default ProductCategoryScreen;
