import { Autocomplete, TextField } from "@mui/material";
import { useEffect, useState } from "react";
import type { TableAttribute } from "../models/TableAttribute";
import { getTableAttributes } from "../services/table.service";

type TableAttributesSelectProps = {
  onSelectAttribute: (value: TableAttribute[]) => void;
  label: string;
};

function TableAttributesSelect({
  onSelectAttribute,
  label,
}: TableAttributesSelectProps) {
  const [attributes, setAttributes] = useState<TableAttribute[]>([]);

  useEffect(() => {
    const fetchAttributes = async () => {
      try {
        const data = await getTableAttributes();
        setAttributes(data);
      } catch (error) {
        console.log(error);
      }
    };

    fetchAttributes();
  }, []);

  return (
    <Autocomplete
      multiple
      id="table-attributes"
      options={attributes}
      getOptionLabel={option => option}
      renderInput={params => (
        <TextField {...params} variant="standard" label={label} />
      )}
      onChange={(_, newValue) => onSelectAttribute(newValue)}
    />
  );
}

export default TableAttributesSelect;
