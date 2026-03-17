import type { TableAttribute } from "./TableAttribute";

export type RecommendRequest = {
  partySize: number;
  date: string;
  time: string;
  userPreferences: TableAttribute[];
};
