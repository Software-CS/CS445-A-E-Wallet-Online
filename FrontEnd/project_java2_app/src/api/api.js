import axios from "axios"
import Cookies from "js-cookie";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {loginStart, loginSuccess, loginFailed, registerStart, registerSuccess, registerFailed}
from '../redux/action/auth_action';
import {useSelector} from "react-redux";
import {getPaymentFailed, getPaymentStart, getPaymentSuccess}
from "../redux/action/bank_account_action";

export const api = axios.create({
    baseURL: `http://localhost:8080`
    // baseURL: `https://project-java2.onrender.com`
    //process.env.REACT_APP_URL_SERVER_HOSTNAME
});

export const notify_error = (text, time)=>{
    toast.error(text, {

        position: "top-center",
        autoClose: time,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",

    })
}

export const notify_success = (text, time)=>{
    toast.success(text, {

        position: "top-center",
        autoClose: time,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",

    })
}

export async function loginUser(data, dispatch, navigate){

    dispatch(loginStart());
    try{
        const res = await api.post("/api/v1/login", data);
        if(res.status >= 200 && res.status < 300){
            dispatch(loginSuccess(res.data));
            navigate("/");
            await new Promise(resolve => setTimeout(resolve, 1000));
            window.location.reload();
        }
    }catch (error) {
        console.log(error.response?.data?.error);
        notify_error(error.response?.data?.error, 2000);
        dispatch(loginFailed({
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
        }));
    }

}

export const createAccount = async (data, dispatch) => {
    dispatch(registerStart());
    try {
        const res = await api.post("/api/v1/create/user", data);
        if(res.status >= 200 && res.status < 300){
            dispatch(registerSuccess(res.data));
        }
    }catch (error) {
        dispatch(registerFailed({
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
        }));
        notify_error(error?.response?.data?.message, 2000);
    }
}

export const verifyAccount = async (data, navigate, dispatch) => {
    dispatch(registerStart());
    try {
        const res = await api.post("/api/v1/verify/user", data,{
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        });
        if(res.status >= 200 && res.status < 300){
            dispatch(registerSuccess(null));
            navigate(`/login?message=${res?.data?.message}`);
            notify_success(res?.data?.message, 2000);
        }
    }catch (error) {
        dispatch(registerFailed({
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
        }));
        notify_error(error.message, 2000);
    }
}

export async function logoutUser(token) {
    try{
        const res = await api.get(`/api/v1/logout`);
        console.log(res.data)
        return res.data;
    }catch (error) {
        throw error;
    }
}


export const getHeader = (token) =>{


    return {
        Authorization: `Bearer ${token}`,
        "Content-Type" : "application/json",
    }
}
export async function getApi(url, token) {
    try{
        const res = await api.get(url,
            {headers: getHeader(token)});

        return res.data;
    }catch (error) {
        throw error;
    }
}

export async function paymentPost(url, token, data, dispatch) {
    dispatch(getPaymentStart());
    try{
        const res = await api.post(url,data, {
            withCredentials: true,
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type" : "multipart/form-data",
            }
        });
        console.log(res.data);
        dispatch(getPaymentSuccess(res.data));
        return res.data;
    }catch (error) {
        notify_error(error.response?.data?.error, 2000);
        dispatch(getPaymentFailed({
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
        }));
        throw error;
    }
}