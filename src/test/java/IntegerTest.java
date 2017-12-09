import org.junit.Test;

/**
 * @author liliangang-1163
 * @date 2017/10/30 16:14
 * @see
 */
public class IntegerTest {

	/**
	 * 测试移位
	 */
	@Test
	public void testShift() {
		int a = 0x8000_0000;
		format(a);
		format(a >> 1);
		format(a >> 31);
		// 首位（符号位）不变
		format(a >> 32);
		// 首位补0
		format(a >>> 1);
		format(Integer.MAX_VALUE);
		format(Integer.MIN_VALUE);
	}

	public static void format(int a) {
		String str;
		System.out.println(String.format("%s:%s:%s", a, str = Integer.toBinaryString(a), str.length()));
	}
}
