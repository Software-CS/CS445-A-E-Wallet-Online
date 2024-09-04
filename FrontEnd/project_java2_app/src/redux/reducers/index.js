import { combineReducers } from "redux";
import reducerArray from "./reducerArray";
import auth_reducer from "../action/auth_action";
import user_reducer from "../action/user_action";
import bank_account_reducer from "../action/bank_account_action";
import payment_reducer from "../action/payment_action";


const rootReducers = combineReducers({
    Array: reducerArray,
    auth : auth_reducer,
    user : user_reducer,
    bank_account : bank_account_reducer,
    payment : payment_reducer
});

export default rootReducers;