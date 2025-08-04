import { jwtDecode } from "jwt-decode";
import axios from "axios";
import config from "./Config/Config";
import { toast } from "react-toastify";

const api = axios.create(
    {
        baseURL: config.baseurl
    }
)

api.interceptors.request.use((request) => {
    const accessToken = localStorage.getItem("accessToken")
    if (accessToken) {
        request.headers.Authorization = `Bearer ${accessToken}`;
    }
    return request;
})

api.interceptors.response.use((response) => response,
    async (error) => {
        
        const originalRequest = error.config;
        const currentPath = window.location.pathname;

        const isOnLoginPage = ["/user/login", "/user/register"].includes(currentPath);

        
        if (error.response?.status === 401 && !originalRequest._retry && !isOnLoginPage) {
            originalRequest._retry = true;
            
            try {
                
                const decodeToken = jwtDecode(localStorage.getItem("accessToken"))
                
                const role = decodeToken.role;
                
                
                const result = await api.post(`/${role[0].toLowerCase()}/getToken`,
                    { refreshToken: localStorage.getItem("refreshToken") })
                
                const newToken = result.data.accessToken;
                if (!newToken) {
                    toast.error("No new access token received");
                }
                
                localStorage.setItem("accessToken", newToken)

                if (!originalRequest.headers) {
                    originalRequest.headers = {};

                }

                originalRequest.headers.Authorization = `Bearer ${newToken}`;

                return api(originalRequest)

            } catch (refreshError) {
                toast.error("Please Re-Login")
                // Logout()
                return Promise.reject(refreshError)
            }
        }
        return Promise.reject(error)

    }
)

 export default api;