import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.pegdown.PegDownProcessor;

import com.alibaba.fastjson.JSON;

/**
 * @author liliangang-1163
 * @date 2018/3/15 11:14
 * @see
 */
public class GithubApiTest {

	@Test
	public void pull() throws IOException {
		Content content = Request.Get("https://api.github.com/repos/Alan3058/python-study/contents/README.md")
				// oauth认证
				// .addHeader("Authorization", "token 6956af35da1dbf326100sasqwd")
				.execute().returnContent();
		System.out.println(content.toString());
		String base64Str = JSON.parseObject(content.toString()).getString("content");
		String contentStr = new String(Base64.decodeBase64(base64Str), "UTF-8");
		System.out.println(contentStr);
		PegDownProcessor markdownProcessor = new PegDownProcessor();
		String html = markdownProcessor.markdownToHtml(contentStr);
		System.out.println(html);
	}
}