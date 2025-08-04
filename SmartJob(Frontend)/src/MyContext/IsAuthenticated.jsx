import { createContext, useContext, useEffect, useState ,useMemo } from "react";

export const IsAuthenticated = createContext()

export function IsAuthenticatedProvide({children}){
    
    const[isLoggedIn , setLoggedIn] = useState(!!localStorage.getItem("accessToken"));
    const [loading, setLoading] = useState(true);

    let localRole;
    
    if(!!localStorage.getItem("role")){
        localRole = localStorage.getItem("role").toLowerCase()
    }
    const [role, setRole] = useState(localRole || "user");
   
    

    const [authModalState, setAuthModalState] = useState('closed'); // 'closed', 'login', 'signup'

    useEffect(()=>{
        const token = localStorage.getItem("accessToken")
        setLoggedIn(!!token);
        setLoading(false);
    },[])

    useEffect(()=>{
        const handleStorageChange = ()=>{
            setLoggedIn(!!localStorage.getItem("accessToken"))
        };
        window.addEventListener("storage" , handleStorageChange)
        
        return ()=> window.removeEventListener("storage" , handleStorageChange)
    },[isLoggedIn])

    const contextValue = useMemo(() => ({
        isLoggedIn,
        setLoggedIn,
        loading,
        role,
        setRole,
        authModalState,
        setAuthModalState
    }), [isLoggedIn, loading, role, authModalState]);

    return(
        <IsAuthenticated.Provider value={contextValue} >
            {children}
        </IsAuthenticated.Provider>
    )
}



export default function useIsAuthenticated(){
    return useContext(IsAuthenticated)
}