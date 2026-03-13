export const createDate = (dateStr: string, timeStr: string) =>
  new Date(`${dateStr}T${timeStr}`);

export const formatDate = (dateTime: Date) => {
  return dateTime.toLocaleDateString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};

export const formatTime = (dateTime: Date) => {
  return dateTime.toLocaleTimeString("en-US", {
    hour: "numeric",
    minute: "2-digit",
    hour12: false,
  });
};
