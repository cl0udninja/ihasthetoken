package com.jelohazi.ihasthetoken.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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

import com.jelohazi.ihasthetoken.domain.Token;
import com.jelohazi.ihasthetoken.domain.TokenStatus;

@Repository
@Transactional
public class TokenRepository implements com.jelohazi.ihasthetoken.jdbc.TypedRepository<Token, UUID> {
    private static final Logger logger = LoggerFactory.getLogger(TokenRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TokenMapper tokenMapper = new TokenMapper();

    private static final class TokenMapper implements RowMapper<Token> {
        @Override
        public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
            Token t = new Token(UUID.fromString(rs.getString("id")),
                    UUID.fromString(rs.getString("user_id")),
                    rs.getTimestamp("created"),
                    rs.getString("status"));
            return t;
        }
    }

    @Override
    public void createOrUpdate(Token t) {
        if (t.getId() != null) {
            Token dbToken = get(t.getId());
            if (dbToken != null) {
                this.jdbcTemplate.update("update token_queue set status = ? where id = ? ",
                        t.getStatus().toString(),
                        t.getId().toString());
                if (!hasInProgressToken()) {
                    nextTokenToProcess();
                }
            } else {
                this.jdbcTemplate.update("insert into token_queue (id, user_id, created, status) "
                        + "values (?, ?, ?, ?)",
                        t.getId(), t.getUserId(), new Date(),
                        t.getStatus() != null ? t.getStatus().toString() : TokenStatus.CREATED.toString());
            }
        } else {
            this.jdbcTemplate.update("insert into token_queue (id, user_id, created, status) "
                    + "values (?, ?, ?, ?)",
                    UUID.randomUUID(), t.getUserId(), new Date(),
                    t.getStatus() != null ? t.getStatus().toString() : TokenStatus.CREATED.toString());
        }
    }

    @Override
    public Token get(UUID id) {
        try {
            return jdbcTemplate.queryForObject("select id, user_id, created, status from token_queue where id = ?",
                    new Object[] { id.toString() }, tokenMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("No result found", e);
            return null;
        }
    }

    @Override
    public List<Token> list() {
        return jdbcTemplate.query("select id, user_id, created, status from token_queue where status <> 'RELEASED'"
                + " order by created asc",
                tokenMapper);
    }

    public boolean hasInProgressToken() {
        return jdbcTemplate.queryForObject("select count(*) from token_queue where status=?",
                new Object[] { TokenStatus.IN_PROGRESS.toString() }, Integer.class) > 0;
    }

    private void nextTokenToProcess() {
        List<Token> next = jdbcTemplate
                .query("select id, user_id, created, status from token_queue where status = 'CREATED'"
                        + " order by created asc limit 1", tokenMapper);
        if (!next.isEmpty()) {
            next.get(0).setStatus(TokenStatus.IN_PROGRESS);
            createOrUpdate(next.get(0));
        }
    }
}
