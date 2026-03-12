import { apiFetch } from "../api/apiFetch";
import type { RecommendRequest } from "../models/RecommendRequest";

export const getRecommendedTable = (request: RecommendRequest) => {
  return apiFetch<number>("recommended-table", {
    method: "POST",
    body: request,
  });
};
