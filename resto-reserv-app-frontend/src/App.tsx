import { useEffect, useState } from "react";
import type { Table } from "./models/Table";
import { getTables } from "./services/table.service";
import Floor from "./components/Floor";
import { handleError } from "./utils/errors";
import { getReservedTables } from "./services/reservation.service";
import ReservationForm from "./components/ReservationForm";
import { getRecommendedTable } from "./services/recommend.service";
import type { RecommendRequest } from "./models/RecommendRequest";
import type { FormErrors } from "./models/FormErrors";
import { validateAvailability, validateReservation } from "./utils/validations";
import { Alert } from "@mui/material";

function App() {
  const [tables, setTables] = useState<Table[]>([]);
  const [bookedTables, setBookedTables] = useState<number[]>([]);
  const [reservationForm, setReservationForm] = useState<ReservationForm>({
    partySize: 0,
    date: "",
    time: "",
    customerName: "",
    phoneNumber: "",
    userPreferences: {},
  });

  const [recommededTable, setRecommendedTable] = useState<number | null>(null);
  const [selectedTable, setSelectedTable] = useState<number | null>(null);

  const [error, setError] = useState<string | null>(null);
  const [formErrors, setFormErrors] = useState<FormErrors>({});
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
    e.preventDefault();
    setLoading(true);
    setError(null);
    setFormErrors({});

    const errors = validateAvailability(reservationForm);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      console.log(errors);
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

  const handleConfirmReservation = () => {
    setLoading(true);
    setError(null);
    setFormErrors({});

    const errors = validateReservation(reservationForm, selectedTable);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      console.log(errors);
      return;
    }

    console.log("send it");
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
      <Floor
        tables={tables}
        booked={bookedTables}
        setSelectedTable={handleTableClick}
        selectedTable={selectedTable}
        recommendedTable={recommededTable}
      />
    </>
  );
}

export default App;
