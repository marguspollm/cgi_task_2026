import { Container, Box, Typography, Stack, Button } from "@mui/material";
import { Link, useNavigate } from "react-router";

function NotFound() {
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box
        minHeight="80vh"
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        textAlign="center"
      >
        <Typography variant="h1" fontWeight={700} color="primary" gutterBottom>
          404
        </Typography>

        <Typography variant="h5" fontWeight={600} gutterBottom>
          Page not found
        </Typography>

        <Typography variant="body1" color="text.secondary" mb={4}>
          This page doesn't exist
        </Typography>

        <Stack direction="row" spacing={2}>
          <Button variant="contained" component={Link} to="/">
            Go Home
          </Button>

          <Button variant="outlined" onClick={() => navigate(-1)}>
            Go Back
          </Button>
        </Stack>
      </Box>
    </Container>
  );
}

export default NotFound;
