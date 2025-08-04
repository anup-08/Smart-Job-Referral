import { useEffect, useState } from "react"
import useIsAuthenticated from "../../MyContext/IsAuthenticated"
import api from "../../api"
import toast from "react-hot-toast"
import JobCard from "../Jobcard/JobCard"

export default function PostedJob() {
    const [jobs, setJobs] = useState([])
    const { role } = useIsAuthenticated()

    const [showWarning, setShowWarning] = useState(false);

    const [jobIdToDelete, setJobIdToDelete] = useState(null);

    useEffect(() => {
        const fetchJobs = async () => {
            try {
                const result = await api.get(`/${role}/getPostedJobs`)
                setJobs(result.data)

            } catch (error) {
                toast.error("Something went wrong")
            }
        }
        fetchJobs()
    }, [])


    useEffect(() => {
        // When the modal is shown, we want to prevent scrolling
        if (showWarning) {
            document.body.style.overflow = 'hidden';
        }else {
            document.body.style.overflow = 'auto';
        }
        // Cleanup function to restore scrolling when the component unmounts
        return () => {
            document.body.style.overflow = 'auto';
        };
    }, [showWarning]);

    const handleOnClick = (id) => {
        setJobIdToDelete(id);
        setShowWarning(true)
    }

    const handleCancle = () => {
        setShowWarning(false)
    }

    const handleDelete = async () => {
        if (!jobIdToDelete) return;
        try {
            await api.delete(`${role}/delete/${jobIdToDelete}`)

            setJobs(jobs.filter(job => job.id !== jobIdToDelete));
            toast.success("Job deleted successfully!");

        } catch (error) {
            toast.error("Failed to delete job.");
        }
        finally {
            // Hide the modal and reset the ID
            setShowWarning(false);
            setJobIdToDelete(null);
        }
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 p-4  rounded-lg">
            {jobs.length > 0 ? (
                jobs.map((item) => (
                    // Spread item properties as props
                    (
                        <div key={item.id} className="relative">
                            <button onClick={()=>handleOnClick(item.id)}
                                // Add your onClick handler for deletion here
                                // onClick={() => handleDelete(item.id)}
                                className="absolute z-10 -top-1 -right-1 p-1.5 text-base sm:-top-2 sm:-right-2 sm:p-2 sm:text-lg bg-red-500 text-white rounded-full cursor-pointer hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400 transition-transform duration-200 hover:scale-110"
                                aria-label="Delete job"
                            >
                                <i className="ri-delete-bin-6-line text-lg leading-none"></i>
                            </button>
                            <JobCard {...item} />
                        </div>
                    )
                ))
            ) : (
                // Correctly render "No Jobs" message
                <div className="flex justify-center items-center h-full">
                    <p className="text-xl text-slate-600 font-extrabold">No Jobs Available Right Now</p>
                </div>
            )}
            {
                showWarning && (
                    // 1. Full-screen overlay with a semi-transparent background
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-xs md:p-0 p-4">
                        <div
                            onClick={(e) => e.stopPropagation()}
                            // 2. Centered content with more padding and consistent spacing
                            className="flex flex-col items-center w-full max-w-sm gap-6 p-8 bg-white rounded-lg shadow-xl"
                        >
                            {/* 3. A large, colored icon to grab attention */}
                            <div className="text-6xl text-red-500">
                                <i className="ri-error-warning-line"></i>
                            </div>

                            {/* 4. Improved typography with a title and description */}
                            <div className="text-center">
                                <h2 className="text-2xl font-bold text-gray-800">Are you sure?</h2>
                                <p className="mt-2 text-gray-500">This action is irreversible and will permanently delete the item.</p>
                            </div>

                            {/* 5. Clearly styled and spaced buttons */}
                            <div className="flex w-full gap-4">
                                <button
                                    onClick={handleCancle}
                                    className="w-full px-4 py-3 font-semibold text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-opacity-75 transition-colors"
                                >
                                    Cancel
                                </button>
                                <button
                                    onClick={handleDelete}
                                    className="w-full px-4 py-3 font-semibold text-white bg-red-600 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-opacity-75 transition-colors"
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    </div>
                )
            }
        </div>
    )
}