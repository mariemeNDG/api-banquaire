package com.micda.BankAPI.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micda.BankAPI.TransferRequest;
import com.micda.BankAPI.Model.Account;
import com.micda.BankAPI.Model.Transaction;
import com.micda.BankAPI.Repository.AccountRepository;
import com.micda.BankAPI.Repository.TransactionRepository;

@RestController
@RequestMapping("/api/accounts")
public class AccountControler {

	final AccountRepository accountRepository;
	final TransactionRepository transactionRepository;
	
	AccountControler( AccountRepository accountRepository, TransactionRepository transactionRepository){
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		
	}
	
	// affichage de toutes les comptes 
	@GetMapping("/liste")
	public ResponseEntity<List<Account>> getAllAccounts(){
		return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
	}
	
	// Creation de compte
	
	@PostMapping("/creer")
	 public ResponseEntity<Account> createAccount(@RequestBody Account account){
    	Account accountCreated = accountRepository.save(account);
    	return new ResponseEntity<>(accountCreated, HttpStatus.CREATED);
    	
    }
	
	   // Consultation du solde du compte
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountId){
    	
    	Optional<Account> account = accountRepository.findById(accountId);
    	
    	if(account.isPresent()) {
    		return new ResponseEntity<>(account.get(), HttpStatus.OK);
    	}
    	else {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    }
    
 // Historique des transactions avec pagination
    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactions(@PathVariable String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
    
    // Effectuer un virement
    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<String> transferFunds(
            @PathVariable String accountId,
            @RequestBody TransferRequest transferRequest) {
        Optional<Account> debiteurOptional = accountRepository.findById(accountId);
        Optional<Account> crediteurOptional = accountRepository.findById(transferRequest.getCreditorId());

        if (debiteurOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte débiteur non trouvé");
        }
        if (crediteurOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte créancier non trouvé");
        }

        Account debiteur = debiteurOptional.get();
        Account crediteur = crediteurOptional.get();

        BigDecimal amount = new BigDecimal(transferRequest.getAmount());
        if (debiteur.getBalance().compareTo(amount) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fonds insuffisants sur le compte débiteur");
        }

        // Effectuer le virement
        debiteur.setBalance(debiteur.getBalance().subtract(amount));
        crediteur.setBalance(crediteur.getBalance().add(amount));

        accountRepository.save(debiteur);
        accountRepository.save(crediteur);

        // Enregistrer les transactions
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccountId(accountId);
        debitTransaction.setAmount(transferRequest.getAmount());
        debitTransaction.setTransactionDate(LocalDateTime.now());
        debitTransaction.setType("DEBIT");
        //debitTransaction.setDescription("Virement vers le compte " + transferRequest.getCreditorId());
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccountId(transferRequest.getCreditorId());
        creditTransaction.setAmount(transferRequest.getAmount());
        creditTransaction.setTransactionDate(LocalDateTime.now());
        creditTransaction.setType("CREDIT");
        //creditTransaction.setDescription("Virement reçu depuis le compte " + accountId);
        transactionRepository.save(creditTransaction);

        return ResponseEntity.ok("Virement effectué avec succès");
    }

}
