import { useCallback, useEffect, useState } from "react";
import api from "../../api";
import useIsAuthenticated from "../../MyContext/IsAuthenticated";
import JobCard from "../Jobcard/JobCard";
import toast from "react-hot-toast";

export default function HomePage() {
    const { role } = useIsAuthenticated();
    const [job, setJob] = useState([]);
    const [loading, setLoading] = useState(true); // Added for loading state

    const [searchJob, setSearchJob] = useState("");

    const fetchAllJob = useCallback(async () => {
        try {
            if (role) {
                const result = await api.get(`/${role}/allJobList`);
                if (Array.isArray(result.data)) {
                    setJob(result.data); 
                }
            }
        } catch (error) {
            toast.error("Something Went Wrong");
            setJob([]); // Clear jobs on error
        } finally {
            setLoading(false); // Stop loading state
        }
    }, []);

    useEffect(() => {
        fetchAllJob()
    }, [])

    const handleSearch = async (e) => {
        e.preventDefault()

        if (!searchJob.trim()) {
            toast.error("Please enter a job title to search.");
            return;
        }

        setLoading(true);

        try {
            const result = await api.get(`${role}/searchJob`, {
                params: {
                    title: searchJob    // As backend expecting a title
                }
            })

            if (result.data) {
                setJob(result.data)
            }

        } catch (error) {

            toast.error(error?.response?.data?.message)
        }
        finally {
            setLoading(false);
        }
    }

    const clearSearch = () => {
        setSearchJob("");
        fetchAllJob();
    };

    if (loading) {
        return <div className="flex justify-center items-center h-screen"><p>Loading...</p></div>;
    }

    return (
        <div className=" p-4 ">
            <form onSubmit={handleSearch} className="flex justify-center items-center gap-2 mb-5 " >

                    <div className=''>
                        <div className='relative'>
                        {/* Icon with absolute positioning */}
                            <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                                <i className="ri-search-eye-line  text-gray-500 transition-transform duration-200 hover:scale-110"></i>
                            </div>

                        {/* Input with enhanced styles */}
                        </div>
                    </div>
                    <input name="searchBar" id="search"
                        className="p-2 pl-10 bg-white border border-gray-300 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200 ease-in-out w-35 md:w-60 font-semibold mr-2"
                        type="search"
                        placeholder="Search Job"
                        value={searchJob}
                        onChange={(e) => setSearchJob(e.target.value)}
                    />
                    <button type="submit" className="bg-blue-500 text-white font-bold py-2 px-4 rounded hover:bg-blue-600" onClick={handleSearch}>
                        Search
                    </button>
                    <button type="button" onClick={clearSearch} className="bg-gray-500 text-white font-bold py-2 px-4 rounded hover:bg-gray-600 ml-2">
                        Clear
                    </button>
                    
                

            </form>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 p-4  rounded-lg">
                {job.length > 0 ? (
                    job.map((item) => (
                        // Spread item properties as props
                        <JobCard {...item} key={item.id} />
                    ))
                ) : (
                    // Correctly render "No Jobs" message
                    <div className="flex justify-center items-center h-full">
                        <p className="text-xl text-slate-600 font-extrabold">No Jobs Available Right Now</p>
                    </div>
                )}
            </div>
        </div>
    );
}