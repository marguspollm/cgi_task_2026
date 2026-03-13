import { useEffect, useState } from "react";
import type { Table } from "./models/Table";
import { getTables } from "./services/table.service";
import Floor from "./components/Floor";
import { handleError } from "./utils/errors";
import { getReservedTables } from "./services/reservation.service";
import ReservationForm from "./components/ReservationForm";
import { getRecommendedTable } from "./services/recommend.service";
import type { RecommendRequest } from "./models/RecommendRequest";

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

    const payload: RecommendRequest = {
      partySize: reservationForm.partySize,
      date: reservationForm.date,
      time: reservationForm.time,
      userPreferences: reservationForm.userPreferences,
    };

    try {
      const recData = await getRecommendedTable(payload);
      const bookedData = await getReservedTables(
        reservationForm.date,
        reservationForm.time,
      );
      setRecommendedTable(recData);
      setBookedTables(bookedData);
    } catch (error) {
      handleError(error, setError);
    }
  };

  const handleFormChange = (name: string, value: unknown) => {
    setReservationForm(prev => ({ ...prev, [name]: value }));
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
      <ReservationForm
        form={reservationForm}
        checkAvailability={handleCheckAvailability}
        formChange={handleFormChange}
        formPreferenceChange={handleFormPreferenceChange}
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
