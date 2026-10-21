import { useRouter } from 'next/router';
import React, { useState, useEffect } from 'react';

const ModuleShopSortBy = () => {
    const Router = useRouter();
    const { action, price_lt, price_gt, page } = Router.query;
    const [selectedSort, setSelectedSort] = useState(action || 'latest');

    function handleChange(event) {
        const sortValue = event.target.value;
        setSelectedSort(sortValue);
        
        // Preserve existing query parameters like price filters
        let queryParams = `?page=1&action=${sortValue}`;
        
        // Preserve price filter if it exists
        if (price_lt && price_gt) {
            queryParams += `&price_lt=${price_lt}&price_gt=${price_gt}`;
        }
        
        Router.push(`/shop${queryParams}`);
    }

    useEffect(() => {
        // Update selected value when action parameter changes
        setSelectedSort(action || 'latest');
    }, [action]);

    return (
        <select
            className="ps-select form-control"
            data-placeholder="Sort Items"
            value={selectedSort}
            onChange={handleChange}
        >
            <option value="latest">Sort by latest</option>
            <option value="avgRating">Sort by average rating</option>
            <option value="lowToHigh">Sort by price: low to high</option>
            <option value="highToLow">Sort by price: high to low</option>
        </select>
    );
};

export default ModuleShopSortBy;
