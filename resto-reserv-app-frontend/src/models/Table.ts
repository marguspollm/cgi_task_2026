import type { TableAttribute } from "./TableAttribute";

export type Table = {
  id?: number;
  capacity: number;
  attributes: TableAttribute[];
  locationX: number;
  locationY: number;
};
