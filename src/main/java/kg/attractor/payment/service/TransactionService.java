package kg.attractor.payment.service;

import kg.attractor.payment.dto.SendMoneyDto;
import kg.attractor.payment.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getHistory(Long accountId);

    void makeTransaction(SendMoneyDto sendMoneyDto);

    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getPendingTransactions();

    void approveTransaction(Long transactionId);

    void rollBackTransaction(Long transactionId);

    void deleteTransaction(Long transactionId);
}
