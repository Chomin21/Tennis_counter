package tennis;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class GameSet {

	int gender, way, setNum;
	Player[] players;
	int[][] totalGender;

	public GameSet() {
		totalGender = new int[2][2]; // [남자 수, 여자 수]
	}

	public void startPage() {
		Scanner sc = new Scanner(System.in);
		String opt = "";
		String regex = "[12]";
		boolean flag = false;
		do {
			if (flag)
				System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");

			System.out.println("=============================");
			System.out.println("테니스 게임\n");
			System.out.println("(1) 시작");
			System.out.println("(2) 결과보기");
			System.out.println("=============================");
			opt = sc.next();

		} while (flag = !opt.matches(regex));
		if (opt.equals("1")) {
			createGame();
		} else if (opt.equals("2")) {
			playerPage();
		}
	}

	public void playerPage() {
		Scanner sc = new Scanner(System.in);
		String opt = "";
		String regex = "[123]";
		boolean flag = false;
		do {
			if (flag)
				System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");

			System.out.println("=============================");
			System.out.println("결과보기\n");
			System.out.println("(1) 선수 목록 보기");
			System.out.println("(2) 특정 선수의 모든 결과보기");
			System.out.println("(3) 이전으로 되돌아가기");
			System.out.println("=============================");
			opt = sc.next();
		} while (flag = !opt.matches(regex));

		if (opt.equals("1")) {
			playerListPage();
		} else if (opt.equals("2")) {
			playerResultPage();
		} else if (opt.equals("3")) {
			startPage();
		}
	}

	public void playerListPage() {
		Scanner sc = new Scanner(System.in);
		String opt = "";
		String regex = "[12]";
		boolean flag = false;
		System.out.println("=============================");
		System.out.println("선수 목록 보기\n");
		//선수 목록 쭊 보여주기
		FileUtil.showPlysList();
		do {
			if (flag)
				System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");

			System.out.println("=============================");
			System.out.println("(1) 특정 선수의 모든 결과보기");
			System.out.println("(2) 이전으로 되돌아가기");
			System.out.println("=============================");
			opt = sc.next();
		} while (flag = !opt.matches(regex));
		if (opt.equals("1")) {
			playerResultPage();
		} else if (opt.equals("2")) {
			playerPage();
		}
	}

	public void playerResultPage() {
		Scanner sc = new Scanner(System.in);
		String opt = "";
		String plyName = null;
		String regex = "[12]";
		int flag2;
		boolean flag = false;
		String pathName = ".\\src\\tennis\\players";
		File f = new File(pathName);
		do {
			if (flag)
				System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");

			System.out.println("=============================");
			System.out.println("선수 결과 보기\n");
			
			if (!f.exists()) {
				System.out.println("현재 등록된 선수 목록이 존재하지 않습니다.");
			}
			else {
				do {
					System.out.print("선수 이름을 입력해주세요 : ");
					plyName = sc.next();
					flag2 =  plyNameValidation( plyName);
				} while (flag2==3);
				
				if(flag2 == 1) {
					FileUtil.onePlyGameInfo(plyName);
				}else {
					System.out.println("존재하지 않는 선수입니다.");
				}
				
			}
			
			
			System.out.println("=============================");
			System.out.println("(1) 첫 화면으로 돌아가기");
			System.out.println("(2) 이전으로 돌아가기");
			opt = sc.next();
		} while (flag = !opt.matches(regex));

		if (opt.equals("1")) {
			startPage();
		} else if (opt.equals("2")) {
			playerPage();
		}
	}

	public int plyNameValidation(String plyName) { //입력을 계속받으려면 false
		String regex2 = "[가-힣a-zA-Z].*";
		if (plyName.matches(regex2)) {
			
			String pathName = ".\\src\\tennis\\players";
			File f = new File(pathName);
			for (String s : f.list()) {
				if (s.equals(plyName+".txt")) {
					return 1;
				}
			}
			return 2;
		} else {
			System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");
			return 3;
		}
	}

	public void createGame() {
		String sw, gender, way, regex;
		boolean flag = false;
		Scanner sc = new Scanner(System.in);
		Player[] players = null;
		regex = "[123]";
		do {
			if (flag)
				System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");
			System.out.print("> 성별을 선택해주세요. (1) 남자 (2) 여자 (3) 혼성 : ");
			gender = sc.next();
		} while (flag = !gender.matches(regex));
		setGender(Integer.parseInt(gender));

		regex = "[12]";
		do {
			if (flag)
				System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");
			if (getGender() == 3) {
				System.out.println("혼성은 자동으로 복식입니다.");
				way = "2";
			} else {
				System.out.print("> 경기 방식을 선택해주세요. (1)단식 (2) 복식 : ");
				way = sc.next();
			}

		} while (flag = !way.matches(regex));

		setWay(Integer.parseInt(way));
		calcSetNum(getGender(), getWay());

		// 선수들 입력받기
		switch (Integer.parseInt(way)) {
		case 1: // 단식
			players = new Player[2];
			inputPlayer(players);
			break;
		case 2: // 복식
			players = new Player[4];
			inputPlayer(players);
			break;
		}
		setPlayers(players);
	}

	public void calcSetNum(int gender, int way) {
		if (gender == 1)
			setSetNum(5);
		else
			setSetNum(3);
	}

	public void inputPlayer(Player[] players) {
		Scanner sc = new Scanner(System.in);
		int num = 1;
		int len = players.length;
		String name, gender;
		char team = 'A';
		boolean flag = false;
		for (int i = 0; i < len; i++) {
			if (i == len / 2) {
				team = 'B';
				num = 1;
			}
			do {
				if (flag)
					System.out.println("유효하지 않은 값이 있습니다. 올바르게 다시 입력해주세요.");
				System.out.printf("%c팀 %d번 선수의 이름과 성별을 입력해주세요 (ex. 홍길동 남) : ", team, num);
				name = sc.next();
				gender = sc.next();
			} while (flag = !checkPlyValidation(getWay(), getGender(), name, gender, team));
			players[i] = new Player(name, gender.equals("남") ? true : false, team);
			FileUtil.writePlyInfo(players[i]);
			num++;
		}
	}

	private boolean checkPlyValidation(int way, int genderKind, String name, String gender, char team) {
		String nameRegex = "[가-힣a-zA-Z].*";
		String genderRegex = "[남여]";

		if (!(gender.matches(genderRegex) && name.matches(nameRegex) && !FileUtil.plyNameDuplicated(name))) {
			return false;
		}

		switch (genderKind) {
		case 1: // 남자
			if (gender.equals("여")) {
				System.out.println("남자만 입력할 수 있습니다.");
				return false;
			}
			break;
		case 2: // 여자
			if (gender.equals("남")) {
				System.out.println("여자만 입력할 수 있습니다.");
				return false;
			}
			break;
		case 3: // 혼성

			if (totalGender[0][0] == 1 && totalGender[0][1] == 1 && totalGender[1][0] == 1 && totalGender[1][1] == 1)
				return true;
			else {
				if (team == 'A') {
					if (gender.equals("남")) {
						if (totalGender[0][0] == 1)
							return false;
						totalGender[0][0]++;
					} else {
						if (totalGender[0][1] == 1)
							return false;
						totalGender[0][1]++;
					}
				} else {
					if (gender.equals("남")) {
						if (totalGender[1][0] == 1)
							return false;
						totalGender[1][0]++;
					} else {
						if (totalGender[1][1] == 1)
							return false;
						totalGender[1][1]++;
					}
				}
			}
			break;
		}
		return true;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public void setSetNum(int setNum) {
		this.setNum = setNum;
	}

	public int getSetNum() {
		return setNum;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getWay() {
		return way;
	}

	public void setWay(int way) {
		this.way = way;
	}
}
