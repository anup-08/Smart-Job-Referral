import { Link, NavLink } from "react-router-dom";
import LoginPage from "../Login/LoginPage";
import SignupPage from "../Signup/SignupPage";
import useIsAuthenticated from "../../MyContext/IsAuthenticated";
import ProfileCard from "../ProfileCaed/ProfileCard";
import { useState , useEffect} from "react";




export default function NavBar() {

  const { authModalState, setAuthModalState, isLoggedIn, role ,setRole } = useIsAuthenticated();
  const [showingPage , setShowingPage] = useState(false)

  const openLoginModal = () => {
    setAuthModalState('login');
    setShowingPage(true)
  }

  const openAdminLoginModel = () => {
    setShowingPage(true)
    setAuthModalState('login')
    setRole("admin")
  };

  const closeModal = () => {
    setShowingPage(false)
    setAuthModalState('closed');
  }

  useEffect(() => {
          // When the modal is shown, we want to prevent scrolling
          if (showingPage) {
              document.body.style.overflow = 'hidden';
          }else {
              document.body.style.overflow = 'auto';
          }
          // Cleanup function to restore scrolling when the component unmounts
          return () => {
              document.body.style.overflow = 'auto';
          };
      }, [showingPage]);


  return (
    <>
      {/* Navbar */}
      <nav className="w-full bg-gray-100 shadow-lg mb-3 rounded-lg mx-auto p-2 px-4 sm:px-6 lg:px-8 ">
        <div className="flex items-center justify-between h-17">
          {/* Left side - Logo & Home */}
          <div className="flex items-center space-x-3">
            <div className="flex items-center space-x-1 cursor-pointer">
              <img src="public/logo.png"
                className="w-35 "
              />
            </div>

            {/* Navigation Home & Login */}
            <div className="hidden md:flex space-x-6 ml-6 font-medium text-gray-700">
              <NavLink to="/"
                type="button"
                className={({ isActive }) => `hover:text-indigo-600 transition duration-200 
                  ${isActive ? "text-indigo-600" : "text-gray-700"}
                  focus:outline-none transform hover:scale-115 `}
              >
                Home
              </NavLink>

              {
                !isLoggedIn && <button onClick={openLoginModal}
                  type="button"
                  className={`hover:text-indigo-600 transition duration-200 
                  transform hover:scale-110`}
                >
                  Login/SignUp
                </button>
              }
            </div>
          </div>

          <div className="flex items-center space-x-4">
            {
              !isLoggedIn && <div className="flex items-center space-x-5">
                <span
                  type="button"
                  className="relative flex items-center space-x-2 text-gray-700"
                >
                  {/* Icon - User with a plus */}
                  <i className="ri-admin-line"></i>

                  <span className="text-sm font-medium select-none">Post Job</span>

                </span>

                {
                  !isLoggedIn && <button onClick={openAdminLoginModel}
                    className="bg-indigo-600 text-white  px-4 py-2 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition-colors">
                    Admin only
                  </button>
                }

              </div>
            }


            {isLoggedIn && (
              <>
                {role === 'admin' && (
                  <Link to={"addjob"}
                    className="bg-indigo-600 text-white  px-4 py-2 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition-colors">
                    Add Jobs
                  </Link>
                )}

                <ProfileCard />
              </>
            )}
          </div>

        </div>
      </nav>

      {authModalState === 'login' && <LoginPage onClose={closeModal} />}
      {authModalState === 'signup' && <SignupPage onClose={closeModal} />}

    </>
  );
}

