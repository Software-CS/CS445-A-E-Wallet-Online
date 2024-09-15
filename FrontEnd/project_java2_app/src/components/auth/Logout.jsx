import React, {useEffect, useState} from "react";
import {Link, NavLink, useNavigate} from "react-router-dom";
import Cookies from "js-cookie";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {useDispatch, useSelector} from "react-redux";
import {logout} from '../../redux/action/auth_action';
import {clearUser} from "../../redux/action/user_action";

const Logout = () => {

    const navigate = useNavigate();
    const dispatch = useDispatch();
    const user = useSelector((state) => state.auth.login.currentUser);

    const handleLogout = async (e)=>{

        e.preventDefault();
        dispatch(logout());
        dispatch(clearUser());
        navigate("/login");
        window.location.reload();
    }

    return (
        <div>
            <NavLink style={{marginLeft:"5px"}}
                  onClick={handleLogout}
                  className="btn btn-outline-success"
                  type="submit"
            >
                Logout
            </NavLink>
        </div>
    );
};

export default Logout;