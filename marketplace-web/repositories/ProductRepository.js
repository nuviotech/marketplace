import axios from 'axios';
import Repository, { baseUrl, serializeQuery, marketplaceUrl, policyMakerUrl, policyMakerKey } from './Repository';

class ProductRepository {
    
    


  /*  //this method helps of SSG (Static side generation) SSR
    async getAllProducts() {
        const reponse = await Repository.get(
            `${marketplaceUrl}/getAllProducts`
        )
            .then((response) => {
                return response.data;
            })
            .catch((error) => ({ error: JSON.stringify(error) }));
        return reponse;
    }
*/

    async getRelatedProducts(keywords){
        const result = await axios.get(`${marketplaceUrl}/getRelatedProduct?keywords=${keywords}`)
        .then(
            (response)=>{
                let data = response.data;
                if (typeof data === 'string') {
                    try { data = JSON.parse(data); } catch(e) {}
                }
                return data;
            },
            (error)=>{
                console.error("Related product(error) : "+error);
                return null;
            }
        )
        return result;
    }

    async getRecords(params) {
        //alert(JSON.stringify(params));
        const reponse = await Repository.get(
            `${marketplaceUrl}/searchRecords?keyword=${serializeQuery(params)}`
        )
            .then((response) => {
                let data = response.data;
                if (typeof data === 'string') {
                    try { data = JSON.parse(data); } catch(e) {}
                }
                return data;
            })
            .catch((error) => ({ error: JSON.stringify(error) }));
          //console.log("get Recorder quesry: products?  : "+JSON.stringify(response) );
        return reponse;
    }

    async getProducts(params) {
    try {
        const response = await Repository.get(
            `${marketplaceUrl}/products?${serializeQuery(
                params
            )}`
        );

        let data = response.data;

        if (typeof data === 'string') {
            try {
                data = JSON.parse(data);
            } catch (e) {
                data = [];
            }
        }

        if (Array.isArray(data)) {
            return data;
        }

        if (data && typeof data === 'object') {
            return data;
        }

        return [];
    } catch (error) {
        console.error(
            '### GET PRODUCTS ERROR ###',
            error
        );

        return [];
    }
}

    async getBrands() {
        //`${baseUrl}/brands`
        const reponse = await Repository.get(`${policyMakerUrl}/brands`,{ 
            headers: {
                apikey: policyMakerKey,
            }
        })
            .then((response) => {
                let data = response.data;
                if (typeof data === 'string') {
                    try { data = JSON.parse(data); } catch(e) {}
                }
                return data;
            })
            .catch((error) => ({ error: JSON.stringify(error) }));
        return reponse;
    }

    async getProductCategories() {
        //${baseUrl}/product-categories
        const reponse = await Repository.get(`${policyMakerUrl}/product-categories`,{ 
            headers: {
                apikey: policyMakerKey,
            }
        })
            .then((response) => {
                let data = response.data;
                if (typeof data === 'string') {
                    try { data = JSON.parse(data); } catch(e) {}
                }
                return data;
            })
            .catch((error) => ({ error: JSON.stringify(error) }));
        //console.log("get all categorys are call. : "+JSON.stringify(reponse));
        return reponse;
    }

    async getTotalRecords(params = {}) {
    try {
        const queryParams = new URLSearchParams();

        /*
         * Price filter
         */
        if (
            params.price_gt !== undefined &&
            params.price_gt !== '' &&
            params.price_gt !== 'undefined'
        ) {
            queryParams.append(
                'price_gt',
                params.price_gt
            );
        }

        if (
            params.price_lt !== undefined &&
            params.price_lt !== '' &&
            params.price_lt !== 'undefined'
        ) {
            queryParams.append(
                'price_lt',
                params.price_lt
            );
        }

        /*
         * Search/category filters
         */
        if (
            params.key !== undefined &&
            params.key !== '' &&
            params.key !== 'undefined'
        ) {
            queryParams.append(
                '_key',
                params.key
            );
        }

        if (
            params.value !== undefined &&
            params.value !== '' &&
            params.value !== 'undefined'
        ) {
            queryParams.append(
                '_value',
                params.value
            );
        }

        if (
            params.title_contains !== undefined &&
            params.title_contains !== '' &&
            params.title_contains !== 'undefined'
        ) {
            queryParams.append(
                'title_contains',
                params.title_contains
            );
        }

        const url =
            `${marketplaceUrl}/products/count?` +
            queryParams.toString();

        console.log(
            '### PRODUCT COUNT URL ###',
            url
        );

        const response =
            await Repository.get(url);

        let data = response.data;

        if (typeof data === 'string') {
            try {
                data = JSON.parse(data);
            } catch (e) {
                data = Number(data);
            }
        }

        console.log(
            '### PRODUCT COUNT RESPONSE ###',
            data
        );

        if (typeof data === 'number') {
            return data;
        }

        if (
            data &&
            typeof data === 'object'
        ) {
            return (
                data.total ??
                data.count ??
                0
            );
        }

        return Number(data) || 0;

    } catch (error) {

        console.error(
            '### PRODUCT COUNT ERROR ###',
            error
        );

        return 0;
    }
}
    
