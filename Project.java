public class linear {
    public static void main(String[] args) {
        //int[] arr = {17,98,100,56,34,7,-3,67,-2,56};

       int[][] arr = {                {111, 2, -10},                {4, -5000, -7876,11},                {7, 88, 9}        };

        int[] result = sum(arr);
        //System.out.println(arr.length);
        //System.out.println("The minimum number of this 2d array is: " + result);
        System.out.println("The number of even digits exist in array is: " + Arrays.toString(result));
    }

    static int[] sum(int[][] arr){
        int[] res = new int[arr.length];
        for(int i=0; i<arr.length; i++){
            int sum = 0;
            for(int j = 0; j< arr[i].length; j++){
                sum = sum + arr[i][j];
            }
            res[i] = sum;
        }
        return res;
    }

