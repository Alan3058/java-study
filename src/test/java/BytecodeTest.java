import org.junit.Test;

/**
 * 字节码测试类
 * @author liliangang-1163
 * @date 2018/5/4 10:10
 * @see
 */
public class BytecodeTest {

	@Test
	public void test() {
		short i = 1234;
		String a = "abcd";
		String b = "efgh";
		String c = "abcdefgh";
		String d = "abcd" + "efgh";
		String e = a + b;
		String f = a + "efgh";
		System.out.println(i);
		System.out.println(c == d);
		System.out.println(e == d);
		System.out.println(f == d);
		System.out.println(f == e);
	}
}
