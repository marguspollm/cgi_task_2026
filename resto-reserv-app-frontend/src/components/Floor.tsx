import type { Table } from "../models/Table";

type FloorProps = {
  tables: Table[];
  booked: number[];
  selectedTable?: number | null;
  setSelectedTable: (id: number) => void;
  recommendedTable: number | null;
};

function Floor({
  tables,
  booked,
  selectedTable,
  setSelectedTable,
  recommendedTable,
}: FloorProps) {
  return (
    <div className="floor">
      {tables.map((table: Table) => {
        const isBooked = booked.includes(table.id);
        const isRecommended = table.id === recommendedTable;
        const isSelected = selectedTable === table.id;

        return (
          <div
            key={table.id}
            onClick={() => setSelectedTable(table.id)}
            className="table"
            style={{
              top: table.locationY,
              left: table.locationX,
              backgroundColor: isSelected
                ? "green"
                : isBooked
                  ? "grey"
                  : isRecommended
                    ? "gold"
                    : "lightgreen",
            }}
          >
            {table.id}
          </div>
        );
      })}
    </div>
  );
}

export default Floor;
