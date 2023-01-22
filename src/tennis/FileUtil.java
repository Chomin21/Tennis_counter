package tennis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

public class FileUtil {

	// 선수 정보 저장

	public static void writePlyInfo(Player ply) {
		String parent = ".\\src\\tennis\\players";
		String child = ply.getName() + ".txt";

		File f = new File(parent);
		File fileName = new File(parent, child);
		if (!f.exists() || !f.isDirectory())
			f.mkdir();

		try (FileOutputStream out = new FileOutputStream(fileName);
				ObjectOutputStream oos = new ObjectOutputStream(out)) {
			oos.writeObject(ply);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 선수 정보 리스트 가져와서 출력하기
	public static void showPlysList() {
		String pathName = ".\\src\\tennis\\players";
		File f = new File(pathName);
		if( !f.exists()) {
			System.out.println("현재 등록된 선수 목록이 존재하지 않습니다.");
		} else {
			System.out.println("이름\t\t성별\t팀");
			for (File s : f.listFiles()) {
				try (FileInputStream in = new FileInputStream(s); ObjectInputStream ois = new ObjectInputStream(in)) {
					Player m2 = (Player) ois.readObject();

					System.out.printf("%s\t\t%s\t%c\n", m2.getName(), m2.getGender() ? "남" : "여", m2.getTeam());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	// 선수 이름 중복 확인
	public static boolean plyNameDuplicated(String name) { //중복이면 true
		String pathName = ".\\src\\tennis\\players";
		File f = new File(pathName);
		
		if(!f.exists()) {
			return false;
		}
		String[] temp = f.list();
		for (String s : f.list()) {
			if (s.equals(name+".txt")) {
				
				System.out.println("해당 선수가 존재합니다.");
				return true;
			}
		}
		return false;
	}
	

	// player 전체 지우기
	public static void plyListDelete() {
		String pathName = ".\\src\\tennis\\players";
		File f = new File(pathName);

		for (File t : f.listFiles()) {
			t.delete();
		}
	}
	// 그동안의 기록 모두 지우기

	// 특정 선수가 게임한 세트 읽어오기
	public static void onePlyGameInfo(String plyName) {

		String path = ".\\src\\tennis\\playerGameList";
		File f = new File(path);
		
		
		File[] fList = f.listFiles();
		ArrayList<File> showList = new ArrayList<>();
		int way = 0, gender = 0;
		String[] plyName1 = new String[2];
		String[] plyName2 = new String[2];
		String str;
		int[] winningGameNum = new int[2];
		ArrayList<String> gameList1 = new ArrayList<>();
		ArrayList<String> gameList2 = new ArrayList<>();
		int[] setScores = new int[2];
		int duece = -1;
		int g = 0;

		// 특정 선수 이름이 포함된 파일 찾기
		for (File t : fList) {
			try (FileReader fr = new FileReader(t); BufferedReader br = new BufferedReader(fr)) {
				while ((str = br.readLine()) != null) {
					if (str.contains(plyName)) {
						showList.add(t);
						break;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < showList.size(); i++) {
			gameList1 = new ArrayList<>();
			gameList2 = new ArrayList<>();
			
			try (FileReader fr = new FileReader(showList.get(i)); BufferedReader br = new BufferedReader(fr)) {
				
				if (i == 0) {
					str = br.readLine();
					way = Integer.parseInt(str);
					str = br.readLine();
					gender = Integer.parseInt(str);
					str = br.readLine();
					if (str.contains(",")) {
						plyName1 = str.split(",");
						str = br.readLine();
						plyName2 = str.split(",");
					} else {
						plyName1[0] = str;
						str = br.readLine();
						plyName2[0] = str;
					}

				} else {
					g = 0;
					while (g++ < 4) {
						br.readLine();
					}
				}
				str = br.readLine();
				String[] strTemp = str.split(",");
				winningGameNum[0] = Integer.parseInt(strTemp[0]);
				winningGameNum[1] = Integer.parseInt(strTemp[1]);
				if (winningGameNum[0] > winningGameNum[1])
					setScores[0]++;
				else
					setScores[1]++;

				String str1, str2;
				String[] strArr1, strArr2;
				
				// 점수 쪼개기
				str1 = br.readLine();
				str2 = br.readLine();

				strArr1 = str1.split("\\s*,\\s*|\\[|\\]");
				strArr2 = str2.split("\\s*,\\s*|\\[|\\]");

				duece = -1;
				for (int m = 1; m < strArr1.length; m++) {
					if ((strArr1[m].equals(strArr2[m])) && ("0".equals(strArr1[m]))) {
						duece = m - 1;
						continue;
					} 
					gameList1.add(strArr1[m]);
					gameList2.add(strArr2[m]);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			disPlyScore(way, gender, plyName1, plyName2, gameList1, gameList2, winningGameNum, duece, i);
			
		}
		System.out.println();

		System.out.println("<승리한 세트 결과>");
		System.out.printf("\tA팀 : %d\t", setScores[0]);
		System.out.printf("\tB팀 : %d\n", setScores[1]);
	}

	public static void disPlyScore(int way, int gender, String[] plyName1, String[] plyName2,
			ArrayList<String> gameList1, ArrayList<String> gameList2, int[] winningGameNum, int duece
			,int setNum) {
		int len = gameList1.size();
		for(int m = 0; m<len;m++) {
			System.out.print("================");
		}
		System.out.printf("\n\n<세트 %d>\n", setNum+1 );
		if (plyName1[1] == null) {
			System.out.printf("\tA팀 / 선수 : %s\n", plyName1[0]);
			System.out.printf("\tB팀 / 선수 : %s\n", plyName2[0]);
		} else {
			System.out.printf("\tA팀 / 선수 : %s, %s\n", plyName1[0], plyName1[1]);
			System.out.printf("\tB팀 / 선수 : %s, %s\n", plyName2[0], plyName2[1]);
		}
		if(duece != -1) {
			System.out.printf("\t듀스 %d번째 게임에서 발생\n", duece);
		}
		for(int m = 0; m<len;m++) {
			System.out.print("- - - - - - - - - - - - - - - - ");
		}

		Iterator<String> ir1 = gameList1.iterator();
		Iterator<String> ir2 = gameList2.iterator();
		
		System.out.println();
		for (int i = 0; i < len; i++) {
			System.out.printf("%-2d게임\t", i + 1);
		}
		System.out.println();
		while (ir1.hasNext()) {
			System.out.printf("%s\t\t",  ir1.next());

		}
		System.out.println();
		while (ir2.hasNext()) {
			System.out.printf("%s\t\t", ir2.next());
		}
		System.out.println();
		for(int m = 0; m<len;m++) {
			System.out.print("- - - - - - - - - - - - - - - - ");
		}
		System.out.println();
		System.out.println("<승리한 게임 결과>");
		System.out.printf("A팀 : %d\t", winningGameNum[0]);
		System.out.printf("B팀 : %d\n\n", winningGameNum[1]);
	
		System.out.println();

	}

	// 결과 하나씩 저장하기
	public static void storeGameInfo(int way, int gender, String[] plyName1, String[] plyName2, Stack<String> gameList1,
			Stack<String> gameList2, int[] winningGameNum) {
		String parent = ".\\src\\tennis\\playerGameList";
		String child;

		// 디렉토리 없으면 만들기
		File f = new File(parent);
		if (!f.exists() || !f.isDirectory())
			f.mkdir();

		int maxNum;
		int[] numList = new int[f.list().length];

		if (numList.length == 0) {
			maxNum = 0;
		} else {
			String[] strList = f.list();
			int ext = strList[0].lastIndexOf(".txt");
			for (int i = 0; i < strList.length; i++) {
				numList[i] = Integer.parseInt(strList[i].substring(0,ext));
			}
			Arrays.sort(numList);

			maxNum = numList[numList.length - 1];

		}

		child = Integer.toString(++maxNum) +".txt" ;

		// players아래 목록 다 가져와서 숫자 중 제일 큰 숫자로 파일 이름 지정.
		File fileName = new File(parent, child);

		// 파일에 저장하기
		try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(Integer.toString(way));
			bw.newLine();
			bw.write(Integer.toString(gender));
			bw.newLine();
			if (plyName1.length == 1) {
				bw.write(String.format("%s", plyName1[0]));
				bw.newLine();
				bw.write(String.format("%s", plyName2[0]));
			} else {
				bw.write(String.format("%s,%s", plyName1[0], plyName1[1]));
				bw.newLine();
				bw.write(String.format("%s,%s", plyName2[0], plyName2[1]));
			}

			bw.newLine();
			bw.write(String.format("%s,%s", winningGameNum[0], winningGameNum[1]));
			bw.newLine();
			bw.write(gameList1.toString());
			bw.newLine();
			bw.write(gameList2.toString());
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
