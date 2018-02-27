import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * TODO
 * @author liliangang-1163
 * @date 2017年9月28日上午13:30:22
 * @see
 */
public class TestJdkNewFeature {

	@Test
	public void test() {
		List<Integer> list = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		System.out.println("测试foreach遍历");
		list.stream().forEach(System.out::print);
		System.out.println();
		System.out.println("测试map计算，并遍历");
		list.stream().map(e -> e * e).forEach(e -> System.out.print("," + e));
		System.out.println();
		System.out.println("测试map计算，collect合并集合，并遍历");
		List<Integer> newlist = list.stream().map(e -> e * e).collect(Collectors.toList());
		newlist.stream().forEach(e -> System.out.print("," + e));
		System.out.println();
		System.out.println("测试filter过滤");
		list.stream().filter(e -> e > 3).forEach(System.out::print);
		System.out.println();
		System.out.println("测试optional空处理");
		Optional<User> user = Optional.ofNullable(null);
		String name = user.map(e -> e.getUsername()).map(n -> n.toUpperCase()).orElse(null);
		System.out.println(name);
		System.out.println();
		List<String> userNames = new ArrayList<User>().stream().map(t -> t.getUsername()).collect(Collectors.toList());
		Assert.assertTrue(userNames != null && userNames.size() == 0);
	}

	@Test
	public void testMultiCollectionCollector() {
		System.out.println("测试多集合合并");
		List<User> us = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			User u = new User("test" + i, i);
			for (int n = 1; n < 3; n++) {
				User sub = new User("test" + i + n, n);
				u.addUsers(sub);
			}
			us.add(u);
		}
		List<User> users = us.stream().flatMap(t -> t.users.stream()).collect(Collectors.toList());
		int sum = us.stream().flatMap(t -> t.users.stream()).mapToInt(t -> t.age).sum();
		Assert.assertTrue(users.size() == 10);
		Assert.assertTrue(sum == 15);
	}

	@Test
	public void testGenerate() {
		System.out.println("测试生成构建流");
		Stream.iterate(0, n -> n + 3).limit(10).forEach(t -> System.out.print(t + " "));
		System.out.println();
		List<User> users = Stream.generate(() -> new User("test", RandomUtils.nextInt(10))).limit(20)
				.collect(Collectors.toList());
		Assert.assertEquals(users.size(), 20);
		Map<Integer, List<User>> map = Stream.generate(() -> new User("test", RandomUtils.nextInt(10))).limit(20)
				.collect(Collectors.groupingBy(t -> t.age));
		map.forEach((k, v) -> System.out.println(k + "岁:合计" + v.size()));
	}

	@Test
	public void testParallel() {
		System.out.println("测试生成构建流");
		List<Integer> list = Stream.generate(() -> RandomUtils.nextInt(1_0000_0000)).limit(1_000_0000)
				.collect(Collectors.toList());
		long fm = System.currentTimeMillis();
		List<Integer> list1 = list.stream().sorted().collect(Collectors.toList());
		long to = System.currentTimeMillis();
		List<Integer> list2 = list.parallelStream().sorted().collect(Collectors.toList());
		long to1 = System.currentTimeMillis();
		for (int i = 0; i < list1.size(); i++) {
			Assert.assertEquals(list1.get(i), list2.get(i));
		}
		System.out.println("串行计算耗时：" + (to - fm));
		System.out.println("并行计算耗时：" + (to1 - to));
	}

	public static class User {

		private String username;
		private int age;
		private List<User> users = new ArrayList<>();

		public User() {
		}

		public User(String username) {
			this.username = username;
		}

		public User(String username, int age) {
			this.username = username;
			this.age = age;
		}

		public String getUsername() {
			return username;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

		public void addUsers(User user) {
			this.users.add(user);
		}
	}
}
