package org.team2.jdbc_team.dao;

import org.junit.jupiter.api.*;
import org.team2.jdbc_team.common.JDBCUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDaoTest {
    // 테스트 대상이 되는 DAO 인스턴스 생성
    private final TransactionDao transactionDao = new TransactionDaoImpl();

    @AfterAll
    static void tearDown() {
        // 모든 테스트 완료 후 커넥션 종료
        JDBCUtil.close();
    }

    @Test
    @DisplayName("3.1 회원의 전체 거래 내역 리스트 조회를 검증한다.")
    @Order(1)
    public void testGetTransactionsByMember() throws SQLException {
        int testMemberNo = 1; // 테스트용 1번 회원

        // DAO 메서드 호출
        List<Map<String, Object>> transactions = transactionDao.getTransactionsByMember(testMemberNo);

        // 검증 (Assertions)
        Assertions.assertNotNull(transactions, "거래 내역 리스트 객체는 null일 수 없습니다.");

        if (!transactions.isEmpty()) {
            System.out.println("[Test 1 성공] 조회된 첫 번째 거래 데이터 확인:");
            Map<String, Object> firstRow = transactions.get(0);
            System.out.println("-> 계좌번호: " + firstRow.get("account_no"));
            System.out.println("-> 금액: " + firstRow.get("amount"));
            System.out.println("-> 카테고리: " + firstRow.get("category"));

            // 필수 데이터 컬럼들이 맵에 잘 담겼는지 key 검증
            Assertions.assertTrue(firstRow.containsKey("transaction_date"));
            Assertions.assertTrue(firstRow.containsKey("transaction_type"));
        } else {
            System.out.println("[Test 1 알림] DB에 회원 1번의 거래 데이터가 존재하지 않습니다.");
        }
    }

    @Test
    @DisplayName("3.2 조건부 상세 거래 내역 검색을 검증한다.")
    @Order(2)
    public void testSearchTransactions() throws SQLException {
        int testMemberNo = 1;

        // 상세 검색 조건 설정 (계좌번호, 기간, 유형 필터링)
        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("accountNo", "110-123-456789");
        searchParams.put("startDate", "2026-05-10");
        searchParams.put("endDate", "2026-05-25");
        searchParams.put("type", "출금"); // '전체', '입금', '출금' 중 선택 가능
        searchParams.put("keyword", "");   // 비워두면 검색어 제외 처리됨

        // DAO 메서드 호출
        List<Map<String, Object>> searchResults = transactionDao.searchTransactions(testMemberNo, searchParams);

        // 검증 (Assertions)
        Assertions.assertNotNull(searchResults, "상세 검색 결과 리스트 객체는 null일 수 없습니다.");

        System.out.println("\n[Test 2 성공] 상세 검색 조건 일치 개수: " + searchResults.size() + "건");

        for (Map<String, Object> row : searchResults) {
            // 조건대로 '출금' 데이터만 필터링 되었거나, 입력한 계좌번호가 일치하는지 단언문으로 확인해볼 수 있습니다.
            // (만약 DAO 쿼리에서 조인 대상 컬럼이 맞지 않으면 이 단계 이전에 SQLException이 터집니다.)
            Assertions.assertEquals("110-123-456789", row.get("account_no"));

            System.out.printf("-> [검색결과] 일시: %s | 금액: %s | 카테고리: %s\n",
                    row.get("transaction_date"), row.get("amount"), row.get("category"));
        }
    }
}
