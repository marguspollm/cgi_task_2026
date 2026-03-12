export type ReservationRequest = {
  customerName: string;
  phoneNumber: string;
  tableId: number;
  date: string;
  time: string;
  partySize: number;
};
