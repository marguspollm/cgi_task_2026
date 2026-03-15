import { apiFetch } from "../api/apiFetch";
import type { Meal } from "../models/Meal";

export const getRecommendedMeals = () => {
  return apiFetch<Meal[]>(`meals`);
};
