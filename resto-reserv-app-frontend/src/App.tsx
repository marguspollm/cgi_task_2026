import { useEffect, useState } from "react";
import "./App.css";
import type { Table } from "./models/Table";
import { getTables } from "./services/table.service";
import Floor from "./components/Floor";
import { handleError } from "./utils/errors";
import { getReservedTables } from "./services/reservation.service";

function App() {
  const [tables, setTables] = useState<Table[]>([]);
  const [bookedTables, setBookedTables] = useState<number[]>([]);
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

  return (
    <>
      <Floor tables={tables} booked={bookedTables} />
    </>
  );
}

export default App;
