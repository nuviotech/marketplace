import React, { useState } from 'react';
import { Slider, Checkbox } from 'antd';
import { useRouter } from 'next/router';

const WidgetShopFilterByPriceRange = () => {
    const Router = useRouter();
    const { page } = Router.query;
    const { query } = Router;
    const [min, setMin] = useState(0);
    const [max, setMax] = useState(20000);

    
//alert("QUery : "+JSON.stringify(query));
    function handleChangeRange(value) {
        setMin(value[0]);
        price_lt: value[1], setMax(value[1]);

        /*  const params = {
            price_gt: value[0],
        };*/
        Router.push(`?price_gt=${value[0]}&price_lt=${value[1]}&page=1&slug=${query?.slug}`);
        /*this.props.dispatch(getProductsByPrice(params));*/
    }

    function underPriceProduct(value){
        
        Router.push(`?price_gt=0&price_lt=${value}&page=1&slug=${query?.slug}`);
    }

    return (
        <aside className="widget widget_shop">
            <figure>
                <h4 className="widget-title">By Price</h4>
                <Slider
                    range
                    defaultValue={[0, 20000]}
                    max={20000}
                    onAfterChange={(e) => handleChangeRange(e)}
                />
                <p>
                    Price: ₹ {min} - ₹ {max}
                </p>
            </figure>
            <figure>
                <h4 className="widget-title">Price</h4>
                
                <span style={{cursor:"pointer"}} onClick={()=>{underPriceProduct(500)}}>Under ₹500</span><br />
                <span style={{cursor:"pointer"}} onClick={()=>{underPriceProduct(1000)}}>Under ₹1000</span><br />
                <span style={{cursor:"pointer"}} onClick={()=>{underPriceProduct(2000)}}>Under ₹2000</span><br />
                <span style={{cursor:"pointer"}} onClick={()=>{underPriceProduct(5000)}}>Under ₹5000</span><br />
                <span style={{cursor:"pointer"}} onClick={()=>{underPriceProduct(10000)}}>Under ₹10000</span><br />
                <span style={{cursor:"pointer"}} onClick={()=>{underPriceProduct(20000)}}>Under ₹20000</span><br />

                <div className='mt-5'>
                    <input className='text-center' placeholder='₹ MIN' style={{width:"35%",border:"1px solid #ffc107",borderRadius:"5px",height:'35px'}} type="text" />
                    <input className='text-center' placeholder='₹ MAX' style={{width:"35%",border:"1px solid #ffc107",borderRadius:"5px",margin:"0 2% ",height:'35px'}}type="text" />
                    <button style={{width:"25%",border:"1px solid #ffc107",borderRadius:"5px",height:'35px'}}>GO</button>
                </div>
            </figure>

        </aside>
    );
};

export default WidgetShopFilterByPriceRange;
