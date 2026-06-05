package org.team2.jdbc_team.service;

import lombok.RequiredArgsConstructor;
import org.team2.jdbc_team.dao.AccountDao;
import org.team2.jdbc_team.domain.AccountVO;
import org.team2.jdbc_team.domain.MemberVO;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountDao accountDao;

    @Override
    public void printMenu(MemberVO loginUser, Scanner scanner) {
        int loggedInMemberNo = loginUser.getMemberNo();
        boolean isChanged = true;

        while (isChanged) {
            System.out.println("\n============================");
            System.out.println("    2. 계좌조회              ");
            System.out.println("============================");
            System.out.println("1. 전체 계좌 조회");
            System.out.println("2. 계좌 상세 조회");
            System.out.println("0. 뒤로가기");
            System.out.print("\n 메뉴 선택 : ");

            int num_select = scanner.nextInt();
            scanner.nextLine();

            switch (num_select) {
                case 1:
                    ShowAllAccount(loggedInMemberNo);
                    break;
                case 2:
                    ShowDetailAccount(loggedInMemberNo);
                    break;
                case 0:
                    System.out.println("메인 메뉴로 돌아갑니다.");
                    isChanged = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }

    // 1. 전체 계좌 목록 조회
    private void ShowAllAccount(int memberNo) {
        System.out.println("============================");
        System.out.println("        전체 계좌 목록        ");
        System.out.println("============================");

        try {
            List<AccountVO> results = accountDao.getAccountsByMember(memberNo);

            if (results == null || results.isEmpty()) {
                System.out.println("조회된 계좌 정보가 없습니다.");
                System.out.println("\n >> 아무 키나 누르면 계좌 조회 메뉴로 돌아갑니다.");
                scanner.nextLine();
                return;
            }

            int rowNumber = 1;

            for (AccountVO row : results) {
                System.out.println("[" + rowNumber + "]");
                System.out.println("은행명   : " + row.getInstName());
                System.out.println("계좌번호 : " + row.getAccountNo());
                System.out.println("계좌종류 : " + row.getAccountType());
                System.out.printf("잔액 : %,d원\n", (row.getAmount() != null ? row.getAmount() : 0L));

                System.out.println();

                rowNumber++;
            }
        } catch (SQLException e) {
            System.out.println("데이터 조회 중 에러 발생: " + e.getMessage());
        }
    }

    // 2. 계좌 상세 조회
    private void ShowDetailAccount(int memberNo) {
        System.out.println("\n============================");
        System.out.println("   계좌 상세 조회              ");
        System.out.println("============================");

        try {
            List<AccountVO> results = accountDao.getAccountsByMember(memberNo);

            if (results == null || results.isEmpty()) {
                System.out.println("상세 조회를 진행할 계좌 정보가 없습니다.");
                System.out.println("\n >> 아무 키나 누르면 계좌 조회 메뉴로 돌아갑니다.");
                scanner.nextLine();
                return;
            }

            System.out.println("\n 계좌 번호를 고르세요.       ");
            System.out.println("----------------------------");
            for (int i = 0; i < results.size(); i++) {
                AccountVO acc = results.get(i);
                System.out.printf("%d. [%s] %s\n", (i + 1), acc.getInstName(), acc.getAccountNo());
            }
            System.out.println("----------------------------");
            System.out.print("\n > 계좌 번호 선택 : ");

            int choiceIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (choiceIndex < 0 || choiceIndex >= results.size()) {
                System.out.println("잘못된 번호를 선택하셨습니다.");
                return;
            }

            AccountVO selected = results.get(choiceIndex);

            if (selected != null) {
                System.out.println("============================");
                System.out.println(" [ " + selected.getInstName() + " 계좌 상세 ]");
                System.out.println("============================");
                System.out.println("계좌번호 : " + selected.getAccountNo());
                System.out.println("계좌종류 : " + selected.getAccountType());
                System.out.printf("잔액 : %,d원\n", (selected.getAmount() != null ? selected.getAmount() : 0L));
                System.out.println("개설일 : " + selected.getOpenDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                System.out.println("----------------------------");
            } else {
                System.out.println("계좌의 세부 정보를 찾을 수 없습니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("올바른 번호를 입력해 주세요.");
        } catch (SQLException e) {
            System.out.println("데이터 조회 중 에러 발생: " + e.getMessage());
        }

        System.out.println("\n >> 아무 키나 누르면 계좌 조회 메뉴로 돌아갑니다.");
        scanner.nextLine();

    }
}
