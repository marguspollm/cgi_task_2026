import type { UserPreferences } from "./UserPreferences";

export type ReservationForm = {
  customerName: string;
  phoneNumber: string;
  partySize: number;
  date: string;
  time: string;
  userPreferences: UserPreferences;
};
