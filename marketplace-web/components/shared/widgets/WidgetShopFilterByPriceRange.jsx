import React, {
    useEffect,
    useState
} from 'react';

import { Slider } from 'antd';
import { useRouter } from 'next/router';

const WidgetShopFilterByPriceRange = () => {
    const Router = useRouter();
    const { query } = Router;

    const [min, setMin] = useState(0);
    const [max, setMax] = useState(20000);

    const [inputMin, setInputMin] = useState('');
    const [inputMax, setInputMax] = useState('');

    /*
     * Keep filter UI synchronized with URL.
     */
    useEffect(() => {
        if (!Router.isReady) {
            return;
        }

        const urlMin =
            query.price_gt !== undefined &&
            query.price_gt !== 'undefined'
                ? Number(query.price_gt)
                : 0;

        const urlMax =
            query.price_lt !== undefined &&
            query.price_lt !== 'undefined'
                ? Number(query.price_lt)
                : 20000;

        setMin(urlMin);
        setMax(urlMax);

        /*
         * Only show values in inputs when
         * a filter exists in the URL.
         */
        if (
            query.price_gt !== undefined &&
            query.price_gt !== 'undefined'
        ) {
            setInputMin(query.price_gt);
        } else {
            setInputMin('');
        }

        if (
            query.price_lt !== undefined &&
            query.price_lt !== 'undefined'
        ) {
            setInputMax(query.price_lt);
        } else {
            setInputMax('');
        }

    }, [
        Router.isReady,
        query.price_gt,
        query.price_lt
    ]);

    /*
     * Apply price filter.
     */
    function applyPriceFilter(
        minimumValue,
        maximumValue
    ) {
        let minimum = Number(minimumValue);
        let maximum = Number(maximumValue);

        if (
            minimumValue === '' ||
            maximumValue === ''
        ) {
            alert(
                'Please enter both minimum and maximum price.'
            );
            return;
        }

        if (
            Number.isNaN(minimum) ||
            Number.isNaN(maximum)
        ) {
            alert(
                'Please enter valid prices.'
            );
            return;
        }

        if (minimum < 0 || maximum < 0) {
            alert(
                'Price cannot be negative.'
            );
            return;
        }

        if (minimum > 20000) {
            minimum = 20000;
        }

        if (maximum > 20000) {
            maximum = 20000;
        }

        if (minimum > maximum) {
            alert(
                'Minimum price cannot be greater than maximum price.'
            );
            return;
        }

        setMin(minimum);
        setMax(maximum);

        setInputMin(String(minimum));
        setInputMax(String(maximum));

        /*
         * Preserve other query parameters.
         *
         * Most importantly, reset page to 1.
         */
        const newQuery = {
            ...query,
            page: 1,
            price_gt: minimum,
            price_lt: maximum
        };

        /*
         * Remove undefined values.
         */
        Object.keys(newQuery).forEach(
            (key) => {
                if (
                    newQuery[key] === undefined ||
                    newQuery[key] === 'undefined'
                ) {
                    delete newQuery[key];
                }
            }
        );

        Router.push({
            pathname: Router.pathname,
            query: newQuery
        });
    }

    /*
     * Slider finished moving.
     */
    function handleSliderAfterChange(value) {
        applyPriceFilter(
            value[0],
            value[1]
        );
    }

    /*
     * GO button.
     */
    function handleGo() {
        applyPriceFilter(
            inputMin,
            inputMax
        );
    }

    /*
     * Input minimum.
     */
    function handleMinChange(e) {
        setInputMin(e.target.value);
    }

    /*
     * Input maximum.
     */
    function handleMaxChange(e) {
        setInputMax(e.target.value);
    }

    /*
     * Press Enter inside either input.
     */
    function handleInputKeyDown(e) {
        if (e.key === 'Enter') {
            handleGo();
        }
    }

    /*
     * Under price shortcuts.
     */
    function underPriceProduct(value) {
        const newQuery = {
            ...query,
            page: 1,
            price_gt: 0,
            price_lt: value
        };

        Object.keys(newQuery).forEach(
            (key) => {
                if (
                    newQuery[key] === undefined ||
                    newQuery[key] === 'undefined'
                ) {
                    delete newQuery[key];
                }
            }
        );

        Router.push({
            pathname: Router.pathname,
            query: newQuery
        });
    }

    return (
        <aside className="widget widget_shop">

            <figure>

                <h4 className="widget-title">
                    By Price
                </h4>

                <Slider
                    range
                    min={0}
                    max={20000}
                    value={[min, max]}
                    onAfterChange={
                        handleSliderAfterChange
                    }
                />

                <p>
                    Price: ₹ {min} - ₹ {max}
                </p>

            </figure>

            <figure>

                <h4 className="widget-title">
                    Price
                </h4>

                <span
                    style={{
                        cursor: 'pointer'
                    }}
                    onClick={() =>
                        underPriceProduct(500)
                    }>
                    Under ₹500
                </span>

                <br />

                <span
                    style={{
                        cursor: 'pointer'
                    }}
                    onClick={() =>
                        underPriceProduct(1000)
                    }>
                    Under ₹1000
                </span>

                <br />

                <span
                    style={{
                        cursor: 'pointer'
                    }}
                    onClick={() =>
                        underPriceProduct(2000)
                    }>
                    Under ₹2000
                </span>

                <br />

                <span
                    style={{
                        cursor: 'pointer'
                    }}
                    onClick={() =>
                        underPriceProduct(5000)
                    }>
                    Under ₹5000
                </span>

                <br />

                <span
                    style={{
                        cursor: 'pointer'
                    }}
                    onClick={() =>
                        underPriceProduct(10000)
                    }>
                    Under ₹10000
                </span>

                <br />

                <span
                    style={{
                        cursor: 'pointer'
                    }}
                    onClick={() =>
                        underPriceProduct(20000)
                    }>
                    Under ₹20000
                </span>

                <br />

                <div className="mt-5">

                    <input
                        className="text-center"
                        placeholder="₹ MIN"
                        type="number"
                        value={inputMin}
                        onChange={
                            handleMinChange
                        }
                        onKeyDown={
                            handleInputKeyDown
                        }
                        style={{
                            width: '35%',
                            border:
                                '1px solid #ffc107',
                            borderRadius: '5px',
                            height: '35px'
                        }}
                    />

                    <input
                        className="text-center"
                        placeholder="₹ MAX"
                        type="number"
                        value={inputMax}
                        onChange={
                            handleMaxChange
                        }
                        onKeyDown={
                            handleInputKeyDown
                        }
                        style={{
                            width: '35%',
                            border:
                                '1px solid #ffc107',
                            borderRadius: '5px',
                            margin: '0 2%',
                            height: '35px'
                        }}
                    />

                    <button
                        onClick={handleGo}
                        style={{
                            width: '25%',
                            border:
                                '1px solid #ffc107',
                            borderRadius: '5px',
                            height: '35px'
                        }}>
                        GO
                    </button>

                </div>

            </figure>

        </aside>
    );
};

export default WidgetShopFilterByPriceRange;