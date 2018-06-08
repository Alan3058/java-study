import org.junit.Test;

import java.util.*;

/**
 * 测试递归与非递归
 */
public class FibTest {

	@Test
	public void testRecurse() {
		System.out.println(fib(7));
		System.out.println(fib1(7));
		print("adsfsdfdsafadsfsadfdsaf");
	}

	private int fib(int n) {
		return n <= 2 ? 1 : fib(n - 1) + fib(n - 2);
	}

	private int fib1(int n) {
		if (n <= 2) {
			return 1;
		}
		int n1 = 1, n2 = 1;
		int result = 0;
		for (int i = 3; i <= n; i++) {
			result = n1 + n2;
			n1 = n2;
			n2 = result;
		}
		return result;
	}

	// 统计字符次数，并按次数排序，如果相同次数则按出现先后顺序显示
	private void print(String str) {
		//inkedHashMap可以保持出现顺序
		LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
		for (int i = 0; i < str.length(); i++) {
			char key = str.charAt(i);
			if (map.containsKey(key)) {
				map.put(key, map.get(key) + 1);
			} else {
				map.put(key, 1);
			}
		}
		//按次数排序
		map.entrySet().stream().sorted(Comparator.comparing(t -> t.getValue()))
				.forEach(t -> System.out.print(t.getKey() + "" + t.getValue()));
	}
}
