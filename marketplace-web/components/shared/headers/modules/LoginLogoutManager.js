// author : Rohit 

import { Component } from "react";
import { login, userIsLogin } from "~/store/auth/action";
import { connect } from 'react-redux';

class Test extends Component {


    constructor(props) {
       // alert(props);
        super(props);
        this.state = {
        };
        if(userIsLogin())
            this.props.dispatch(login())

    }
    render(){
        return(
            <>  
            </>
        )
    } 

}
const mapStateToProps = state => {
    return state.auth;
};
export default connect(mapStateToProps)(Test); 