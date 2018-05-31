import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {

	@Test
	public void testProxy() {
		UserService userService = (UserService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[] { UserService.class }, new InvocationHandler() {

					private UserService delegate = param -> {
						System.out.println("-----调用中------");
						return "test," + param;
					};

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("调用前");
						Object result = method.invoke(delegate, args);
						System.out.println("调用后");
						return result;
					}
				});
		System.out.println("调用：" + userService.execute("hello"));
	}

	interface UserService {

		String execute(String param);
	}
}
