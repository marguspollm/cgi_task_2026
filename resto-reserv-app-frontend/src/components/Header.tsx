import {
  AppBar,
  Box,
  Button,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemText,
  Toolbar,
} from "@mui/material";
import { useState } from "react";
import { Link } from "react-router";

function Header() {
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = (open: boolean) => () => {
    setMenuOpen(open);
  };

  const mobileMenu = (
    <Box sx={{ width: 260 }} role="presentation" onClick={toggleMenu(false)}>
      <Divider />

      <List>
        <ListItemButton component={Link} to="/">
          <ListItemText primary="Reservation" />
        </ListItemButton>
        <ListItemButton component={Link} to="/admin/reservations">
          <ListItemText primary="Admin view" />
        </ListItemButton>
      </List>
    </Box>
  );

  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        borderBottom: "1px solid",
        borderColor: "divider",
      }}
    >
      <Toolbar sx={{ minHeight: 48, px: 2 }}>
        <Box sx={{ display: { xs: "none", md: "flex" }, gap: 2, flexGrow: 1 }}>
          <Button component={Link} to="/" color="inherit">
            Make a Reservation
          </Button>
          <Button component={Link} to="/admin/reservations" color="inherit">
            Admin view
          </Button>
        </Box>

        <Box sx={{ display: { xs: "flex", md: "none" } }}>
          <IconButton color="inherit" onClick={toggleMenu(true)}>
            ☰
          </IconButton>
        </Box>

        <Drawer anchor="left" open={menuOpen} onClose={toggleMenu(false)}>
          {mobileMenu}
        </Drawer>
      </Toolbar>
    </AppBar>
  );
}

export default Header;
