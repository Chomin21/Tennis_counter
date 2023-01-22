package tennis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Gameboard {
	private ArrayList<String>[] gameBoard = new ArrayList[3];
	private int setNum;
	private int way;
	private int gender;
	private String teamName1;
	private String teamName2;
	private String[] plyName1;
	private String[] plyName2;
	private int[] winningSetNum;
	private int[] winningGameNum;
	private Stack<String> gameList1;
	private Stack<String> gameList2;

	public Gameboard(int setNum, String teamName1, String teamName2, String[] plyName1, String[] plyName2,
			int[] getWinningSetNum, int[] winningGameNum, Stack<String> gameList1, Stack<String> gameList2, int way,
			int gender) {
		gameBoard[0] = new ArrayList<String>();
		gameBoard[1] = new ArrayList<String>();
		gameBoard[2] = new ArrayList<String>();

		this.setNum = setNum;
		this.teamName1 = teamName1;
		this.teamName2 = teamName2;
		this.plyName1 = plyName1;
		this.plyName2 = plyName2;
		this.winningSetNum = getWinningSetNum;
		this.winningGameNum = winningGameNum;
		this.gameList1 = gameList1;
		this.gameList2 = gameList2;
	}

	public void refreshInfo(int[] getWinningSetNum, int[] getWinningGameNum, Stack<String> getGameList1,
			Stack<String> getGameList2) {
		this.winningSetNum = getWinningSetNum;
		this.winningGameNum = getWinningGameNum;
		this.gameList1 = getGameList1;
		this.gameList2 = getGameList2;
	}

	public void enterKeyEvent() {
		Scanner sc = new Scanner(System.in);
		sc.nextLine();

	}

	public void clearGameBoard(int runningSetNum) {
		this.gameBoard[0].clear();
		this.gameBoard[1].clear();
		this.gameBoard[2].clear();

		// 초기화
		// 첫행 시작
		this.gameBoard[0].add(0, runningSetNum + "세트    ");

		// 둘째&셋째행 시작 : 복식때 출력되는 팀이름과 선수이름
		if (this.plyName1.length != 1 && this.plyName2.length != 1) {
			this.gameBoard[1].add(0, this.teamName1 + "/" + this.plyName1[0] + ", " + this.plyName1[1]);
			this.gameBoard[2].add(0, this.teamName2 + "/" + this.plyName2[0] + ", " + this.plyName2[1]);
		}
		// 둘째&셋째행 시작 : 단식때 출력되는 팀이름과 선수이름
		else {
			this.gameBoard[1].add(0, this.teamName1 + "/" + this.plyName1[0]);
			this.gameBoard[2].add(0, this.teamName2 + "/" + this.plyName2[0]);
		}
	}

	// 한 게임이 끝날 때 추가
	public void plusGame() {
		// 현재 진행중인 게임과 각팀의 점수 공간 추가
		this.gameBoard[0].add(winningGameNum[0] + winningGameNum[1] + 1 + "게임");
		this.gameBoard[1].add("0");
		this.gameBoard[2].add("0");
	}

	// 세트 계기판 출력
	public void dispSetBoard() {
		System.out.println();
		System.out.println();
		System.out.println(
				"================================================================================================");
		System.out.println(">>각 팀이 승리한 세트");
		System.out.printf("\tA팀 : %s\t", winningSetNum[0]);
		System.out.printf("\tB팀 : %s\n", winningSetNum[1]);
		System.out.println(
				"================================================================================================");
		System.out.println();
	}

	// 점수판 출력
	public void printScoreBoard() {
		// 첫째 행 출력 ( 진행중인 세트, 숫자 기록 )
		if (this.gameBoard[0].size() > 11) {
			System.out.printf("%s\t\t\t", this.gameBoard[0].get(0));
			for (int i = this.gameBoard[0].size() - 10; i < this.gameBoard[0].size(); i++) {
				if (this.gameBoard[0].get(i).length() > 3)
					System.out.printf("%s\t", this.gameBoard[0].get(i));
				else
					System.out.printf("%s\t\t", this.gameBoard[0].get(i));
			}
		} else {
			System.out.printf("%s\t\t\t", this.gameBoard[0].get(0));
			for (int i = 1; i < this.gameBoard[0].size(); i++) {
				if (this.gameBoard[0].get(i).length() > 3)
					System.out.printf("%s\t", this.gameBoard[0].get(i));
				else
					System.out.printf("%s\t\t", this.gameBoard[0].get(i));
			}
		}
		System.out.println("승리한 게임");

		// 둘째 행 출력 ( A팀의 선수이름, 게임 점수 기록 )
		if (this.gameBoard[1].size() > 11) {
			System.out.printf("%s\t\t\t", this.gameBoard[1].get(0));
			for (int i = this.gameBoard[1].size() - 10; i < this.gameBoard[1].size(); i++) {
				System.out.printf("%s\t\t", this.gameBoard[1].get(i));
			}
		} else {
			System.out.printf("%s\t\t\t", this.gameBoard[1].get(0));
			for (int i = 1; i < this.gameBoard[1].size(); i++) {
				System.out.printf("%s\t\t", this.gameBoard[1].get(i));
			}
		}
		System.out.printf("%d", this.winningGameNum[0]);
		System.out.println();

		// 셋째 행 출력 ( B팀의 선수이름, 게임 점수 기록 )
		if (this.gameBoard[2].size() > 11) {
			System.out.printf("%s\t\t\t", this.gameBoard[2].get(0));
			for (int i = this.gameBoard[2].size() - 10; i < this.gameBoard[2].size(); i++) {
				System.out.printf("%s\t\t", this.gameBoard[2].get(i));
			}
		} else {
			System.out.printf("%s\t\t\t", this.gameBoard[2].get(0));
			for (int i = 1; i < this.gameBoard[2].size(); i++) {
				System.out.printf("%s\t\t", this.gameBoard[2].get(i));
			}
		}
		System.out.printf("%d", this.winningGameNum[1]);
		System.out.println(
				"\n\n================================================================================================");
	}

	// 포인트를 진행할 때 마다 호출
	public void dispScoreBoard() { // 현재 게임 포인트 출력
		// 초기 0점 점수판 출력
		if ((this.gameBoard[1].get(gameBoard[1].size() - 1) == "0")
				&& (this.gameBoard[2].get(gameBoard[1].size() - 1) == "0")) {
			this.dispSetBoard();
			this.printScoreBoard();
		}

		// 엔터 이벤트
		enterKeyEvent();
		 //자동으로 게임 진행
//		   try {
//			   Thread.sleep(100);			   
//		   } catch (Exception e) {
//			   e.printStackTrace();
//		   }

		// 세트 계기판 출력
		this.dispSetBoard();

		// 진행중인 게임 점수판 추가
		if (this.gameList1 != null) {
			this.gameBoard[1].remove(this.gameBoard[1].size() - 1);
			this.gameBoard[2].remove(this.gameBoard[2].size() - 1);
			this.gameBoard[1].add(this.gameList1.peek());
			this.gameBoard[2].add(this.gameList2.peek());
		}

		// 진행중인 게임 점수판 출력
		printScoreBoard();
	}
}
