const initalState = {
    user: [],
    roles : [],
    users: [],
    cities: [],
    bookings: [],
    isLoading: false,
    isError: false,
};

const reducerArray = (state = initalState, action) => {
    switch (action.type) {

        case "LOAD_USER_PROFILE":
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case "LOAD_USER_PROFILE_SUCCESS":
            return {
                ...state,
                user: action.user,
                isLoading: false,
            };
        default:
            return state;
    }
};

export default reducerArray;