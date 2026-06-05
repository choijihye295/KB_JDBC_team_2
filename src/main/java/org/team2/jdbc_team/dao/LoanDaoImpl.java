package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.LoanVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDaoImpl implements LoanDao{
    Connection conn = JDBCUtil.getConnection();
    private static final String SELECT_LOAN_INFORMATION_BY_MEMBER_NO =
            """
            SELECT loan_name AS '대출명', inst_name  AS '기관명', loan_amount  AS '대출금액', interest_rate  AS '금리',loan_date AS '만기일', concat(inst_name, '(' , account_no ,')') AS '납입계좌'
            FROM MEMBER
            INNER JOIN LOAN
            USING(member_no)
            INNER JOIN FINANCIAL_INSTITUTION
            USING(inst_code)
            WHERE member_no=?
            ORDER BY member_no ASC
            """;
    private static final String SELECT_LOAN_SUM_AMOUNT_BY_MEMBER_NO =
            """
            SELECT SUM(loan_amount) AS '총 대출금액'
            FROM LOAN
            WHERE member_no =?
            """;

    private LoanVO loanMap(ResultSet rs) throws SQLException {
        return LoanVO.builder()
                .loan_name(rs.getString("대출명"))
                .inst_name(rs.getString("기관명"))
                .loan_amount(rs.getLong("대출금액"))
                .interest_rate(rs.getBigDecimal("금리"))
                .loan_date(rs.getDate("만기일"))
                .loan_account(rs.getString("납입계좌"))
                .build();
    }
    @Override
    public List<LoanVO> getList(int member_no) {
        try(PreparedStatement pstmt = conn.prepareStatement(SELECT_LOAN_INFORMATION_BY_MEMBER_NO)){
            int index = 1;
            pstmt.setInt(index++,member_no);
            try(ResultSet rs = pstmt.executeQuery()){
                List<LoanVO> loanList = new ArrayList<>();
                while(rs.next()) {
                    loanList.add(loanMap(rs));
                }
                return loanList;
            }


        }
        catch(SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Integer> getTotalSumOfLoan(int member_no) {
        try(PreparedStatement pstmt = conn.prepareStatement(SELECT_LOAN_SUM_AMOUNT_BY_MEMBER_NO)){
            int index = 1;
            pstmt.setInt(index++,member_no);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()) {
                    return Optional.of(rs.getInt("총 대출금액"));
                }
                return Optional.empty();
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
