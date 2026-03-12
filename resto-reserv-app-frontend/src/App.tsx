import { useEffect, useState } from "react";
import type { Table } from "./models/Table";
import { getTables } from "./services/table.service";
import Floor from "./components/Floor";
import { handleError } from "./utils/errors";
import { getReservedTables } from "./services/reservation.service";

function App() {
  const [tables, setTables] = useState<Table[]>([]);
  const [bookedTables, setBookedTables] = useState<number[]>([]);

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

  return (
    <>
      <Floor
        tables={tables}
        booked={bookedTables}
        setSelectedTable={handleTableClick}
        selectedTable={selectedTable}
      />
    </>
  );
}

export default App;
