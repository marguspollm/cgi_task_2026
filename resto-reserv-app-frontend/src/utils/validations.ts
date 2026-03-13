import type { FormErrors } from "../models/FormErrors";
import type { ReservationForm } from "../models/ReservationForm";

export const validateAvailability = (form: ReservationForm): FormErrors => {
  const errors: FormErrors = {};

  if (!form.partySize || form.partySize < 1)
    errors.partySize = "Party size must be at least 1";
  if (!form.date) errors.date = "Date is required";
  if (!form.time) errors.time = "Time is required";

  return errors;
};

export const validateReservation = (
  form: ReservationForm,
  selectedTable: number | null,
): FormErrors => {
  const errors: FormErrors = {};

  if (!selectedTable) errors.tableId = "Please select a table";
  if (!form.customerName) errors.customerName = "Customer name required";
  if (!form.phoneNumber) errors.phoneNumber = "Phone number required";

  return errors;
};
