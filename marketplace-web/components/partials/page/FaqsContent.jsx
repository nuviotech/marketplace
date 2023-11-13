import React from 'react';

const FaqsContent = () => (
    <div className="table-responsive">
        <table className="table ps-table--faqs">
            <tbody>
                <tr>
                    <td className="heading" rowSpan="5">
                        <h4>SHIPPING</h4>
                    </td>
                    <td className="question">
                        What shipping options do you offer?         
                    </td>
                    <td>
                         We offer several shipping options, including standard, express, and international shipping. We also offer free shipping on orders over a certain amount.
                    </td>
                </tr>
                <tr>
                    <td className="question">How long does it take to receive my order?</td>
                    <td>
                        The delivery time varies depending on the shipping method you choose and your location. Standard shipping usually takes 3-5 business days, while express shipping takes 1-2 business days. International shipping can take up to 10 business days.
                    </td>
                </tr>
                <tr>
                    <td className="question">
                        How much does shipping cost?
                    </td>
                    <td>
                        The shipping cost depends on the shipping method you choose, the weight and dimensions of the package, and your location. You can calculate the shipping cost at checkout before placing your order.
                    </td>
                </tr>
                <tr>
                    
                    <td className="question">
                        Do you ship internationally?
                    </td>
                    <td>
                        Yes, we ship to most countries worldwide. However, shipping rates and delivery times may vary depending on the destination country.
                    </td>
                </tr>
                <tr>
                    <td className="question">Can I track my order?</td>
                    <td>
                        Yes, you can track your order by logging into your account or using the tracking number provided in the shipping confirmation email.
                    </td>
                </tr>
                <tr>
                    <td className="heading" rowSpan="5">
                        <h4>Order & Retunrs</h4>
                    </td>
                    <td className="question"> What is your return policy?</td>
                    <td>
                        Our return policy allows for returns within 7 days of delivery for a full refund or exchange for selective orders. Please see our return policy page for more details.
                    </td>
                </tr>
                <tr>
                    <td className="question">
                        How do I change my shipping address? 
                    </td>
                    <td>
                        You can change your shipping address by logging into your account and updating your address information before your order ships. If your order has already shipped, please contact our customer service team for assistance.
                    </td>
                </tr>
                <tr>
                    <td className="question">
                        What happens if my package is lost or damaged during shipping?
                    </td>
                    <td>
                        If your package is lost or damaged during shipping, please contact our customer service team immediately so we can assist you with filing a claim with the shipping carrier.
                    </td>
                </tr>
                <tr>
                    <td className="question">Can I expedite my shipping for an additional fee?</td>
                    <td>
                        Yes, you can expedite your shipping for an additional fee. The cost and delivery time will vary depending on the shipping method you choose and your location. You can see the options and cost at checkout before placing your order.
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
);

export default FaqsContent;
