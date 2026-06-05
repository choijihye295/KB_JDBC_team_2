package org.team2.jdbc_team.service;

import org.team2.jdbc_team.dao.FinanceDAO;
import org.team2.jdbc_team.dao.FinanceDAOImpl;
import org.team2.jdbc_team.domain.AccountDetailVO;
import org.team2.jdbc_team.domain.AssetVO;
import org.team2.jdbc_team.domain.ExpenseVO;
import org.team2.jdbc_team.domain.MonthlyAssetVO;

import java.time.LocalDate;
import java.util.List;

public class FinanceServiceImpl implements FinanceService {

    private final FinanceDAO dao = new FinanceDAOImpl();

    @Override
    public void showExpenseReport(int memberNo, String yearMonth, String title) {

        int totalExpense = dao.getMonthExpense(memberNo, yearMonth);
        List<ExpenseVO> list = dao.categoryStatistics(memberNo, yearMonth);

        System.out.println("=========================================");
        System.out.printf("[ %s ]\n", title);
        System.out.println("=========================================");
        System.out.printf("• 이번 달 중 사용 금액: %,d원\n\n", totalExpense);
        System.out.println("[ 카테고리별 지출 통계 ]");

        if (list.isEmpty()) {
            System.out.println("지출 내역이 없습니다.");
        } else {
            for (ExpenseVO vo : list) {
                vo.setTotalAmount(totalExpense);
                System.out.println(vo);
            }
        }
        System.out.println("-----------------------------------------");
    }

    @Override
    public void showCurrentAsset(int memberNo) {
        AssetVO asset = dao.currentAsset(memberNo);
        System.out.println("=========================================");
        System.out.println("[ 현재 자산 현황 ]");
        System.out.println("=========================================");
        System.out.println("기준일자 : " + LocalDate.now());
        System.out.println("-----------------------------------------");
        if (asset != null) {
            System.out.printf("총 자산 : %,d원\n", asset.getTotalAsset());
            System.out.printf("총 부채 : %,d원\n", asset.getTotalLoan());
            System.out.printf("순자산 : %,d원\n", asset.getNetAsset());
        } else {
            System.out.println("자산 정보가 존재하지 않습니다.");
        }
        System.out.println("-----------------------------------------");
    }

    @Override
    public void showAssetDetails(int memberNo) {
        List<AccountDetailVO> details = dao.getAssetDetails(memberNo);
        System.out.println("=========================================");
        System.out.println("[ 자산 상세 조회 ]");
        System.out.println("=========================================");

        String currentType = "";
        for (AccountDetailVO vo : details) {
            if (!vo.getAccountType().equals(currentType)) {
                currentType = vo.getAccountType();
                System.out.printf("\n[%s]\n", currentType);
            }
            System.out.printf("%s : %,d원\n", vo.getName(), vo.getAmount());
        }
        System.out.println("-----------------------------------------");
    }

    @Override
    public void showMonthlyAssetChanges(int memberNo) {
        List<MonthlyAssetVO> history = dao.getMonthlyAssetChanges(memberNo);
        System.out.println("=========================================");
        System.out.println("[ 월별 자산 변화 ]");
        System.out.println("=========================================");
        for (MonthlyAssetVO vo : history) {
            System.out.printf("%s : %,d원\n", vo.getYearMonth(), vo.getNetAsset());
        }
        System.out.println("-----------------------------------------");
    }
}