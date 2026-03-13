import type { TableAttribute } from "./TableAttribute";

export type Table = {
  id: number;
  capacity: number;
  attribute: TableAttribute[];
  locationX: number;
  locationY: number;
};
