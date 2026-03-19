import {
  Alert,
  Box,
  CircularProgress,
  Divider,
  Grid,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import type { ReservationResponse } from "../../models/ReservationResponse";
import type { ReservationFilters } from "../../models/ReservationFilters";
import { getReservations } from "../../services/reservation.service";
import { handleError } from "../../utils/errors";
import type { Pageable } from "../../models/Pageable";
import TimeSelect from "../../components/TimeSelect";

/**
 * Admin page for Reservations
 *
 * Admin page for filtering reservations by customer name, date or time.
 * Displays the reservations table with filters
 */
function AdminReservationsPage() {
  const [filters, setFilters] = useState<ReservationFilters>({
    page: 0,
    date: "",
    time: "",
    customerName: "",
    size: 25,
  });
  const [response, setResponse] =
    useState<Pageable<ReservationResponse> | null>(null);

  // Debounce solution with help of Gemini AI
  const [nameInput, setNameInput] = useState("");

  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        setLoading(true);
        const data = await getReservations(filters);
        setResponse(data);
      } catch (error) {
        handleError(error, setError);
      } finally {
        setLoading(false);
      }
    };

    fetchReservations();
  }, [filters]);

  // Debounce solution with help of AI
  useEffect(() => {
    const timer = setTimeout(() => {
      setFilters(prev => ({
        ...prev,
        customerName: nameInput.trim(),
        page: 0,
      }));
    }, 500);

    return () => clearTimeout(timer);
  }, [nameInput]);

  const handleInputChange = (name: string, value: unknown) => {
    if (name === "customerName") {
      // Debounce with help of AI
      setNameInput(value as string);
    } else {
      setFilters(prev => ({ ...prev, [name]: value, page: 0 }));
    }
  };

  const handlePageChange = (_: unknown, newPage: number) => {
    setFilters(prev => ({ ...prev, page: newPage }));
  };

  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setFilters(p => ({
      ...p,
      size: parseInt(event.target.value, 10),
      page: 0,
    }));
  };

  return (
    <>
      {error && <Alert severity="error">{error}</Alert>}

      <Box
        sx={{
          mb: 3,
        }}
      >
        <Typography variant="h4" gutterBottom>
          Reservations Table
        </Typography>
        <Divider sx={{ mt: 1, mb: 4 }} />
        <Grid container spacing={2} alignItems="left">
          <Grid size={{ xs: 12, sm: "auto" }}>
            <TextField
              label="Date"
              type="date"
              value={filters.date}
              onChange={e => handleInputChange("date", e.target.value)}
              slotProps={{
                inputLabel: { shrink: true },
              }}
            />
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <TimeSelect
              value={filters.time}
              onChange={handleInputChange}
              fullWidth={true}
              mode={"admin"}
            />
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <TextField
              label="Name"
              type="customerName"
              value={nameInput}
              onChange={e => handleInputChange("customerName", e.target.value)}
              slotProps={{
                inputLabel: { shrink: true },
              }}
            />
          </Grid>
        </Grid>
      </Box>
      {loading ? (
        <>
          <CircularProgress size="3rem" />
        </>
      ) : (
        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Phone</TableCell>
                <TableCell>Table</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Time</TableCell>
                <TableCell>Party Size</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {response?.content.length === 0 && (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    <Typography>No matching reservations found</Typography>
                  </TableCell>
                </TableRow>
              )}
              {response?.content.map((r: ReservationResponse) => (
                <TableRow key={r.id}>
                  <TableCell>{r.customerName}</TableCell>
                  <TableCell>{r.phoneNumber}</TableCell>
                  <TableCell>{r.tableId}</TableCell>
                  <TableCell>{r.date}</TableCell>
                  <TableCell>{r.time}</TableCell>
                  <TableCell>{r.partySize}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <TablePagination
            component="div"
            count={response?.page.totalElements || 0}
            page={filters.page}
            onPageChange={handlePageChange}
            rowsPerPage={filters.size}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </Paper>
      )}
    </>
  );
}

export default AdminReservationsPage;
