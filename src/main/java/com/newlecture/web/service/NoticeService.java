package com.newlecture.web.service;

import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public class NoticeService {

	public int removeNoticeAll(int[] ids) {

		return 0;
	}

	public int pubNoticeAll(int[] oids, int[] cids) {

		List<String> oidsList = new ArrayList<>();
		for (int i = 0; i < oids.length; i++) {
			oidsList.add(String.valueOf(oids[i]));
		}
		List<String> cidsList = new ArrayList<>();
		for (int i = 0; i < cids.length; i++) {
			cidsList.add(String.valueOf(cids[i]));
		}

		return pubNoticeAll(oidsList, cidsList);
	}

	public int pubNoticeAll(List<String> oids, List<String> cids) {
		String oidsCSV = String.join(",", oids);
		String cidsCSV = String.join(",", cids);
		;

		return pubNoticeAll(oidsCSV, cidsCSV);
	}

	public int pubNoticeAll(String oidsCSV, String cidsCSV) {

		int result = 0;
		String sqlOpen = String.format("UPDATE NOTICE SET PUB=1 WHERE ID IN (%s)", oidsCSV);
		String sqlClose = String.format("UPDATE NOTICE SET PUB=0 WHERE ID IN (%s)", cidsCSV);

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			Statement stOpen = con.createStatement();
			result += stOpen.executeUpdate(sqlOpen);

			Statement stClose = con.createStatement();
			result += stClose.executeUpdate(sqlClose);

			stOpen.close();
			stClose.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public int insertNotice(Notice notice) {
		int result = 0;
		String params = "";
		String sql = "INSERT INTO NOTICE(TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES(?, ?, ?, ?, ?)";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);

			st.setString(1, notice.getTitle());
			st.setString(2, notice.getContent());
			st.setString(3, notice.getWriterId());
			st.setBoolean(4, notice.getPub());
			st.setString(5, notice.getFiles());
			result = st.executeUpdate();

			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public int deleteNotice(int id) {
		return 0;
	}

	public int updateNotice(Notice notice) {
		return 0;
	}

	public List<Notice> getNoticeNewestLise() {
		return null;
	}

	public List<NoticeView> getNoticeList() {

		return getNoticeList("title", "", 1);
	}

	public List<NoticeView> getNoticeList(int page) {

		return getNoticeList("title", "", page);
	}

	public List<NoticeView> getNoticeList(String filed, String query, int page) {
		List<NoticeView> list = new ArrayList<>();

		String sql = "SELECT * FROM (" + "    SELECT ROWNUM NUM, N.* " + "    FROM (SELECT * FROM NOTICE_VIEW WHERE "
				+ filed + " LIKE ?  ORDER BY REGDATE DESC ) N " + ") " + "WHERE NUM BETWEEN ? AND ?";

		// 1, 11, 21 , 31 -> an = 1+(page-1)*10
		// 10, 20, 30, 40 -> page*10

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			st.setInt(2, 1 + (page - 1) * 10);
			st.setInt(3, page * 10);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				// String content = rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
				NoticeView notice = new NoticeView(id, title, writerId, regdate, hit, files, pub,
						// content,
						cmtCount);
				list.add(notice);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getNoticeCount() {
		return getNoticeCount("title", "");
	}

	public List<NoticeView> getNoticePubList(String filed, String query, int page) {
		List<NoticeView> list = new ArrayList<>();

		String sql = "SELECT * FROM (" + "    SELECT ROWNUM NUM, N.* " + "    FROM (SELECT * FROM NOTICE_VIEW WHERE "
				+ filed + " LIKE ? AND PUB = 1 ORDER BY REGDATE DESC ) N " + ") " + "WHERE NUM BETWEEN ? AND ?";

		// 1, 11, 21 , 31 -> an = 1+(page-1)*10
		// 10, 20, 30, 40 -> page*10

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			st.setInt(2, 1 + (page - 1) * 10);
			st.setInt(3, page * 10);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				// String content = rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
				NoticeView notice = new NoticeView(id, title, writerId, regdate, hit, files, pub,
						// content,
						cmtCount);
				list.add(notice);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getNoticeCount(String filed, String query) {
		int count = 0;

		String sql = "SELECT COUNT(ID) COUNT FROM (" + "    SELECT ROWNUM NUM, N.* "
				+ "    FROM (SELECT * FROM NOTICE WHERE " + filed + " LIKE ? ORDER BY REGDATE DESC ) N " + ") ";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	public int getPubNoticeCount(String filed, String query) {
		int count = 0;

		String sql = "SELECT COUNT(ID) COUNT FROM (" + "    SELECT ROWNUM NUM, N.* "
				+ "    FROM (SELECT * FROM NOTICE WHERE " + filed + " LIKE ? AND PUB = 1 ORDER BY REGDATE DESC ) N " + ") ";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}
	
	public Notice getNotice(int id) {
		Notice notice = null;
		String sql = "SELECT * FROM NOTICE WHERE ID = ?";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(nid, title, writerId, regdate, hit, files, content, pub);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notice;
	}

	public Notice getNextNotice(int id) {
		Notice notice = null;

		String sql = "SELECT * FROM (SELECT * FROM NOTICE ORDER BY REGDATE ASC)"
				+ " WHERE REGDATE > (SELECT REGDATE FROM NOTICE WHERE ID = ?)" + " AND ROWNUM = 1";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(nid, title, writerId, regdate, hit, files, content, pub);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Notice getPrevNotice(int id) {
		Notice notice = null;

		String sql = "SELECT * FROM (SELECT * FROM NOTICE ORDER BY REGDATE DESC)"
				+ " WHERE REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID = ?)" + " AND ROWNUM = 1";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				int nid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regdate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				boolean pub = rs.getBoolean("PUB");

				notice = new Notice(nid, title, writerId, regdate, hit, files, content, pub);
			}
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int deleteNoticeAll(int[] ids) {
		int result = 0;

		String params = "";

		for (int i = 0; i < ids.length; i++) {
			params += ids[i];
			if (i == ids.length - 1) {
				params += ",";
			}
		}

		String sql = "DELETE NOTICE WHERE ID IN (" + params + ")";

		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String className = "oracle.jdbc.driver.OracleDriver";
		String userid = "newlec";
		String pwd = "!han1689118";

		try {
			Class.forName(className);
			Connection con = DriverManager.getConnection(url, userid, pwd);
			Statement st = con.createStatement();
			result = st.executeUpdate(sql);

			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

}
