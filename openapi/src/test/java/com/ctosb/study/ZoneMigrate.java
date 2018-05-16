
package com.ctosb.study;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctosb.html2markdown.Html2Md;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * zone的数据迁移
 * @author liliangang-1163
 * @date 2018/4/28 18:09
 * @see
 */
public class ZoneMigrate {

	private String url, user, pwd;
	private Connection conn;
	private String separator = System.getProperty("line.separator");

	@Before
	public void setUp() throws Exception {
		url = "jdbc:mysql://localhost:3306/zone";
		user = "root";
		pwd = "";
		Class.forName("com.mysql.jdbc.Driver");
		// 建立到MySQL的连接
		conn = DriverManager.getConnection(url, user, pwd);
	}

	@Test
	public void migrate() throws SQLException {
		String format = "yyyy-MM-dd";
		QueryRunner runner = new QueryRunner();
		String sql = "SELECT a.id,a.TITLE,REPLACE(a.KEYWORD,\";\",\",\") as KEYWORD,b.CONTENT,GROUP_CONCAT(d.`NAME`) as SECTIONNAME,a.CREATE_TIME FROM article a, article_content b, article_section c,section d WHERE a.ID = b.ARTICLE_ID and b.ARTICLE_ID = c.article_id and c.section_id = d.id "
				+ " and c.article_id not in (SELECT tt.article_id from article_section tt where tt.section_id in (22291312279552,22295506583552,22341652316160,19998801309728768)) group by a.title,b.CONTENT,A.CREATE_TIME";
		List<Map<String, Object>> result = runner.query(conn, sql, new MapListHandler());
		result.stream().forEach(t -> {
			try {
				Date createTime = (Date) t.get("CREATE_TIME");
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				String filename = "d:/tmp/" + dateFormat.format(createTime) + "-" + t.get("TITLE") + ".md";
				StringBuilder content = new StringBuilder();
				content.append("---" + separator);
				content.append("layout: post" + separator);
				content.append("title: [" + t.get("TITLE") + "]" + separator);
				content.append("categories: [" + t.get("SECTIONNAME") + "]" + separator);
				content.append("tags: [" + t.get("KEYWORD") + "]" + separator);
				content.append("id: [" + t.get("ID") + "]" + separator);
				content.append("fullview: false" + separator);
				content.append("---" + separator);
				content.append(Html2Md.convertHtml((String) t.get("CONTENT"), "UTF-8") + separator);
				FileUtils.writeStringToFile(new File(filename), content.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 更新tag
	 * @date 2018/5/1 19:22
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @throws SQLException
	 */
	@Test
	public void updateTags() throws SQLException {
		String format = "yyyy-MM-dd";
		QueryRunner runner = new QueryRunner();
		String sql = "SELECT a.TITLE,REPLACE(a.KEYWORD,\";\",\",\") as KEYWORD,b.CONTENT,GROUP_CONCAT(d.`NAME`) as SECTIONNAME,a.CREATE_TIME FROM article a, article_content b, article_section c,section d WHERE a.ID = b.ARTICLE_ID and b.ARTICLE_ID = c.article_id and c.section_id = d.id "
				+ " group by a.title,b.CONTENT,A.CREATE_TIME";
		List<Map<String, Object>> result = runner.query(conn, sql, new MapListHandler());
		result.stream().forEach(t -> {
			try {
				Date createTime = (Date) t.get("CREATE_TIME");
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				String filename = "d:/tmp/" + dateFormat.format(createTime) + "-" + t.get("TITLE") + ".md";
				String sectionname = "categories: [" + t.get("SECTIONNAME") + "]" + separator;
				String content = "tags: [" + t.get("KEYWORD") + "]" + separator;
				addTags(sectionname, content, filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private static void addTags(String sectionname, String tags, String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			return;
		}
		String fileString = FileUtils.readFileToString(file, "UTF-8");
		if (fileString.contains(tags)) {
			return;
		}
		String content = fileString.replace(sectionname, sectionname + tags);
		FileUtils.writeStringToFile(file, content);
	}

	@Test
	public void updateResourceUrl() {
		File dir = new File("E:\\other\\alan3058.github.io\\_posts");
		File[] files = dir.listFiles();
		Arrays.stream(files).forEach(file -> {
			try {
				String content = FileUtils.readFileToString(file);
				if (content.contains("http://file.ctosb.com//upload")
						|| content.contains("http://file.ctosb.com/upload")
						|| content.contains("http://ctosb.com/ueditor/dialogs/attachment/fileTypeImages")
						|| content.contains("http://dl2.iteye.com/upload/attachment/0110/4777/6f4513d7-5746-31ee-a2a8-")
						|| content.contains("http://dl2.iteye.com/upload/attachment/0110/4779/0b97b24f-8a4d-3968-8d8a-")
						|| content.contains("http://dl2.iteye.com/upload/attachment/0110/4584/ab1b03cc-6f49-339e-a022-")
						|| content.contains("http://dl2.iteye.com/upload/attachment/0110/5279/13c54fc7-d58f-3199-bc8c-")
						|| content.contains("http://dl2.iteye.com/upload/attachment/0110/5307/a86d0799-534f-3b9b-b592-")
						|| content.contains("http://dl2.iteye.com/upload/attachment/0110/4599/6659a319-f878-3b7d-bf3a-")
						|| content.contains("http://dl.iteye.com/topics/download/42903542-fcfc-3c45-8f2c-0dd28d6ccd93")
						|| content.contains("http://dl.iteye.com/topics/download/fe27332b-e18c-320c-a00b-f77376ebe9db")
						) {
					String newContent = content.replace("http://file.ctosb.com//upload", "/assets/resources")
							.replace("http://file.ctosb.com/upload", "/assets/resources")
							.replace("http://ctosb.com/ueditor/dialogs/attachment/fileTypeImages", "/assets/resources")
							.replace("http://dl2.iteye.com/upload/attachment/0110/4777/6f4513d7-5746-31ee-a2a8-", "/assets/resources/image/20160201/")
							.replace("http://dl2.iteye.com/upload/attachment/0110/4779/0b97b24f-8a4d-3968-8d8a-", "/assets/resources/image/20160201/")
							.replace("http://dl2.iteye.com/upload/attachment/0110/4584/ab1b03cc-6f49-339e-a022-", "/assets/resources/image/20160202/")
							.replace("http://dl2.iteye.com/upload/attachment/0110/5279/13c54fc7-d58f-3199-bc8c-", "/assets/resources/image/20160203/")
							.replace("http://dl2.iteye.com/upload/attachment/0110/5307/a86d0799-534f-3b9b-b592-", "/assets/resources/image/20160203/")
							.replace("http://dl2.iteye.com/upload/attachment/0110/4599/6659a319-f878-3b7d-bf3a-", "/assets/resources/image/20160203/")
							.replace("http://dl.iteye.com/topics/download/42903542-fcfc-3c45-8f2c-0dd28d6ccd93", "/assets/resources/file/20160202/0dd28d6ccd93.zip")
							.replace("http://dl.iteye.com/topics/download/fe27332b-e18c-320c-a00b-f77376ebe9db", "/assets/resources/file/20160203/f77376ebe9db.zip")
							;
					FileUtils.writeStringToFile(file, newContent);
				}
				Pattern compile = Pattern.compile("\\(http[^)]+\\)");
				Matcher matcher = compile.matcher(content);
				while (matcher.find()) {
					System.out.println(matcher.group(0));
				}
				 System.out.println(file.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
