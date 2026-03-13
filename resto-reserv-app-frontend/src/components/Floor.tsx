import { Tooltip } from "@mui/material";
import type { Table } from "../models/Table";
import type { TableAttribute } from "../models/TableAttribute";

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
  const getTableAttribute = (attr: TableAttribute) => {
    if (attr === "WINDOW") return "Windw table";
    else if (attr === "EASY_ACCESSIBLE") return "Easily accessible";
    else if (attr === "PRIVATE") return "Private table";
    else if (attr === "NEAR_KIDS_AREA") return "Near kids area";
    else return "";
  };

  return (
    <div className="floor">
      {tables.map((table: Table) => {
        const isBooked = booked.includes(table.id);
        const isRecommended = table.id === recommendedTable;
        const isSelected = selectedTable === table.id;

        const backgroundColor = isSelected
          ? "green"
          : isBooked
            ? "grey"
            : isRecommended
              ? "gold"
              : "lightgreen";

        return (
          <Tooltip
            key={table.id}
            title={
              <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
                {isRecommended && <strong>⭐ Recommended ⭐</strong>}
                <span>Table: {table.id}</span>
                <span>Seats: {table.capacity}</span>
                <span>Status: {isBooked ? "Booked" : "Available"}</span>
                {table.attribute && (
                  <span>{getTableAttribute(table.attribute)}</span>
                )}
              </div>
            }
          >
            <div
              key={table.id}
              onClick={() => setSelectedTable(table.id)}
              className="table"
              style={{
                top: `${(table.locationY / 650) * 100}%`,
                left: `${(table.locationX / 650) * 100}%`,
                backgroundColor: backgroundColor,
                color: isBooked ? "darkgrey" : "darkgreen",
              }}
            >
              {table.capacity}
            </div>
          </Tooltip>
        );
      })}
    </div>
  );
}

export default Floor;
