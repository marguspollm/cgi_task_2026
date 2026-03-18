import type { FormErrors, NewTableErrors } from "../models/FormErrors";
import type { ReservationFormState } from "../models/ReservationFormState";

/**
 * Validates the availability form fields before checking table availability
 *
 * @param form Ration form state to validate
 * @returns FormErros containing field names and error messages, empty if no errors
 */
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

/**
 * Validates the reservation form before submission
 *
 * @param form The reservation form state to validate
 * @param selectedTable ID of the selected table
 * @returns FormErrors containing field names and error messages, empty if no errors
 */
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

/**
 * Validates a new table capacity in the floor editor
 *
 * @param capacity Table capacity to validate
 * @returns NewTableErrors containing error messages, empty if no errors
 */
export const validateNewTable = (capacity: number): NewTableErrors => {
  const errors: NewTableErrors = {};
  if (capacity <= 0) errors.capacity = "Needs to be atleast 1";
  return errors;
};
