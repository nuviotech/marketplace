import { Button } from "antd";
import Axios from "axios";
import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { marketplaceUrl } from "~/repositories/Repository";

export const ModuleProductVariation = ({ product }) => {
    const [variationProducts, setVariationProducts] = useState([]);
    const router = useRouter();

    const getVariations = async () => {
        if (product?.productVariationId && product?.productVariationId != 'NA') {
            await Axios.get(`${marketplaceUrl}/getProductVariations?variationId=${product?.productVariationId}`)
                .then(
                    (response) => {
                        setVariationProducts(response?.data);
                    },
                    (error) => {
                        console.error("product variation : " + error);
                        return null;
                    }
                )
        }
    }

    useEffect(() => {
        getVariations();
    }, [product]);


    //style classes
    const containerStyle = {
        display: 'flex',
        overflowX: 'auto',
        paddingTop:'5px',
        paddingBottom:'10px',
        whiteSpace: 'nowrap',
    };

    const buttonStyle = {
        marginRight: '10px',
        flexShrink: 0,
    };

    const scrollbarStyle = {
        height: '8px',
        backgroundColor: '#1890ff',
        borderRadius: '4px',
    };


    return (
        <>
            <strong>Variations</strong>
            <div style={containerStyle}>

                {

                    variationProducts.map(vd => {
                        var variation = "";
                        var variationProduct = JSON.parse(vd?.product);
                        JSON?.parse(vd?.data)?.map(vName => {
                            if (vName?.key?.length > 0 || vName.value?.length > 0)
                                variation += vName?.key + " : " + vName.value + " ";
                        })

                        return (

                            <Link href="/product/[pid]" as={`/product/${variationProduct?.title?.replaceAll("/", " | ").replaceAll(" ", "-")}&pid=${variationProduct?.id}`}>
                                {product?.id === variationProduct?.id ?
                                    <Button key={variationProduct?.id} type="primary" style={buttonStyle}>
                                        {variation}
                                    </Button>
                                    // <button className="btn btn-bg btn-info mx-1 p-2"><b></b></button>
                                    :
                                    <Button key={variationProduct?.id} type="primary" ghost style={buttonStyle}>
                                        {variation}
                                    </Button>
                                    // <button className="btn btn-bg btn-outline-dark mx-1 p-2">{variation}</button>
                                }
                            </Link>
                        )
                    })
                }
            </div>



        </>
    )
}