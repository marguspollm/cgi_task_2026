import type { TableAttribute } from "./TableAttribute";

export type ReservationFormState = {
  customerName: string;
  phoneNumber: string;
  partySize: number;
  date: string;
  time: string;
  tablePreferences: TableAttribute[];
};
