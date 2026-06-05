package org.team2.jdbc_team.app;

import org.team2.jdbc_team.dao.*;
import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.MemberVO;
import org.team2.jdbc_team.service.*;

import java.util.Scanner;

public class MyDataApp {
    MemberService memberService;
    TransactionService transactionService;
    AccountService accountService;
    FinanceApp financeApp;

    private MemberVO loginUser = null;

    MenuItem[] beforeLoginMenu;
    MenuItem[] afterLoginMenu;
    MenuItem[] curMenu;

    Scanner sc = new Scanner(System.in);

    public MyDataApp() {
        MemberDao member = new MemberDaoImpl();
        memberService = new MemberServiceImpl(member);
        TransactionDao transactionDao = new TransactionDaoImpl();
        transactionService = new TransactionServiceImpl(transactionDao);
        AccountDao accountDao = new AccountDaoImpl();
        accountService = new AccountServiceImpl(accountDao);
        this.financeApp = new FinanceApp();

        // 로그인 전 화면
        beforeLoginMenu = new MenuItem[] {
                new MenuItem("회원가입", memberService::register),
                new MenuItem("로그인", this::login),
                new MenuItem("종료", this::exit)
        };

        //dks6979@naver.com

        // 로그인 후 화면
        afterLoginMenu = new MenuItem[] {
                new MenuItem("계좌 조회", () -> accountService.printMenu(loginUser, sc)),
                new MenuItem("거래내역 조회", () -> transactionService.printMenu(loginUser, sc)),
                new MenuItem("소비 분석", () -> financeApp.showExpenseMenu(loginUser.getMemberNo(), sc)),
                new MenuItem("자산 관리", () -> financeApp.showAssetMenu(loginUser.getMemberNo(), sc)),
                new MenuItem("대출 및 보험 조회", () -> new LoanAndInsuranceApp(loginUser.getMemberNo()).run()),
                new MenuItem("로그아웃", this::logout)

        };

        curMenu = beforeLoginMenu;
    }

    private void login() {
        MemberVO user = memberService.login();
        if (user != null) {
            this.loginUser = user;
            this.curMenu = afterLoginMenu;
        }
    }

    private void logout() {
        System.out.println(">> 로그아웃 되었습니다.");
        this.loginUser = null;
        this.curMenu = beforeLoginMenu;
    }

    public void exit() {
        sc.close();
        JDBCUtil.close();
        System.exit(0);
    }

    public void printMenu() {
        System.out.println("\n===================================================");
        System.out.println("\t\t[ 마이데이터 통합 자산관리 시스템 ]");
        System.out.println("===================================================");
        for (int i = 0; i < curMenu.length; i++) {
            System.out.println(i+1 + ". " + curMenu[i].getTitle());
        }
        System.out.println("---------------------------------------------------");
    }

    public int getMenuIndex() {
        System.out.printf("> 선택 (1-%d): ", curMenu.length);
        String choice = sc.nextLine();

        try {
            int num = Integer.parseInt(choice);

            if (num < 1 || num > curMenu.length) {
                System.out.println("잘못된 번호입니다. 범위 내의 숫자를 입력해주세요.");
                return -1;
            }

            return num - 1;
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력할 수 있습니다.");
            return -1;
        }
    }

    public void run() {
        while (true) {
            printMenu();
            int ix = getMenuIndex();

            if (ix == -1) {
                continue;
            }

            Runnable command = curMenu[ix].getCommand();
            command.run();
        }
    }

    public static void main(String[] args) {
        MyDataApp app = new MyDataApp();
        app.run();
    }
}
