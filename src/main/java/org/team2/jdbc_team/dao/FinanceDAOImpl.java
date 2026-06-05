package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.AccountDetailVO;
import org.team2.jdbc_team.domain.AssetVO;
import org.team2.jdbc_team.domain.ExpenseVO;
import org.team2.jdbc_team.domain.MonthlyAssetVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinanceDAOImpl implements FinanceDAO {
    private final Connection conn = JDBCUtil.getConnection();

    @Override
    public int getMonthExpense(int memberNo, String yearMonth) {
        String sql = """
                SELECT IFNULL(SUM(amount), 0) AS total
                FROM `TRANSACTION`
                WHERE member_no = ?
                  AND transaction_type = '출금'
                  AND DATE_FORMAT(transaction_date, '%Y-%m') = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberNo);
            pstmt.setString(2, yearMonth);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("월별 지출 금액 조회 중 데이터베이스 오류 발생 [회원번호: " + memberNo + "]", e);
        }
        return 0;
    }

    @Override
    public List<ExpenseVO> categoryStatistics(int memberNo, String yearMonth) {
        List<ExpenseVO> list = new ArrayList<>();
        String sql = """
                SELECT category, SUM(amount) AS total
                FROM `TRANSACTION`
                WHERE member_no = ?
                  AND transaction_type = '출금'
                  AND DATE_FORMAT(transaction_date, '%Y-%m') = ?
                GROUP BY category
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberNo);
            pstmt.setString(2, yearMonth);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ExpenseVO vo = new ExpenseVO(
                            rs.getString("category"),
                            rs.getInt("total")
                    );
                    list.add(vo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("카테고리별 지출 통계 조회 중 데이터베이스 오류 발생 [회원번호: " + memberNo + "]", e);
        }
        return list;
    }

    @Override
    public AssetVO currentAsset(int memberNo) {
        String sql = """
                SELECT
                    (SELECT IFNULL(SUM(amount), 0) FROM ACCOUNT WHERE member_no = ?) AS asset,
                    (SELECT IFNULL(SUM(loan_amount), 0) FROM LOAN WHERE member_no = ?) AS loan
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberNo);
            pstmt.setInt(2, memberNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new AssetVO(rs.getInt("asset"), rs.getInt("loan"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("현재 자산 현황 조회 중 데이터베이스 오류 발생 [회원번호: " + memberNo + "]", e);
        }
        return null;
    }

    @Override
    public List<AccountDetailVO> getAssetDetails(int memberNo) {
        List<AccountDetailVO> list = new ArrayList<>();
        String sql = """
                SELECT '임출금 계좌' AS account_type, account_name AS name, amount 
                FROM ACCOUNT 
                WHERE member_no = ?
                UNION ALL
                SELECT '대출 부채' AS account_type, loan_name AS name, loan_amount AS amount 
                FROM LOAN 
                WHERE member_no = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberNo);
            pstmt.setInt(2, memberNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new AccountDetailVO(
                            rs.getString("account_type"),
                            rs.getString("name"),
                            rs.getInt("amount")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("자산 상세 내역 조회 중 데이터베이스 오류 발생 [회원번호: " + memberNo + "]", e);
        }
        return list;
    }

    @Override
    public List<MonthlyAssetVO> getMonthlyAssetChanges(int memberNo) {
        List<MonthlyAssetVO> list = new ArrayList<>();


        String sql = "SELECT " +
                "  DATE_FORMAT(transaction_date, '%Y-%m') AS asset_month, " +
                "  SUM(CASE WHEN transaction_type = '입금' THEN amount ELSE -amount END) AS net_asset " +
                "FROM `TRANSACTION` " +
                "WHERE member_no = ? " +
                "GROUP BY DATE_FORMAT(transaction_date, '%Y-%m') " +
                "ORDER BY asset_month ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new MonthlyAssetVO(
                            rs.getString("asset_month"),
                            rs.getInt("net_asset")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("월별 자산 변화 조회 중 데이터베이스 오류 발생 [회원번호: " + memberNo + "]", e);
        }
        return list;
    }
}