import { apiFetch } from "../api/apiFetch";
import type { RecommendRequest } from "../models/RecommendRequest";

/**
 * Requests the backend to recommend a suitable table based on reservation criteria
 *
 * @param request Recommendation criteria including party size, date, time, and preferred attributes
 * @returns Promise resolving to the ID of the recommended table
 */
export const getRecommendedTable = (request: RecommendRequest) => {
  return apiFetch<number>("recommended-table", {
    method: "POST",
    body: request,
  });
};
