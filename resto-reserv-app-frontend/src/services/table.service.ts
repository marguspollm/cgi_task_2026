import { apiFetch } from "../api/apiFetch";
import type { Table } from "../models/Table";

export const getTables = () => {
  return apiFetch<Table[]>("tables");
};
