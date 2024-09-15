import React, {useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {notify_error, verifyAccount} from "../../api/api";
import {useNavigate} from "react-router-dom";

const Form = (props) => {

    const registerUser = useSelector((state) => state.auth.register.registerUser);
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const toUrlSearchParams = (data) => {
        const params = new URLSearchParams();
        for (const key in data) {
            if (data.hasOwnProperty(key)) {
                params.append(key, data[key]);
            }
        }
        return params;
    }
    const handleVerify = async (e) => {
        e.preventDefault();
        const form = e.target;
        const verify_token = form.elements?.token.value;
        if(!verify_token){
            notify_error("Enter token, please!",2000);
            return;
        }
        try{
            props?.setShow(true);
            await new Promise(resolve => setTimeout(resolve, 2000));
            await verifyAccount(toUrlSearchParams(
                {
                    email : registerUser?.new_user?.email,
                    password : registerUser?.new_user?.password,
                    verify_token : verify_token
                }
            ), navigate, dispatch);
        }catch (ex) {
            console.log(ex);
            notify_error("Verify account failed!",2000);
        }
        props?.setShow(false);
    }


    return (
        <div>
            {registerUser ?
                <form onSubmit={handleVerify}>
                    <div className="row gy-3 gy-md-4 overflow-hidden">
                        <div className="col-12">
                            <label htmlFor="email" className="form-label">
                                Check your mail and enter token <span className="text-danger">*</span>
                            </label>
                            <div className="input-group">
                            <span className="input-group-text">
                              <svg
                                  xmlns="http://www.w3.org/2000/svg"
                                  width={16}
                                  height={16}
                                  fill="currentColor"
                                  className="bi bi-envelope"
                                  viewBox="0 0 16 16"
                              >
                                <path
                                    d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z"/>
                              </svg>
                            </span>
                                <input
                                    type="text"
                                    className="form-control"
                                    id="token"
                                    name="token"
                                    placeholder="Token..."
                                    value={props.token}
                                    onChange={event=>
                                    props.setToken(event.target.value)}
                                />
                            </div>
                        </div>

                        <div className="col-12">
                            <div className="d-grid">
                                <button className="btn btn-primary btn-lg" type="submit">
                                    Verify
                                </button>
                            </div>
                        </div>
                    </div>
                </form> :
                <form onSubmit={props?.handleRegister}>
                    <div className="row gy-3 gy-md-4 overflow-hidden">
                        <div className="col-12">
                            <label htmlFor="email" className="form-label">
                                Email <span className="text-danger">*</span>
                            </label>
                            <div className="input-group">
                            <span className="input-group-text">
                              <svg
                                  xmlns="http://www.w3.org/2000/svg"
                                  width={16}
                                  height={16}
                                  fill="currentColor"
                                  className="bi bi-envelope"
                                  viewBox="0 0 16 16"
                              >
                                <path
                                    d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z"/>
                              </svg>
                            </span>
                                <input
                                    type="email"
                                    className="form-control"
                                    id="email"
                                    name="email"
                                    placeholder="Email..."
                                    value={props.register.email}
                                    onChange={props.handleInputChange}
                                />
                            </div>
                        </div>
                        <div className="col-12">
                            <label htmlFor="password" className="form-label">
                                Password <span className="text-danger">*</span>
                            </label>
                            <div className="input-group">
                            <span className="input-group-text">
                              <svg
                                  xmlns="http://www.w3.org/2000/svg"
                                  width={16}
                                  height={16}
                                  fill="currentColor"
                                  className="bi bi-key"
                                  viewBox="0 0 16 16"
                              >
                                <path
                                    d="M0 8a4 4 0 0 1 7.465-2H14a.5.5 0 0 1 .354.146l1.5 1.5a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0L13 9.207l-.646.647a.5.5 0 0 1-.708 0L11 9.207l-.646.647a.5.5 0 0 1-.708 0L9 9.207l-.646.647A.5.5 0 0 1 8 10h-.535A4 4 0 0 1 0 8zm4-3a3 3 0 1 0 2.712 4.285A.5.5 0 0 1 7.163 9h.63l.853-.854a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.793-.793-1-1h-6.63a.5.5 0 0 1-.451-.285A3 3 0 0 0 4 5z"/>
                                <path d="M4 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"/>
                              </svg>
                            </span>
                                <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    className="form-control"
                                    value={props.register.password}
                                    onChange={props.handleInputChange}
                                    placeholder="Password"
                                />
                            </div>
                        </div>
                        <div className="col-12">
                            <div className="d-grid">
                                <button className="btn btn-primary btn-lg" type="submit">
                                    Sign up
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            }
        </div>
    );
};

export default Form;