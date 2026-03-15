import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Typography,
} from "@mui/material";
import type { Meal } from "../models/Meal";

type MealsCarouselProps = {
  recommendedMeals: Meal[];
  handlePreoderClick: (id: number) => void;
};

function MealsCarousel({
  recommendedMeals,
  handlePreoderClick,
}: MealsCarouselProps) {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: 2,
      }}
    >
      {recommendedMeals.length !== 0 && (
        <Box>
          <Typography variant="h6" mb={2}>
            Recommended meals for your visit
          </Typography>

          <Box
            sx={{
              display: "flex",
              gap: 2,
              overflowX: "auto",
              pb: 1,
              alignItems: "stretch",
            }}
          >
            {recommendedMeals.map(meal => (
              <Card
                key={meal.id}
                sx={{
                  width: 220,
                  flexShrink: 0,
                  display: "flex",
                  flexDirection: "column",
                }}
              >
                <CardMedia
                  component="img"
                  height="140"
                  image={meal.image}
                  alt={meal.name}
                />

                <CardContent
                  sx={{
                    flexGrow: 1,
                  }}
                >
                  <Typography variant="subtitle1" fontWeight={600}>
                    {meal.name}
                  </Typography>

                  <Typography variant="body2" color="text.secondary">
                    {meal.category}
                  </Typography>

                  <Typography mt={1} fontWeight={400}>
                    €{meal.price}
                  </Typography>
                </CardContent>

                <CardActions sx={{ mt: "auto" }}>
                  <Button
                    size="small"
                    variant="contained"
                    onClick={() => handlePreoderClick(meal.id)}
                  >
                    Pre-order
                  </Button>
                </CardActions>
              </Card>
            ))}
          </Box>
        </Box>
      )}
    </Box>
  );
}

export default MealsCarousel;
