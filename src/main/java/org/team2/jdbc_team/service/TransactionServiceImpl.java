package org.team2.jdbc_team.service;

import lombok.RequiredArgsConstructor;
import org.team2.jdbc_team.dao.TransactionDao;
import org.team2.jdbc_team.domain.MemberVO;
import org.team2.jdbc_team.domain.TransactionVO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final Scanner scanner = new Scanner(System.in);
    private final TransactionDao transactionDao;

    @Override
    public void printMenu(MemberVO loginUser, Scanner scanner) {
        int memberNo = loginUser.getMemberNo();

        while (true) {
            System.out.println("\n=================================");
            System.out.println(" 2. 거래내역 조회");
            System.out.println("=================================");
            System.out.println(" 1. 전체 거래 내역 조회");
            System.out.println(" 2. 상세 거래 내역 검색");
            System.out.println(" 0. 뒤로가기");
            System.out.print("\n메뉴 선택 : ");

            String menu = scanner.nextLine();

            // memberNo는 main에서 들어온다고 가정
            if (menu.equals("1")) {
                showAllTransactions(memberNo);
            } else if (menu.equals("2")) {
                searchDetailedTransactions(memberNo);
            } else if (menu.equals("0")) {
                System.out.println("메인 메뉴로 돌아갑니다.");
                break;
            } else {
                System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
            }
        }
    }

    private void showAllTransactions(int memberNo) {
        System.out.println("\n==========================================================================");
        System.out.println("2.1 전체 거래 내역 조회");
        System.out.println("==========================================================================");

        System.out.printf("%-14s | %-10s | %-10s | %-5s | %s\n", "거래일시", "금액", "잔액", "카테고리", "계좌");

        System.out.println("--------------------------------------------------------------------------");

        try {
            List<TransactionVO> transactions = transactionDao.getTransactionsByMember(memberNo);

            for (TransactionVO vo : transactions) {
                long amount = vo.getAmount();
                String type = vo.getTransactionType();

                LocalDateTime date = vo.getTransactionDate();
                String dateStr = date.toString().replace("T", " ").substring(0, 16);

                String amountStr = ("출금".equals(type) || amount < 0)
                        ? "-" + String.format("%,d", Math.abs(amount))
                        : "+" + String.format("%,d", amount);

                System.out.printf("%s | %10s | %10s원 | %-4s | %s\n",
                        dateStr,
                        amountStr,
                        String.format("%,d", vo.getBalanceAfter()),
                        vo.getCategory(),
                        vo.getAccountNo());
            }
        } catch (SQLException e) {
            System.out.println("데이터 조회 오류: " + e.getMessage());
        }
    }

    private void searchDetailedTransactions(int memberNo) {
        System.out.println("\n=================================");
        System.out.println("2.2 상세 거래 내역 검색");
        System.out.println("=================================");

        Map<String, String> searchParams = new HashMap<>();
        System.out.print("계좌번호 입력: "); searchParams.put("accountNo", scanner.nextLine().trim());
        System.out.print("시작일자 입력 (YYYY-MM-DD): "); searchParams.put("startDate", scanner.nextLine().trim());
        System.out.print("종료일자 입력 (YYYY-MM-DD): "); searchParams.put("endDate", scanner.nextLine().trim());

        // 거래유형 숫자로 선택
        String type = "전체";
        while (true) {
            System.out.print("거래유형 선택 (1.전체 / 2.입금 / 3.출금): ");
            String typeMenu = scanner.nextLine().trim();
            if (typeMenu.equals("1") || typeMenu.isEmpty()) { type = "전체"; break; }
            if (typeMenu.equals("2")) { type = "입금"; break; }
            if (typeMenu.equals("3")) { type = "출금"; break; }
            System.out.println("잘못된 번호입니다. 1~3번 중에서 선택해주세요.");
        }
        searchParams.put("type", type);

        System.out.print("검색어 입력 (없으면 Enter): "); searchParams.put("keyword", scanner.nextLine().trim());

        System.out.print("\nEnter 입력 시 조회 / q 입력 시 전체 메뉴 > ");
        if (scanner.nextLine().equalsIgnoreCase("q")) return;

        System.out.println("\n=================================");
        System.out.println("2.2 상세 거래 내역 조회 결과");
        System.out.println("=================================");

        try {
            List<TransactionVO> results = transactionDao.searchTransactions(memberNo, searchParams);

            if (results.isEmpty()) {
                System.out.println("조건에 맞는 거래 내역이 존재하지 않습니다.");
                return;
            }

            for (TransactionVO vo : results) {
                LocalDateTime date = vo.getTransactionDate();
                System.out.println("일시: " + date.toString().substring(0, 16));
                System.out.println("계좌: " + vo.getAccountNo());
                System.out.printf("금액: %,d원\n", vo.getAmount());
                System.out.printf("잔액: %,d원\n", vo.getBalanceAfter());
                System.out.println("카테고리: " + vo.getCategory());
                System.out.println("내용: " + (vo.getDescription() == null || vo.getDescription().isEmpty() ? "입력 X" : vo.getDescription()));
                System.out.println("---------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("상세 검색 중 에러 발생: " + e.getMessage());
        }
    }
}
