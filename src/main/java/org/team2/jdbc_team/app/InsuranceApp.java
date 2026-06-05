package org.team2.jdbc_team.app;

import org.team2.jdbc_team.dao.InsuranceDao;
import org.team2.jdbc_team.dao.InsuranceDaoImpl;
import org.team2.jdbc_team.domain.InsuranceVO;

import java.util.List;

public class InsuranceApp implements Runnable{
    private InsuranceDao insuranceDao = new InsuranceDaoImpl();
    int memberNo;
    private static final String DOUBLE_LINE ="\n=============================================================================================================";
    private static final String SINGLE_LINE ="\n-------------------------------------------------------------------------------------------------------------";
    private final String[] insuranceList = {"보험명","보험사","월 납입금","만기일","납입계좌"};
    private final int[] insuranceWidthList = {18, 14, 16, 14, 30};
    public InsuranceApp(int memberNo){
        this.memberNo = memberNo;
    }
    private int getMemberNo(){
        return memberNo;
    }

    @Override
    public void run() {
        List<InsuranceVO> insuranceLists =  insuranceDao.getInsuranceList(memberNo);
        if(insuranceLists.isEmpty()){
            System.out.println(DOUBLE_LINE);
            System.out.println("가입한 보험이 없습니다.");
            System.out.println(DOUBLE_LINE);
        }
        else{
            System.out.println(DOUBLE_LINE);
            insuranceHeader();
            System.out.println(DOUBLE_LINE);
            insuranceBody(insuranceLists);
            System.out.println(DOUBLE_LINE);
            insuranceEnd();
            System.out.println(DOUBLE_LINE);
        }
    }

    public void insuranceHeader(){
        printCenter("보험 조회");
    }
    private void printCenter(String text) {
        int padding = (DOUBLE_LINE.length() - text.length()) / 2;
        System.out.printf("%" + (padding + text.length()) + "s%n", text);
    }

    public void insuranceBody(List<InsuranceVO> insuranceLists){
        printCenter("[보험 목록]");
        System.out.println(SINGLE_LINE);
        printTitleInsuranceList();
        System.out.println(SINGLE_LINE);
        printUserInsuranceList(insuranceLists);
    }
    public void printTitleInsuranceList(){
        for(int i=0;i<insuranceWidthList.length;i++){
            System.out.printf("%-" + insuranceWidthList[i] +"s",insuranceList[i]);
        }
    }

    public void printUserInsuranceList(List<InsuranceVO> insuranceLists){
        for(InsuranceVO insurance : insuranceLists){
            int index = 0;

            printColumn(insurance.getInsurance_name(), insuranceWidthList[index++]);
            printColumn(insurance.getInst_name(), insuranceWidthList[index++]);
            printColumn(String.format("%,d원", insurance.getMonthly_payment()), insuranceWidthList[index++]);
            printColumn(String.valueOf(insurance.getMaturity_date()), insuranceWidthList[index++]);
            printColumn(String.valueOf(insurance.getInsurance_account()), insuranceWidthList[index++]);

            System.out.println("|");
        }
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

    private void insuranceEnd(){
        System.out.printf("총 월 납입금 합계"+"%,d"+"원",insuranceDao.getSumMonthlyPayment(memberNo).orElse(0L));
    }


}
