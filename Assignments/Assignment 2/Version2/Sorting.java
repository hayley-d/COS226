import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Sorting {
    public Sorting(){
    }

    public static void bubbleSortList(List<Integer> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    // Swap
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    public static void bubblesort(int[] array) {
        for(int i = 0; i < array.length-1;i++)
        {
            for(int j = i+1; j < array.length;j++)
            {
                if(array[i] > array[j])
                {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    public static void selectionsort(int[] array)
    {
        int minIndex = 0;
        for(int i = 0; i < array.length-1; i++)
        {
            minIndex = i;
            for(int j = i+1; j < array.length;j++)
            {
                if(array[j] < array[minIndex])
                {
                    minIndex = j;
                }
            }
            if(minIndex != i)
            {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }

    public static void insertionSort(int[] array) {
        int size = array.length;

        for (int step = 1; step < size; step++)
        {
            int key = array[step];
            int j = step - 1;

            while (j >= 0 && key < array[j]) {
                array[j + 1] = array[j];
                --j;
            }

            array[j + 1] = key;
        }
    }


}
