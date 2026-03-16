import { Route, Routes } from "react-router";
import NotFound from "./pages/NotFound";
import ReservationPage from "./pages/ReservationPage";
import { Box } from "@mui/material";
import Header from "./components/Header";
import AdminDashboardPage from "./pages/admin/AdminDashboardPage";
import AdminFloorEditor from "./pages/admin/AdminFloorEditor";
import AdminReservationsPage from "./pages/admin/AdminReservationsPage";

function App() {
  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <Header />

      <Box sx={{ flexGrow: 1, mt: "65px" }}>
        <Routes>
          <Route path="/" element={<ReservationPage />} />
          <Route path="/admin" element={<AdminDashboardPage />}>
            <Route path="reservations" element={<AdminReservationsPage />} />
            <Route path="floor" element={<AdminFloorEditor />} />
          </Route>

          <Route path="/*" element={<NotFound />} />
        </Routes>
      </Box>
    </Box>
  );
}

export default App;
