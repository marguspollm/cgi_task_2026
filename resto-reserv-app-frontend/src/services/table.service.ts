import { apiFetch } from "../api/apiFetch";
import type { Table } from "../models/Table";
import type { TableAttribute } from "../models/TableAttribute";

/**
 * Retrieves all restaurant tables from the backend
 * @returns Promise resolving to an array of all available tables
 */
export const getTables = () => {
  return apiFetch<Table[]>("tables");
};

/**
 * Saves or updates a list of tables on the backend
 *
 * @param payload Array of Table objects to save/update
 * @returns Promise resolving to the saved tables with updated IDs
 */
export const saveTables = (payload: Table[]) => {
  return apiFetch<Table[]>("tables", { method: "POST", body: payload });
};

/**
 * Retrieves all available table attributes
 * @returns Promise resolving to an array of table attribute options
 */
export const getTableAttributes = () => {
  return apiFetch<TableAttribute[]>("table-attributes");
};

/**
 * Deletes a table from the backend
 *
 * @param id The ID of the table to delete
 * @returns Promise resolving to a boolean indicating success
 */
export const deleteTable = (id: number) => {
  const params = new URLSearchParams();
  if (id) {
    params.append("id", id.toString());
  }
  return apiFetch<boolean>(`tables?${params}`, { method: "DELETE" });
};
