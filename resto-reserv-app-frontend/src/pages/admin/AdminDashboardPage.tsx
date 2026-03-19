import {
  Box,
  Container,
  Drawer,
  List,
  ListItemButton,
  ListItemText,
  Stack,
  Toolbar,
  Typography,
} from "@mui/material";
import { useState } from "react";
import { Link } from "react-router";
import { Outlet } from "react-router";

/**
 * Admin dashboard page
 *
 * Admin page with menu links to pages - Floor plan, Reservations table
 */
function AdminDashboardPage() {
  const [selectedIndex, setSelectedIndex] = useState(0);

  const handleListItemClick = (
    _: React.MouseEvent<HTMLAnchorElement, MouseEvent>,
    index: number,
  ) => {
    setSelectedIndex(index);
  };

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
          <ListItemButton
            component={Link}
            to="/admin/reservations"
            selected={selectedIndex === 1}
            onClick={event => handleListItemClick(event, 1)}
          >
            <ListItemText primary="Reservations" />
          </ListItemButton>

          <ListItemButton
            component={Link}
            to="/admin/floor"
            selected={selectedIndex === 2}
            onClick={event => handleListItemClick(event, 2)}
          >
            <ListItemText primary="Floor edit" />
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
          <Container>
            <Stack spacing={3}>
              <Outlet />
            </Stack>
          </Container>
        </Box>
      </Box>
    </Box>
  );
}

export default AdminDashboardPage;
