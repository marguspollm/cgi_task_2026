import { ApiError } from "../models/ApiError";

/**
 * Unified error handler that processes different error types and displays user-friendly messages
 * Handles ApiError instances, standard Error objects, and unknown error types
 *
 * @param error The error object to handle (can be ApiError, Error, or unknown)
 * @param setError Callback function to set the error message in UI state
 */
export const handleError = (
  error: unknown,
  setError: (message: string) => void,
) => {
  // Handle API errors from backend
  if (error instanceof ApiError) {
    setError(error.payload?.message || "Something went wrong");
    return;
  }

  // Handle standard JavaScript errors
  if (error instanceof Error) {
    setError(error.message);
    return;
  }

  // Handle unknown error types
  setError("Something went wrong =(");
};
