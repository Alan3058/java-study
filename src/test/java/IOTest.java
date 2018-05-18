import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * io nio test
 * @author liliangang-1163
 * @date 2018/5/18 11:17
 * @see
 */
public class IOTest {

	/**
	 * 单线程，通过telnet的send命令测试
	 * @date 2018/5/18 15:56
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @throws IOException
	 */
	@Test
	public void SocketTest1() throws IOException {
		ServerSocket serverSocket = new ServerSocket(1111);
		while (true) {
			Socket socket = serverSocket.accept();// 阻塞
			InputStream inputStream = socket.getInputStream();
			print(inputStream);
		}
	}

	/**
	 * 多线程处理
	 * @date 2018/5/18 15:56
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @throws IOException
	 */
	@Test
	public void SocketTest2() throws IOException {
		ServerSocket serverSocket = new ServerSocket(1111);
		while (true) {
			Socket socket = serverSocket.accept();// 阻塞
			InputStream inputStream = socket.getInputStream();
			// 不使用线程池处理
			// new Thread(() -> print(inputStream)).start();
			// 使用线程池处理
			ExecutorService threadPool = Executors.newFixedThreadPool(10);
			threadPool.execute(() -> print(inputStream));
		}
	}

	/**
	 * 打印流的信息
	 * @date 2018/5/18 16:02
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @param inputStream
	 */
	private void print(InputStream inputStream) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] temp = new byte[1024];
			int length;
			while ((length = inputStream.read(temp)) != -1) {// 阻塞
				outputStream.write(temp, 0, length);
				String content = new String(outputStream.toByteArray(), "utf-8");
				print(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void SocketChannelTest1() throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.bind(new InetSocketAddress(1111));
		while (true) {
			SocketChannel socketChannel = serverSocket.accept();
			print("有客户端已连接上");
			print(socketChannel);
		}
	}

	@Test
	public void SocketChannelTest2() throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.configureBlocking(false);
		serverSocket.socket().bind(new InetSocketAddress(1111));
		Selector selector = Selector.open();
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			selector.select();
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				iterator.remove();
				if (selectionKey.isAcceptable()) {
					ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
					SocketChannel socketChannel = channel.accept();
					socketChannel.configureBlocking(false);
					print("有客户端已连接上");
					socketChannel.register(selector, SelectionKey.OP_READ);
				} else if (selectionKey.isReadable()) {
					SocketChannel channel = (SocketChannel) selectionKey.channel();
					channel.configureBlocking(false);
					print("准备读取客户端信息");
					print(channel);
				}
			}
		}
	}

	private void print(SocketChannel socketChannel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int length;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((length = socketChannel.read(buffer)) > 0) {
			outputStream.write(buffer.array(), 0, length);
		}
		print(new String(outputStream.toByteArray(), "utf-8"));
	}

	private void print(String str) {
		System.out.println(String.format("%s" + str, Thread.currentThread().getName()));
	}
}
