package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.domain.InsuranceVO;

import java.util.List;
import java.util.Optional;

public interface InsuranceDao {
    public List<InsuranceVO> getInsuranceList(int member_no);
    public Optional<Long> getSumMonthlyPayment(int member_no);
}
