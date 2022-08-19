package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        return balance;
    }

    @Override
    public BigDecimal getBalanceByUsername(String username) {
        String sql = "SELECT balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
        return balance;
    }

    @Override
    public Long findAccountIdByUsername(String username) {
        String sql = "SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE username = ?;";
        Long accountId = jdbcTemplate.queryForObject(sql, Long.class, username);
        return accountId;
    }


    public boolean verifyAccount(Long id) {
        String sql = "SELECT account_id FROM account WHERE account_id = ?";
        try {
            Long accountId = jdbcTemplate.queryForObject(sql, Long.class, id);
            if (Objects.equals(accountId, id)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Invalid account");
            throw new NullPointerException("Invalid account");
        }

    }

    // not using at the moment
    @Override
    public List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Account account = mapRowToAccount(sqlRowSet);
            allAccounts.add(account);
        }
        return allAccounts;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setId(rs.getLong("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUserId(rs.getLong("user_id"));
        return account;
    }

}


