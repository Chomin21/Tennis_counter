package tennis;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class TennisCounter {
   // 선수 이름/팀
   private String teamName1;
   private String teamName2;
   private Player[] players;
   private String[] plyName1;
   private String[] plyName2;
   int way;
   int gender;

   // 새로운 세트마다 저장해놓고 초기화
   private Stack<String> gameList1;
   private Stack<String> gameList2;

   private Gameboard gameboard;

   int[] winningSetNum; // [A팀이 이긴 세트 수, B팀이 이긴 세트 수]
   int[] winningGameNum;// [A팀이 이긴 게임수, B팀이 이긴 게임 수]

   int setNum;
   private int p1, p2;

   private int dueceP1, dueceP2;

   private final String[] scoreTable = { "0", "15", "30", "40", "60" };
   private final String[] dueceScore = { "40", "40A" };

   public TennisCounter() {
   }

   public TennisCounter(int setNum, Player[] players,int way, int gender) {
      this.setNum = setNum;
      this.players = players;
      this.plyName1 = new String[players.length / 2];
      this.plyName2 = new String[players.length / 2];
      this.way = way;
      this.gender = gender;

      this.winningSetNum = new int[2];
      this.winningGameNum = new int[2];

      if (this.plyName1.length != 1 && this.plyName2.length != 1) {
         this.plyName1[0] = players[0].getName();
         this.plyName1[1] = players[1].getName();
         this.plyName2[0] = players[2].getName();
         this.plyName2[1] = players[3].getName();
         this.teamName1 = String.valueOf(players[0].getTeam());
         this.teamName2 = String.valueOf(players[2].getTeam());
      } else {
         this.plyName1[0] = players[0].getName();
         this.plyName2[0] = players[1].getName();
         this.teamName1 = String.valueOf(players[0].getTeam());
         this.teamName2 = String.valueOf(players[1].getTeam());
      }

      this.gameboard = new Gameboard(getSetNum(), getTeamName1(), getTeamName2(), getPlyName1(), getPlyName2(),
            getWinningSetNum(), getWinningGameNum(), getGameList1(), getGameList2(), getWay(), getGender());

   }

  

public void playingTennisGame(int setNum) {

      int finish = setNum / 2 + 1;
      for (int i = 0; i < setNum; i++) {

         winningGameNum[0] = 0;
         winningGameNum[1] = 0;

         if (winningSetNum[0] == finish || winningSetNum[1] == finish) {
            break;
         }

         initGameboard(i + 1);
         playingSet();

      }
      gameboard.dispSetBoard();
      System.out.printf("\t\t%s팀 WIN!!\n\n", getWinTeam());
   }

   private String getWinTeam() {
      return this.winningSetNum[0] > this.winningSetNum[1] ? "A" : "B";
   }

   public void initGameboard(int runningSetNum) {
      gameboard.clearGameBoard(runningSetNum);
      gameboard.plusGame();
   }

   public void playingGameboard() {
      gameboard.refreshInfo(getWinningSetNum(), getWinningGameNum(), getGameList1(), getGameList2());
      gameboard.dispScoreBoard();

   };

   public void playingSet() {

      int gameNum = 1; // 몇번째 게임인지
      this.p1 = 0;
      this.p2 = 0;
      this.gameList1 = new Stack<>();
      this.gameList2 = new Stack<>();

      while (winningGameNum[0] < 6 && winningGameNum[1] < 6) { // 6게임 미만인 경우 계속 진행
         this.gameList1.push("0");
         this.gameList2.push("0");

         if (!isGameDuece(winningGameNum)) {
            playingGame();
            gameboard.plusGame();
         } else {
            gameDueceRunning(winningGameNum, gameNum);
         }
      }

      if (winningGameNum[0] > winningGameNum[1])
         winningSetNum[0]++;
      else
         winningSetNum[1]++;
      FileUtil.storeGameInfo(way, gender, plyName1,plyName2,gameList1,gameList2,winningGameNum);

   }

   public void playingGame() {
      while (p1 < 4 && p2 < 4) { // 4포인트
         if (!isPointDuece(this.p1, this.p2)) {
            pointWinner(getRanNum());
         } else { // 듀스인 경우
            pointDueceRunning();
            break;
         }
      }

      if ((this.p1 - this.p2) > 0) {
         winningGameNum[0]++;
         this.playingGameboard();
         this.p1 = 0;
         this.p2 = 0;
      } else {
         winningGameNum[1]++;
         this.playingGameboard();
         this.p1 = 0;
         this.p2 = 0;
      }
   }

   private void gameDueceRunning(int[] winningGameNum, int gameNum) {

      while (Math.abs(winningGameNum[0] - winningGameNum[1]) < 2) {
         gameList1.push("0");
         gameList2.push("0");
         this.playingGameboard();
         playingGame();
         gameboard.plusGame();
      }
   }

   private boolean isGameDuece(int[] winningGameNum) {
      if (winningGameNum[0] == 5 && winningGameNum[1] == 5) {
         return true;
      }
      return false;
   }

   public void pointWinner(int p) {
      if (p == 1) {
         this.p1++;
         this.gameList1.pop();
         this.gameList1.push(scoreTable[p1]);
         this.playingGameboard();
      } else {
         this.p2++;
         this.gameList2.pop();
         this.gameList2.push(scoreTable[p2]);
         this.playingGameboard();
      }
   }

   public void pointDueceRunning() {
      while (Math.abs(this.dueceP1 - this.dueceP2) < 2) {
         if (getRanNum() == 1) {
            this.dueceP1++;

            if (gameList1.peek().equals(dueceScore[0]) && gameList2.peek().equals(dueceScore[1])) {
               this.gameList2.pop();
               this.gameList2.push(dueceScore[0]);
               this.playingGameboard();
            } else if (gameList1.peek().equals(dueceScore[0]) && gameList2.peek().equals(dueceScore[0])) {
               this.gameList1.pop();
               this.gameList1.push(dueceScore[1]);
               this.playingGameboard();
            }
            if (Math.abs(this.dueceP1 - this.dueceP2) < 2)
               continue;

         } else {
            this.dueceP2++;

            if (gameList2.peek().equals(dueceScore[0]) && gameList1.peek().equals(dueceScore[1])) {
               this.gameList1.pop();
               this.gameList1.push(dueceScore[0]);
               this.playingGameboard();
            } else if (gameList1.peek().equals(dueceScore[0]) && gameList2.peek().equals(dueceScore[0])) {
               this.gameList2.pop();
               this.gameList2.push(dueceScore[1]);
               this.playingGameboard();
            }
            if (Math.abs(this.dueceP1 - this.dueceP2) < 2)
               continue;
         }
      }

      if (this.dueceP1 > this.dueceP2) {
         this.gameList1.pop();
         this.gameList1.push(scoreTable[++this.p1]);
         this.playingGameboard();
      } else {
         this.gameList2.pop();
         this.gameList2.push(scoreTable[++this.p2]);
         this.playingGameboard();
      }

      // 듀스 포인트 초기화
      this.dueceP1 = 0;
      this.dueceP2 = 0;
   }

   private boolean isPointDuece(int p1, int p2) {
      if (p1 == 3 && p2 == 3) {
         return true;
      }
      return false;
   }

   // 랜덤 숫자 발생
   public int getRanNum() {
      Random rnd = new Random();
      return rnd.nextInt(2) + 1;
   }

   public void getEnter() {
      Scanner sc = new Scanner(System.in);
      if ("\n".equals(sc.nextLine())) {

      }
   }

   public int getSetNum() {
      return setNum;
   }

   public void setSetNum(int setNum) {
      this.setNum = setNum;
   }

   public String getTeamName1() {
      return teamName1;
   }

   public void setTeamName1(String teamName1) {
      this.teamName1 = teamName1;
   }

   public String getTeamName2() {
      return teamName2;
   }

   public void setTeamName2(String teamName2) {
      this.teamName2 = teamName2;
   }

   public int[] getWinningSetNum() {
      return winningSetNum;
   }

   public void setWinningSetNum(int[] winningSetNum) {
      this.winningSetNum = winningSetNum;
   }

   public int[] getWinningGameNum() {
      return winningGameNum;
   }

   public void setWinningGameNum(int[] winningGameNum) {
      this.winningGameNum = winningGameNum;
   }

   public String[] getPlyName1() {
      return plyName1;
   }

   public void setPlyName1(String[] plyName1) {
      this.plyName1 = plyName1;
   }

   public String[] getPlyName2() {
      return plyName2;
   }

   public void setPlyName2(String[] plyName2) {
      this.plyName2 = plyName2;
   }

   public Stack<String> getGameList1() {
      return gameList1;
   }

   public void setGameList1(Stack<String> gameList1) {
      this.gameList1 = gameList1;
   }

   public Stack<String> getGameList2() {
      return gameList2;
   }

   public void setGameList2(Stack<String> gameList2) {
      this.gameList2 = gameList2;
   }
   public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
}