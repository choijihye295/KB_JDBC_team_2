package org.team2.jdbc_team.app;

import org.team2.jdbc_team.service.FinanceService;
import org.team2.jdbc_team.service.FinanceServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FinanceApp {
    private final FinanceService financeService = new FinanceServiceImpl();

    public void showExpenseMenu(int memberNo, Scanner scanner) {
        while (true) {
            System.out.println("\n소비 분석");
            System.out.println("-----------------------------------------");
            System.out.println("=========================================");
            System.out.println("[ 소비 분석 ]");
            System.out.println("=========================================");
            System.out.println("1. 이번 달 소비 리포트");
            System.out.println("2. 월별 소비 조회");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.println("-----------------------------------------");
            System.out.print("> 선택 : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    financeService.showExpenseReport(memberNo, currentMonth, "이번 달 소비");
                    System.out.println("\n>> 엔터를 누르면 소비 분석 메뉴로 이동합니다.");
                    scanner.nextLine();
                    break;

                case 2:
                    System.out.println("조회하고 싶은 년월을 입력해주세요");
                    System.out.print("> 입력 (YYYY-MM) : ");
                    String inputMonth = scanner.nextLine().trim();

                    if (!inputMonth.contains("-") || inputMonth.length() < 7) {
                        System.out.println("[경고] 올바른 형식(예: 2026-05)으로 입력해 주세요.");
                        System.out.println(">> 엔터를 누르면 메뉴로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    String[] dateParts = inputMonth.split("-");

                    String year = dateParts[0].trim();
                    String month = dateParts[1].trim();
                    String title = year + "년 " + month + "월 소비";

                    financeService.showExpenseReport(memberNo, inputMonth, title);

                    System.out.println("\n>> 엔터를 누르면 소비 분석 메뉴로 이동합니다.");
                    scanner.nextLine();
                    break;


                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    public void showAssetMenu(int memberNo, Scanner scanner) {
        while (true) {
            System.out.println("\n=========================================");
            System.out.println("5. 자산 관리");
            System.out.println("=========================================");
            System.out.println("1. 현재 자산 조회");
            System.out.println("2. 자산 상세 조회");
            System.out.println("3. 월별 자산 변화 조회");
            System.out.println("0. 뒤로가기");
            System.out.println("-----------------------------------------");
            System.out.print("메뉴 선택 : ");

            int subMenu = scanner.nextInt();
            scanner.nextLine();

            if (subMenu == 0) break;

            switch (subMenu) {
                case 1:
                    financeService.showCurrentAsset(memberNo);
                    System.out.println(">> 아무 키나 누르면 자산 관리 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                case 2:
                    financeService.showAssetDetails(memberNo);
                    System.out.println(">> 아무 키나 누르면 자산 관리 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                case 3:
                    financeService.showMonthlyAssetChanges(memberNo);
                    System.out.println(">> 아무 키나 누르면 자산 관리 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }
}
