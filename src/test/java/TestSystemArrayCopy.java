import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 测试系统数组复制
 * @author liliangang-1163
 * @date 2017/10/31 16:15
 * @see
 */
public class TestSystemArrayCopy {

	@Test
	public void testArrayCopy() {
		String[] array = new String[] { "1as", "2dfs", "adf" };
		String[] destArr = new String[array.length];
		System.arraycopy(array, 0, destArr, 0, array.length);
		for (int i = 0; i < array.length; i++) {
			assertTrue(array[i] == destArr[i]);
		}
	}
}
