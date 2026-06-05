package org.team2.jdbc_team.app;

import java.util.Scanner;

public class LoanAndInsuranceApp implements Runnable {

    private static final String DOUBLE_LINE =
            "\n=================================================================================================";
    private static final String SINGLE_LINE ="\n-------------------------------------------------------------------------------------------------";


    private int memberNo;
    public LoanAndInsuranceApp(int memberNo){
        this.memberNo = memberNo;
    }
    public int getMemberNo(){
        return memberNo;
    }

    private Scanner sc = new Scanner(System.in);

    @Override
    public void run() {
        Runnable[] function = {
                new LoanApp(memberNo),
                new InsuranceApp(memberNo)
        };

        while (true) {
            printLoanAndInsuranceHeader();
            printLoanAndInsuranceBody();
            int userInput = printLoanAndInsuranceEnd();

            if(userInput == 0){
                System.out.println("메인 메뉴로 돌아갑니다.");
                return;
            }

            function[userInput-1].run();
        }

    }

    public void printDOUBLE_LINE(){
        System.out.println(DOUBLE_LINE);
    }

    public void printLoanAndInsuranceHeader(){
        printDOUBLE_LINE();
        printCenter("대출/보험 조회");
        printDOUBLE_LINE();
    }

    private void printCenter(String text) {
        int padding = (DOUBLE_LINE.length() - text.length()) / 2;
        System.out.printf("%" + (padding + text.length()) + "s%n", text);
    }

    private void printLoanAndInsuranceBody(){
        printCenter("1. 대출조회");
        printCenter("2. 보험조회");
        printCenter("0. 뒤로가기");
    }

    private int printLoanAndInsuranceEnd() {
        printDOUBLE_LINE();
        int userInput = -99;
        while(true) {
            System.out.println("메뉴선택 >> ");
            try {
                userInput = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요");
                continue;
            }
            if(userInput <= -1 || userInput >= 3){
                System.out.println("0~2사이 숫자를 입력해주세요.");
                continue;
            }
            if(userInput>= 0 && userInput <= 2){
                break;
            }
        }
        return userInput;
    }
}
