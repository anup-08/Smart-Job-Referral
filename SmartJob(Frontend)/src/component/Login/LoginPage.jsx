import { useState } from 'react';
import { Login } from '../../Auth';
import useIsAuthenticated from '../../MyContext/IsAuthenticated';

const LoginPage = ({ onClose }) => {

    const [data, setdata] = useState({
        userName:"",
        password:""
    });

    const { setAuthModalState ,role, setLoggedIn ,setRole  } = useIsAuthenticated()

    const handleSubmit = async (e) => {
        e.preventDefault()
        
        const result = await Login({ userName:data.userName, password:data.password, role })
        setRole(result[0].toLowerCase());
        
        
        setLoggedIn(!!localStorage.getItem("accessToken"))
        onClose()
    }

    const handleChange = (e)=>{
        const {name , value} = e.target
        setdata({...data,[name]:value})
    }

    return (
        <>
            <div className="flex items-center justify-center fixed inset-0 bg-[rgba(189,189,189,0.9)] overflow-y-hidden" onClick={onClose}>
                <div
                    onClick={(e) => e.stopPropagation()}
                    className="w-full max-w-md p-8 space-y-6 bg-gray-100 rounded-lg shadow-md relative">
                    <button onClick={onClose}
                        className='absolute top-4 right-4 text-gray-500 hover:text-gray-800'><i className="ri-close-line"></i></button>
                    <h2 className="text-2xl font-extrabold text-center text-gray-700">Login</h2>
                    <form onSubmit={handleSubmit}>
                        <div>

                            <label className="block text-m font-medium mb-2 text-gray-700 " htmlFor='email'>Email</label>
                            <input
                                type="email"
                                name='userName'
                                required
                                id='email'
                                className="mt-1 block w-full p-2 pl-4 mb-3  border-gray-300  shadow-sm border rounded-md focus:outline-none focus:ring focus:ring-blue-400"
                                placeholder="Enter Your Email"
                                value={data.userName}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label className="block text-m mb-2 font-medium text-gray-700" htmlFor='pass'>Password</label>
                            <input
                                type="password"
                                id='pass'
                                name='password'
                                required
                                className="mt-1 block w-full p-2 pl-4 border rounded-md  border-gray-300 shadow-sm focus:outline-none focus:ring focus:ring-blue-400"
                                placeholder="Enter your password"
                                value={data.password}
                                onChange={handleChange}
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full px-4 py-2 mt-6 text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none">
                            Login
                        </button>
                    </form>
                    <p className="text-center mt-4">
                        Don't have an account?
                        
                        <button
                            onClick={() => setAuthModalState('signup')}
                            className="font-medium text-blue-600 hover:underline ml-1"
                        >
                            Sign up
                        </button>
                    </p>
                </div>
            </div>

        </>
    );
};

export default LoginPage;
