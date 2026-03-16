import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogContent,
  DialogTitle,
  Snackbar,
  Typography,
} from "@mui/material";
import { createDate, formatDate, formatTime } from "../utils/formatters";
import type { ReservationResponse } from "../models/ReservationResponse";
import { useEffect, useState } from "react";
import type { Meal } from "../models/Meal";
import { getRecommendedMeals } from "../services/meals.service";
import MealsCarousel from "./MealsCarousel";

type ReservationConfirmationCardProps = {
  open: boolean;
  handleOpen: (vald: boolean) => void;
  confirmedReservation: ReservationResponse | null;
};

function ReservationConfirmationCard({
  open,
  handleOpen,
  confirmedReservation,
}: ReservationConfirmationCardProps) {
  const [recommendedMeals, setRecommendedMeals] = useState<Meal[]>([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [loadingMeals, setLoadingMeals] = useState(false);

  useEffect(() => {
    const fetchMeals = async () => {
      if (!confirmedReservation) return;
      try {
        setLoadingMeals(true);
        const data = await getRecommendedMeals();
        setRecommendedMeals(data);
      } catch (error) {
        console.log(error);
      } finally {
        setLoadingMeals(false);
      }
    };

    fetchMeals();
  }, [confirmedReservation]);

  const handlePreOrder = (id: number) => {
    setRecommendedMeals(prev => prev.filter(meal => meal.id !== id));
    setSnackbarOpen(true);
  };

  return (
    <>
      <Dialog
        open={open}
        onClose={() => handleOpen(false)}
        maxWidth="md"
        fullWidth
      >
        <Box sx={{ p: 3 }}>
          <DialogTitle
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            Reservation Confirmed
            <Button onClick={() => handleOpen(false)} variant="contained">
              Close
            </Button>
          </DialogTitle>

          {confirmedReservation && (
            <DialogContent dividers>
              <Typography>
                <strong>Name: </strong> {confirmedReservation.customerName}
              </Typography>
              <Typography>
                <strong>Phone: </strong> {confirmedReservation.phoneNumber}
              </Typography>
              <Typography>
                <strong>Table: </strong> {confirmedReservation.tableId}
              </Typography>
              <Typography>
                <strong>Date: </strong>
                {formatDate(
                  createDate(
                    confirmedReservation.date,
                    confirmedReservation.time,
                  ),
                )}
              </Typography>
              <Typography>
                <strong>Time: </strong>
                {formatTime(
                  createDate(
                    confirmedReservation.date,
                    confirmedReservation.time,
                  ),
                )}
              </Typography>
              <Typography>
                <strong>Party Size: </strong> {confirmedReservation.partySize}
              </Typography>
            </DialogContent>
          )}
          <Box>
            {loadingMeals && (
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  py: 4,
                }}
              >
                <CircularProgress size={32} />
              </Box>
            )}
            {!loadingMeals && recommendedMeals.length > 0 && (
              <MealsCarousel
                recommendedMeals={recommendedMeals}
                handlePreoderClick={handlePreOrder}
              />
            )}
          </Box>
        </Box>
      </Dialog>
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={() => setSnackbarOpen(false)}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity="success"
          variant="filled"
          sx={{ width: "100%" }}
        >
          Meal ordered successfully
        </Alert>
      </Snackbar>
    </>
  );
}

export default ReservationConfirmationCard;
