package tennis;

import java.util.Scanner;

public class TennisGame {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String regex, sw;
		boolean flag;
		
		do {
			GameSet gs = new GameSet();
			gs.startPage();
			
			
			Player[] players = gs.getPlayers();
			int setNum = gs.getSetNum();
			int way = gs.getWay();
			int gender = gs.getGender();
			
			//계수기 생성
			TennisCounter tc = new TennisCounter(setNum, players,way,gender);
			tc.playingTennisGame(setNum);
			
			//게임 종료
			regex="[^yY]";
			System.out.print("경기를 재시작하시려먼 y, 끝내시려면 아무키나 누르세요.");
			sw = sc.next();
		} while (flag = !sw.matches(regex));
		System.out.println("======= 테니스 경기 종료 =======");
		sc.close();
	}
}