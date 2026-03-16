export const generateTimeSlots = () => {
  return Array.from(
    { length: 24 },
    (_, i) => `${String(i).padStart(2, "0")}:00`,
  );
};
