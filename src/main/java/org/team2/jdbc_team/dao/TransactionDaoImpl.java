package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.TransactionVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionDaoImpl implements TransactionDao {
    private String TRANSACTION_LIST =
            "SELECT * FROM TRANSACTION WHERE member_no = ? ORDER BY transaction_date DESC";

    private TransactionVO map(ResultSet rs, int memberNo) throws SQLException {
        return TransactionVO.builder()
                .transactionDate(rs.getObject("transaction_date", LocalDateTime.class))
                .amount(rs.getLong("amount"))
                .balanceAfter(rs.getLong("balance_after"))
                .category(rs.getString("category"))
                .accountNo(rs.getString("account_no"))
                .transactionType(rs.getString("transaction_type"))
                .description(rs.getString("description"))
                .memberNo(memberNo)
                .build();
    }

    @Override
    public List<TransactionVO> getTransactionsByMember(int memberNo) throws SQLException {
        List<TransactionVO> list = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(TRANSACTION_LIST)) {
            pstmt.setInt(1, memberNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, memberNo));
                }
            }
        }
        return list;
    }

    @Override
    public List<TransactionVO> searchTransactions(int memberNo, Map<String, String> searchParams) throws SQLException {
        String accountNo = searchParams.get("accountNo");
        String startDate = searchParams.get("startDate");
        String endDate = searchParams.get("endDate");
        String type = searchParams.get("type");
        String keyword = searchParams.get("keyword");

        //베이스 쿼리
        StringBuilder sql = new StringBuilder(
                "SELECT transaction_date, account_no, amount, balance_after, category, description, transaction_type " +
                        "FROM TRANSACTION WHERE member_no = ? "
        );

        // ? 에 넣을 값 저장할 리스트
        List<Object> queryParams = new ArrayList<>();
        queryParams.add(memberNo); //첫 번째 ? 자리는 무조건 memberNo

        // 사용자 입력한 조건 넣기
        if (accountNo != null && !accountNo.isEmpty()) {
            sql.append("AND account_no = ? ");
            queryParams.add(accountNo);
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND transaction_date >= ? ");
            queryParams.add(startDate + " 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND transaction_date <= ? ");
            queryParams.add(endDate + " 23:59:59");
        }
        if (type != null && !type.equals("전체") && !type.isEmpty()) {
            sql.append("AND transaction_type = ? ");
            queryParams.add(type);
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append("AND (category LIKE ? OR description LIKE ?) ");
            queryParams.add("%" + keyword + "%"); // category 용
            queryParams.add("%" + keyword + "%"); // description 용
        }

        sql.append("ORDER BY transaction_date DESC"); //무조건 수행

        List<TransactionVO> list = new ArrayList<>();
        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < queryParams.size(); i++) {
                pstmt.setObject(i + 1, queryParams.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, memberNo));
                }
            }
        }
        return list;
    }
}
