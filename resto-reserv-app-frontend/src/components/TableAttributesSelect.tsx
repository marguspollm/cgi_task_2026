import { Autocomplete, CircularProgress, TextField } from "@mui/material";
import { useEffect, useState } from "react";
import type { TableAttribute } from "../models/TableAttribute";
import { getTableAttributes } from "../services/table.service";
import React from "react";

type TableAttributesSelectProps = {
  onSelectAttribute: (value: TableAttribute[]) => void;
  label: string;
  values: TableAttribute[];
};

function TableAttributesSelect({
  onSelectAttribute,
  label,
  values,
}: TableAttributesSelectProps) {
  const [attributes, setAttributes] = useState<TableAttribute[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchAttributes = async () => {
      try {
        setLoading(true);
        const data = await getTableAttributes();
        setAttributes(data);
      } catch (error) {
        console.log(error);
      } finally {
        setLoading(false);
      }
    };

    fetchAttributes();
  }, []);

  return (
    <Autocomplete
      multiple
      id="table-attributes"
      value={values}
      options={attributes}
      getOptionLabel={option => option}
      renderInput={params => (
        <TextField
          {...params}
          variant="standard"
          label={label}
          slotProps={{
            input: {
              ...params.InputProps,
              endAdornment: (
                <React.Fragment>
                  {loading ? (
                    <CircularProgress color="inherit" size={20} />
                  ) : null}
                  {params.InputProps.endAdornment}
                </React.Fragment>
              ),
            },
          }}
        />
      )}
      onChange={(_, newValue) => onSelectAttribute(newValue)}
      loading={loading}
    />
  );
}

export default TableAttributesSelect;
