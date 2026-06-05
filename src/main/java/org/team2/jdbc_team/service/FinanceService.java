package org.team2.jdbc_team.service;

public interface FinanceService {

    void showExpenseReport(int memberNo, String yearMonth, String title);

    void showCurrentAsset(int memberNo);

    void showAssetDetails(int memberNo);

    void showMonthlyAssetChanges(int memberNo);
}