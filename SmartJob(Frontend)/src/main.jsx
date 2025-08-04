import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from 'react-router-dom'

import Layout from './Layout'
import { AddJobs, ErrorPage, HomePage, LoginPage, PostedJob, ReferralHome, SignUpPage } from './component'
import { IsAuthenticatedProvide } from './MyContext/IsAuthenticated'
import ProtectedRoute from "./ProtectedRoute/ProtectedRoute"


const router = createBrowserRouter(

  createRoutesFromElements(
    <Route path='/' element= {<Layout />} errorElement={<ErrorPage />} >
      <Route index element = {<HomePage />}/>
      <Route path='login' element={<LoginPage />} />
      <Route path='signup' element={<SignUpPage />} />
      <Route path='seeReferrals' element={<ReferralHome />} />

      <Route element={<ProtectedRoute />}>
        <Route path='addjob' element={<AddJobs />} />
        <Route path='getPostedJobs' element={<PostedJob />} />
      </Route>

    </Route>
  )

)


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <IsAuthenticatedProvide >
      <RouterProvider router={router} />
    </IsAuthenticatedProvide>
  </StrictMode>,
)
