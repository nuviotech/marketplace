import React from 'react';

import { Tabs } from 'antd';
import PartialDescription from '~/components/elements/detail/description/PartialDescription';
import PartialSpecification from '~/components/elements/detail/description/PartialSpecification';
import PartialVendor from '~/components/elements/detail/description/PartialVendor';
import PartialReview from '~/components/elements/detail/description/PartialReview';
import PartialOffer from '~/components/elements/detail/description/PartialOffer';

const { TabPane } = Tabs;

const DefaultDescription = ({product}) => {
   // console.log("PRODUCT "+JSON.stringify(product));
    return (
        <div className="ps-product__content ps-tab-root">
            
            <Tabs defaultActiveKey="1">
                <TabPane tab="Description" key="1">
                    <PartialDescription product={product} />
                </TabPane>
                <TabPane tab="Specification" key="2">
                    <PartialSpecification product={product} />
                </TabPane>
               
                
                <TabPane tab="Reviews" key="3">
                    <PartialReview  product={product}/>
                </TabPane>
                <TabPane tab="Vendor" key="4">
                    <PartialVendor product={product} />
                </TabPane>
                <TabPane tab="Questions and Answers" key="5">
                    no data available.
                </TabPane>
                <TabPane tab="More Offers" key="6">
                    <PartialOffer />
                </TabPane>
            </Tabs>
        </div>
    );
};

export default DefaultDescription;
