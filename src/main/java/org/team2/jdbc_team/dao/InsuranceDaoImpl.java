package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.InsuranceVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsuranceDaoImpl implements InsuranceDao{
    Connection conn = JDBCUtil.getConnection();
    private final String insuranceSql = """
            SELECT insurance_name  AS '보험명', inst_name AS '보험사' , monthly_payment AS '월 납입금', maturity_date AS '만기일' , concat(inst_name, '(' , account_no ,')') AS '납입계좌'
            FROM MEMBER
            INNER JOIN INSURANCE
            USING(member_no)
            INNER JOIN FINANCIAL_INSTITUTION
            USING(inst_code)
            WHERE member_no = ?
            ORDER BY insurance_no ASC;
            """;

    private final String getSumMonthlyPaymentSql = """
            SELECT SUM(monthly_payment) as '총 월 납입금액'
            FROM INSURANCE
            WHERE member_no = ?
            """;

    private InsuranceVO insuranceMap(ResultSet rs) throws SQLException {
        return InsuranceVO.builder()
                .insurance_name(rs.getString("보험명"))
                .inst_name(rs.getString("보험사"))
                .monthly_payment(rs.getLong("월 납입금"))
                .maturity_date(rs.getDate("만기일"))
                .insurance_account(rs.getString("납입계좌"))
                .build();
                //String insurance_name;
        //    String inst_name;
        //    long monthly_payment;
        //    Date maturity_date;
        //    String insurance_account;
    }
    @Override
    public List<InsuranceVO> getInsuranceList(int member_no) {
        try(PreparedStatement pstmt = conn.prepareStatement(insuranceSql)){
            List<InsuranceVO> InsuranceList = new ArrayList<>();
            int index = 1;
            pstmt.setInt(index++,member_no);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    InsuranceList.add(insuranceMap(rs));
                }
                return InsuranceList;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Long> getSumMonthlyPayment(int member_no) {
        try(PreparedStatement pstmt = conn.prepareStatement(getSumMonthlyPaymentSql)){
            int index = 1;
            pstmt.setInt(index++, member_no);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return Optional.of(rs.getLong("총 월 납입금액"));
                }

            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
