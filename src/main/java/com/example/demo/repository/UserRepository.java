package com.example.demo.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserEntity;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserEntity findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        List<UserEntity> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserEntity user = new UserEntity();
            user.setUserId(rs.getInt("user_id"));
            user.setUserName(rs.getString("user_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setAddress(rs.getString("address"));
            return user;
        }, email);

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }
}
