
package com.ctosb.study;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ctosb.html2markdown.Html2Md;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;
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
}
