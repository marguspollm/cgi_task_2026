/**
 * Generates an array of 24 time slots for the entire day (00:00 to 23:00)
 * Each slot is formatted as HH:00 with leading zeros.
 *
 * @returns Array of 24 time strings like ['00:00', '01:00', ..., '23:00']
 */
export const generateTimeSlots = () => {
  return Array.from(
    { length: 24 },
    (_, i) => `${String(i).padStart(2, "0")}:00`,
  );
};
