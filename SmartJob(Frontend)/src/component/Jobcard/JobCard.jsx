import { useState } from "react";
import ReferralForm from "../ReferralForm/ReferralForm";
import useIsAuthenticated from "../../MyContext/IsAuthenticated";


const JobCard = (props) => {

  const [showForm, setShowForm] = useState(false)
  const { isLoggedIn, setAuthModalState, role } = useIsAuthenticated()

  const handleClose = () => setShowForm(false)

  const handleReferClick = () => {
    if (isLoggedIn) {
      // If the user is logged in, show the referral form
      setShowForm(true);
    } else {
      // If not logged in, set the global state to open the login modal
      setAuthModalState('login');
    }
  };

  return (
    <>
      <div className="flex flex-col max-w-90 rounded-2xl overflow-hidden shadow-lg bg-slate-50 p-5 h-full mb-3">

        {/* This new div will grow and push the button to the bottom */}
        <div className="flex-grow">
          <div className="font-bold text-xl mb-4">Job Title : {props.title}</div>
          <p className="text-black text-base mb-3">Company Name :<span className='font-semibold'> {props.companyName?.toUpperCase()}</span></p>
          <p className="text-black text-base mb-3">Department :<span className='font-semibold'>  {props.department}</span> </p>
          <p className="text-black text-base mb-3">Description :<span className='font-semibold'>  {props.description}</span> </p>
          <p className="text-black text-base mb-3">Location :
            <span className='text-red-700 font-semibold'> {props.location?.join(',')}</span>
          </p>
          <p className="text-black text-base mb-3">Posted Date :<span className='font-semibold'> {new Date(props.postedAt).toLocaleDateString()}</span></p>
          <p className="text-black text-base mb-3">Experince :<span className='font-semibold'> {`${props.experienceRequired}yr`}</span> </p>
        </div>

        
        <div className="mt-auto pt-4"> 
          <div className='transform transition duration-200 hover:scale-105'>
            {
              role === 'user' && <button onClick={handleReferClick}
                type='button'
                className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded w-full">
                Refer Candidate
              </button>
            }
          </div>
        </div>
      </div>

      {showForm && isLoggedIn && <ReferralForm onClose={handleClose} jobId={props.id} />}

    </>


  );
};

export default JobCard;
