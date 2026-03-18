import {
  Button,
  Divider,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import type { FormErrors } from "../models/FormErrors";
import type { ReservationFormState } from "../models/ReservationFormState";
import TableAttributesSelect from "./TableAttributesSelect";
import type { TableAttribute } from "../models/TableAttribute";
import TimeSelect from "./TimeSelect";

type ReservationFormProps = {
  form: ReservationFormState;
  selectedTableId: number | null;
  errors: FormErrors;
  loading: boolean;
  checkAvailability: (e: React.SubmitEvent<HTMLFormElement>) => void;
  createReservation: () => void;
  formChange: (name: string, value: unknown) => void;
  formPreferenceChange: (value: TableAttribute[]) => void;
  tablePreferences: TableAttribute[];
};

function ReservationForm({
  form,
  selectedTableId,
  errors,
  loading,
  checkAvailability,
  createReservation,
  formChange,
  formPreferenceChange,
  tablePreferences,
}: ReservationFormProps) {
  return (
    <Paper sx={{ p: 3 }}>
      <form onSubmit={checkAvailability} noValidate>
        <Stack spacing={3}>
          <Grid container spacing={2}>
            <Grid size={{ xs: 12, md: 5 }}>
              <TextField
                label="Date:"
                type="date"
                value={form.date}
                fullWidth
                required
                slotProps={{
                  inputLabel: { shrink: true },
                }}
                onChange={e => formChange("date", e.target.value)}
                error={!!errors.date}
                helperText={errors.date}
              />
            </Grid>

            <Grid size={{ xs: 12, md: 5 }}>
              <TimeSelect
                mode={"user"}
                value={form.time}
                onChange={formChange}
                errors={errors}
                fullWidth={true}
              />
            </Grid>

            <Grid size={{ xs: 12, md: 5 }}>
              <TextField
                label="Party size:"
                type="number"
                value={form.partySize}
                fullWidth
                required
                onChange={e => formChange("partySize", e.target.value)}
                error={!!errors.partySize}
                helperText={errors.partySize}
              />
            </Grid>
          </Grid>

          <Grid container spacing={1}>
            <Grid size={{ xs: 12 }}>
              <TableAttributesSelect
                onSelectAttribute={formPreferenceChange}
                label={"Table preferences"}
                values={tablePreferences}
              />
            </Grid>
          </Grid>

          <Button type="submit" variant="contained" disabled={loading}>
            Check availability
          </Button>

          {selectedTableId && (
            <>
              <Divider />
              <Typography>Customer information:</Typography>

              <Grid container spacing={2}>
                <Grid size={{ xs: 12, md: 6 }}>
                  <TextField
                    label="Customer name"
                    value={form.customerName}
                    fullWidth
                    required
                    onChange={e => formChange("customerName", e.target.value)}
                    error={!!errors.customerName}
                    helperText={errors.customerName}
                  />
                </Grid>
                <Grid size={{ xs: 12, md: 6 }}>
                  <TextField
                    label="Phone number"
                    value={form.phoneNumber}
                    fullWidth
                    required
                    onChange={e => formChange("phoneNumber", e.target.value)}
                    error={!!errors.phoneNumber}
                    helperText={errors.phoneNumber}
                  />
                </Grid>
              </Grid>
              <Button
                onClick={createReservation}
                variant="contained"
                color="success"
                loading={loading}
                disabled={!form.customerName && !form.phoneNumber}
              >
                Create reservation
              </Button>
            </>
          )}
        </Stack>
      </form>
    </Paper>
  );
}

export default ReservationForm;
