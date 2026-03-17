import { useEffect, useState } from "react";
import { getTables, saveTables } from "../../services/table.service";
import type { Table } from "../../models/Table";
import type { MovabelTable } from "../../models/MovableTable";
import {
  Box,
  Stack,
  Paper,
  Typography,
  TextField,
  Button,
  Alert,
} from "@mui/material";
import { handleError } from "../../utils/errors";

function AdminFloorEditor() {
  const [tables, setTables] = useState<MovabelTable[]>([]);

  const [capacity, setCapacity] = useState(0);
  const [attributes, setAttributes] = useState<string>("");

  const [loading, setLoading] = useState(false);
  const [disableAdd, setDisableAdd] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function getMovableTables(tables: Table[]) {
    return tables.map(table => {
      return {
        id: table.id,
        locationX: table.locationX,
        locationY: table.locationY,
        capacity: table.capacity,
        attributes: table.attributes,
        new: false,
      };
    });
  }

  useEffect(() => {
    const fetchTables = async () => {
      try {
        setLoading(true);
        const data = await getTables();
        const movableTables = getMovableTables(data);
        setTables(movableTables);
      } catch (error) {
        handleError(error, setError);
      } finally {
        setLoading(false);
      }
    };
    fetchTables();
  }, []);

  const onDragOver = (e: React.DragEvent<HTMLDivElement>) => e.preventDefault();

  const onDrop = (e: React.DragEvent<HTMLDivElement>) => {
    if (!e.dataTransfer) return;
    const id = parseInt(e.dataTransfer.getData("id"));
    const containerRect = e.currentTarget.getBoundingClientRect();

    const mouseX = e.clientX - containerRect.left;
    const mouseY = e.clientY - containerRect.top;

    const newX = (mouseX / containerRect.width) * 650;
    const newY = (mouseY / containerRect.height) * 650;

    setTables(prev =>
      prev.map(tabel => {
        if (tabel.id !== id) return tabel;
        return { ...tabel, locationX: newX, locationY: newY };
      }),
    );
  };

  const onDropDelete = (e: React.DragEvent<HTMLDivElement>) => {
    if (!e.dataTransfer) return;

    const id = parseInt(e.dataTransfer.getData("id"));
    setTables(prev => prev.filter(t => t.id !== id));
  };

  const handleAddTable = (e: React.MouseEvent) => {
    e.preventDefault();
    const floorElement = document.querySelector(".floor");
    if (!floorElement) return;
    const containerRect = floorElement.getBoundingClientRect();
    const newX = containerRect.width / 2;
    const newY = containerRect.height / 2;

    const newTable = {
      id: Date.now(),
      locationX: newX,
      locationY: newY,
      capacity: 0,
      attributes: [],
      new: true,
    };
    setTables(prev => [...prev, newTable]);
    setDisableAdd(true);
  };

  const handleSave = async () => {
    try {
      setLoading(true);
      setError(null);
      const payload: Table[] = tables.map(table => {
        const { new: isNew, id, ...rest } = table;
        if (isNew) return { ...rest };
        return { ...rest, id };
      });
      const data = await saveTables(payload);
      setTables(getMovableTables(data));
    } catch (error) {
      handleError(error, setError);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box p={3}>
      {error && <Alert severity="warning">{error}</Alert>}
      <Stack spacing={3}>
        <Paper
          elevation={3}
          sx={{
            position: "relative",
            height: 400,
            bgcolor: "#f5f5f5",
            border: "2px dashed #ccc",
            overflow: "hidden",
          }}
          onDragOver={onDragOver}
          onDrop={onDrop}
        >
          {tables.map(table => (
            <Box
              key={table.id}
              draggable
              onDragStart={e =>
                table.id && e.dataTransfer.setData("id", table.id.toString())
              }
              sx={{
                position: "absolute",
                top: `${(table.locationY / 650) * 100}%`,
                left: `${(table.locationX / 650) * 100}%`,
                transform: "translate(-50%, -50%)",
                bgcolor: "darkgreen",
                color: "white",
                px: 2,
                py: 1,
                borderRadius: 2,
                cursor: "grab",
                width: 40,
                textAlign: "center",
              }}
            >
              <Typography variant="body2" fontWeight="bold">
                {table.new ? "NEW" : `#${table.id}`}
              </Typography>
              <Typography variant="caption">Cap: {table.capacity}</Typography>
              <Typography variant="caption" display="block">
                {table.attributes}
              </Typography>
            </Box>
          ))}
        </Paper>

        <Paper
          elevation={1}
          sx={{
            p: 2,
            textAlign: "center",
            bgcolor: "#ffe6e6",
            border: "2px dashed red",
            color: "red",
          }}
          onDragOver={onDragOver}
          onDrop={onDropDelete}
        >
          Delete Area (drag table here)
        </Paper>

        <Paper elevation={2} sx={{ p: 2 }}>
          <Stack spacing={2}>
            <TextField
              label="Attributes"
              value={attributes}
              onChange={e => setAttributes(e.target.value)}
              fullWidth
            />

            <TextField
              label="Capacity"
              type="number"
              value={capacity}
              onChange={e => setCapacity(Number(e.target.value))}
              fullWidth
            />

            <Stack direction="row" spacing={2}>
              <Button
                variant="contained"
                onClick={handleAddTable}
                disabled={disableAdd}
              >
                Add Table
              </Button>
              <Button variant="outlined" onClick={handleSave} loading={loading}>
                Save
              </Button>
            </Stack>
          </Stack>
        </Paper>
      </Stack>
    </Box>
  );
}

export default AdminFloorEditor;
