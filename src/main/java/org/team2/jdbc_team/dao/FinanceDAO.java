package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.domain.AccountDetailVO;
import org.team2.jdbc_team.domain.AssetVO;
import org.team2.jdbc_team.domain.ExpenseVO;
import org.team2.jdbc_team.domain.MonthlyAssetVO;

import java.util.List;

public interface FinanceDAO {

    int getMonthExpense(int memberNo, String yearMonth);

    List<ExpenseVO> categoryStatistics(int memberNo, String yearMonth);

    AssetVO currentAsset(int memberNo);

    List<AccountDetailVO> getAssetDetails(int memberNo);

    List<MonthlyAssetVO> getMonthlyAssetChanges(int memberNo);
}