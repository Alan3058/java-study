
package origin;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import util.Constants;

/**
 * crud test
 * @author liliangang-1163
 * @date 2018/5/11 17:59
 * @see
 */
public class CrudTest {

	ZooKeeper zooKeeper;
	Watcher watcher;

	@Before
	public void before() throws IOException {
		watcher = event -> System.out.println(String.format("------------wacher,event:%s,path:%s,state:%s",
				event.getType(), event.getPath(), event.getState()));
		zooKeeper = new ZooKeeper(Constants.connection, Constants.timeout, watcher);
	}

	@Test
	public void crud() throws Exception {
		String path = "/curator";
		List<String> nodes = zooKeeper.getChildren("/", false);
		zooKeeper.register(watcher);
		System.out.println("query all nodes:" + String.join(",", nodes));
		String test = zooKeeper.create(path, "abc".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		Stat stat = zooKeeper.exists(path, true);
		assert (stat != null && stat.getVersion() == 0);
		System.out.println("create success node:" + test + ",version:" + stat.getVersion());
		stat = zooKeeper.setData(path, test.getBytes(), 0);
		assert (stat.getVersion() == 1);
		System.out.println("update data, stat:" + stat + ",version:" + stat.getVersion());
		zooKeeper.delete(path, 1);
		stat = zooKeeper.exists(path, watcher);
		System.out.println("success delete.exist:" + (stat != null));
	}
}
