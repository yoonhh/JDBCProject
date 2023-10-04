package makeFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class makeFile {

	public boolean makeFile(String fileName, ArrayList<String> alContent, boolean bAppend) {

		boolean isok;

		try {
			FileWriter fw = new FileWriter(fileName, bAppend);
			BufferedWriter bw = new BufferedWriter(fw);// FileWriter와 BufferedWrter를 함께쓰면 성능이 올라감

			String strContent;
			for (int i = 0; i < alContent.size(); i++) {
				strContent = alContent.get(i);
				bw.write(strContent);// 내용 출력
				bw.newLine();
			}

			bw.flush();// 남아있는 데이터를 모두 출력
			bw.close();// 스트림을 닫음
			fw.close();// 스트림을 닫음

			isok = true;

		} catch (Exception e) {
			e.printStackTrace();
			isok = false;
		}
		return isok;// 
	}

}
