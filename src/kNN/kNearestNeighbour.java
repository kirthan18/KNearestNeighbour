package kNN;

import ARFFReader.ARFFReader;
import data.ARFFContinuousInstance;
import data.Distance;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kirthanaaraghuraman on 10/11/16.
 */
public class kNearestNeighbour {

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

    private static Distance[] computeEuclideanDistance(ArrayList<ARFFContinuousInstance> instance,
                                                       ArrayList<ArrayList<ARFFContinuousInstance>> trainSetData) {
        Distance[] distance = new Distance[trainSetData.size()];

        for(int i = 0; i < trainSetData.size(); i++) {
            double sumOfSquares = 0.0;
            ArrayList<ARFFContinuousInstance> trainInstance = trainSetData.get(i);
            for (int j = 0; j < instance.size(); j++) {
                sumOfSquares += Math.pow((instance.get(j).mInstanceValue - trainInstance.get(j).mInstanceValue),2);
            }
            distance[i] = new Distance(Math.sqrt(sumOfSquares), i,
                    mTrainSetArffReader
                            .getDataInstanceList()
                            .get(i)[mTrainSetArffReader.getDataInstanceList().get(i).length-1]);
        }
        return distance;
    }

    private static void classifyTestSet(ArrayList<ArrayList<ARFFContinuousInstance>> trainSetData,
                                        ArrayList<ArrayList<ARFFContinuousInstance>> testSetData) {
        // For each instance in test set, compute the distance to every instance in train set.
        // Sort the distances and find the k nearest neighbours
        // Compute the majority class among the k nearest neighbours
        // Print it

        int numCorrectlyClassified = 0;
        double accuracy = 0.0;

        System.out.println("k value : " + k);

        for(int i = 0; i < mTestSetArffReader.getDataInstanceList().size(); i++) {
            Distance[] distance = computeEuclideanDistance(testSetData.get(i), trainSetData);
            //sort based on distance measure
            Arrays.sort(distance);

            //Find k nearest neighbours' class
            int[] classCount = new int[mTrainSetArffReader.getARFFClass().mNoOfClasses];
            for(int j = 0; j < k; j++) {
                for(int l = 0; l < mTrainSetArffReader.getARFFClass().mNoOfClasses; l++) {
                    if (distance[j].getInstanceClass()
                            .equalsIgnoreCase(mTrainSetArffReader.getARFFClass().mClassLabels[l])) {
                        classCount[l]++;
                    }
                }
            }

            //Classify based on majority class
            String nearestNeighbourClass = distance[0].getInstanceClass();
            int majorityClassIndex = -1;
            int max = -1;
            boolean isTie = false;

            for(int l = 0; l < classCount.length; l++) {
                if(classCount[l] == max) {
                    isTie = true;
                    if(mTrainSetArffReader.getARFFClass().mClassLabels[l].equalsIgnoreCase(nearestNeighbourClass)) {
                        max = classCount[l];
                        majorityClassIndex = l;
                    } else {
                        //This class doesn't contain the nearest neighbour.
                        //So proceed to find the other majority classes if they exist.
                    }
                }
                if(classCount[l] > max) {
                    isTie = false;
                    max = classCount[l];
                    majorityClassIndex = l;
                }
            }

            if (isTie) {
                ArrayList<Integer> tieArray = new ArrayList<>();

                for (int o = 0; o < distance.length - 1; o++) {
                    if (distance[o].getDistance() == distance[o + 1].getDistance()) {
                        tieArray.add(o, getClassIndex(distance[o].getInstanceClass()));
                        tieArray.add(o + 1, getClassIndex(distance[o + 1].getInstanceClass()));
                    } else {
                        break;
                    }
                }


                int finalClassIndex = 9999999;
                for (int o = 0; o < tieArray.size(); o++) {
                    if (tieArray.get(o) < finalClassIndex) {
                        finalClassIndex = tieArray.get(o);
                    }
                }
                if (finalClassIndex != 9999999) {
                    majorityClassIndex = finalClassIndex;
                }
            }

            String predictedClass = mTestSetArffReader.getARFFClass().mClassLabels[majorityClassIndex];
            String actualClass = mTestSetArffReader.getDataInstanceList()
                    .get(i)[mTestSetArffReader.getDataInstanceList().get(i).length - 1];

            if(predictedClass == actualClass) {
                numCorrectlyClassified++;
            }
            System.out.println("Predicted class : " + predictedClass + "\tActual class : " + actualClass);
        }
        accuracy = (double) numCorrectlyClassified / (double) mTestSetArffReader.getDataInstanceList().size();
        System.out.println("Number of correctly classified instances : " + numCorrectlyClassified);
        System.out.println("Total number of instances : " + mTestSetArffReader.getDataInstanceList().size());
        System.out.println("Accuracy : " + accuracy);
    }

