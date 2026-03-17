import { apiFetch } from "../api/apiFetch";
import type { Table } from "../models/Table";
import type { TableAttribute } from "../models/TableAttribute";

export const getTables = () => {
  return apiFetch<Table[]>("tables");
};

export const saveTables = (payload: Table[]) => {
  return apiFetch<Table[]>("tables", { method: "POST", body: payload });
};

export const getTableAttributes = () => {
  return apiFetch<TableAttribute[]>("table-attributes");
};
