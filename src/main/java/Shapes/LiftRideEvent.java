package Shapes;

public class LiftRideEvent {
    Integer resortID; // Integer | ID of the resort the skier is at
    String seasonID; // String | ID of the ski season
    String dayID; // String | ID number of ski day in the ski season
    Integer skierID; // Integer | ID of the skier riding the lift
    LiftRide liftRide;

    public LiftRideEvent(Integer resortID, String seasonID, String dayID, Integer skierID, LiftRide liftRide) {
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.liftRide = liftRide;
    }

    public Integer getResortID() {
        return resortID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public Integer getSkierID() {
        return skierID;
    }

    public LiftRide getLiftRide() {
        return liftRide;
    }
}
