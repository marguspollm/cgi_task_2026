import { useEffect, useState } from "react";
import {
  deleteTable,
  getTables,
  saveTables,
} from "../../services/table.service";
import type { Table } from "../../models/Table";
import type { MovabelTable } from "../../models/MovableTable";
import {
  Stack,
  Paper,
  Typography,
  TextField,
  Button,
  Alert,
  Container,
  Snackbar,
} from "@mui/material";
import { handleError } from "../../utils/errors";
import Floor from "../../components/Floor";
import { validateNewTable } from "../../utils/validations";
import type { NewTableErrors } from "../../models/FormErrors";
import type { TableAttribute } from "../../models/TableAttribute";
import TableAttributesSelect from "../../components/TableAttributesSelect";

function AdminFloorEditor() {
  const [tables, setTables] = useState<MovabelTable[]>([]);
  const [originalTables, setOriginalTables] = useState<MovabelTable[]>([]);

  const [newCapacity, setNewCapacity] = useState(0);
  const [newAttributes, setNewAttributes] = useState<TableAttribute[]>([]);

  const [saveDisabled, setSaveDisabled] = useState(true);

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState<string | null>(null);
  const [formErrors, setFormErrors] = useState<NewTableErrors | null>(null);

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
        setOriginalTables(movableTables);
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
    setSaveDisabled(false);
    const dragTable = JSON.parse(e.dataTransfer.getData("table"));
    const containerRect = e.currentTarget.getBoundingClientRect();

    const mouseX = e.clientX - containerRect.left;
    const mouseY = e.clientY - containerRect.top;

    const newX = (mouseX / containerRect.width) * 650;
    const newY = (mouseY / containerRect.height) * 650;

    setTables(prev =>
      prev.map(tabel => {
        if (tabel.id !== dragTable.id) return tabel;
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

    setFormErrors(null);
    const errors = validateNewTable(newCapacity);

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);
      return;
    }

    const floorElement = document.getElementById("floor");
    if (!floorElement) return;

    setSaveDisabled(false);

    const containerRect = floorElement.getBoundingClientRect();
    const newX = containerRect.width / 2;
    const newY = containerRect.height / 2;

    const newTable = {
      id: Date.now(),
      locationX: newX,
      locationY: newY,
      capacity: newCapacity,
      attributes: newAttributes,
      new: true,
    };
    setTables(prev => [...prev, newTable]);
    setNewCapacity(0);
    setNewAttributes([]);
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
      const movableTables = getMovableTables(data);
      setTables(movableTables);
      setOriginalTables(movableTables);
      setNewAttributes([]);
      showSnackbar("Tables saved!");
    } catch (error) {
      handleError(error, setError);
      setTables([...originalTables]);
      setSaveDisabled(true);
    } finally {
      setLoading(false);
    }
  };

  const showSnackbar = (message: string) => {
    setSuccessMessage(message);
    setSnackbarOpen(true);
  };

  return (
    <>
      {error && (
        <Alert severity="warning" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Container sx={{ py: 3 }}>
        <Stack spacing={3}>
          <Typography sx={{ fontWeight: 700, textAlign: "center" }}>
            Table Layout Editor
          </Typography>

          <Floor
            mode="edit"
            tables={tables}
            onDragOver={onDragOver}
            onDrop={onDrop}
            onDropDelete={onDropDelete}
          />
          <Button
            variant="contained"
            onClick={handleSave}
            loading={loading}
            disabled={saveDisabled}
            fullWidth
            color="success"
          >
            Save
          </Button>

          <Paper elevation={3} sx={{ p: 3, maxWidth: 500 }}>
            <Stack spacing={2}>
              <TextField
                label="Capacity"
                type="number"
                value={newCapacity}
                onChange={e => setNewCapacity(Number(e.target.value))}
                fullWidth
                error={!!formErrors?.capacity}
                helperText={formErrors?.capacity}
              />

              <TableAttributesSelect
                onSelectAttribute={setNewAttributes}
                label="Add table attributes"
                values={newAttributes}
              />

              <Stack direction="row" spacing={2}>
                <Button
                  variant="contained"
                  onClick={handleAddTable}
                  fullWidth
                  loading={loading}
                >
                  Add Table
                </Button>
              </Stack>
            </Stack>
          </Paper>
        </Stack>
      </Container>
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={() => setSnackbarOpen(false)}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity="success"
          variant="filled"
          sx={{ width: "100%" }}
        >
          {successMessage}
        </Alert>
      </Snackbar>
    </>
  );
}

export default AdminFloorEditor;
