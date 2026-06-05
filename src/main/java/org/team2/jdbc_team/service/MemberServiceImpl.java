package org.team2.jdbc_team.service;

import lombok.RequiredArgsConstructor;
import org.team2.jdbc_team.dao.MemberDao;
import org.team2.jdbc_team.domain.MemberVO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    final MemberDao dao;

    private String getString(String prompt) {
        System.out.print(prompt);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    @Override
    public void register() {
        System.out.println("\n[ 회원가입 ]");
        String email = getString(" - 이메일 > ");

        try {
            MemberVO member = dao.login(email);
            if (member != null) {
                System.out.println(">> 이미 등록된 이메일입니다.");
                return;
            }

            String password = getString(" - 비밀번호 > ");
            String name = getString(" - 이름 > ");
            String phone = getString(" - 전화번호 (ex. 010-1234-5678) > ");
            String birthStr = getString(" - 생년월일 (ex. YYYY-MM-DD) > ");
            LocalDate birthDate = LocalDate.parse(birthStr);

            MemberVO newMember = new MemberVO();
            newMember.setEmail(email);
            newMember.setPassword(password);
            newMember.setName(name);
            newMember.setPhone(phone);
            newMember.setBirthDate(birthDate);

            int result = dao.create(newMember);

            if (result > 0) {
                System.out.println("\n>> 회원가입이 완료되었습니다.");
            } else {
                System.out.println("\n>> 회원가입에 실패했습니다.");
            }

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println(">> 생년월일 형식이 올바르지 않습니다.");
        } catch (SQLException e) {
            System.out.println("\n>> 오류가 발생했습니다.\n" + e.getMessage());
        }
    }

    @Override
    public MemberVO login() {
        System.out.println("\n[ 로그인 ]");
        String email = getString(" - 이메일 입력 : ");
        String password = getString(" - 비밀번호 입력 : ");

        try {
            MemberVO member = dao.login(email);

            if (member == null) {
                System.out.println("\n>> 존재하지 않는 이메일입니다.");
                return null;
            }
            if (!member.getPassword().equals(password)) {
                System.out.println("\n>> 비밀번호가 일치하지 않습니다.");
                return null;
            }

            System.out.println("\n>> 로그인 성공! " + member.getName() + "님 환영합니다.");
            return member;
        } catch (SQLException e) {
            System.out.println("\n>> 오류가 발생했습니다.\n" + e.getMessage());
            return null;
        }
    }
}
