import type { UserPreferences } from "./UserPreferences";

export type ReservationFormState = {
  customerName: string;
  phoneNumber: string;
  partySize: number;
  date: string;
  time: string;
  userPreferences: UserPreferences;
};
