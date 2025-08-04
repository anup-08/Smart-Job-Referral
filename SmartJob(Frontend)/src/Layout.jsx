import { Outlet } from "react-router-dom";
import { NavBar } from "./component";
import { Toaster } from "react-hot-toast"


export default function Layout(){
    return (
        <>
            <NavBar />
            <Outlet />
            <Toaster />
            
        </>
        
    )
}