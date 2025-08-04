import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import useIsAuthenticated from "../MyContext/IsAuthenticated"

const ProtectedRoute = () => {
  const { isLoggedIn } = useIsAuthenticated();

  if (!isLoggedIn) {
    // If not logged in, redirect to the home page
    return <Navigate to="/" replace />;
  }

  // If logged in, render the child route's element
  return <Outlet />;
  
};

export default ProtectedRoute;