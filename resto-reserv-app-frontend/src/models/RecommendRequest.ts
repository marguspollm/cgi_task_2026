import type { UserPreferences } from "./UserPreferences";

export type RecommendRequest = {
  partySize: number;
  date: string;
  time: string;
  userPreferences: UserPreferences;
};
