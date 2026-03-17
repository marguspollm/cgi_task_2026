import { Box, Paper, Tooltip, Typography } from "@mui/material";
import type { TableAttribute } from "../models/TableAttribute";
import type { MovabelTable } from "../models/MovableTable";
import type { Table } from "../models/Table";

type FloorProps = {
  tables: MovabelTable[] | Table[];
  booked?: number[];
  selectedTableId?: number | null;
  setSelectedTable?: (id: number) => void;
  recommendedTableId?: number | null;
  onDragOver?: (e: React.DragEvent<HTMLDivElement>) => void;
  onDrop?: (e: React.DragEvent<HTMLDivElement>) => void;
  onDropDelete?: (e: React.DragEvent<HTMLDivElement>) => void;
  mode: "view" | "edit";
};

function Floor({
  tables,
  booked,
  selectedTableId,
  setSelectedTable,
  recommendedTableId,
  onDragOver,
  onDrop,
  onDropDelete,
  mode,
}: FloorProps) {
  const isEdit = mode === "edit";
  const isBooked = (id: number) => booked?.includes(id);

  const getTableAttribute = (attr: TableAttribute[]) => {
    const str: string[] = [];
    if (attr.includes("WINDOW")) str.push("Window table");
    if (attr.includes("EASY_ACCESSIBLE")) str.push("Easily accessible");
    if (attr.includes("PRIVATE")) str.push("Private table");
    if (attr.includes("NEAR_KIDS_AREA")) str.push("Near kids area");
    return str.join("\r\n");
  };

  return (
    <Box>
      <Paper
        id="floor"
        elevation={3}
        sx={{
          position: "relative",
          height: 400,
          bgcolor: "background.default",
          border: theme => `1px dashed ${theme.palette.divider}`,
          borderRadius: 2,
          overflow: "hidden",
        }}
        onDragOver={isEdit ? onDragOver : undefined}
        onDrop={isEdit ? onDrop : undefined}
      >
        {tables.map(table => {
          const bookedState = table.id && isBooked(table.id);
          const selected = selectedTableId === table.id;
          const recommended = recommendedTableId === table.id;
          const isNew = "new" in table && table.new;

          return (
            <Tooltip
              key={table.id}
              arrow
              title={
                <>
                  <Typography variant="caption" display="block">
                    Table #{table.id}
                  </Typography>
                  <Typography variant="caption" display="block">
                    Capacity: {table.capacity}
                  </Typography>
                  {table.attributes && (
                    <Typography variant="caption" display="block">
                      {getTableAttribute(table.attributes)}
                    </Typography>
                  )}
                  {bookedState && (
                    <Typography variant="caption" color="error">
                      Booked
                    </Typography>
                  )}
                </>
              }
            >
              <Box
                key={table.id}
                draggable={isEdit}
                onDragStart={
                  isEdit
                    ? e =>
                        table.id &&
                        e.dataTransfer.setData("id", table.id.toString())
                    : undefined
                }
                onClick={() => {
                  if (!isEdit && !bookedState && setSelectedTable && table.id) {
                    setSelectedTable(table.id);
                  }
                }}
                sx={{
                  position: "absolute",
                  top: `${(table.locationY / 650) * 100}%`,
                  left: `${(table.locationX / 650) * 100}%`,
                  transform: "translate(-50%, -50%)",

                  width: 30,
                  height: 30,
                  borderRadius: "50%",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",

                  cursor: isEdit ? "grab" : "pointer",
                  transition: "all 0.2s ease",

                  bgcolor: bookedState
                    ? "grey.300"
                    : selected
                      ? "primary.dark"
                      : recommended
                        ? "success.main"
                        : "background.paper",

                  color: bookedState ? "text.disabled" : "text.primary",

                  border: theme =>
                    selected
                      ? `3px solid ${theme.palette.primary.light}`
                      : recommended
                        ? `2px solid ${theme.palette.success.light}`
                        : `1px solid ${theme.palette.divider}`,

                  boxShadow: selected ? 6 : "0 2px 6px rgba(0,0,0,0.15)",
                  opacity: bookedState ? 0.6 : 1,

                  "&:hover": {
                    transform: "translate(-50%, -50%) scale(1.08)",
                  },
                }}
              >
                <Typography variant="body2" fontWeight={700}>
                  {isNew ? "NEW" : table.id}
                </Typography>

                {isEdit && (
                  <>
                    <Typography
                      variant="caption"
                      display="block"
                      sx={{ opacity: 0.8 }}
                    >
                      {table.capacity}
                    </Typography>
                  </>
                )}
              </Box>
            </Tooltip>
          );
        })}
      </Paper>

      {isEdit && (
        <Paper
          elevation={0}
          sx={{
            mt: 2,
            p: 2,
            textAlign: "center",
            bgcolor: "error.lighter",
            color: "error.main",
            border: theme => `1px dashed ${theme.palette.error.main}`,
            borderRadius: 2,
          }}
          onDragOver={onDragOver}
          onDrop={onDropDelete}
        >
          <Typography variant="body2" fontWeight={600}>
            Drag here to delete table
          </Typography>
        </Paper>
      )}
    </Box>
  );
}

export default Floor;
