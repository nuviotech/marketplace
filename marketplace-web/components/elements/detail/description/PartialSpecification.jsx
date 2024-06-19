import React from 'react';

const PartialSpecification = ({ product }) => {
    function isValidJSON(str) {
        try {
            JSON.parse(str);
            return true;
        } catch (e) {
            return false;
        }
    }

    return (
        <div className="table-responsive">
            <table className="table table-bordered ps-table ps-table--specification">
                <tbody>

                    {

                        isValidJSON(product?.specifications) ?
                            JSON?.parse(product?.specifications)?.map(spec => {
                                return <tr>
                                    <td>{spec?.key}</td>
                                    <td>{spec?.value}</td>
                                </tr>
                            })
                            :
                            <>no data available !!</>



                    }


                </tbody>
            </table>
        </div>
    );
};

export default PartialSpecification;
