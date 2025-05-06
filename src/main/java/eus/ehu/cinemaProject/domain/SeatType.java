package eus.ehu.cinemaProject.domain;

public enum SeatType {
    NORMAL("-fx-background-color: #666666;"),
    COMFORTABLE("-fx-background-color: #2266cc;"),
    PREMIUM("-fx-background-color: #ea9308;"),
    OCCUPIED("-fx-background-color: #333333;");

    private final String style;

    SeatType(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }


}
