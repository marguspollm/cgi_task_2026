import { ApiError } from "../models/ApiError";

export const handleError = (
  error: unknown,
  setError: (message: string) => void,
) => {
  if (error instanceof ApiError) {
    setError(error.payload?.message || "Something went wrong");
    return;
  }

  if (error instanceof Error) {
    setError(error.message);
    return;
  }

  setError("Something went wrong =(");
};
