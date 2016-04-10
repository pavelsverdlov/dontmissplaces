package svp.com.dontmissplaces.model;

/**
 * Created by Pasha on 4/10/2016.
 */
public class Traffic {
    public static final Movement walking = new Movement(
        new SpeedValue(new Value(0.61f, 1.43f), new Value(67.8f,109.1f), SpeedTypes.Slow),
        new SpeedValue(new Value(1.43f, 1.9f), new Value(109.1f,125.0f), SpeedTypes.Custom),
        new SpeedValue(new Value(1.9f, 2.28f), new Value(125.0f,137.9f), SpeedTypes.Fast)
    );


    public static final class Movement {
        public final SpeedValue slow;
        public final SpeedValue custom;
        public final SpeedValue fast;

        public Movement(SpeedValue slow, SpeedValue custom, SpeedValue fast) {
            this.slow = slow;
            this.custom = custom;
            this.fast = fast;
        }

        public SpeedTypes getSpeedType(double speed, double dis, long timeSpent) {

            return SpeedTypes.Undefined;
        }
    }
    public enum SpeedTypes{
        Undefined, Slow, Custom, Fast
    }
    public static final class SpeedValue {

        //m/s
        public final Value speed;
        //step/min
        public final Value steps;
        public final SpeedTypes type;

        public SpeedValue(Value speed, Value steps, SpeedTypes type) {
            this.speed = speed;
            this.steps = steps;
            this.type = type;
        }
    }
    public static final class Value {
        public final float min;
        public final float max;

        public Value(float min, float max) {
            this.min = min;
            this.max = max;
        }
    }
}
