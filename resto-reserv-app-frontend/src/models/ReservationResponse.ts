import type { ReservationRequest } from "./ReservationRequest";

export type ReservationResponse = ReservationRequest & { id: number };
