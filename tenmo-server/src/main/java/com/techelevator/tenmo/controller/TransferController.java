package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.HttpConstraint;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;
    User user;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/transfers/{username}/history", method = RequestMethod.GET)
    public List<Transfer> transfersByUser(Principal principal, @PathVariable(name = "username") String username) throws AccessDeniedException {
        if (!Objects.equals(principal.getName(), username)) {
            throw new AccessDeniedException("ERROR: Access denied");
        }
        return transferDao.transferHistory(username);
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @RequestMapping(value = "/users/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getMyBalance(Principal principal, @PathVariable(name = "id") Long accountId) throws AccessDeniedException {
        if (!Objects.equals(accountDao.findAccountIdByUsername(principal.getName()), accountId)) {
            throw new AccessDeniedException("ERROR: Access denied");
        }
        return accountDao.getBalanceByAccountId(accountId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/transfers")
    public void createTransfer(Principal principal, @RequestBody @Valid TransferDTO newTransfer) throws AccessDeniedException {
        int checkNegative = newTransfer.getTransferAmount().compareTo(BigDecimal.ZERO);
        Transfer transfer = new Transfer(accountDao.findAccountIdByUsername(principal.getName()), newTransfer.getRecipientId(), newTransfer.getTransferAmount());

                if (!Objects.equals(accountDao.findAccountIdByUsername(principal.getName()), newTransfer.getSenderId())) {
                    throw new AccessDeniedException("ERROR: Must send money from your own account.");
                } else if (newTransfer.getRecipientId() == null) {
                    throw new AccessDeniedException("ERROR: Recipient account invalid.");
                } else if (!accountDao.verifyAccount(newTransfer.getRecipientId())) {
                    throw new AccessDeniedException("ERROR: Recipient account invalid.");
                } else if (Objects.equals(accountDao.findAccountIdByUsername(principal.getName()), newTransfer.getRecipientId())) {
                    throw new AccessDeniedException("ERROR: Cannot send money to yourself.");
                } else if ((newTransfer.getTransferAmount().compareTo(accountDao.getBalanceByUsername(principal.getName()))) == 1) {
                    throw new AccessDeniedException("ERROR: Insufficient funds.");
                } else if (checkNegative != 1) {
                    throw new AccessDeniedException("ERROR: Cannot send a negative value.");
                } else {
                    transferDao.createTransfer(transfer);
                    transferDao.executeTransfer(transfer);
                }
    }


    @PostAuthorize("#username == authentication.principal.username")
    @RequestMapping(value = "/transfers/{username}", method = RequestMethod.GET)
    public List<Transfer> transferHistory(Principal principal, @PathVariable(name = "username") String username) {
        return transferDao.transferHistory(principal.getName());
    }


}
