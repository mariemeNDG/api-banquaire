package com.micda.BankAPI;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.micda.BankAPI.Model.Account;
import com.micda.BankAPI.Model.Transaction;
import com.micda.BankAPI.Repository.AccountRepository;
import com.micda.BankAPI.Repository.TransactionRepository;
import com.micda.BankAPI.Soap.SoapRequestResponse.GetBalanceRequest;
import com.micda.BankAPI.Soap.SoapRequestResponse.GetBalanceResponse;
import com.micda.BankAPI.Soap.SoapRequestResponse.GetTransactionsRequest;
import com.micda.BankAPI.Soap.SoapRequestResponse.GetTransactionsResponse;
import com.micda.BankAPI.Soap.SoapRequestResponse.TransactionSoap;
import com.micda.BankAPI.Soap.SoapRequestResponse.TransferFundsRequest;
import com.micda.BankAPI.Soap.SoapRequestResponse.TransferFundsResponse;


@Endpoint
public class SoapEndpoint {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	private static final String NAMESPACE_URI = "http://api/accounts";
	
	//Consultation du solde du compte 
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBalanceRequest")
    @ResponsePayload
    public GetBalanceResponse getBalance(@RequestPayload GetBalanceRequest request) {
        GetBalanceResponse response = new GetBalanceResponse();
        Account account = accountRepository.findByNumcomp(request.getAccountId());

        if (account != null) {
            response.setBalance(account.getBalance());
        } else {
            response.setBalance(BigDecimal.ZERO);
        }

        return response;
    }
    
    // Historique des transactions 
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getTransactionsRequest")
    @ResponsePayload
    public GetTransactionsResponse getTransactions(@RequestPayload GetTransactionsRequest request) {
        GetTransactionsResponse response = new GetTransactionsResponse();

        // Récupérer toutes les transactions du compte sans pagination
        List<Transaction> transactions = transactionRepository.findByAccountId(request.getAccountId());

        // Convertir en TransactionSoap (pour répondre en SOAP)
        List<TransactionSoap> transactionList = new ArrayList<>();
        for (Transaction txn : transactions) {
            TransactionSoap txnSoap = new TransactionSoap();
            txnSoap.setTransactionId(txn.getId().toString());
            txnSoap.setAmount(BigDecimal.valueOf(txn.getAmount()));
            txnSoap.setTransactionDate(Date.from(txn.getTransactionDate().atZone(ZoneId.systemDefault()).toInstant()));
            transactionList.add(txnSoap);
        }
        response.setTransactions(transactionList);
        return response;
    }
    
    //Transfer des fonds
    /**
     * Méthode pour effectuer un virement entre deux comptes.
     * @param request La requête SOAP contenant les informations du virement.
     * @return La réponse SOAP indiquant si l'opération a été réussie ou non.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "transferFundsRequest")
    @ResponsePayload
    public TransferFundsResponse transferFunds(@RequestPayload TransferFundsRequest request) {
        TransferFundsResponse response = new TransferFundsResponse();

        // Récupérer le créditeur et le débiteur par leurs numéros de compte
        Account creditor = accountRepository.findByNumcomp(request.getCreditorId());
        Account debtor = accountRepository.findByNumcomp(request.getDebtorId());

        if (creditor != null && debtor != null && request.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            // Débiter le créditeur
            creditor.setBalance(creditor.getBalance().subtract(request.getAmount()));

            // Créditez le débiteur
            debtor.setBalance(debtor.getBalance().add(request.getAmount()));

            // Enregistrer la transaction dans la base de données
            Transaction transaction = new Transaction();
            transaction.setCreditor(creditor.getNumcomp());
            transaction.setDebtor(debtor.getNumcomp());
            transaction.setAmount(request.getAmount());
            transactionRepository.save(transaction);

            // Sauvegarder les modifications dans les comptes
            accountRepository.save(creditor);
            accountRepository.save(debtor);

            response.setStatus("Virement effectuée avec succès.");
        } else {
            response.setStatus("Echec de la transaction!");
        }

        return response;
    }

}
