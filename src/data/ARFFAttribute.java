package data;

/**
 * Created by kirthanaaraghuraman on 10/11/16.
 */
public class ARFFAttribute {
    public static final int NOMINAL = 0;

    public static final int NUMERIC = 1;

    public int mAttributeOrdinal = -1;

    public String mAttributeName;

    public int mAttributeType;

    public String[] mAttributeValues;

    public ARFFAttribute(int attributeOrdinal, String attributeName, int attributeType, String[] attributeValues) {
        this.mAttributeOrdinal = attributeOrdinal;
        this.mAttributeName = attributeName;
        this.mAttributeType = attributeType;
        this.mAttributeValues = attributeValues;
    }

    public int getNumberOfAttributeValues() {
        return mAttributeValues.length;
    }
}
