import {
  Alert,
  Box,
  CircularProgress,
  Container,
  Grid,
  Typography,
} from "@mui/material";
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
import type { TableAttribute } from "../models/TableAttribute";

/**
 * ReservationPage
 *
 * Main page for customer reservations. Displays the restaurant floor plan,
 * manages the reservation form, handles table selection and availability checking,
 * and processes reservation confirmations
 *
 * Workflow:
 * 1. Load all available tables and current reservations on component mount
 * 2. Customer fills in date, time, party size, and table preferences
 * 3. System recommends available tables based on criteria
 * 4. Customer selects a table visually on the floor plan
 * 5. Customer enters personal details (name, phone)
 * 6. System creates the reservation and provides confirmation
 */
function ReservationPage() {
  const today = new Date().toISOString().split("T")[0];
  const defaultForm = {
    partySize: 0,
    date: today,
    time: "",
    customerName: "",
    phoneNumber: "",
    tablePreferences: [],
  };
  const [tables, setTables] = useState<Table[]>([]);
  const [bookedTables, setBookedTables] = useState<number[]>([]);
  const [reservationForm, setReservationForm] =
    useState<ReservationFormState>(defaultForm);
  const [confirmedReservation, setConfirmedReservation] =
    useState<ReservationResponse | null>(null);

  const [recommendedTableId, setRecommendedTableId] = useState<number | null>(
    null,
  );
  const [selectedTableId, setSelectedTableId] = useState<number | null>(null);
  const [tablePreferences, setTablePreferences] = useState<TableAttribute[]>(
    [],
  );

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

  /**
   * Handles table selection on the floor plan
   * Prevents selection of already booked tables
   */
  const handleTableClick = (id: number) => {
    if (bookedTables.includes(id)) return;
    setSelectedTableId(id);
  };

  // Handles and validates available tables check
  const handleCheckAvailability = async (
    e: React.SubmitEvent<HTMLFormElement>,
  ) => {
    // Don't allow check when loading in progress
    if (loading) return;
    e.preventDefault();
    setLoading(true);
    setError(null);
    setFormErrors({});
    setSelectedTableId(null);
    setRecommendedTableId(null);

    //Validate required form fields
    const errors = validateAvailability(reservationForm);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);
      return;
    }

    try {
      // Create recommendation request payload
      const payload: RecommendRequest = {
        partySize: reservationForm.partySize,
        date: reservationForm.date,
        time: reservationForm.time,
        tablePreferences: tablePreferences,
      };

      //Get recommended table and booked table IDs from backend
      const recData = await getRecommendedTable(payload);
      const bookedData = await getReservedTables(
        reservationForm.date,
        reservationForm.time,
      );
      setRecommendedTableId(recData);
      setBookedTables(bookedData);
    } catch (error) {
      handleError(error, setError);
    } finally {
      setLoading(false);
    }
  };

  // Handles and validates creating resrvation
  const handleCreateReservation = async () => {
    // Don't allow creating reservation when no selected table or loading in progress
    if (!selectedTableId || loading) return;
    setLoading(true);
    setError(null);
    setFormErrors({});

    // Validates required form fields from creating reservation
    const errors = validateReservation(reservationForm, selectedTableId);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);
      return;
    }

    try {
      // Create reservation payload
      const payload: ReservationRequest = {
        customerName: reservationForm.customerName,
        phoneNumber: reservationForm.phoneNumber,
        tableId: selectedTableId,
        date: reservationForm.date,
        time: reservationForm.time,
        partySize: reservationForm.partySize,
      };

      // Send reservation info and get updated tables data from backend
      const confirmedReservation = await createReservation(payload);
      const bookedData = await getReservedTables(
        reservationForm.date,
        reservationForm.time,
      );
      setConfirmedReservation(confirmedReservation);
      setSuccessOpen(true);
      setBookedTables(bookedData);
      setSelectedTableId(null);
      setRecommendedTableId(null);
      setReservationForm(defaultForm);
      setTablePreferences([]);
    } catch (error) {
      handleError(error, setError);
      if (error instanceof ApiError) {
        // Check if backend sends back validaton errors
        if (error.payload?.errors) {
          setFormErrors(error.payload.errors);
        }
      }
    } finally {
      setLoading(false);
    }
  };

  // Updates a specific form field and clears its error message
  const handleFormChange = (name: string, value: unknown) => {
    setReservationForm(prev => ({ ...prev, [name]: value }));
    setFormErrors(prev => ({
      ...prev,
      [name]: undefined,
    }));
  };

  // Updates the selected table attribute preferences
  const handleFormPreferenceChange = (value: TableAttribute[]) => {
    setTablePreferences(value);
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

              {loading ? (
                <>
                  <CircularProgress size="3rem" />
                </>
              ) : (
                <Floor
                  mode="view"
                  tables={tables}
                  booked={bookedTables}
                  selectedTableId={selectedTableId}
                  setSelectedTable={handleTableClick}
                  recommendedTableId={recommendedTableId}
                />
              )}
            </Box>
          </Grid>

          <Grid size={{ xs: 12, md: 5 }}>
            <Box sx={{ width: "100%", maxWidth: 500 }}>
              <ReservationForm
                form={reservationForm}
                checkAvailability={handleCheckAvailability}
                formChange={handleFormChange}
                formPreferenceChange={handleFormPreferenceChange}
                selectedTableId={selectedTableId}
                loading={loading}
                createReservation={handleCreateReservation}
                errors={formErrors}
                tablePreferences={tablePreferences}
              />
            </Box>
          </Grid>
        </Grid>
      </Container>
    </>
  );
}

export default ReservationPage;
