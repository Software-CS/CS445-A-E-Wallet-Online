import React, {useEffect} from 'react';
import PropTypes from 'prop-types';
import {fetchUsers} from "./fetch";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

const User = props => {

    const user = useSelector((state) => state.auth.login?.currentUser);
    const navigate = useNavigate();

    useEffect(() => {
        if(user?.accessToken){
            fetchUsers(user?.accessToken, navigate).then(
                res=>console.log(res)
            )
        }
    }, []);

    return (
        <div>
            <h2>Users</h2>
        </div>
    );
};

User.propTypes = {
    
};

export default User;