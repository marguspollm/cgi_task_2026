import { MenuItem, TextField } from "@mui/material";
import { generateTimeSlots } from "../utils/constants";
import type { FormErrors } from "../models/FormErrors";

type InputTimeSlotsProps = {
  value?: string;
  onChange: (name: string, value: unknown) => void;
  errors?: FormErrors;
  fullWidth: boolean;
};

function InputTimeSlots({
  value,
  onChange,
  errors,
  fullWidth,
}: InputTimeSlotsProps) {
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
      sx={{ minWidth: "200px" }}
    >
      <MenuItem key={""} value={""}>
        All times
      </MenuItem>
      {generateTimeSlots().map(slot => (
        <MenuItem key={slot} value={slot}>
          {slot}
        </MenuItem>
      ))}
    </TextField>
  );
}

export default InputTimeSlots;
