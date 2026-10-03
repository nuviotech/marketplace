import React, { useEffect, useState } from 'react';
import { Pagination } from 'antd';
import Product from '~/components/elements/products/Product';
import ProductWide from '~/components/elements/products/ProductWide';
import ProductRepository from '~/repositories/ProductRepository';
import ModuleShopSortBy from '~/components/partials/shop/modules/ModuleShopSortBy';
import { useRouter } from 'next/router';
import { generateTempArray } from '~/utilities/common-helpers';
import SkeletonProduct from '~/components/elements/skeletons/SkeletonProduct';
import useGetProducts from '~/hooks/useGetProducts';

const ShopItems = ({ columns = 4, pageSize = 20 }) => {
    const Router = useRouter();
    const { query } = Router;

    const page = query.page ? parseInt(query.page) : 1;

    const [listView, setListView] = useState(true);
    const [total, setTotal] = useState(0);

    const [classes, setClasses] = useState(
        'col-xl-4 col-lg-4 col-md-3 col-sm-6 col-6'
    );

    const { productItems, loading, getProducts } = useGetProducts();

    /*
     * Change grid/list view
     */
    function handleChangeViewMode(e) {
        e.preventDefault();
        setListView(!listView);
    }

    /*
     * Pagination
     *
     * IMPORTANT:
     * Always preserve the current price filter when
     * moving between pages.
     */
    function handlePagination(selectedPage) {
        const params = new URLSearchParams();

        params.set('page', selectedPage);

        if (
            query.price_gt !== undefined &&
            query.price_gt !== '' &&
            query.price_gt !== 'undefined'
        ) {
            params.set('price_gt', query.price_gt);
        }

        if (
            query.price_lt !== undefined &&
            query.price_lt !== '' &&
            query.price_lt !== 'undefined'
        ) {
            params.set('price_lt', query.price_lt);
        }

        if (
            query.action !== undefined &&
            query.action !== '' &&
            query.action !== 'undefined'
        ) {
            params.set('action', query.action);
        }

        Router.push(`/shop?${params.toString()}`);
    }

    /*
     * Get total product count
     */
    async function getTotalRecords(params) {
        try {
            const responseData =
                await ProductRepository.getTotalRecords(params);

            console.log(
                '### SHOP PRODUCT COUNT ###',
                responseData
            );

            if (typeof responseData === 'number') {
                setTotal(responseData);
            } else if (
                responseData &&
                typeof responseData === 'object'
            ) {
                setTotal(
                    responseData.total ??
                    responseData.count ??
                    0
                );
            } else {
                setTotal(Number(responseData) || 0);
            }
        } catch (error) {
            console.error(
                '### SHOP PRODUCT COUNT ERROR ###',
                error
            );

            setTotal(0);
        }
    }

    /*
     * Set product grid columns
     */
    function handleSetColumns() {
        switch (columns) {
            case 2:
                setClasses(
                    'col-xl-6 col-lg-6 col-md-6 col-sm-6 col-6'
                );
                break;

            case 4:
                setClasses(
                    'col-xl-3 col-lg-4 col-md-6 col-sm-6 col-6'
                );
                break;

            case 6:
                setClasses(
                    'col-xl-2 col-lg-4 col-md-6 col-sm-6 col-6'
                );
                break;

            default:
                setClasses(
                    'col-xl-4 col-lg-4 col-md-3 col-sm-6 col-6'
                );
        }
    }

    /*
     * Get products whenever the URL query changes.
     *
     * Example:
     *
     * /shop?page=1&price_gt=200&price_lt=500
     *
     * /shop?page=2&price_gt=200&price_lt=500
     *
     * Both requests keep the same price filter.
     */
    useEffect(() => {
        if (!Router.isReady) {
            return;
        }

        /*
         * Calculate offset from page number.
         * Page 1 = offset 0, Page 2 = offset 20, etc.
         */
        const offset = (page - 1) * pageSize;

        const params = {
            _start: offset,
            _limit: pageSize,
        };

        const hasPriceFilter =
            query.price_gt !== undefined &&
            query.price_lt !== undefined &&
            query.price_gt !== '' &&
            query.price_lt !== '' &&
            query.price_gt !== 'undefined' &&
            query.price_lt !== 'undefined';

        const hasAction =
            query.action !== undefined &&
            query.action !== '' &&
            query.action !== 'undefined';

        /*
         * Price filter
         */
        if (hasPriceFilter) {
            params.price_gt = query.price_gt;
            params.price_lt = query.price_lt;
        }

        /*
         * Sorting
         */
        if (hasAction) {
            params.action = query.action;
        }

        console.log(
            '### SHOP PARAMS ###',
            params
        );

        getTotalRecords(params);
        getProducts(params);
        handleSetColumns();

    }, [
        Router.isReady,
        query.page,
        query.price_gt,
        query.price_lt,
        query.action
    ]);

    /*
     * Products view
     */
    let productItemsView;

    if (!loading) {
        let itemsList = Array.isArray(productItems)
            ? productItems
            : typeof productItems === 'string'
            ? (() => {
                  try {
                      return JSON.parse(productItems);
                  } catch (e) {
                      return [];
                  }
              })()
            : [];

        if (
            Array.isArray(itemsList) &&
            itemsList.length > 0
        ) {
            if (listView) {
                const items = itemsList.map((item) => (
                    <div
                        className={classes}
                        key={
                            item?.id ||
                            item?.productRefId
                        }>
                        <Product
                            product={item}
                            height="195px"
                            start={true}
                        />
                    </div>
                ));

                productItemsView = (
                    <div className="ps-shop-items">
                        <div className="row">
                            {items}
                        </div>
                    </div>
                );
            } else {
                productItemsView = itemsList.map(
                    (item) => (
                        <ProductWide
                            product={item}
                            key={
                                item?.id ||
                                item?.productRefId
                            }
                        />
                    )
                );
            }
        } else {
            productItemsView = (
                <p>No product found.</p>
            );
        }
    } else {
        const skeletonItems =
            generateTempArray(12).map((item) => (
                <div
                    className={classes}
                    key={item}>
                    <SkeletonProduct />
                </div>
            ));

        productItemsView = (
            <div className="row">
                {skeletonItems}
            </div>
        );
    }

    return (
        <div className="ps-shopping">

            <div className="ps-shopping__header">

                <p>
                    <strong className="mr-2">
                        {total}
                    </strong>
                    Products found
                </p>

                <div className="ps-shopping__actions">

                    <ModuleShopSortBy />

                    <div className="ps-shopping__view">

                        <p>View</p>

                        <ul className="ps-tab-list">

                            <li
                                className={
                                    listView === true
                                        ? 'active'
                                        : ''
                                }>
                                <a
                                    href="#"
                                    onClick={(e) =>
                                        handleChangeViewMode(
                                            e
                                        )
                                    }>
                                    <i className="icon-grid"></i>
                                </a>
                            </li>

                            <li
                                className={
                                    listView !== true
                                        ? 'active'
                                        : ''
                                }>
                                <a
                                    href="#"
                                    onClick={(e) =>
                                        handleChangeViewMode(
                                            e
                                        )
                                    }>
                                    <i className="icon-list4"></i>
                                </a>
                            </li>

                        </ul>

                    </div>

                </div>

            </div>

            <div className="ps-shopping__content">
                {productItemsView}
            </div>

            <div className="ps-shopping__footer text-center">

                <div className="ps-pagination">

                    <Pagination
                        total={total}
                        pageSize={pageSize}
                        responsive={true}
                        showSizeChanger={false}
                        current={page}
                        onChange={(selectedPage) =>
                            handlePagination(
                                selectedPage
                            )
                        }
                    />

                </div>

            </div>

        </div>
    );
};

export default ShopItems;