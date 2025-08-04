import toast from "react-hot-toast";
import api from "../../api"
import useIsAuthenticated from "../../MyContext/IsAuthenticated";
import { useState, useEffect } from "react";
import { Logout } from "../../Auth";
import {useNavigate } from "react-router-dom";

export default function ProfileCard() {

    const { role , setLoggedIn } = useIsAuthenticated();
    const [toggle, setToggle] = useState(false)
    const navigate = useNavigate()

    const [userData, setUserData] = useState({
        id: "",
        email: "",
        name: ""
    })


    const fetchUserData = async () => {
        try {

            const result = await api.get(`/${role}/getUser`);
            setUserData(result.data);
            setToggle(true);
        }
         catch (error) {
            toast.error("Something went wrong fetching user data.");
        }
        
    };

    const handleOnClick = () => {
        setToggle(!toggle)
    }

    const handleLogout = () => {
        Logout()
        setLoggedIn(false)
        setToggle(!toggle)
    }

    const handleClick =() =>{
        navigate("/seeReferrals")
        setToggle(!toggle)
    }

    const handlePostClick = () =>{
        navigate("/getPostedJobs")
        setToggle(!toggle)
    }


    return (
        <div className="relative">
            <div onClick={fetchUserData}
                className="flex items-center  rounded-full bg-amber-600 shadow-lg">
                <img src="public/user-139.png" className="h-13 hover:cursor-pointer duration-200 hover:scale-105" />
            </div>

            {toggle && <>
                <div className=" fixed inset-0 z-10" onClick={handleOnClick} ></div>
                <div className="absolute top-full right-0 mt-4 w-56 shadow-2xl p-4 flex flex-col gap-2 bg-gray-50 rounded-2xl z-20">
                    <p className="font-semibold">Id : {userData.id}</p>
                    <p className="font-semibold">Name : {userData.name}</p>
                    <p className="font-semibold">Email : {userData.email}</p>
                    <p className="font-semibold">Role : {userData.role}</p>
                    {
                        role === 'user' ? <button onClick={handleClick}
                        className="h-fit hover:underline text-indigo-500 hover:cursor-pointer">See All Referrals</button> :<>
                         <button 
                        onClick={handleClick}
                        className="h-fit hover:underline text-indigo-500 hover:cursor-pointer">See Referred Candidates</button>
                         <button 
                         onClick={handlePostClick}
                        className="h-fit hover:underline text-indigo-500 hover:cursor-pointer">See Your Post</button>
                        </>
                    }
                    <div onClick={handleLogout}
                        className="flex justify-center text-red-500 hover:underline ml-1 h-fit hover:cursor-pointer">Logout</div>
                </div>
            </>

            }
        </div>
    )
}