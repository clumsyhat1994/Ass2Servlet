package Shapes;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class LiftRide {
    @SerializedName("time")
    private Integer time = null;
    @SerializedName("liftID")
    private Integer liftID = null;

    public LiftRide() {
    }

    public Integer getTime() {
        return this.time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLiftID() {
        return this.liftID;
    }

    public void setLiftID(Integer liftID) {
        this.liftID = liftID;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            LiftRide liftRide = (LiftRide)o;
            return Objects.equals(this.time, liftRide.time) && Objects.equals(this.liftID, liftRide.liftID);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.time, this.liftID});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LiftRide {\n");
        sb.append("    time: ").append(this.toIndentedString(this.time)).append("\n");
        sb.append("    liftID: ").append(this.toIndentedString(this.liftID)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}