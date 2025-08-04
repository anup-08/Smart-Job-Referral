import { useState } from "react";
import api from "../../api";
import useIsAuthenticated from "../../MyContext/IsAuthenticated";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

export default function AddJobs() {

    const { role } = useIsAuthenticated()
    const navigate = useNavigate()

    const [formData, setFormData] = useState({
        title: "",
        description: "",
        department: "",
        location: [],
        experienceRequired: "",
        companyName: ""
    })

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post(`${role}/addJob`, {
                title: formData.title,
                description: formData.description,
                department: formData.department,
                location: formData.location,
                experienceRequired: formData.experienceRequired,
                companyName: formData.companyName
            })
            toast.success("Successfully Added")
            setTimeout(() => {
                navigate("/");
            }, 1000); 

        } catch (error) {
            toast.error("Unsuccessfull attempt")
        }
    }

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData({ ...formData, [name]: value })
    }

    const handleLocationChange = (index, e) => {
        const newLocation = [...formData.location];
        newLocation[index] = e.target.value;
        setFormData({ ...formData, location: newLocation })
    }

    const handleRemoveLocation = (index) => {
        const newLocation = formData.location.filter((_, i) => i != index);
        setFormData({ ...formData, location: newLocation })
    }

    const handleAddLocation = () => {
        setFormData({ ...formData, location: [...formData.location, ""] });
    }

    return (
        <div className="w-full mt-5 mx-auto p-5 bg-white rounded-lg shadow-md relative overflow-y-hidden" onClick={(e) => e.stopPropagation()}>

            <div className="flex justify-center"><h2 className="text-xl font-semibold text-indigo-500 mb-4">ADD JOBS</h2></div>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700" htmlFor="companyName">Company Name</label>
                    <input
                        type="text"
                        id="companyName"
                        name='companyName'
                        value={formData.companyName}
                        onChange={handleChange}
                        required
                        placeholder='Enter Company Name'
                        className="mt-1 block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700" htmlFor="title">Title</label>
                    <input
                        placeholder='Enter Title'
                        type="text"
                        id="title"
                        name='title'
                        value={formData.title}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700" htmlFor="department">Department</label>
                    <input
                        placeholder='Enter Department'
                        type="text"
                        id="department"
                        name='department'
                        value={formData.department}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700" htmlFor="description">Description</label>
                    <textarea
                        placeholder='Enter Description'
                        id="description"
                        name='description'
                        rows={2}
                        value={formData.description}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700" htmlFor="experienceRequired">Experience Required</label>
                    <input
                        type="number"
                        id="experienceRequired"
                        name='experienceRequired'
                        value={formData.experienceRequired}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-24 border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
                    />
                </div>

                {/* Dynamic Location Inputs Section */}
                <div>
                    <label className="block text-sm font-medium text-gray-700">Locations</label>
                    {formData.location.map((location, index) => (
                        <div key={index} className="flex items-center gap-2 mt-1">
                            <input
                                type="text"
                                placeholder="Enter Location"
                                value={location}
                                onChange={(e) => handleLocationChange(index, e)}
                                required
                                className="block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
                            />
                            {formData.location.length > 1 && (
                                <button
                                    type="button"
                                    onClick={() => handleRemoveLocation(index)}
                                    className="px-3 py-2 text-sm text-white bg-red-500 rounded-md hover:bg-red-600"
                                >
                                    <i className="ri-delete-bin-line"></i>
                                </button>
                            )}
                        </div>
                    ))}
                    <button
                        type="button"
                        onClick={handleAddLocation}
                        className="mt-2 text-sm text-blue-600 font-semibold hover:text-blue-800"
                    >
                        + Add Another Location
                    </button>
                </div>


                <button
                    type="submit"
                    className="w-full py-2 px-4 bg-blue-600 mt-5 hover:scale-105 duration-200 text-white font-semibold rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                >
                    Add job
                </button>
            </form>
        </div>

    );
}