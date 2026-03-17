import { ApiError } from "../models/ApiError";

const BACKEND_URL = "http://localhost:8080/";

export async function apiFetch<T>(
  endpoint: string,
  options: {
    method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
    body?: unknown;
    headers?: Record<string, string>;
  } = {},
): Promise<T> {
  const { method = "GET", body, headers = {} } = options;

  const res = await fetch(`${BACKEND_URL}${endpoint}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  const data = await res.json();

  if (!res.ok) {
    throw new ApiError({
      message: data.message || "API Error",
      status: res.status,
      errors: data.errors || null,
    });
  }

  return data as T;
}
