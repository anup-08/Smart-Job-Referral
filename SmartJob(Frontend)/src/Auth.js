import toast from "react-hot-toast";
import api from "./api"
import { jwtDecode } from "jwt-decode";


export const Login = async(props) =>{
   
     
    try {
        
        const result = await api.post(`/${props.role}/login`,{userName:props.userName , password:props.password})
        toast.success("Logged In")

        const accessToken = result.data.accessToken
        const refreshToken = result.data.refreshToken

        const decodedToken = jwtDecode(accessToken)

        localStorage.setItem("accessToken" , accessToken)
        localStorage.setItem("refreshToken" , refreshToken)
        localStorage.setItem("role" , decodedToken.role)
        

        return decodedToken.role;

    } catch (error) {
        toast.error("Invalid UserName or Password")
        return null
    }
    
}

export const SignUp = async(props) => {
    try {
        
        const result = await api.post(`/${props.role}/register`,{name:props.name , 
            email:props.email , 
            password: props.password})

        const accessToken = result.data.accessToken
        const refreshToken = result.data.refreshToken

        const decodedToken = jwtDecode(accessToken)

        localStorage.setItem("accessToken" , accessToken)
        localStorage.setItem("refreshToken" , refreshToken)
        localStorage.setItem("role" , decodedToken.role)

        toast.success("Successfully Registerd")
        

    } catch (error) {
        const message = error?.response?.data?.message

        //Multiple Error ------->
        if(message && typeof message == "object" ){
            Object.values(message).forEach(msg => toast.error(msg));
        }
        else{
            //Single Error ------>
            toast.error((error?.response?.data?.message) || "Registration Failed")
        }
        return null
    }
}

export const Logout = () =>{
    localStorage.removeItem("accessToken")
    localStorage.removeItem("refreshToken")
    localStorage.removeItem("role")
}