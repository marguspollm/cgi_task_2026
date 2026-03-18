import { apiFetch } from "../api/apiFetch";
import type { Pageable } from "../models/Pageable";
import type { ReservationFilters } from "../models/ReservationFilters";
import type { ReservationRequest } from "../models/ReservationRequest";
import type { ReservationResponse } from "../models/ReservationResponse";

export const getReservedTables = (date?: string, time?: string) => {
  const params = new URLSearchParams();
  if (date && time) {
    params.append("date", date);
    params.append("time", time);
  }
  return apiFetch<number[]>(`reserved-tables?${params}`);
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
  params.append("size", filters.size.toString());
  params.append("sort", "date,time,asc");

  if (filters.customerName) params.append("customerName", filters.customerName);
  if (filters.date) params.append("date", filters.date);
  if (filters.time) params.append("time", filters.time);

  return apiFetch<Pageable<ReservationResponse>>(`reservations?${params}`);
};
