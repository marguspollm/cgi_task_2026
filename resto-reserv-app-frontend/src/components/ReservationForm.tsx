import {
  Button,
  Checkbox,
  Divider,
  FormControlLabel,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import type { FormErrors } from "../models/FormErrors";
import type { ReservationFormState } from "../models/ReservationFormState";
import InputTimeSlots from "./InputTimeSlots";

type ReservationFormProps = {
  form: ReservationFormState;
  selectedTableId: number | null;
  errors: FormErrors;
  loading: boolean;
  checkAvailability: (e: React.SubmitEvent<HTMLFormElement>) => void;
  confirmReservation: () => void;
  formChange: (name: string, value: unknown) => void;
  formPreferenceChange: (name: string, value: unknown) => void;
};

function ReservationForm({
  form,
  selectedTableId,
  errors,
  loading,
  checkAvailability,
  confirmReservation,
  formChange,
  formPreferenceChange,
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
              <InputTimeSlots
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

          <Divider />
          <Typography>Table preferences:</Typography>

          <Grid container spacing={1}>
            <Grid size={{ xs: 12, md: 4 }}>
              <FormControlLabel
                label="Near window"
                control={
                  <Checkbox
                    checked={form.userPreferences.isWindow || false}
                    onChange={e =>
                      formPreferenceChange("isWindow", e.target.checked)
                    }
                  />
                }
              />
            </Grid>
            <Grid size={{ xs: 12, md: 4 }}>
              <FormControlLabel
                label="Private"
                control={
                  <Checkbox
                    checked={form.userPreferences.isPrivate || false}
                    onChange={e =>
                      formPreferenceChange("isPrivate", e.target.checked)
                    }
                  />
                }
              />
            </Grid>
            <Grid size={{ xs: 12, md: 4 }}>
              <FormControlLabel
                label="Near kids area"
                control={
                  <Checkbox
                    checked={form.userPreferences.isNearKidsArea || false}
                    onChange={e =>
                      formPreferenceChange("isNearKidsArea", e.target.checked)
                    }
                  />
                }
              />
            </Grid>
            <Grid size={{ xs: 12, md: 4 }}>
              <FormControlLabel
                label="Easily accessible"
                control={
                  <Checkbox
                    checked={form.userPreferences.isEasyAccess || false}
                    onChange={e =>
                      formPreferenceChange("isEasyAccess", e.target.checked)
                    }
                  />
                }
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
                onClick={confirmReservation}
                variant="contained"
                color="success"
                loading={loading}
                disabled={!form.customerName && !form.phoneNumber}
              >
                Confirm reservation
              </Button>
            </>
          )}
        </Stack>
      </form>
    </Paper>
  );
}

export default ReservationForm;
