import {createSlice} from "@reduxjs/toolkit"

const auth_action = createSlice({
        name: "auth",
        initialState : {
            login : {
                currentUser: null,
                message : null,
                isFetching: false,
                error: false
            },
            register : {
                registerUser: null,
                message : null,
                isFetching: false,
                error: false
            },

       },

        reducers: {

            registerStart : (state)=>{
                state.register.isFetching = true;
            },
            registerSuccess : (state, action) =>{

                state.register.isFetching = false;
                state.register.registerUser = action.payload;
                state.register.error = false;
            },
            registerFailed : (state, action)=>{
                state.register.isFetching = false;
                state.register.message = action.payload;
                state.register.error = true;
            },


            loginStart : (state)=>{
                state.login.isFetching = true;
            },
            loginSuccess : (state, action) =>{

                state.login.isFetching = false;
                state.login.message = "Login successfully";
                state.login.currentUser = action.payload;
                state.login.error = false;
            },
            loginFailed : (state, action)=>{
                state.login.isFetching = false;
                state.login.message = action.payload;
                state.login.error = true;
            },
            logout: state => {
                state.login.currentUser = null;
            }
        }
    }
);

export const {
    registerStart,
    registerSuccess,
    registerFailed  ,

    loginStart,
    loginSuccess,
    loginFailed,
    logout
} = auth_action.actions;

export default auth_action.reducer;





