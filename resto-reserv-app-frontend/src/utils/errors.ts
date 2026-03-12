export const handleError = (
  error: unknown,
  setError: (message: string) => void,
) => {
  setError(getErrorMessage(error));
};

const getErrorMessage = (error: unknown) => {
  if (error instanceof Error) {
    return error.message.replaceAll(" ", "").toLowerCase();
  }
  return "Something went wrong =(";
};
