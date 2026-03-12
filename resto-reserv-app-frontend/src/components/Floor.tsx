import type { Table } from "../models/Table";

type FloorProps = {
  tables: Table[];
  booked: number[];
  selectedTable?: number;
  setSelectedTable?: () => void;
  recommendedTable?: number;
};

function Floor({
  tables,
  booked,
  selectedTable,
  setSelectedTable,
  recommendedTable,
}: FloorProps) {
  return (
    <>
      {tables.map(table => {
        return <div>{table.id}</div>;
      })}
    </>
  );
}

export default Floor;
