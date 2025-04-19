package eus.ehu.cinemaProject.domain;

public enum SeatType {
    NORMAL("-fx-background-color: gray;"),
    COMFORTABLE("-fx-background-color: blue;"),
    PREMIUM("-fx-background-color: gold;"),
    OCCUPIED("-fx-background-color: red;"); // Occupied seats in red

    private final String style;

    SeatType(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
