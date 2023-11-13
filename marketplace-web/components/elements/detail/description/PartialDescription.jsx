import React from 'react';

const PartialDescription = ({product}) => (
    <div className="ps-document">
        <h5>{product.title}</h5>
        <div className='mt-3' dangerouslySetInnerHTML={{__html:product.description}}>
        
        </div>
    </div>
);

export default PartialDescription;
