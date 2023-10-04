package asysel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import makeFile.makeFile;

public class ASYSEL {
	private Properties prop = new Properties();
	private Properties prop2 = new Properties();
	private ArrayList<String> al = new ArrayList<String>();
	private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

	public ASYSEL() {

		Connection con = null;
		PreparedStatement psmt = null;
		makeFile mf = new makeFile();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String dName = formatter.format(date);


		try {
			prop.load(new FileInputStream("/home/pj394122/xtorm/JDBCProject01/config/info.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String driver = prop.getProperty("driver");
		String url = prop.getProperty("url");
		String id = prop.getProperty("id");
		String pw = prop.getProperty("pw");
		String logfile = null;
		try {
			long start = System.currentTimeMillis();
			Class.forName(driver);
			prop2.loadFromXML(new FileInputStream("/home/pj394122/xtorm/JDBCProject01/config/Query.xml"));
			String moveStart = formatter2.format(start);
			System.out.println("프로그램시작 : " + moveStart);
			al.add("프로그램시작 : " + moveStart);

			String sql = prop2.getProperty("ASYSEL");
			
			con = DriverManager.getConnection(url, id, pw);
			con.setAutoCommit(false);
			logfile = "/home/pj394122/xtorm/migLog/ASYSEL/ASYSELMove_"+dName;

			psmt = con.prepareStatement(sql);

			File f = new File("/home/pj394122/xtorm/JDBCProject01/config/EID.list");
			FileReader fr;

			fr = new FileReader(f);

			BufferedReader br = new BufferedReader(fr);
			
			String strEid = null;
			int result = 0;

			while ((strEid = br.readLine()) != null) {
				psmt.setString(1, strEid);
				result = psmt.executeUpdate();
				con.commit();
			}

				
				System.out.println("이관 성공");
				System.out.println("업무 이관 수 : " + result);
				al.add("업무 이관 수 : " + result);
				
				br.close();
			
			

		} catch (ClassNotFoundException e) {
			logfile = "/home/pj394122/xtorm/migLog/ASYSEL/Error_ASYSELMove"+dName;
			al.add("드라이버 오류");
			System.out.println("드라이버 오류");
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				con.rollback();
				logfile = "/home/pj394122/xtorm/migLog/ASYSEL/Error_ASYSELMove"+dName;
				System.out.println("이관 실패");
				al.add("이관 오류");
			} catch (SQLException e1) {
				logfile = "/home/pj394122/xtorm/migLog/ASYSEL/Error_DataMove" + dName;
				al.add("DB 오류");
				e1.printStackTrace();
				e1.printStackTrace();
			}
			
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		long end = System.currentTimeMillis();
		String moveEnd = formatter2.format(end);
		al.add("프로그램 종료 : " + moveEnd);
		System.out.println("프로그램 종료 : " + moveEnd);
		mf.makeFile(logfile, al, true);

	}

}
