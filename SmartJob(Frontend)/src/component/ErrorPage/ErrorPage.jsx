import { useRouteError, Link } from "react-router-dom";

export default function ErrorPage() {
  // This hook provides the actual error that was thrown
  const error = useRouteError();

  return (
    <div className="flex flex-col items-center justify-center min-h-screen gap-6 p-4 text-center bg-gray-50">
      <div className="text-8xl text-red-500">
        <i className="ri-emotion-sad-line"></i>
      </div>
      <div className="space-y-2">
        <h1 className="text-4xl font-extrabold text-gray-800">Oops!</h1>
        <p className="text-lg text-gray-600">Sorry, an unexpected error has occurred.</p>
        <p className="px-4 py-2 text-base text-red-700 bg-red-100 rounded-md">
          <i>{error.statusText || error.message}</i>
        </p>
      </div>
      <Link
        to="/"
        className="px-6 py-3 font-semibold text-white bg-indigo-600 rounded-md hover:bg-indigo-700 transition-colors"
      >
        Go Back Home
      </Link>
    </div>
  );
}