import { MenuItem, TextField } from "@mui/material";
import { generateTimeSlots } from "../utils/constants";
import type { FormErrors } from "../models/FormErrors";

type TimeSelectProps = {
  mode: "admin" | "user";
  value?: string;
  onChange: (name: string, value: unknown) => void;
  errors?: FormErrors;
  fullWidth: boolean;
};

function TimeSelect({
  value,
  onChange,
  errors,
  fullWidth,
  mode,
}: TimeSelectProps) {
  return (
    <TextField
      label="Time"
      select
      value={value}
      fullWidth={fullWidth}
      required
      onChange={e => onChange("time", e.target.value)}
      error={!!errors?.time}
      helperText={errors?.time}
      slotProps={{
        select: {
          MenuProps: {
            disableEnforceFocus: true,
          },
        },
      }}
    >
      {mode === "admin" && (
        <MenuItem key={""} value={""}>
          All times
        </MenuItem>
      )}

      {generateTimeSlots().map(slot => (
        <MenuItem key={slot} value={slot}>
          {slot}
        </MenuItem>
      ))}
    </TextField>
  );
}

export default TimeSelect;
