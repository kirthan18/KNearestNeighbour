package kNN;

import ARFFReader.ARFFReader;
import data.ARFFContinuousInstance;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kirthanaaraghuraman on 10/11/16.
 */
public class kNearestNeighbour {

    private static ARFFReader mArffReader;
    private static int k = -1;
    /**
     * ARFFReader instance for training set
     */
    private static ARFFReader mTrainSetArffReader;

    /**
     * ARFFReader instance for test set
     */
    private static ARFFReader mTestSetArffReader;

    /**
     * Parses the given input ARFF File
     *
     * @param filename Name/Path of file to be parsed
     * @return ARFFReader instance
     */
    private static ARFFReader parseARFFFile(String filename) {
        ARFFReader arffReader = ARFFReader.getInstance(filename);
        arffReader.parseARFFFile();
        return arffReader;
    }

    private static class Distance {
        private double mDistance;
        private int mInstanceOrdinal;

        public Distance(double distanceValue, int attributeOrdinal){
            this.mDistance = distanceValue;
            this.mInstanceOrdinal = attributeOrdinal;
        }
    }

    private static Distance[] computeEuclideanDistance(ArrayList<ARFFContinuousInstance> instance,
                                                 ArrayList<ArrayList<ARFFContinuousInstance>> trainSetData) {
        Distance[] distance = new Distance[trainSetData.size()];
        double sumOfSquares = 0.0;

        for(int i = 0; i < trainSetData.size(); i++) {
            ArrayList<ARFFContinuousInstance> trainInstance = trainSetData.get(i);
            for (int j = 0; j < instance.size(); j++) {
                sumOfSquares += Math.pow((instance.get(j).mInstanceValue - trainInstance.get(j).mInstanceValue),2);
            }
            distance[i] = new Distance(Math.sqrt(sumOfSquares), i);
        }
        return distance;
    }

    private static void classifyTestSet(ArrayList<ArrayList<ARFFContinuousInstance>> trainSetData,
                                        ArrayList<ArrayList<ARFFContinuousInstance>> testSetData) {
        // For each instance in test set, compute the distance to every instance in train set.
        // Sort the distances and find the k nearest neighbours
        // Compute the majority class among the k nearest neighbours
        // Print it

        for(int i = 0; i < mTestSetArffReader.getDataInstanceList().size(); i++) {
            Distance[] distance = computeEuclideanDistance(testSetData.get(i), trainSetData);
            //TODO - sort based on distance measure
            Arrays.sort(distance);
            //TODO - Find k nearest neighbours
            //TODO - Classify based on majority class
        }
    }

    private static ArrayList<ArrayList<ARFFContinuousInstance>> constructInstanceData(ARFFReader reader) {
        ArrayList<ArrayList<ARFFContinuousInstance>> instanceList = new ArrayList<>();
        ArrayList<ARFFContinuousInstance> instanceAttributeList = new ArrayList<>();
        for(int i = 0; i < reader.getDataInstanceList().size(); i++) {
            String[] instance = reader.getDataInstanceList().get(i);
            for(int j = 0; j < reader.getAttributeList().size(); j++) {
                ARFFContinuousInstance continuousInstance = new ARFFContinuousInstance(i,
                        Double.parseDouble(instance[j]),
                        instance[instance.length - 1]);
                instanceAttributeList.add(continuousInstance);
            }
            instanceList.add(instanceAttributeList);
        }
        return instanceList;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Program usage: knn <training_set> <test_set> <k>");
            return;
        }
        String trainFile = args[0];
        String testFile = args[1];
        k = Integer.parseInt(args[2]);
        mTrainSetArffReader = parseARFFFile(trainFile);
        mTestSetArffReader = parseARFFFile(testFile);

        ArrayList<ArrayList<ARFFContinuousInstance>> trainSetData = constructInstanceData(mTrainSetArffReader);
        ArrayList<ArrayList<ARFFContinuousInstance>> testSetData = constructInstanceData(mTestSetArffReader);
        classifyTestSet(trainSetData, testSetData);

    }


}
