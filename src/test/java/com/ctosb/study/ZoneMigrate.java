
package com.ctosb.study;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
		QueryRunner runner = new QueryRunner();
		String sql = "SELECT a.TITLE,b.CONTENT,GROUP_CONCAT(d.`NAME`) as SECTIONNAME FROM article a, article_content b, article_section c,section d WHERE a.ID = b.ARTICLE_ID and b.ARTICLE_ID = c.article_id and c.section_id = d.id group by a.title,b.CONTENT";
		List<Map<String, Object>> result = runner.query(conn, sql, new MapListHandler());
		result.stream().forEach(t -> {
			try {
				String filename = "d:/tmp/" + t.get("TITLE") + ".md";
				FileUtils.writeStringToFile(new File(filename), (String) t.get("CONTENT"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
