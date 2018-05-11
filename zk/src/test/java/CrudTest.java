import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.apache.zookeeper.CreateMode;
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

    CuratorFramework client;
    String connection = "192.168.10.44:2182";
    int timeout = 5000;

    @Before
    public void before() {
        client = CuratorFrameworkFactory.builder().connectString(connection).retryPolicy(new RetryForever(1000))
                .connectionTimeoutMs(timeout).sessionTimeoutMs(timeout).build();
    }

    @Test
    public void crud() throws Exception {
        client.start();
        List<String> nodes = client.getChildren().forPath("/");
        System.out.println("query all nodes:" + String.join(",", nodes));
        String path = "/curator/3";
        String test = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,
                "abc".getBytes());
        System.out.println("create success node:" + test + ",version:"+client.checkExists().forPath(path).getVersion());
        Stat stat = client.setData().forPath(path, test.getBytes());
        System.out.println("update data, stat:" + stat+",version:"+stat.getVersion());
        client.delete().forPath(path);
        System.out.println("success delete.exist:" + (client.checkExists().forPath(path) != null));
    }
}
