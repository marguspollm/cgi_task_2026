import { apiFetch } from "../api/apiFetch";
import type { Pageable } from "../models/Pageable";
import type { ReservationFilters } from "../models/ReservationFilters";
import type { ReservationRequest } from "../models/ReservationRequest";
import type { ReservationResponse } from "../models/ReservationResponse";

/**
 * Fetches the list of reserved table IDs for a selected date and time
 * If date and time are not provided, returns all tables reserved for today/now
 *
 * @param date Optional reservation date (YYYY-MM-DD format)
 * @param time Optional reservation time (HH:mm format)
 * @returns Promise resolving to an array of reserved table IDs
 */
export const getReservedTables = (date?: string, time?: string) => {
  const params = new URLSearchParams();
  if (date && time) {
    params.append("date", date);
    params.append("time", time);
  }
  return apiFetch<number[]>(`reserved-tables?${params}`);
};

/**
 * Creates a new reservation
 *
 * @param request The reservation details including customer info, date, time, table ID, and party size
 * @returns Promise resolving to the created reservation with confirmation ID and details
 */
export const createReservation = (request: ReservationRequest) => {
  return apiFetch<ReservationResponse>("create-reservation", {
    method: "POST",
    body: request,
  });
};

/**
 * Retrieves a paginated list of reservations with optional filtering
 *
 * @param filters Filter criteria including pagination (page, size), date, time, and customer name
 * @returns Promise resolving to a paginated response containing reservations matching the filters
 */
export const getReservations = (filters: ReservationFilters) => {
  const params = new URLSearchParams();
  params.append("page", filters.page.toString());
  params.append("size", filters.size.toString());
  params.append("sort", "date,time,asc");

  // Add optional filters if provided
  if (filters.customerName) params.append("customerName", filters.customerName);
  if (filters.date) params.append("date", filters.date);
  if (filters.time) params.append("time", filters.time);

  return apiFetch<Pageable<ReservationResponse>>(`reservations?${params}`);
};
