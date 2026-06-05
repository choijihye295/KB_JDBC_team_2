package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.domain.AccountVO;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
    List<AccountVO> getAccountsByMember(int memberNo) throws SQLException;

    AccountVO getAccountDetail(String accountNo) throws SQLException;
}
