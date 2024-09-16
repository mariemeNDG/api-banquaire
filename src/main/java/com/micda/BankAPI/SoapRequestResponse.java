package com.micda.BankAPI;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;


public class SoapRequestResponse {

	// Classe publique principale : GetBalanceRequest
    public static class GetBalanceRequest {
        private String accountId;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }

    // Classe GetBalanceResponse
    public static class GetBalanceResponse {
        private BigDecimal balance;

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }

    // Classe GetTransactionsRequest
    public static class GetTransactionsRequest {
        private String accountId;
        private int pageNumber;
        private int pageSize;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    // Classe GetTransactionsResponse
    public static class GetTransactionsResponse {
        private List<TransactionSoap> transactions;

        public List<TransactionSoap> getTransactions() {
            return transactions;
        }

        public void setTransactions(List<TransactionSoap> transactions) {
            this.transactions = transactions;
        }
    }

    // Classe TransactionSoap
    public static class TransactionSoap {
        private String transactionId;
        private BigDecimal amount;
        private String description;
        private Date transactionDate;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(Date localDateTime) {
            this.transactionDate = localDateTime;
        }
    }

    // Classe TransferFundsRequest
    public static class TransferFundsRequest {
        private String creditorId;
        private String debtorId;
        private BigDecimal amount;
        private String currency;

        public String getCreditorId() {
            return creditorId;
        }

        public void setCreditorId(String creditorId) {
            this.creditorId = creditorId;
        }

        public String getDebtorId() {
            return debtorId;
        }

        public void setDebtorId(String debtorId) {
            this.debtorId = debtorId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    // Classe TransferFundsResponse
    public static class TransferFundsResponse {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
