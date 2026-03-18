import { ApiError } from "../models/ApiError";

const BACKEND_URL = "http://localhost:8080/";

/**
 * Fetch wrapper for making API requests to the backend
 *
 * @template T Response type
 * @param endpoint API endpoint path
 * @param options Request options
 * @returns Promise resolving to the typed response data
 * @throws ApiError
 *
 * @example
 * // GET request
 * const result = await apiFetch<Meal[]>("meals");;
 *
 * // POST request
 * const result = await apiFetch<Table[]>("tables", { method: "POST", body: payload });
 */
export async function apiFetch<T>(
  endpoint: string,
  options: {
    method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
    body?: unknown;
    headers?: Record<string, string>;
  } = {},
): Promise<T> {
  const { method = "GET", body, headers = {} } = options;

  // Create the full URL and make the request
  const res = await fetch(`${BACKEND_URL}${endpoint}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  // Parse the response as JSON
  const data = await res.json();

  // Handle error responses
  if (!res.ok) {
    throw new ApiError({
      message: data.message || "API Error",
      status: res.status,
      errors: data.errors || null,
    });
  }

  return data as T;
}
