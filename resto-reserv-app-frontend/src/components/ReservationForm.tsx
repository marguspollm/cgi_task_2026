import type { ReservationForm } from "../models/ReservationForm";

type ReservationFormProps = {
  form: ReservationForm;
  checkAvailability: (e: React.SubmitEvent<HTMLFormElement>) => void;
  formChange: (name: string, value: unknown) => void;
  formPreferenceChange: (name: string, value: unknown) => void;
};

function ReservationForm({
  form,
  checkAvailability,
  formChange,
  formPreferenceChange,
}: ReservationFormProps) {
  return (
    <form onSubmit={checkAvailability}>
      <label>
        Name:
        <input
          type="text"
          value={form.customerName}
          onChange={e => formChange("customerName", e.target.value)}
          required
        />
      </label>
      <label>
        Phone number:
        <input
          type="text"
          value={form.phoneNumber}
          onChange={e => formChange("phoneNumber", e.target.value)}
          required
        />
      </label>

      <label>
        Party size:
        <input
          type="number"
          value={form.partySize}
          onChange={e => formChange("partySize", e.target.value)}
          required
        />
      </label>
      <label>
        Select date:
        <input
          type="date"
          value={form.date}
          onChange={e => formChange("date", e.target.value)}
          required
        />
      </label>
      <label>
        Select time:
        <input
          type="time"
          step={300}
          value={form.time}
          onChange={e => formChange("time", e.target.value)}
          required
        />
      </label>

      <label>
        <input
          type="checkbox"
          checked={form.userPreferences.isWindow || false}
          onChange={e => formPreferenceChange("isWindow", e.target.checked)}
        />
        Near window
      </label>
      <label>
        <input
          type="checkbox"
          checked={form.userPreferences.isPrivate || false}
          onChange={e => formPreferenceChange("isPrivate", e.target.checked)}
        />
        Private area
      </label>
      <label>
        <input
          type="checkbox"
          checked={form.userPreferences.isNearKidsArea || false}
          onChange={e =>
            formPreferenceChange("isNearKidsArea", e.target.checked)
          }
        />
        Near kids area
      </label>
      <label>
        <input
          type="checkbox"
          checked={form.userPreferences.isEasyAccess || false}
          onChange={e => formPreferenceChange("isEasyAccess", e.target.checked)}
        />
        Easily accessible
      </label>
      <button type="submit">Check availability</button>
    </form>
  );
}

export default ReservationForm;
