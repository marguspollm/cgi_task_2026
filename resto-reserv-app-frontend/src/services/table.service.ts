import { apiFetch } from "../api/apiFetch";
import type { Table } from "../models/Table";

export const getTables = () => {
  return apiFetch<Table[]>("tables");
};

export const saveTables = (payload: Table[]) => {
  return apiFetch<Table[]>("tables", { method: "POST", body: payload });
};
