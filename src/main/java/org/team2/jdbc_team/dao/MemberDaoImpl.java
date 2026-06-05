package org.team2.jdbc_team.dao;

import org.team2.jdbc_team.common.JDBCUtil;
import org.team2.jdbc_team.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDaoImpl implements MemberDao {
    Connection conn = JDBCUtil.getConnection();

    @Override
    public int create(MemberVO member) throws SQLException {
        String sql = "insert into member (name, email, password, phone, birth_date) values (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPassword());
            stmt.setString(4, member.getPhone());
            stmt.setDate(5, java.sql.Date.valueOf(member.getBirthDate()));

            int count = stmt.executeUpdate();
            return count;
        }
    }

    @Override
    public MemberVO login(String email) throws SQLException {
        String sql = "select * from member where email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MemberVO member = new MemberVO();
                    member.setMemberNo(rs.getInt("member_no"));
                    member.setName(rs.getString("name"));
                    member.setEmail(rs.getString("email"));
                    member.setPassword(rs.getString("password"));
                    member.setPhone(rs.getString("phone"));
                    member.setBirthDate(rs.getDate("birth_date").toLocalDate());
                    member.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
                    return member;
                }
            }
        }
        return null;
    }
}
