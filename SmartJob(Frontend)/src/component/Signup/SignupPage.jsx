import { useState } from 'react';
import { SignUp } from '../../Auth';
import useIsAuthenticated from '../../MyContext/IsAuthenticated';


const SignupPage = ({ onClose }) => {

    const [formData, setFormData] = useState({
        name:"",
        email:"",
        password:""
    })
    
    const { role, setLoggedIn, setAuthModalState } = useIsAuthenticated()

    const handleOnSubmit = async (e) => {
        e.preventDefault()
        const result = await SignUp({ name:formData.name, email:formData.email, password:formData.password, role })
        setLoggedIn(!!localStorage.getItem("accessToken"))
        onClose()
    }

    const handleChange =(e)=>{
        const {name,value} = e.target
        setFormData({...formData,[name]:value})
    }

    return (
        <div className="flex items-center justify-center fixed inset-0 bg-[rgba(189,189,189,0.9)] overflow-y-hidden" onClick={onClose}>
            <div 
                onClick={(e) => e.stopPropagation()}
                className="w-full max-w-md p-8 space-y-6 bg-gray-100 rounded-lg shadow-md relative">
                <button onClick={onClose}
                    className='absolute top-4 right-4 text-gray-500 hover:text-gray-800'><i className="ri-close-line"></i></button>
                <h2 className="text-2xl font-extrabold text-center text-gray-700">Sign Up</h2>
                <form onSubmit={handleOnSubmit}>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2" htmlFor='name'>Name</label>
                        <input
                            type="text"
                            id='name'
                            name='name'
                            required
                            className="mt-1 mb-3 pl-4 block w-full p-2  border-gray-300 shadow-sm border rounded-md focus:outline-none focus:ring focus:ring-blue-400"
                            placeholder="Enter Full Name"
                            value={formData.name}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2" htmlFor='email'>Email</label>
                        <input
                            type="email"
                            id='email'
                            name='email'
                            required
                            className="mt-1 mb-3 pl-4 block w-full p-2 border rounded-md  border-gray-300 shadow-sm focus:outline-none focus:ring focus:ring-blue-400"
                            placeholder="Enter Email"
                            value={formData.email}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2" htmlFor='pass'>Password</label>
                        <input
                            type="password"
                            required
                            id='pass'
                            name='password'
                            className="mt-1 mb-3 pl-4 block w-full p-2 border rounded-md  border-gray-300  shadow-sm focus:outline-none focus:ring focus:ring-blue-400"
                            placeholder="Enter Password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full px-4 py-2 mt-6 text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none">
                        Sign Up
                    </button>
                </form>
                <p className="text-center mt-4">
                    Already have an account?
                    <button
                        onClick={() => setAuthModalState('login')}
                        className="font-medium text-blue-600 hover:underline ml-1"
                    >
                        Login
                    </button>
                </p>
            </div>
        </div>
    );
};

export default SignupPage;
