package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account) {
        if (account.getUsername().isEmpty()) {
            return null;
        }

        if (account.getPassword().length() < 4) {
            return null;
        }

        Account existing = this.accountDAO.getAccountByUsername(account.getUsername());
        if (existing != null) {
            return null;
        }

        Account newAccount = this.accountDAO.insertAccount(account);

        return newAccount;
    }

    public Account handleLogin(String username, String password) {
        Account account = this.accountDAO.getAccountByUsername(username);
    
        if (account == null || !account.getPassword().equals(password)) {
            return null;
        }

        return account;
    }

    public Account getAccountById(int accountId) {
        return this.accountDAO.getAccountById(accountId);
    }

    public Account getAccountByUsername(String username) {
        return this.accountDAO.getAccountByUsername(username);
    }
}
