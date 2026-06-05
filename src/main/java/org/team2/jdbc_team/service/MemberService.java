package org.team2.jdbc_team.service;

import org.team2.jdbc_team.domain.MemberVO;

public interface MemberService {
    void register();

    MemberVO login();
}
