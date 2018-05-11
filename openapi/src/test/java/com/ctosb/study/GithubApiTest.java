
package com.ctosb.study;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.pegdown.PegDownProcessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctosb.study.githubapi.GithubUtil;

/**
 * @author liliangang-1163
 * @date 2018/3/15 11:14
 * @see
 */
public class GithubApiTest {

	String issueUri = "https://api.github.com/repos/Alan3058/alan3058.github.io/issues";
	String labelUri = "https://api.github.com/repos/Alan3058/alan3058.github.io/labels";
	String commentUri = "https://api.github.com/repos/Alan3058/alan3058.github.io/issues/comments";
	String token = "e24d19498ddc0743c533d0149d2990514a2855c6";

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
		// sitemap中获取文章地址
		List<String> urls = getBlogUrls();
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
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("title", title);
					jsonObject.put("body", url.replace("http://alan3058.github.io", "http://alan.ctosb.com"));
					jsonObject.put("labels", new String[] { "Gitalk", url.replace("http://alan3058.github.io", "") });
					System.out.println(JSON.toJSONString(jsonObject));
					// 创建issue
					GithubUtil.createInssue(issueUri, JSON.toJSONString(jsonObject), token);
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

	/**
	 * 测试更新issue
	 * @date 2018/5/11 13:24
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testUpdateInssue() throws IOException {
		List<JSONArray> jsonArrays = GithubUtil.queryInssues(issueUri, Arrays.asList(), token, 10);
		// 获取所有issue信息
		Map<String, String> titleNumberMap = jsonArrays.stream().flatMap(t -> t.stream()).collect(
				Collectors.toMap(t -> ((JSONObject) t).getString("title"), t -> ((JSONObject) t).getString("number")));
		List<String> blogUrls = getBlogUrls();
		blogUrls.forEach(t -> {
			try {
				String title = Jsoup
						.parse(new URL(t.replace("http://alan3058.github.io", "http://alan.ctosb.com")), 10000).title();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("body", t);
				Response response = Request.Patch(issueUri + "/" + titleNumberMap.get(title))
						.addHeader("Authorization", "token " + token)
						.addHeader("Accept", "application/vnd.github.symmetra-preview+json")
						.body(new StringEntity(jsonObject.toJSONString(), HTTP.UTF_8)).execute();
				HttpResponse httpResponse = response.returnResponse();
				System.out.println("status line:" + httpResponse.getStatusLine());
				String content = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("content:" + content);
				System.out.println(JSON.toJSONString(jsonArrays));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 测试查询label
	 * @date 2018/5/11 13:24
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testQueryLabel() {
		List<JSONArray> jsonArrays = GithubUtil.queryInssues(labelUri, Arrays.asList(), token, 3);
		System.out.println(JSON.toJSONString(jsonArrays));
	}

	/**
	 * 测试删除label
	 * @date 2018/5/11 13:24
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testDeleteLabel() throws IOException {
		String labelName = "/auto-deploy-9.Gitlab-pull-request-auto-hook";
		Response response = Request.Delete(labelUri + "/" + labelName).addHeader("Authorization", "token " + token)
				.addHeader("Accept", "application/vnd.github.symmetra-preview+json").execute();
		HttpResponse httpResponse = response.returnResponse();
		System.out.println("status line:" + httpResponse.getStatusLine());
	}

	/**
	 * 测试更新label
	 * @date 2018/5/11 13:24
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testUpdateLabel() throws IOException {
		// sitemap中获取文章地址
		List<String> urls = getBlogUrls();
		urls.forEach(url -> {
			try {
				String newLabel = url.replace("http://alan3058.github.io", "");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", newLabel);
				// /xxx --> /xxx/xxx
				if (newLabel.split("/").length == 3) {
					Response response = Request.Patch(labelUri + "/" + "/" + newLabel.split("/")[2])
							.addHeader("Authorization", "token " + token)
							.addHeader("Accept", "application/vnd.github.symmetra-preview+json")
							.body(new StringEntity(jsonObject.toJSONString(), HTTP.UTF_8)).execute();
					HttpResponse httpResponse = response.returnResponse();
					System.out.println("status line:" + httpResponse.getStatusLine());
					String content = EntityUtils.toString(httpResponse.getEntity());
					System.out.println("content:" + content);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 测试查询comment
	 * @date 2018/5/11 13:24
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testQueryComment() {
		List<JSONArray> jsonArrays = GithubUtil.queryInssues(commentUri, Arrays.asList(), token, 3);
		System.out.println(JSON.toJSONString(jsonArrays));
	}

	/**
	 * 获取文章地址
	 * @date 2018/5/11 13:42
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @return
	 * @throws IOException
	 */
	private List<String> getBlogUrls() throws IOException {
		String siteurl = "http://alan.ctosb.com/sitemap.xml";
		Elements elements = Jsoup.parse(new URL(siteurl), 10000).getElementsByTag("url");
		// sitemap中获取文章地址
		List<String> urls = elements.stream().filter(t -> t.html().contains("lastmod"))
				.flatMap(t -> t.getElementsByTag("loc").stream()).map(t -> t.text()).collect(Collectors.toList());
		return urls;
	}
}
