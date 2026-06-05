package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.AccountVO;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    @Override
    public List<AccountVO> getAccountsByMember(int memberNo) throws SQLException {
        List<AccountVO> list = new ArrayList<>();
        String ACCOUNT_LIST =
                "SELECT A.*, FIN.inst_name " +
                        "FROM ACCOUNT AS A " +
                        "LEFT OUTER JOIN financial_institution AS FIN ON FIN.inst_code = A.inst_code " +
                        "WHERE A.member_no = ? " +
                        "ORDER BY A.inst_code";

        Connection conn = JDBCUtil.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(ACCOUNT_LIST)) {

            stmt.setInt(1, memberNo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AccountVO account = new AccountVO();

                    account.setAccountNo(rs.getString("account_no"));
                    account.setMemberNo(rs.getInt("member_no"));
                    account.setInstCode(rs.getInt("inst_code"));
                    account.setAccountName(rs.getString("account_name"));
                    account.setAccountType(rs.getString("account_type"));
                    account.setInstName(rs.getString("inst_name"));

                    if (rs.getBigDecimal("amount") != null) {
                        account.setAmount(rs.getBigDecimal("amount").toBigInteger());
                    }
                    if (rs.getTimestamp("open_date") != null) {
                        account.setOpenDate(rs.getTimestamp("open_date").toLocalDateTime());
                    }

                    list.add(account);

                }
            }
        }
        return list;
    }


    public AccountVO getAccountDetail(String accountNo) throws SQLException {
        String sql = "SELECT account_no, account_type, amount, open_date " +
                "FROM ACCOUNT " +
                "WHERE account_no = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AccountVO vo = new AccountVO();
                    vo.setAccountNo(rs.getString("account_no"));
                    vo.setAccountType(rs.getString("account_type"));
                    vo.setAmount(BigInteger.valueOf(rs.getLong("amount")));

                    if (rs.getTimestamp("open_date") != null) {
                        vo.setOpenDate(rs.getTimestamp("open_date").toLocalDateTime());
                    }
                    return vo;
                }
            }
        }
        return null;
    }

}
