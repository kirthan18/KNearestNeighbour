package data;

/**
 * Created by kirthanaaraghuraman on 10/11/16.
 */
public class ARFFClass {
    public int mNoOfClasses;

    public String[] mClassLabels;

    public boolean mIsClassNumeric;

    public ARFFClass(String[] classLabels, boolean isClassNumeric) {
        this.mIsClassNumeric = isClassNumeric;
        if (classLabels != null) {
            this.mNoOfClasses = classLabels.length;
            this.mClassLabels = classLabels;
        } else {
            this.mClassLabels = null;
            this.mNoOfClasses = -1;
        }
    }
}
