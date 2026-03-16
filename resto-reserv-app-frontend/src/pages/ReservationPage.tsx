import { Alert, Box, Container, Grid, Typography } from "@mui/material";
import Floor from "../components/Floor";
import { useEffect, useState } from "react";
import type { FormErrors } from "../models/FormErrors";
import type { RecommendRequest } from "../models/RecommendRequest";
import type { ReservationRequest } from "../models/ReservationRequest";
import type { Table } from "../models/Table";
import { getRecommendedTable } from "../services/recommend.service";
import {
  getReservedTables,
  createReservation,
} from "../services/reservation.service";
import { getTables } from "../services/table.service";
import { handleError } from "../utils/errors";
import {
  validateAvailability,
  validateReservation,
} from "../utils/validations";
import type { ReservationFormState } from "../models/ReservationFormState";
import ReservationForm from "../components/ReservationForm";
import { ApiError } from "../models/ApiError";
import type { ReservationResponse } from "../models/ReservationResponse";
import ReservationConfirmationCard from "../components/ReservationConfirmationCard";
import { createDate, formatDate } from "../utils/formatters";

function ReservationPage() {
  const today = new Date().toISOString().split("T")[0];
  const defaultForm = {
    partySize: 0,
    date: today,
    time: "",
    customerName: "",
    phoneNumber: "",
    userPreferences: {},
  };
  const [tables, setTables] = useState<Table[]>([]);
  const [bookedTables, setBookedTables] = useState<number[]>([]);
  const [reservationForm, setReservationForm] =
    useState<ReservationFormState>(defaultForm);
  const [confirmedReservation, setConfirmedReservation] =
    useState<ReservationResponse | null>(null);

  const [recommededTable, setRecommendedTable] = useState<number | null>(null);
  const [selectedTable, setSelectedTable] = useState<number | null>(null);

  const [error, setError] = useState<string | null>(null);
  const [formErrors, setFormErrors] = useState<FormErrors>({});
  const [successOpen, setSuccessOpen] = useState(false);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchTables = async () => {
      try {
        const data = await getTables();
        setTables(data);
      } catch (error) {
        handleError(error, setError);
      }
    };
    fetchTables();
  }, []);

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const data = await getReservedTables();
        setBookedTables(data);
      } catch (error) {
        handleError(error, setError);
      }
    };
    fetchBookings();
  }, []);

  const handleTableClick = (id: number) => {
    if (bookedTables.includes(id)) return;
    setSelectedTable(id);
  };

  const handleCheckAvailability = async (
    e: React.SubmitEvent<HTMLFormElement>,
  ) => {
    if (loading) return;
    e.preventDefault();
    setLoading(true);
    setError(null);
    setFormErrors({});
    setSelectedTable(null);

    const errors = validateAvailability(reservationForm);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);
      return;
    }

    try {
      const payload: RecommendRequest = {
        partySize: reservationForm.partySize,
        date: reservationForm.date,
        time: reservationForm.time,
        userPreferences: reservationForm.userPreferences,
      };

      const recData = await getRecommendedTable(payload);
      const bookedData = await getReservedTables(
        reservationForm.date,
        reservationForm.time,
      );
      setRecommendedTable(recData);
      setBookedTables(bookedData);
    } catch (error) {
      handleError(error, setError);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirmReservation = async () => {
    if (!selectedTable || loading) return;
    setLoading(true);
    setError(null);
    setFormErrors({});

    const errors = validateReservation(reservationForm, selectedTable);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);
      return;
    }

    try {
      const payload: ReservationRequest = {
        customerName: reservationForm.customerName,
        phoneNumber: reservationForm.phoneNumber,
        tableId: selectedTable,
        date: reservationForm.date,
        time: reservationForm.time,
        partySize: reservationForm.partySize,
      };

      const confirmedReservation = await createReservation(payload);
      const bookedData = await getReservedTables(
        reservationForm.date,
        reservationForm.time,
      );
      setConfirmedReservation(confirmedReservation);
      setSuccessOpen(true);
      setBookedTables(bookedData);
      setSelectedTable(null);
      setRecommendedTable(null);
      setReservationForm(defaultForm);
    } catch (error) {
      handleError(error, setError);
      if (error instanceof ApiError) {
        if (error.payload?.errors) {
          setFormErrors(error.payload.errors);
        }
      }
    } finally {
      setLoading(false);
    }
  };

  const handleFormChange = (name: string, value: unknown) => {
    setReservationForm(prev => ({ ...prev, [name]: value }));
    setFormErrors(prev => ({
      ...prev,
      [name]: undefined,
    }));
  };

  const handleFormPreferenceChange = (name: string, value: unknown) => {
    setReservationForm(prev => ({
      ...prev,
      userPreferences: {
        ...prev.userPreferences,
        [name]: value,
      },
    }));
  };

  return (
    <>
      {error && <Alert severity="error">{error}</Alert>}
      <Container sx={{ py: 3 }}>
        <ReservationConfirmationCard
          open={successOpen}
          handleOpen={setSuccessOpen}
          confirmedReservation={confirmedReservation}
        />

        <Grid container spacing={{ xs: 2, md: 4 }}>
          <Grid size={{ xs: 12, md: 7 }}>
            <Box sx={{ width: "100%" }}>
              <Typography sx={{ mb: 2, fontWeight: 700, textAlign: "center" }}>
                Available tables for:{" "}
                {reservationForm.time
                  ? formatDate(
                      createDate(reservationForm.date, reservationForm.time),
                    )
                  : formatDate(new Date())}
              </Typography>

              <Floor
                tables={tables}
                booked={bookedTables}
                setSelectedTable={handleTableClick}
                selectedTable={selectedTable}
                recommendedTable={recommededTable}
              />
            </Box>
          </Grid>

          <Grid size={{ xs: 12, md: 5 }}>
            <Box sx={{ width: "100%", maxWidth: 500 }}>
              <ReservationForm
                form={reservationForm}
                checkAvailability={handleCheckAvailability}
                formChange={handleFormChange}
                formPreferenceChange={handleFormPreferenceChange}
                selectedTable={selectedTable}
                loading={loading}
                confirmReservation={handleConfirmReservation}
                errors={formErrors}
              />
            </Box>
          </Grid>
        </Grid>
      </Container>
    </>
  );
}

export default ReservationPage;
