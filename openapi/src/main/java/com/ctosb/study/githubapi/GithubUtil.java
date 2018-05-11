
package com.ctosb.study.githubapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * github util
 * @author liliangang-1163
 * @date 2018/5/11 13:12
 * @see
 */
public class GithubUtil {

	private GithubUtil() {
	}

	/**
	 * 查询inssue api(查多页)，see https://developer.github.com/v3/issues/#list-issues-for-a-repository
	 * @date 2018/5/11 13:10
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @param uri
	 * @param nameValuePairs
	 * @param token
	 * @param page
	 * @return
	 */
	public static List<JSONArray> queryInssues(String uri, List<NameValuePair> nameValuePairs, String token, int page) {
		List<JSONArray> jsonArrays = IntStream.range(1, page + 1).mapToObj(t -> {
			try {
				return queryInssue(uri + "?page=" + t, nameValuePairs, token);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new JSONArray();
		}).collect(Collectors.toList());
		return jsonArrays;
	}

	/**
	 * 查询inssue api（只查第一页）
	 * @date 2018/5/11 13:11
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @param uri
	 * @param nameValuePairs
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static JSONArray queryInssue(String uri, List<NameValuePair> nameValuePairs, String token)
			throws IOException {
		Response response = Request
				.Get(uri + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8)))
				.addHeader("Authorization", "token " + token)
				.addHeader("Accept", "application/vnd.github.symmetra-preview+json").execute();
		HttpResponse httpResponse = response.returnResponse();
		System.out.println("status line:" + httpResponse.getStatusLine());
		String content = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("content:" + content);
		JSONArray jsonArray = JSONObject.parseArray(content);
		return jsonArray;
	}

	/**
	 * 创建inssue api ，see https://developer.github.com/v3/issues/#create-an-issue
	 * @param uri
	 * @param json
	 * @param token
	 * @throws IOException
	 */
	public static void createInssue(String uri, String json, String token) throws IOException {
		Response response = Request.Post(uri).addHeader("Authorization", "token " + token)
				.addHeader("Accept", "application/vnd.github.symmetra-preview+json")
				.body(new StringEntity(json, HTTP.UTF_8)).execute();
		HttpResponse httpResponse = response.returnResponse();
		System.out.println("status line:" + httpResponse.getStatusLine());
		System.out.println(EntityUtils.toString(httpResponse.getEntity()));
	}
}
