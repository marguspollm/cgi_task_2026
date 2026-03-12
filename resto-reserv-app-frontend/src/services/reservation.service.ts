import { apiFetch } from "../api/apiFetch";
import type { ReservationRequest } from "../models/ReservationRequest";
import type { ReservationResponse } from "../models/ReservationResponse";

export const getReservedTables = () => {
  return apiFetch<number[]>(`reservations`);
};

export const createReservation = (request: ReservationRequest) => {
  return apiFetch<ReservationResponse>("create-reservation", {
    method: "POST",
    body: request,
  });
};
