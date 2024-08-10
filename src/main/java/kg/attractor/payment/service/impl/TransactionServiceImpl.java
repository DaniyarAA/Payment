package kg.attractor.payment.service.impl;

import kg.attractor.payment.dao.AccountDao;
import kg.attractor.payment.dao.TransactionDao;
import kg.attractor.payment.dao.UserDao;
import kg.attractor.payment.dto.SendMoneyDto;
import kg.attractor.payment.dto.TransactionDto;
import kg.attractor.payment.errors.NotFoundAccountException;
import kg.attractor.payment.errors.NotFoundUserException;
import kg.attractor.payment.model.Account;
import kg.attractor.payment.model.Transaction;
import kg.attractor.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final UserDao userDao;

    @Override
    public List<TransactionDto> getHistory(Long accountId){
        var list = transactionDao.getHistory(accountId);
        return list.stream()
                .map(e -> TransactionDto.builder()
                        .id(e.getId())
                        .senderId(e.getSenderId())
                        .receiverId(e.getReceiverId())
                        .amount(e.getAmount())
                        .status(e.getStatus())
                        .createdAt(e.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public void makeTransaction(SendMoneyDto sendMoneyDto){
        Account senderAccount = accountDao.getByAccountNumber(sendMoneyDto.getReceiverAccountNumber())
                .orElseThrow(() -> new NotFoundAccountException("Not found sender account number"));
        Account receiverAccount = accountDao.getByAccountNumber(sendMoneyDto.getReceiverAccountNumber())
                .orElseThrow(() -> new NotFoundAccountException("Not found receiver account number"));
        if(senderAccount.getUserId() != getCurrentUserId()){
            throw new RuntimeException("Sender account is not the current user");
        }

        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            throw new RuntimeException("Currencies of the accounts do not match");
        }

        if (senderAccount.getBalance().compareTo(sendMoneyDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds in sender's account");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(sendMoneyDto.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(sendMoneyDto.getAmount()));

        accountDao.updateAccount(senderAccount);
        accountDao.updateAccount(receiverAccount);

        Transaction transaction = Transaction.builder()
                .senderId(senderAccount.getId())
                .receiverId(receiverAccount.getId())
                .amount(sendMoneyDto.getAmount())
                .status(sendMoneyDto.getAmount().compareTo(BigDecimal.TEN) > 0 ? "PENDING" : "COMPLETED")
                .createdAt(LocalDateTime.now())
                .build();
        transactionDao.saveTransaction(transaction);
    }


    @Override
    public List<TransactionDto> getAllTransactions(){
        var list = transactionDao.getAllTransactions();
        return list.stream()
                .map(e -> TransactionDto.builder()
                        .id(e.getId())
                        .senderId(e.getSenderId())
                        .receiverId(e.getReceiverId())
                        .amount(e.getAmount())
                        .status(e.getStatus())
                        .createdAt(e.getCreatedAt())
                        .build())
                .toList();
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDao.getUserByPhoneNumber(auth.getName()).orElseThrow(
                () -> new NotFoundUserException("User not found")
        ).getId();
    }


}
