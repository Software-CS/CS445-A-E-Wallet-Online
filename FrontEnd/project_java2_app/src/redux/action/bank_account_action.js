import {createSlice} from "@reduxjs/toolkit"

const bank_account_action = createSlice({
        name: "bank_account",
        initialState : {
            bank_acc: {
                data: null,
                isFetching: false,
                error: false
            },
            generate_payment_token : {
                data: null,
                isFetching: false,
                error: false
            },
            payment_token : {
                data: null,
                isFetching: false,
                error: false
            },
            payment : {
                data: null,
                isFetching: false,
                error: false
            }
        },

        reducers: {

            getBankAccountUserStart: (state) => {
                state.bank_acc.isFetching = true;
            },
            getBankAccountUserSuccess: (state, action) => {
                state.bank_acc.isFetching = false;
                state.bank_acc.data = action.payload;
                state.bank_acc.error = false;
            },
            getBankAccountUserFailed: (state) => {
                state.bank_acc.isFetching = false;
                state.bank_acc.error = true;
            },



            getTokenGenStart: (state) => {
                state.generate_payment_token.isFetching = true;
            },
            getTokenGenSuccess: (state, action) => {
                state.generate_payment_token.isFetching = false;
                state.generate_payment_token.data = action.payload;
                state.generate_payment_token.error = false;
            },
            getTokenGenFailed: (state, action) => {
                state.generate_payment_token.isFetching = false;
                state.generate_payment_token.data = action.payload;
                state.generate_payment_token.error = true;
            },


            getPaymentTokenStart: (state) => {
                state.payment_token.isFetching = true;
            },
            getPaymentTokenSuccess: (state, action) => {
                state.payment_token.isFetching = false;
                state.payment_token.data = action.payload;
                state.payment_token.error = false;
            },
            getPaymentTokenFailed: (state, action) => {
                state.payment_token.isFetching = false;
                state.payment_token.data = action.payload;
                state.payment_token.error = true;
            },


            getPaymentStart: (state) => {
                state.payment.isFetching = true;
            },
            getPaymentSuccess: (state, action) => {
                state.payment.isFetching = false;
                state.payment.data = action.payload;
                state.payment.error = false;
            },
            getPaymentFailed: (state, action) => {
                state.payment.isFetching = false;
                state.payment.data = action.payload;
                state.payment.error = true;
            },

        }
    }
);

export const {

    getBankAccountUserStart,
    getBankAccountUserSuccess,
    getBankAccountUserFailed,

    getTokenGenStart,
    getTokenGenSuccess,
    getTokenGenFailed,

    getPaymentTokenStart,
    getPaymentTokenSuccess,
    getPaymentTokenFailed,

    getPaymentStart,
    getPaymentSuccess,
    getPaymentFailed

} = bank_account_action.actions;

export default bank_account_action.reducer;