    //getProduct
    async getProductsById(payload) {
    const response = await Repository.get(
        `${marketplaceUrl}/products/${payload}`
    )
        .then((response) => {
            let data = response.data;

            if (typeof data === 'string') {
                try {
                    data = JSON.parse(data);
                } catch (e) {}
            }

            return data;
        })
        .catch((error) => ({
            error: JSON.stringify(error),
        }));

    return response;
}


    async getProductsByCategory(payload) {
        const reponse = await Repository.get(
            `${marketplaceUrl}/product-categories?slug=${payload?.slug}&_limit=${payload?._limit}&_start=${payload?._start}&price_lt=${payload?.price_lt}&price_gt=${payload?.price_gt}`
        )
            .then((response) => {
                let data = response.data;
                if (typeof data === 'string') {
                    try { data = JSON.parse(data); } catch(e) {}
                }
                if (Array.isArray(data) && data.length > 0) {
                    return data;
                } else {
                    return null;
                }
            })
            .catch(() => {
                return null;
            });
        return reponse;
    }

    
    async getProductsByBrand(payload) {
       // alert("## "+payload)
        const reponse = await Repository.get(
            `${marketplaceUrl}/brands?slug=${payload}`
        )
            .then((response) => {
                let data = response.data;
                if (typeof data === 'string') {
                    try { data = JSON.parse(data); } catch(e) {}
                }
                if (Array.isArray(data) && data.length > 0) {
                    return data;
                } else {
                    return null;
                }
            })
            .catch(() => {
                return null;
            });
       // console.log("@@@@ get product by brands : "+reponse);    
        return reponse;
    }

    async getProductsByIds(productIds) {
    try {
        if (!productIds) {
            return [];
        }

        let ids = [];

        /*
         * Support both:
         *
         * Array:
         * ["id1", "id2", "id3"]
         *
         * and existing string:
         * "id_in=id1&id_in=id2"
         */

        if (Array.isArray(productIds)) {
            ids = productIds;
        } else if (typeof productIds === 'string') {

            /*
             * If caller already gives:
             *
             * id_in=abc&id_in=xyz
             *
             * use it directly.
             */
            if (productIds.includes('id_in=')) {
                const queryString =
                    productIds.replace(/^&+|&+$/g, '');

                const response = await Repository.get(
                    `${marketplaceUrl}/productsByIds?${queryString}`
                );

                let data = response.data;

                if (typeof data === 'string') {
                    try {
                        data = JSON.parse(data);
                    } catch (e) {
                        return [];
                    }
                }

                return Array.isArray(data)
                    ? data
                    : [];
            }

            /*
             * Otherwise treat the string as comma-separated IDs.
             */
            ids = productIds
                .split(',')
                .map((id) => id.trim())
                .filter(Boolean);
        }

        ids = ids
            .map((id) => String(id).trim())
            .filter(Boolean);

        if (ids.length === 0) {
            return [];
        }

        const queryParams = new URLSearchParams();

        ids.forEach((id) => {
            queryParams.append('id_in', id);
        });

        const url =
            `${marketplaceUrl}/productsByIds?${queryParams.toString()}`;

        console.log(
            '### GET PRODUCTS BY IDS ###',
            url
        );

        const response = await Repository.get(url);

        let data = response.data;

        if (typeof data === 'string') {
            try {
                data = JSON.parse(data);
            } catch (e) {
                console.error(
                    '### PRODUCTS BY IDS JSON ERROR ###',
                    e
                );

                return [];
            }
        }

        console.log(
            '### PRODUCTS BY IDS RESPONSE ###',
            data
        );

        if (Array.isArray(data)) {
            return data;
        }

        return [];

    } catch (error) {
        console.error(
            '### PRODUCTS BY IDS ERROR ###',
            error
        );

        return [];
    }
}

   
}

export default new ProductRepository();
