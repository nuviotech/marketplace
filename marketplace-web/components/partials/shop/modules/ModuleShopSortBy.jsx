import { useRouter } from 'next/router';
import React from 'react';
import { useState } from 'react';

const ModuleShopSortBy = () => {
    const Router = useRouter();

    function handleChange  (event){
        Router.push(`/shop?page=1&action=`+event);
    } 

    return (
        <select
            className="ps-select form-control"
            data-placeholder="Sort Items" onChange={(v)=>{handleChange(v.target.value)}} >
            <option value='latest'>Sort by latest</option>
            <option value="avgRating">Sort by average rating</option>
            <option value="lowToHigh">Sort by price: low to high</option>
            <option value="highToLow">Sort by price: high to low</option>
        </select>
    );
};

export default ModuleShopSortBy;
