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
  Snackbar,
  Divider,
} from "@mui/material";
import { handleError } from "../../utils/errors";
import Floor from "../../components/Floor";
import { validateNewTable } from "../../utils/validations";
import type { NewTableErrors } from "../../models/FormErrors";
import type { TableAttribute } from "../../models/TableAttribute";
import TableAttributesSelect from "../../components/TableAttributesSelect";

/**
 * Admin page for Tables arrangement on floor plan
 *
 * Admin page for rearranging tabel on restaurant floor, creating new tables and deleting tables
 */
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

  /**
   * Creates new array of all existing tables tha can be moved on the floor
   *
   * @param tables Array of tables from backend
   * @returns Array of tables with extra paramaeters to make them movabel on floor
   */
  function getMovableTables(tables: Table[]): MovabelTable[] {
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

  // Handle drag of given table by updating state
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

  // Handle the table dragged to delete zone and its deletion
  const onDropDelete = async (e: React.DragEvent<HTMLDivElement>) => {
    setError(null);
    if (!e.dataTransfer) return;
    const { id, isNew } = JSON.parse(e.dataTransfer.getData("table"));

    if (!id) return;
    // If table is created and not saved in database then delete from state
    if (isNew) {
      setTables(prev => prev.filter(t => t.id !== id));
      return;
    }

    // If table is in database, then send delete request
    try {
      setLoading(true);

      await deleteTable(id);
      setTables(prev => prev.filter(t => t.id !== id));

      showSnackbar(`Deleted table with id - ${id}`);
    } catch (error) {
      handleError(error, setError);
    } finally {
      setLoading(false);
    }
  };

  // Handles adding a new table with a temporary id and new parameter
  const handleAddTable = (e: React.MouseEvent) => {
    e.preventDefault();

    // Validate the ded table has capacity > 0
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

    // Create new table in the middle of the floor
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

  // Handle the save of new and rearranged floor tables
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
      <Typography variant="h4" gutterBottom>
        Floor tables Editor
      </Typography>
      <Divider sx={{ mt: 1, mb: 4 }} />
      {error && (
        <Alert severity="warning" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
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
