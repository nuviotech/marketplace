import React, { useEffect, useRef, useState } from 'react';
import Link from 'next/link';
import Router from 'next/router';
import { Spin } from 'antd';
import ProductRepository from '~/repositories/ProductRepository';
import ProductSearchResult from '~/components/elements/products/ProductSearchResult';
import useProduct from '~/hooks/useProduct';



function useDebounce(value, delay) {
    const [debouncedValue, setDebouncedValue] = useState(value);
    useEffect(() => {
        // Update debounced value after delay
        const handler = setTimeout(() => {
            setDebouncedValue(value);
        }, delay);

        return () => {
            clearTimeout(handler);
        };
    }, [value, delay]);

    return debouncedValue;
}

const SearchHeader = () => {
    const inputEl = useRef(null);
    const [isSearch, setIsSearch] = useState(false);
    const [keyword, setKeyword] = useState('');
    const [resultItems, setResultItems] = useState(null);
    const [loading, setLoading] = useState(false);
    const debouncedSearchTerm = useDebounce(keyword, 300);

    const [categoriesArray, setCategoriesArray] = useState(['all']);
    const [fetchCat, setfetchCat] = useState(true);

    function handleClearKeyword() {
        setKeyword('');
        setIsSearch(false);
        setLoading(false);
    }

    function handleSubmit(e) {
        e.preventDefault();
        Router.push(`/search?keyword=${keyword}&page=1&price_lt=0&price_gt=0`);
    }

    useEffect(async () => {
        if (debouncedSearchTerm) {
            setLoading(true);
            if (keyword) {
                const queries = {
                    _limit: 5,
                    title_contains: keyword,
                };
                const products = ProductRepository.getRecords(queries);
                products.then((result) => {
                    setLoading(false);
                    setResultItems(result);
                    setIsSearch(true);
                });

            } else {
                setIsSearch(false);
                setKeyword('');
            }
            if (loading) {
                setIsSearch(false);
            }
        } else {
            setLoading(false);
            setIsSearch(false);
        }
        /*
        if (fetchCat) {
            setCategoriesArray(await ProductRepository?.getProductCategories());
            setfetchCat(false);
        }*/
    }, [debouncedSearchTerm]);

    // Views
    let productItemsView,
        clearTextView,
        selectOptionView,
        loadingView,
        loadMoreView;
    if (!loading) {
        if (resultItems && resultItems.length > 0) {
            if (resultItems.length > 5) {
                loadMoreView = (
                    <div className="ps-panel__footer text-center">
                    </div>
                );
            }
            const { thumbnailImage, price, title } = useProduct();
            productItemsView = resultItems.map((product) => (
                <div className="ps-product ps-product--wide ps-product--search-result">
                    <div className="ps-product__thumbnail">
                        {/*<Link href="/product/[pid]" as={`/product/${product.title.replaceAll("/", " | ").replaceAll(" ", "-")}&pid=${product.id}`}>
                            <a>{thumbnailImage(product)}</a>
                        </Link>*/}
                        <Link href="/product/[pid]" as={`/product/${product.title.replaceAll("/", " | ").replaceAll(" ", "-")}&pid=${product.id}`}>
                            <a>{thumbnailImage(product)}</a>
                        </Link>
                    </div>
                    <div className="ps-product__content">
                        {title(product)}
                        <div className="ps-product__rating">

                            <span>{product.ratingCount}</span>
                        </div>
                        {price(product)}
                    </div>
                </div>
            ))

            //<ProductSearchResult product={product} />







        } else {
            productItemsView = <p>No product found.</p>;
        }
        if (keyword !== '') {
            clearTextView = (
                <span className="ps-form__action" onClick={handleClearKeyword}>
                    <i className="icon icon-cross2"></i>
                </span>
            );
        }
    } else {
        loadingView = (
            <span className="ps-form__action">
                <Spin size="small" />
            </span>
        );
    }
    /*
        selectOptionView = categoriesArray?.map((option) => (
            <option value={option.name} key={option.categoryId}>
                {option.name}
            </option>
        ));*/

    return (
        <form
            className="ps-form--quick-search"
            method="get"
            action="/"
            onSubmit={handleSubmit}>
            {/* <div className="ps-form__categories">
                <select className="form-control">
                    <option value="all" key="0">
                        All
                    </option>
                    {selectOptionView}
                </select>
                </div>*/}
            <div className="ps-form__input">
                <input
                    ref={inputEl}
                    className="form-control"
                    type="text"
                    value={keyword}
                    placeholder="I'm shopping for..."
                    onChange={(e) => setKeyword(e.target.value)}
                />
                {clearTextView}
                {loadingView}
            </div>
            <button onClick={handleSubmit}>Search</button>
            <div
                className={`ps-panel--search-result${isSearch ? ' active ' : ''
                    }`}>
                <div className="ps-panel__content">{productItemsView}</div>
                {loadMoreView}
            </div>
        </form>
    );
};

export default SearchHeader;
