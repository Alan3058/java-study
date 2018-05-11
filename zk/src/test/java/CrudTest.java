import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * crud test
 * @author liliangang-1163
 * @date 2018/5/11 17:59
 * @see
 */
public class CrudTest {

	CuratorFramework client;
	int timeout = 10000;

	@Before
	public void before() {
		client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").retryPolicy(new RetryForever(1000))
				.connectionTimeoutMs(timeout).sessionTimeoutMs(timeout).build();
	}

	@Test
	public void crud() throws Exception {
		client.start();
		List<String> nodes = client.getChildren().forPath("/");
		System.out.println(String.join(",", nodes));
		String test = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/curator/3",
				"abc".getBytes());
		System.out.println(test);
	}
}
