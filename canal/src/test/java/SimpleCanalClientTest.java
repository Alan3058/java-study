import java.io.IOException;
import java.net.InetSocketAddress;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import org.junit.Test;

/**
 * 单机模式的测试例子
 * @author liliangang-1163
 * @date 2018/5/24 16:58
 * @see
 */
public class SimpleCanalClientTest {

	@Test
	public void testCanal() throws InterruptedException, IOException {
		// 根据ip，直接创建链接，无HA的功能
		String destination = "example";
		String ip = AddressUtils.getHostIp();
		CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, 11111), destination, "",
				"");
		final AbstractCanalClientTest clientTest = new AbstractCanalClientTest(destination);
		clientTest.setConnector(connector);
		clientTest.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				try {
					AbstractCanalClientTest.logger.info("## stop the canal client");
					clientTest.stop();
				} catch (Throwable e) {
					AbstractCanalClientTest.logger.warn("##something goes wrong when stopping canal:", e);
				} finally {
					AbstractCanalClientTest.logger.info("## canal client is down.");
				}
			}
		});
		System.in.read();
	}
}
