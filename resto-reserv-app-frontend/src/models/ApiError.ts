export class ApiError extends Error {
  status?: number | string;
  payload?: {
    message: string;
    errors?: Record<string, string> | null;
  };

  constructor(payload: {
    message: string;
    status?: number | string;
    errors?: Record<string, string> | null;
  }) {
    super(payload.message);
    this.payload = payload;
    this.status = payload.status;

    Object.setPrototypeOf(this, ApiError.prototype);
  }
}
