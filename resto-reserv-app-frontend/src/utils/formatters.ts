import type { TableAttribute } from "../models/TableAttribute";

/**
 * Combines a date string and time string into a single Date object
 *
 * @param dateStr Date in YYYY-MM-DD format
 * @param timeStr Time in HH:mm format
 * @returns Date object representing the combined date and time
 */
export const createDate = (dateStr: string, timeStr: string) =>
  new Date(`${dateStr}T${timeStr}`);

/**
 * Formats Date into a readable string with full date details
 *
 * @param dateTime The Date object to format
 * @returns Formatted string like "Sunday, December 31, 1999"
 */
export const formatDate = (dateTime: Date | null): string => {
  if (!dateTime) return "";
  return dateTime.toLocaleDateString("en-US", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};

/**
 * Formats Date into a time string in 24-hour format
 *
 * @param dateTime Date object to format
 * @returns Formatted time string like "12:00"
 */
export const formatTime = (dateTime: Date | null): string => {
  if (!dateTime) return "";
  return dateTime.toLocaleTimeString("en-US", {
    hour: "numeric",
    minute: "2-digit",
    hour12: false,
  });
};

/**
 * Formats TableAttributes to readable attributes
 *
 * @param attr Array of table attributes
 * @returns Readable table attributs array
 */
export const getTableAttribute = (attr: TableAttribute[]) => {
  const str: string[] = [];
  if (attr.includes("WINDOW")) str.push("Window table");
  if (attr.includes("EASY_ACCESSIBLE")) str.push("Easily accessible");
  if (attr.includes("PRIVATE")) str.push("Private table");
  if (attr.includes("NEAR_KIDS_AREA")) str.push("Near kids area");
  return str.join("\r\n");
};
