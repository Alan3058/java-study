
package com.ctosb.study;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctosb.study.githubapi.GithubUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.pegdown.PegDownProcessor;

import com.alibaba.fastjson.JSON;

/**
 * @author liliangang-1163
 * @date 2018/3/15 11:14
 * @see
 */
public class GithubApiTest {

	String issueUri = "https://api.github.com/repos/Alan3058/alan3058.github.io/issues";
	String token = "4940f23a26c3dc99a0b99f7af169a6dbde1a16312e";

	/**
	 * see https://developer.github.com/v3/#authentication see
	 * https://developer.github.com/v3/repos/contents/#get-contents
	 * @date 2018/3/16 13:37
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @throws IOException
	 */
	@Test
	public void testPull() throws IOException {
		Content content = Request.Get("https://api.github.com/repos/Alan3058/python-study/contents/README.md")
				// oauth认证
				// .addHeader("Authorization", "token " + token)
				.execute().returnContent();
		System.out.println(content.toString());
		String base64Str = JSON.parseObject(content.toString()).getString("content");
		String contentStr = new String(Base64.decodeBase64(base64Str), "UTF-8");
		System.out.println(contentStr);
		PegDownProcessor markdownProcessor = new PegDownProcessor();
		// md转html
		String html = markdownProcessor.markdownToHtml(contentStr);
		System.out.println(html);
	}

	/**
	 * 测试创建issue，see https://developer.github.com/v3/issues/#create-an-issue
	 * @date 2018/5/10 13:34
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testCreateInssue() throws IOException {
		String siteurl = "http://alan.ctosb.com/sitemap.xml";
		Elements elements = Jsoup.parse(new URL(siteurl), 10000).getElementsByTag("url");
		// sitemap中获取文章地址
		List<String> urls = elements.stream().filter(t -> t.html().contains("lastmod"))
				.flatMap(t -> t.getElementsByTag("loc").stream()).map(t -> t.text()).collect(Collectors.toList());
		// 获取所有issue信息
		List<JSONArray> jsonArrays = GithubUtil.queryInssues(issueUri, Arrays.asList(), token, 5);
		// 获取所有issue title
		List<String> titles = jsonArrays.stream().flatMap(t -> t.stream()).map(t -> ((JSONObject) t).getString("title"))
				.collect(Collectors.toList());
		urls.stream().forEach(url -> {
			try {
				String title = Jsoup.parse(new URL(url), 10000).title();
				// 已创建的不用创建
				if (!titles.contains(title)) {
					JSONObject nameValuePairs = new JSONObject();
					nameValuePairs.put("title", title);
					nameValuePairs.put("body", url.replace("http://alan3058.github.io", "http://alan.ctosb.com"));
					nameValuePairs.put("labels",
							new String[] { "Gitalk", url.replace("http://alan3058.github.io", "") });
					System.out.println(JSON.toJSONString(nameValuePairs));
					// 创建issue
					GithubUtil.createInssue(issueUri, JSON.toJSONString(nameValuePairs), token);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 测试查询issue
	 * @date 2018/5/11 13:24
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testQueryInssue() {
		List<JSONArray> jsonArrays = GithubUtil.queryInssues(issueUri, Arrays.asList(), token, 3);
		System.out.println(JSON.toJSONString(jsonArrays));
	}
}
