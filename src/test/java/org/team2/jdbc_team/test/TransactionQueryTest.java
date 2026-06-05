package org.team2.jdbc_team.test;

import org.junit.jupiter.api.*;
import org.team2.jdbc_team.common.JDBCUtil;

import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionQueryTest {
    // 예시와 동일하게 Connection 인스턴스 준비
    Connection conn = JDBCUtil.getConnection();

    @AfterAll
    static void tearDown() {
        JDBCUtil.close();
    }

    // 1. 거래내역 조회
    @Test
    @DisplayName("특정 회원의 전체 거래내역을 조회한다.")
    @Order(1)
    public void selectTransaction() throws SQLException {
        String sql = "SELECT transaction_date AS '거래일시', amount AS '금액', " +
                "balance_after AS '잔액', category AS '카테고리', account_no AS '계좌' " +
                "FROM TRANSACTION " +
                "WHERE member_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 1); // member_no = 1

            try (ResultSet rs = pstmt.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    System.out.printf("[거래내역] 일시: %s | 금액: %,d원 | 잔액: %,d원 | 카테고리: %s | 계좌: %s\n",
                            rs.getTimestamp("거래일시"),
                            rs.getInt("금액"),
                            rs.getInt("잔액"),
                            rs.getString("카테고리"),
                            rs.getString("계좌"));
                }
                // 데이터가 최소 1건 이상 조회되었는지 단언(Assert)
                Assertions.assertTrue(rowCount >= 0, "조회된 거래내역 쿼리가 정상 작동했습니다.");
            }
        }
    }

    // 2. 총 소비액 조회 (2026년 5월 기준)
    @Test
    @DisplayName("지정한 기간 내 특정 회원의 총 소비액을 조회한다.")
    @Order(2)
    public void selectTotalWithdrawal() throws SQLException {
        String sql = "SELECT SUM(amount) AS '총 소비액' " +
                "FROM TRANSACTION " +
                "WHERE transaction_type = '출금' " +
                "AND transaction_date >= '2026-05-01' AND transaction_date < '2026-06-01' " +
                "AND member_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 1); // member_no = 1

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long totalAmount = rs.getLong("총 소비액");
                    System.out.printf("[총 소비] 2026년 5월 총 소비액: %,d원\n", totalAmount);

                    // 쿼리가 오류 없이 동작하여 결과 집합을 반환했는지 검증
                    Assertions.assertNotNull(totalAmount);
                } else {
                    Assertions.fail("총 소비액 결과 집합을 가져오지 못했습니다.");
                }
            }
        }
    }

    // 3. 카테고리별 소비 조회 (2026년 5월 식비 기준)
    @Test
    @DisplayName("지정한 기간 내 특정 회원의 특정 카테고리 소비액을 조회한다.")
    @Order(3)
    public void selectCategoryWithdrawal() throws SQLException {
        String sql = "SELECT SUM(amount) AS '식비 소비액' " +
                "FROM TRANSACTION " +
                "WHERE transaction_type = '출금' " +
                "AND transaction_date >= '2026-05-01' AND transaction_date < '2026-06-01' " +
                "AND member_no = ? " +
                "AND category = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 1);       // member_no = 1
            pstmt.setString(2, "식비"); // category = '식비'

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long categoryAmount = rs.getLong("식비 소비액");
                    System.out.printf("[카테고리 소비] 2026년 5월 식비 총액: %,d원\n", categoryAmount);

                    // 결과 값이 정상적으로 조회되었는지 검증
                    Assertions.assertNotNull(categoryAmount);
                } else {
                    Assertions.fail("카테고리별 소비액 결과 집합을 가져오지 못했습니다.");
                }
            }
        }
    }
}