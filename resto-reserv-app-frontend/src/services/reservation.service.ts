import { apiFetch } from "../api/apiFetch";
import type { ReservationRequest } from "../models/ReservationRequest";
import type { ReservationResponse } from "../models/ReservationResponse";

export const getReservedTables = (date?: string, time?: string) => {
  const payload = date && time ? `?date=${date}&time=${time}` : "";
  return apiFetch<number[]>(`reservations${payload}`);
};

export const createReservation = (request: ReservationRequest) => {
  return apiFetch<ReservationResponse>("create-reservation", {
    method: "POST",
    body: request,
  });
};
