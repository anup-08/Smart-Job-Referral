import { useCallback, useEffect, useState } from "react"
import api from "../../api"
import useIsAuthenticated from "../../MyContext/IsAuthenticated"
import ReferralCard from "./ReferralCard"
import { useNavigate } from "react-router-dom"
import toast from "react-hot-toast"

export default function ReferralHome() {

    const [referral, setReferral] = useState([])

    const { role, isLoggedIn } = useIsAuthenticated()
    const navigate = useNavigate()


    const fetchData = useCallback(async () => {
        try {
            if (role === 'user') {
                const data = await api.get(`/${role}/getReferralDetails`)
                setReferral(data.data)
            }
            else if (role === 'admin') {
                const data = await api.get(`/${role}/getPostedJobReferral`)
                setReferral(data.data)
            }

        } catch (error) {
            navigate("/")
            toast.error(error.response.data.message)
        }

    }, [role, navigate])


    useEffect(() => {
        if (isLoggedIn) {
            fetchData();
        } else {
            navigate("/");
        }
    }, [isLoggedIn, fetchData, navigate]);

    return (
        <div className="md:flex items-start gap-2">
            {
                referral.length > 0 ? (
                    referral.map((iteam) => (
                        <ReferralCard {...iteam} key={iteam.referralId} />
                    ))
                ) : 
                    (
                        // Correctly render "No Jobs" message
                        <div className="flex justify-center items-center h-full">
                            <p className="text-xl text-slate-600 font-extrabold">No Referrals</p>
                        </div>
                    )
            }
        </div>
    )
}