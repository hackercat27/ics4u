import java.util.Arrays;

public class Statistics {

    private Statistics() {}

    //iterate through each value of the array using the first->second->etc value
    //increment counter for each duplicate value
    //create a highest counter int value
    //the highest counter becomes the new highest counter value
    //therefore the number with the highest count = mode

    public static double mode(int [] arr){
        int highestCount = 0;
        int mode = 0;
        //System.out.println(Arrays.toString(arr));
        for(int i = 0; i<arr.length; i++){
            int counter = 0;
            //num is used to compare to each component of the array
            int num = arr[i];
            for(int j = 0; j<arr.length; j++){
                if(num == arr[j]){
                    counter++;
                }
            }
            //highestCounter will always be 1 so
            //highestCounter+1
            if(counter>highestCount+1){
                highestCount = counter;
                mode = num;
            }
        }
        return mode;
    }


    public static double quartileOne (int [] arr)
    {
        Arrays.sort (arr); //sort the array from smallest to largest
        int halfPoint = arr.length /2; //determine the midpoint of the array
        int [] lowerHalfArray = Arrays.copyOf(arr, halfPoint); //create a copy of the array only to the half-way point
        int lowerHalfSize = lowerHalfArray.length; //1/2 way point
        if ((lowerHalfSize % 2 == 1)) //If there is an odd number of elements, pick the middle
        {
            return (lowerHalfArray[lowerHalfSize/2]); //return the first quartile
        }
        else //if there is an even number of elements, pick the average of the middle two
        {
            return (lowerHalfArray[lowerHalfSize/2 -1] + lowerHalfArray[lowerHalfSize / 2]) /2.0; //return the first quartile
        }
    }


    public static double quartileThree (int [] arr)
    {
        Arrays.sort (arr) ; //sort the array from smallest to highest
        int halfPoint = arr.length /2 ; //determine the half way point

        int [] upperHalfArray = Arrays.copyOfRange (arr,halfPoint,arr.length); //creates a copy of the previous array only for numbers between the haldway poiunt and the end.
        int upperHalfSize = upperHalfArray.length;
        {if ((upperHalfSize % 2 == 1)) //If there is an odd number of elements, pick the middle
        {
            return (upperHalfArray[upperHalfSize/2]); //return the first quartile
        }
        else //if there is an even number of elements, pick the average of the middle two
        {
            return (upperHalfArray[upperHalfSize/2 -1] + upperHalfArray[upperHalfSize / 2]) /2.0; //return the first quartile
        }

        }
    }

    /**
     * Calculates the median of the given int array.
     * This method assumes the array is non-null and of length 1 or greater.
     *
     * @author Tobin Brenner
     * @param arr Data to calculate the median
     * @return the median value
     */
    public static double median(int[] arr) {
        // sort the array and then pick the middle number
        int[] sorted = new int[arr.length];
        System.arraycopy(arr, 0, sorted, 0, arr.length);
        Arrays.sort(sorted);

        boolean twoMedians = arr.length % 2 == 0;
        int middleIndex = arr.length / 2;

        if (twoMedians) {
            return mean(new int[] {sorted[middleIndex - 1], sorted[middleIndex]});
        }
        else {
            return sorted[middleIndex];
        }
    }


    public static double quartileTwo(int[] arr) {
        return median(arr);
    }

    public static double mean(int [] ar)
    {
        // creating the funky variable
        double temper = 0;
        // for loop adding up all of ar together
        for(int i = 0; i < ar.length; i++)
        {
            temper = temper + ar[i];
        }
        // dividing temper by the size of ar
        temper = temper / ar.length;
        // yabadabadoo, return to sender
        return temper;
    }

}
