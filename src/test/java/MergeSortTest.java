import java.util.Arrays;

import org.junit.Test;

/**
 * 归并排序
 * @date 2018/6/19 22:52
 * @author alan
 * @since 1.0.0
 */
public class MergeSortTest {

	@Test
	public void testMergeSort() {
		mergeSort(new int[] { 1, 3, 5, 7, 2, 4, 6, 8 });
	}

	private void mergeSort(int[] array) {
		mergeSort(array, 0, array.length - 1, new int[array.length]);
		System.out.println(Arrays.toString(array));
	}

	private void mergeSort(int[] array, int begin, int to, int[] temp) {
		if (begin < to) {
			int mid = (begin + to) / 2;
			mergeSort(array, begin, mid, temp);
			mergeSort(array, mid + 1, to, temp);
			merge(array, begin, mid, to, temp);
		}
	}

	private void merge(int[] array, int begin, int mid, int to, int[] temp) {
		int i = begin, j = mid + 1;
		int t = 0;
		while (i <= mid && j <= to) {
			if (array[i] > array[j]) {
				temp[t++] = array[j++];
			} else {
				temp[t++] = array[i++];
			}
		}
		while (i <= mid) {
			temp[t++] = array[i++];
		}
		while (j <= to) {
			temp[t++] = array[j++];
		}
		t = 0;
		while (begin <= to) {
			array[begin++] = temp[t++];
		}
	}
}
