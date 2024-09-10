package com.micda.BankAPI.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micda.BankAPI.Model.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, String>{

}
