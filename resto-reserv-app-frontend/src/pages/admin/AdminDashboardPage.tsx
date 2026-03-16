import {
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemText,
  Toolbar,
  Typography,
} from "@mui/material";
import { Link } from "react-router";
import { Outlet } from "react-router";

function AdminDashboardPage() {
  return (
    <Box sx={{ display: "flex", minHeight: "100vh" }}>
      <Drawer
        variant="permanent"
        sx={{
          width: 250,
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: 250,
            boxSizing: "border-box",
            top: "65px",
            height: `calc(100% - 65px)`,
            borderRight: "1px solid",
            borderColor: "divider",
          },
        }}
      >
        <Toolbar>
          <Typography fontWeight={700}>Admin</Typography>
        </Toolbar>

        <List>
          <ListItemButton component={Link} to="/admin/reservations">
            <ListItemText primary="Reservations" />
          </ListItemButton>

          <ListItemButton component={Link} to="/admin/floor">
            <ListItemText primary="Floor View" />
          </ListItemButton>
        </List>
      </Drawer>

      <Box sx={{ flexGrow: 1 }}>
        <Toolbar />

        <Box
          sx={{
            flexGrow: 1,
            ml: "10px",
          }}
        >
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
}

export default AdminDashboardPage;
