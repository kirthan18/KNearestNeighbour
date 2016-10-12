package data;

/**
 * Created by kirthanaaraghuraman on 10/11/16.
 */
public class ARFFClass {
    public int mNoOfClasses;

    public String[] mClassLabels;

    public ARFFClass(String[] classLabels) {
        if (classLabels != null) {
            this.mNoOfClasses = classLabels.length;
            this.mClassLabels = classLabels;
        }
    }
}
