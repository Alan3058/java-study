import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import sun.misc.Unsafe;

/*
 * @Project Name: java-study
 * @File Name: TestHashMap.java
 * @Package Name:
 * @Date: 2017年10月17日上午11:20:51
 * @Creator: liliangang-1163
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */
/**
 * TODO
 * @author liliangang-1163
 * @date 2017年10月17日上午11:20:51
 * @see
 */
public class TestHashMap {

	@Test
	public void testHashMap() {
		Map<Integer, Integer> map = new HashMap<>(6);
		map.put(-1, -1);
		for (int i = 0; i < 100; i++) {
			Integer pi = i * 16 + 1;
			map.put(pi, pi);
		}
		map.get(1);
		map.get(17);
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			// 首次调用会调用iterator方法生成一个迭代器
			System.out.print(key + " ");
		}
		for (Integer key : keys) {
			// 会生成一个新的迭代器迭代
			System.out.print(key + " ");
		}
	}

	@Test
	public void testConcurrentHashMap() throws Exception {
		Map<Integer, Integer> map = new ConcurrentHashMap<>(6);
		for (int i = 0; i < 100; i++) {
			Integer pi = i * 16 + 1;
			map.put(pi, pi);
		}
		map.get(1);
		map.get(17);
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			System.out.print(key + " ");
		}
	}

	@Test
	public void testConcurrentLinkedQueue() throws Exception {
		Queue<Integer> queue = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < 10; i++) {
			Integer pi = i;
			queue.offer(pi);
		}
	}

	@Test
	public void testUnsafe() throws Exception {
		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		Unsafe unsafe = (Unsafe) field.get(null);
		// 跳过构造器，创建对象
		Person person = (Person) unsafe.allocateInstance(Person.class);
		assertEquals(person.age, 0);
		// 获取对象字段地址相对于对象地址的偏移量
		long offset = unsafe.objectFieldOffset(Person.class.getDeclaredField("age"));
		// 为对象age字段赋值（对象地址+字段偏移量地址定位字段）
		unsafe.putInt(person, offset, 20);
		assertEquals(person.age, 20);
	}

	class Person {

		private int age;

		private Person() {
			System.out.println("constructor initing");
			age = 18;
		}
	}
}
