import api from "../../api";
import toast from "react-hot-toast";
import useIsAuthenticated from "../../MyContext/IsAuthenticated";
import { useState , useRef } from "react";

export default function ReferralCard(props) {
  // A map to hold all status-related styles for easier management
  const statusStyles = {
    REFERRED: {
      accent: 'border-l-indigo-500',
      tag: 'bg-indigo-100 text-indigo-800',
      icon: 'text-indigo-600'
    },
    INTERVIEWED: {
      accent: 'border-l-blue-500',
      tag: 'bg-blue-100 text-blue-800',
      icon: 'text-blue-600'
    },
    HIRED: {
      accent: 'border-l-green-500',
      tag: 'bg-green-100 text-green-800',
      icon: 'text-green-600'
    },
    REJECTED: {
      accent: 'border-l-red-500',
      tag: 'bg-red-100 text-red-800',
      icon: 'text-red-600'
    },
    DEFAULT: {
      accent: 'border-l-gray-500',
      tag: 'bg-gray-100 text-gray-800',
      icon: 'text-gray-600'
    }
  };

  const { role } = useIsAuthenticated()
  const date = new Date(props.referredAt).toLocaleDateString()
  const [status, setStatus] = useState(props.status);
  const [toggle, setToggle] = useState(false)
  const [resumeFileName, setResumeFileName] = useState(props.fileName); // State for resume file name
  const fileInputRef = useRef(null); // Ref for the hidden file input

  // Get the styles for the current status, or default if not found
  let currentStyles = statusStyles[status] || statusStyles.DEFAULT;

  const saveStatus = async (newStatus, id) => {
    try {
      await api.post(`${role}/setStatus/${props.referralId}`, { newStatus })
      setStatus(newStatus);
      toast.success("Status Updated Successfully");
    } catch (error) {

      toast.error("Failed to update status.");
    }
  }

  const handleStatusChange = (e) => {
    const status = e.target.value
    saveStatus(status)
  };

  const handleClick = () => {
    if (role === 'admin') {
      setToggle(!toggle)
    }
  }

  const handleFileChange = async (e) => {
    const file = e.target.files[0]
    console.log("enter");
    
    const data = new FormData();
    data.append('file', file)

    try {

      
      await api.put(`${role}/updateDoc/${props.referralId}`, data, {
        headers: {
          "Content-Type": 'multipart/form-data'
        }
      })
      setResumeFileName(file.name);
      
      
      toast.success("Resume updated successfully!");
    } catch (error) {
      
      
      toast.error("Failed to update resume.");
    }
  }

  const handleUpdateClick = () => {
    fileInputRef.current.click();
  };

  const handleDownload = async () => {
    try {
      const response = await api.get(`/${role}/referralData/${props.referralId}`, {
        responseType: 'blob', // Important for file downloads
      });
      const file = new Blob([response.data], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      window.open(fileURL, '_blank');
    } catch (error) {
      
      toast.error("Could not download resume.");
    }
  };

  return (
    <div onClick={handleClick}
      className={`w-full mt-5 md:ml-5 ml-2 max-w-sm bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 border border-gray-200 ${currentStyles.accent} border-l-4`}>
      {/* Header: Candidate Info & Status */}
      <div className="p-5">
        <div className="flex justify-between items-start">
          <div className="flex items-center gap-4">

            <div>
              <h3 className="text-xl font-bold text-gray-900">{props.candidateName}</h3>
              <p className="text-sm text-gray-500">{props.candidateEmail}</p>
            </div>
          </div>

          


          {
            role === "admin" ? <select
              value={status}
              onChange={handleStatusChange}
              className={`px-3 py-1 text-xs font-bold rounded-full appearance-none ${currentStyles.tag}`}
            >
              {Object.keys(statusStyles).filter(status => status !== 'DEFAULT').map(status => (
                <option key={status} value={status}>
                  {status}
                </option>
              ))}
            </select> : <span className={`px-3 py-1 text-xs font-bold rounded-full appearance-none ${currentStyles.tag}`}>{status}</span>
          }
        </div>
      </div>

      {/* Body: Job and Date Details with Icons */}
      <div className="px-5 pb-5 space-y-4 border-t border-gray-100 pt-4">
        <div className="flex items-center gap-3 text-sm">

          <div>
            <p className="text-gray-500">Job Position</p>
            <p className="font-semibold text-gray-800">{props.jobTitle}</p>
          </div>
        </div>

        <div className="flex items-center gap-3 text-sm">

          <div>
            <p className="text-gray-500">Referred On</p>
            <p className="font-semibold text-gray-800">{date}</p>
          </div>
        </div>

        {/* --- NEW: Resume Section --- */}
          <div className="text-sm">
            <p className="text-gray-500">Resume</p>
            {resumeFileName ? (
              <div className="flex items-center justify-between mt-1">
                <span className="font-semibold text-gray-800 truncate pr-2">{resumeFileName}</span>
                <button onClick={handleDownload} className="text-blue-600 hover:text-blue-800 font-semibold">Download</button>
              </div>
            ) : (
              <p className="font-semibold text-gray-800">No Resume Uploaded</p>
            )}
          </div>
        {role === 'user' && (
          <div>
            <input
              type="file"
              accept="application/pdf"
              ref={fileInputRef}
              onChange={handleFileChange}
              className="hidden" // Hidden file input
            />
            <button onClick={handleUpdateClick} className="w-full text-center py-2 px-4 text-sm bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold rounded-md">
              {resumeFileName ? 'Update Resume' : 'Upload Resume'}
            </button>
          </div>
        )}
        {
          toggle && <>
            <div>
              <p className="text-gray-500">Referred By</p>
              <p className="font-semibold text-gray-800">{props.referredByName}</p>
            </div>
            <div>
              <p className="text-gray-500">Referred Email</p>
              <p className="font-semibold text-gray-800">{props.referredByEmail}</p>
            </div>
          </>
        }
      </div>


    </div>
  );
}