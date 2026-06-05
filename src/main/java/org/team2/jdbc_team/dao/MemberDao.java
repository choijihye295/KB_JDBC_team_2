package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.domain.MemberVO;

import java.sql.SQLException;

public interface MemberDao {
    // 회원가입
    int create(MemberVO member) throws SQLException;

    // 로그인
    MemberVO login(String email) throws SQLException;
}
