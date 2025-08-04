import React, { useState } from 'react';
import toast from 'react-hot-toast';
import api from '../../api';
import useIsAuthenticated from '../../MyContext/IsAuthenticated';

const ReferralForm = ({ onClose, jobId }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    resume: null,
  });

  const { role } = useIsAuthenticated()

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === 'resume') {

      const selectedFile = files[0];

      if (selectedFile && selectedFile.type != "application/pdf") {
        toast.error("Please upload a PDF.")
      }

      setFormData({ ...formData, resume: files[0] });
    }
    else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();

    data.append('candidateName', formData.name);
    data.append('candidateEmail', formData.email);
    data.append('jobId', jobId);

    //Append the file if it exists
    if (formData.resume) {
      data.append('resume', formData.resume);
    }

    try {

      await api.post(`/${role}/referral`, data,{
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      toast.success("successfully REFERRED")


    } catch (error) {

      toast.error((error.response.data.message) || "Enter valid Name or Email")
    }
    onClose()
  };

  return (
    <div className='flex items-center justify-center fixed inset-0 bg-[rgba(189,189,189,0.9)]' onClick={onClose}>
      <div className="md:w-1/3 mx-auto p-5 bg-white rounded-lg shadow-md relative" onClick={(e) => e.stopPropagation()}>
        <button onClick={onClose}
          className='absolute top-4 right-4 text-gray-500 hover:text-gray-800'><i className="ri-close-line"></i></button>
        <h2 className="text-xl font-semibold mb-4">Referral Form</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700" htmlFor="name">Name</label>
            <input
              type="text"
              id="name"
              name='name'
              value={formData.name}
              onChange={handleChange}
              required
              placeholder='Enter Candidate Name'
              className="mt-1 block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700" htmlFor="email">Email</label>
            <input
              placeholder='Enter Candidate Email'
              type="email"
              id="email"
              name='email'
              value={formData.email}
              onChange={handleChange}
              required
              className="mt-1 block w-full border p-2 pl-3 border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700" htmlFor="resume">Upload Resume</label>
            <input
              type="file"
              id="resume"
              name='resume'
              onChange={handleChange}
              accept="application/pdf"
              className="mt-1 block w-full border p-1 pl-3  border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <button
            type="submit"
            className="w-full py-2 px-4 bg-blue-600 mt-5 hover:scale-105 duration-200 text-white font-semibold rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default ReferralForm;
