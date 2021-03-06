package data;

/**
 * Created by kirthanaaraghuraman on 10/12/16.
 */
public class Distance implements Comparable<Distance>{
    private double mDistance;
    private int mInstanceOrdinal;
    private String mInstanceClass;

    public Distance(double distanceValue, int instanceOrdinal, String instanceClass){
        this.mDistance = distanceValue;
        this.mInstanceOrdinal = instanceOrdinal;
        this.mInstanceClass = instanceClass;
    }

    public double getDistance() {
        return mDistance;
    }

    public int getInstanceOrdinal() {
        return mInstanceOrdinal;
    }

    public String getInstanceClass() {
        return mInstanceClass;
    }

    @Override
    public int compareTo(Distance dist) {
        if (this.mDistance == dist.mDistance) {
            return 0;
        } else if (this.mDistance > dist.mDistance) {
            return 1;
        } else {
            return -1;
        }
    }
}
