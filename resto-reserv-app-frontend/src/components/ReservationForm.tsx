import {
  Button,
  Checkbox,
  FormControlLabel,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import type { FormErrors } from "../models/FormErrors";
import type { ReservationForm } from "../models/ReservationForm";

type ReservationFormProps = {
  form: ReservationForm;
  selectedTable: number | null;
  errors: FormErrors;
  loading: boolean;
  checkAvailability: (e: React.SubmitEvent<HTMLFormElement>) => void;
  confirmReservation: () => void;
  formChange: (name: string, value: unknown) => void;
  formPreferenceChange: (name: string, value: unknown) => void;
};

function ReservationForm({
  form,
  selectedTable,
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
            <Grid>
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

            <Grid>
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

            <Grid>
              <TextField
                label="Time:"
                type="time"
                value={form.time}
                fullWidth
                required
                slotProps={{
                  inputLabel: { shrink: true },
                }}
                onChange={e => formChange("time", e.target.value)}
                error={!!errors.time}
                helperText={errors.time}
              />
            </Grid>
          </Grid>

          <Typography>Preferences:</Typography>

          <Grid container>
            <Grid>
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
            <Grid>
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
            <Grid>
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
            <Grid>
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

          <Button type="submit" variant="contained">
            Check availability
          </Button>

          {selectedTable && (
            <>
              <Typography>Customer information:</Typography>
              <Grid container>
                <Grid>
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
                <Grid>
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
