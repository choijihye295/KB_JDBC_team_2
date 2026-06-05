package org.team2.jdbc_team.app;

import org.team2.jdbc_team.dao.LoanDao;
import org.team2.jdbc_team.dao.LoanDaoImpl;
import org.team2.jdbc_team.domain.LoanVO;

import java.util.List;

public class LoanApp implements Runnable{
    private LoanDao loanDao = new LoanDaoImpl();
    private final String[] loanStringlist = {"대출명", "기관명","대출금액","금리","만기일","납입계좌"};
    private final int[] loanWidthList = {18, 14, 16, 10, 14, 30};
    private static final String DOUBLE_LINE =
            "=======================================================================================================================";
    private static final String SINGLE_LINE="-----------------------------------------------------------------------------------------------------------------------";
    private int member_no;

    public LoanApp(int member_no){
        this.member_no = member_no;
    }
    public int get_member_no(){
        return member_no;
    }


    @Override
    public void run() {
        List<LoanVO> listLoan = loanDao.getList(get_member_no());
        if(listLoan.isEmpty()){
            printSingleLine();
            System.out.println("현재 대출중인 상품이 없습니다.");
            printSingleLine();
        }
        else {
            printDoubleLine();
            loanHeader();
            printDoubleLine();
            loanBody(listLoan);
            printDoubleLine();
            loanEnd();
            printDoubleLine();
        }
        return;
    }

    private void printDoubleLine(){
        System.out.println("\n"+DOUBLE_LINE);
    }



    private void printCenter(String text) {
        int padding = (DOUBLE_LINE.length() - text.length()) / 2;
        System.out.printf("%" + (padding + text.length()) + "s%n", text);
    }

    private void loanHeader(){
        printCenter("대출조회");
    }

    private void loanBody(List<LoanVO> listLoan){
        printCenter("[대출 목록]");
        printSingleLine();
        printLoanBodyTitle();
        printSingleLine();
        printUserLoanList(listLoan);
    }

    private void printSingleLine(){
        System.out.println(SINGLE_LINE);
    }

    private void printLoanBodyTitle(){
        for(int i = 0; i < loanStringlist.length; i++){
            printColumn(loanStringlist[i], loanWidthList[i]);
        }
        System.out.println("|");
    }

    private void printUserLoanList(List<LoanVO> listLoan){
        for(LoanVO loan : listLoan){
            int index = 0;

            printColumn(loan.getLoan_name(), loanWidthList[index++]);
            printColumn(loan.getInst_name(), loanWidthList[index++]);
            printColumn(String.format("%,d원", loan.getLoan_amount()), loanWidthList[index++]);
            printColumn(String.valueOf(loan.getInterest_rate()), loanWidthList[index++]);
            printColumn(String.valueOf(loan.getLoan_date()), loanWidthList[index++]);
            printColumn(loan.getLoan_account(), loanWidthList[index++]);

            System.out.println("|");
        }
    }

    private void loanEnd(){
        System.out.printf("총 대출금액  :   %,d", loanDao.getTotalSumOfLoan(member_no).orElse(0));
    }

    private int getDisplayWidth(String text) {
        int width = 0;

        for (char ch : text.toCharArray()) {
            if (ch >= '가' && ch <= '힣') {
                width += 2;
            } else {
                width += 1;
            }
        }

        return width;
    }

    private void printColumn(String text, int width) {
        if (text == null) {
            text = "";
        }

        System.out.print("| " + text);

        int padding = width - getDisplayWidth(text);

        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }

        System.out.print(" ");
    }




}
