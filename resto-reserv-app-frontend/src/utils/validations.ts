import type { FormErrors, NewTableErrors } from "../models/FormErrors";
import type { ReservationFormState } from "../models/ReservationFormState";

export const validateAvailability = (
  form: ReservationFormState,
): FormErrors => {
  const errors: FormErrors = {};

  if (!form.partySize || form.partySize < 1)
    errors.partySize = "Party size must be at least 1";
  if (!form.date) errors.date = "Date is required";
  if (!form.time) errors.time = "Time is required";

  return errors;
};

export const validateReservation = (
  form: ReservationFormState,
  selectedTable: number | null,
): FormErrors => {
  const errors: FormErrors = {};

  if (!selectedTable) errors.tableId = "Please select a table";
  if (!form.customerName) errors.customerName = "Customer name required";
  if (!form.phoneNumber) errors.phoneNumber = "Phone number required";

  return errors;
};

export const validateNewTable = (capacity: number): NewTableErrors => {
  const errors: NewTableErrors = {};
  if (capacity <= 0) errors.capacity = "Needs to be atleast 1";
  return errors;
};
