package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalanceByAccountId(Long accountId);

    BigDecimal getBalanceByUsername(String username);

    Long findAccountIdByUsername(String username);

    boolean verifyAccount(Long id);
    
    List<Account> getAllAccounts();
}
