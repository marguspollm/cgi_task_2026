import { apiFetch } from "../api/apiFetch";
import type { Pageable } from "../models/Pageable";
import type { ReservationFilters } from "../models/ReservationFilters";
import type { ReservationRequest } from "../models/ReservationRequest";
import type { ReservationResponse } from "../models/ReservationResponse";

export const getReservedTables = (date?: string, time?: string) => {
  const payload = date && time ? `?date=${date}&time=${time}` : "";
  return apiFetch<number[]>(`reserved-tables${payload}`);
};

export const createReservation = (request: ReservationRequest) => {
  return apiFetch<ReservationResponse>("create-reservation", {
    method: "POST",
    body: request,
  });
};

export const getReservations = (filters: ReservationFilters) => {
  const params = new URLSearchParams();
  params.append("page", filters.page.toString());
  params.append("size", "20");

  if (filters.customerName) params.append("customerName", filters.customerName);
  if (filters.date) params.append("date", filters.date);
  if (filters.time) params.append("time", filters.time);

  return apiFetch<Pageable<ReservationResponse>>(`reservations?${params}`);
};
