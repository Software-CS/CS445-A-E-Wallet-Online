import React, {lazy, useEffect, useState} from 'react';
import {Link, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {createAccount, loginUser, notify, verifyAccount} from "../../api/api";
import Form from "./Form";
import {registerFailed, registerStart, registerSuccess} from "../../redux/action/auth_action";
import loading from "../../assets/img/loading_dark.gif";

const Register = () => {

    const [register, setRegister] = useState({
        email: "",
        password: ""
    });
    const [token, setToken] = useState('');
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const registerUser = useSelector((state) => state.auth.register.registerUser);
    const registerErrorMessage = useSelector((state) => state.auth.register?.message);
    const [show, setShow] = useState(false);

    const handleInputChange = (e) =>{

        setRegister({...register, [e.target.name] : e.target.value});
    }
    const handleRegister = async (e)=>{

        e.preventDefault();
        if(registerErrorMessage){
            setShow(true);
            await new Promise(resolve => setTimeout(resolve, 2500));
        }
        setShow(true);
        await createAccount(register, dispatch);
        setShow(false);
    }
    
    const clearRegisterUser = () => {
        dispatch(registerStart());
        dispatch(registerSuccess(null));
        dispatch(registerFailed(null));
    }

    return (
        <div>
            <div className="py-3 py-md-5">
                <div className="container">
                    <div className="row justify-content-md-center">
                        <div className="col-12 col-md-11 col-lg-8 col-xl-7 col-xxl-6">
                            <div className="bg-white p-4 p-md-5 rounded shadow-sm">
                                <div className="row">
                                    <div className="col-12">
                                        <div className="text-center mb-5">
                                            {!show &&
                                                <a href="#!">
                                                    <h1>SIGN UP</h1>
                                                </a>}
                                            {show && <div>
                                                <a href="#!" className={"mb-4"}>
                                                    <h1>SIGN UP...</h1>
                                                </a>
                                                <img src={loading ? loading : ''}
                                                     alt={loading ? loading : ''}
                                                     width="20%"
                                                />
                                            </div>}
                                        </div>
                                    </div>
                                </div>
                                {/*Form handle*/}
                                <Form handleInputChange={handleInputChange}
                                      handleRegister={handleRegister}
                                      register={register}
                                      setShow={setShow}
                                      setToken={setToken}
                                      token={token}
                                />
                                <div className="row">
                                    <div className="col-12">
                                        <hr className="mt-5 mb-4 border-secondary-subtle"/>
                                        <div
                                            className="d-flex gap-2 gap-md-4 flex-column flex-md-row justify-content-md-center">
                                            <a href="#!" >
                                                Create new account
                                            </a>
                                            <Link onClick={clearRegisterUser} className="link-secondary text-decoration-none">
                                                CLEAR SIGN UP DATA
                                            </Link>
                                            <a href="#!" className="link-secondary text-decoration-none">
                                                Forgot password
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Register;