package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.domain.LoanVO;

import java.util.List;
import java.util.Optional;

public interface LoanDao {
    List<LoanVO> getList(int member_no);
    Optional<Integer> getTotalSumOfLoan(int member_no);
}
