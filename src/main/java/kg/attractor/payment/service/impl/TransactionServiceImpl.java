package kg.attractor.payment.service.impl;

import kg.attractor.payment.dao.AccountDao;
import kg.attractor.payment.dao.RollBackDao;
import kg.attractor.payment.dao.TransactionDao;
import kg.attractor.payment.dao.UserDao;
import kg.attractor.payment.dto.SendMoneyDto;
import kg.attractor.payment.dto.TransactionDto;
import kg.attractor.payment.errors.InsufficientFundsException;
import kg.attractor.payment.errors.NotFoundAccountException;
import kg.attractor.payment.errors.NotFoundTransactionException;
import kg.attractor.payment.errors.NotFoundUserException;
import kg.attractor.payment.model.Account;
import kg.attractor.payment.model.RollbackLog;
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
    private final RollBackDao rollBackDao;

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
        Account senderAccount = accountDao.getByAccountNumber(sendMoneyDto.getSenderAccountNumber())
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
        return convertToDto(list);
    }

    @Override
    public List<TransactionDto> getPendingTransactions(){
        var list = transactionDao.getPendingTransactions();
        return convertToDto(list);
    }


    @Override
    public void approveTransaction(Long transactionId){
       Transaction transaction = transactionDao.getTransaction(transactionId)
                .orElseThrow(() -> new NotFoundTransactionException("Not found transaction"));
       if(!transaction.getStatus().equals("PENDING")){
           throw new RuntimeException("Transaction is not PENDING");
       }
        Account senderAccount = accountDao.getAccountById(transaction.getSenderId())
                .orElseThrow(() -> new NotFoundAccountException("Not found sender account number"));
        Account receiverAccount = accountDao.getAccountById(transaction.getReceiverId())
                .orElseThrow(() -> new NotFoundAccountException("Not found receiver account number"));
        if (senderAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds in the sender's account");
        }
        senderAccount.setBalance(senderAccount.getBalance().subtract(transaction.getAmount()));
        accountDao.updateAccount(senderAccount);

        receiverAccount.setBalance(receiverAccount.getBalance().add(transaction.getAmount()));
        accountDao.updateAccount(receiverAccount);

        transactionDao.updateTransactionStatus(transactionId, "COMPLETED");
    }


    @Override
    public void rollBackTransaction(Long transactionId){
        Transaction transaction = transactionDao.getTransaction(transactionId)
                .orElseThrow(() -> new NotFoundTransactionException("Not found transaction"));
        Account senderAccount = accountDao.getAccountById(transaction.getSenderId())
                .orElseThrow(() -> new NotFoundAccountException("Not found sender account number"));
        Account receiverAccount = accountDao.getAccountById(transaction.getReceiverId())
                .orElseThrow(() -> new NotFoundAccountException("Not found receiver account number"));
        if (receiverAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in recipient's account for rollback");
        }

        receiverAccount.setBalance(receiverAccount.getBalance().subtract(transaction.getAmount()));
        senderAccount.setBalance(senderAccount.getBalance().add(transaction.getAmount()));

        accountDao.updateAccount(senderAccount);
        accountDao.updateAccount(receiverAccount);

        transactionDao.updateTransactionStatus(transactionId, "ROLLED_BACK");

        RollbackLog log = RollbackLog.builder()
                .transactionId(transactionId)
                .adminId(getCurrentUserId())
                .action("ROLLED_BACK")
                .timestamp(LocalDateTime.now())
                .build();
        rollBackDao.save(log);
    }



    @Override
    public void deleteTransaction(Long transactionId){
        Transaction transaction = transactionDao.getTransaction(transactionId)
                .orElseThrow(() -> new NotFoundTransactionException("Transaction not found"));
        if (!"ROLLED_BACK".equals(transaction.getStatus())) {
            throw new RuntimeException("Transaction can only be deleted after it has been rolled back");
        }
        transactionDao.updateTransactionStatus(transactionId, "DELETED");
        RollbackLog log = RollbackLog.builder()
                .transactionId(transactionId)
                .adminId(getCurrentUserId())
                .action("DELETED")
                .timestamp(LocalDateTime.now())
                .build();
        rollBackDao.save(log);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDao.getUserByPhoneNumber(auth.getName()).orElseThrow(
                () -> new NotFoundUserException("User not found")
        ).getId();
    }


    public List<TransactionDto> convertToDto(List<Transaction> list){
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


}