    private static int getClassIndex(String className) {
        int index = -1;

        for(int i = 0; i < mTrainSetArffReader.getARFFClass().mNoOfClasses; i++) {
            if(className.equalsIgnoreCase(mTrainSetArffReader.getARFFClass().mClassLabels[i])) {
                index = i;
            }
        }
        return index;
    }

    private static ArrayList<ArrayList<ARFFContinuousInstance>> constructInstanceData(ARFFReader reader) {
        ArrayList<ArrayList<ARFFContinuousInstance>> instanceList = new ArrayList<>();

        for(int i = 0; i < reader.getDataInstanceList().size(); i++) {
            ArrayList<ARFFContinuousInstance> instanceAttributeList = new ArrayList<>();
            String[] instance = reader.getDataInstanceList().get(i);
            for(int j = 0; j < reader.getAttributeList().size(); j++) {
                ARFFContinuousInstance continuousInstance = new ARFFContinuousInstance(i,
                        Double.parseDouble(instance[j]));
                instanceAttributeList.add(continuousInstance);
            }
            instanceList.add(instanceAttributeList);
        }
        return instanceList;
    }

    public static void predictTestSet(ArrayList<ArrayList<ARFFContinuousInstance>> trainSetData,
                                      ArrayList<ArrayList<ARFFContinuousInstance>> testSetData) {
        double meanAbsoluteError = 0.0;

        System.out.println("k value : " + k);

        // For each instance in test set, compute the euclidean distance to all instances in the train set
        // Sort the instances by distance
        // Find k nearest neighbors
        // Return the average class values of the k nearest neighbours

        for(int i = 0; i < mTestSetArffReader.getDataInstanceList().size(); i++) {
            Distance[] distance = computeEuclideanDistance(testSetData.get(i), trainSetData);
            //sort based on distance measure
            Arrays.sort(distance);

            double predictedValue = 0.0;
            double actualValue = 0.0;

            for(int l = 0; l < k; l++) {
                predictedValue += Double.parseDouble(distance[l].getInstanceClass());
            }

            predictedValue = predictedValue / (double) k;
            actualValue = Double.parseDouble(mTestSetArffReader.getDataInstanceList()
                    .get(i)[mTestSetArffReader.getDataInstanceList().get(i).length - 1]);
            meanAbsoluteError += Math.abs(predictedValue - actualValue);

            System.out.println("Predicted value : " + String.format("%f", predictedValue)
                    + "\tActual value : " + String.format("%f",actualValue));
        }

        meanAbsoluteError = meanAbsoluteError/ (double) mTestSetArffReader.getDataInstanceList().size();
        System.out.println("Mean absolute error : " + meanAbsoluteError);
        System.out.println("Total number of instances : " + mTestSetArffReader.getDataInstanceList().size());

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

        if(mTrainSetArffReader.getARFFClass().mIsClassNumeric) {
            predictTestSet(trainSetData, testSetData);
        } else {
            classifyTestSet(trainSetData, testSetData);
        }

    }


}
