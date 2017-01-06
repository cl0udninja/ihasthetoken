package com.jelohazi.ihasthetoken.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jelohazi.ihasthetoken.domain.User;

@Repository
@Transactional
public class UserRepository implements com.jelohazi.ihasthetoken.jdbc.TypedRepository<User, UUID> {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserMapper userMapper = new UserMapper();

    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User(UUID.fromString(rs.getString("id")), rs.getString("name"), rs.getString("email"));
            return u;
        }
    }

    @Override
    public void createOrUpdate(User u) {
        if (u.getId() != null && get(u.getId()) != null) {
            this.jdbcTemplate.update("update token_users set name = ?, email = ? where id = ? ", u.getName(),
                    u.getEmail(), u.getId().toString());
        } else {
            this.jdbcTemplate.update("insert into token_users (id, name, email) values (?, ?, ?)", UUID.randomUUID(),
                    u.getName(), u.getEmail());
        }
    }

    @Override
    public User get(UUID id) {
        try {
            return jdbcTemplate.queryForObject("select id, name, email from token_users where id = ?",
                    new Object[] { id.toString() }, userMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No result found", e);
            return null;
        }
    }

    @Override
    public List<User> list() {
        return jdbcTemplate.query("select id, name, email from token_users order by name asc", userMapper);
    }
}
