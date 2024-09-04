import {getApi, notify_error} from "../../api/api";
import {getUserFailed, getUserStart, getUserSuccess} from "./user_action";
import {getBankAccountUserStart, getBankAccountUserSuccess, getBankAccountUserFailed,
getTokenGenStart, getTokenGenSuccess, getTokenGenFailed} from "./bank_account_action";
import {fetchPaymentToken} from "../../components/payment/fetch";


export const getUser = async (token, dispatch) => {
    dispatch(getUserStart());
    try {
        const data = await getApi("/api/user/profile", token);
        await dispatch(getUserSuccess(data));
    } catch (err) {
        console.error("error: ", err);
        dispatch(getUserFailed());
    }
};

export const getBankAccount = async (token, dispatch) => {
    dispatch(getBankAccountUserStart());
    try {
        const data = await getApi("/api/user/bank_account", token);

        await dispatch(getBankAccountUserSuccess(data));
    } catch (err) {
        console.error("error: ", err);
        dispatch(getBankAccountUserFailed());
    }
};


export const getPaymentToken = async (token, dispatch) => {
    dispatch(getTokenGenStart());
    try {
        const data = await getApi("/api/user/generate_payment_token", token);
        await fetchPaymentToken(token, dispatch);
        await dispatch(getTokenGenSuccess(data));
    } catch (error) {
        console.log(error.response);
        notify_error(error.response?.data?.message,2000);
        dispatch(getTokenGenFailed({
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
        }));
    }
};


