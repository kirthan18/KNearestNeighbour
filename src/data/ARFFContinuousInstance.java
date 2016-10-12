package data;

/**
 * Created by kirthanaaraghuraman on 10/12/16.
 */
public class ARFFContinuousInstance implements Comparable<ARFFContinuousInstance>{
    public int mInstanceOrdinal = -1;

    public double mInstanceValue = 0.0;

    public ARFFContinuousInstance(int instanceOrdinal, double instanceValue) {
        this.mInstanceOrdinal = instanceOrdinal;
        this.mInstanceValue = instanceValue;
    }

    @Override
    public int compareTo(ARFFContinuousInstance continuousInstance) {
        if (this.mInstanceValue == continuousInstance.mInstanceValue) {
            return 0;
        } else if (this.mInstanceValue > continuousInstance.mInstanceValue) {
            return 1;
        } else {
            return -1;
        }
    }
}
