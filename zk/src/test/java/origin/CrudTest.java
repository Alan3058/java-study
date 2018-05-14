
package origin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * crud test
 * @author liliangang-1163
 * @date 2018/5/11 17:59
 * @see
 */
public class CrudTest {

	ZooKeeper zooKeeper;
	String connection = "10.41.1.127:2181";
	int timeout = 5000;

	@Before
	public void before() throws IOException {
		zooKeeper = new ZooKeeper(connection, timeout, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				System.out.println("wacher------------" + event.getPath() + event.getType());
			}
		});
	}

	@Test
	public void crud() throws Exception {
		String path = "/curator/3";
		List<String> nodes = zooKeeper.getChildren("/", false);
		System.out.println("query all nodes:" + String.join(",", nodes));
		String test = zooKeeper.create(path, "abc".getBytes(), Arrays.asList(new ACL()), CreateMode.EPHEMERAL);
		Stat stat = zooKeeper.exists(path, false);
		assert (stat.getVersion() == 0);
		System.out.println("create success node:" + test + ",version:" + stat.getVersion());
		// stat = client.setData().forPath(path, test.getBytes());
		// assert (stat.getVersion() == 1);
		// System.out.println("update data, stat:" + stat + ",version:" +
		// stat.getVersion());
		// client.delete().forPath(path);
		// stat = client.checkExists().forPath(path);
		// System.out.println("success delete.exist:" + (stat != null));
	}
}
