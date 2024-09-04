import {getApi} from "../../../api/api";

export async function fetchUsers(token, navigate) {
    try{
        if(token){
            return await getApi("/api/admin/users", token);
        }
    }catch (error) {
        navigate('/');
        console.error("ERROR: ", error);
    }
}