package wallet;

import customexception.NotEnoughMoneyException;
import customexception.NotExistException;

import java.sql.SQLException;
import java.util.Random;

public class TransactionService {

    private TransactionRepository transactionRepository = new TransactionRepository();
    private WalletService walletService = new WalletService();



    public TransactionService() throws SQLException {
    }

    public void withDraw(Integer amount, Integer walletId) throws SQLException {
        Wallet wallet = walletService.findById(walletId);
        if (wallet == null){
            throw new NotExistException("/Wallet doesn't exist");
        }
        if (amount > wallet.getAmount()) {
            System.out.println(wallet);
            throw new NotEnoughMoneyException("/Not enough money in wallet!");
        }
        else {
            wallet.setAmount(wallet.getAmount() - amount);
            walletService.update(wallet);
            Transaction transaction = new Transaction(wallet, amount, Status.ACCEPTED, Type.WITHDRAW);
            transactionRepository.insert(transaction);
        }
    }

    public void deposit(Integer amount, Integer walletId) throws SQLException {
        Wallet wallet = walletService.findById(walletId);
        if (wallet == null){
            throw new NotExistException("Wallet doesn't exist");
        }
        wallet.setAmount(wallet.getAmount() + amount);
        walletService.update(wallet);
        Transaction transaction = new Transaction(wallet, amount, Status.ACCEPTED, Type.DEPOSIT);
        transactionRepository.insert(transaction);
    }
    public void findById(Integer id) throws SQLException {
        System.out.println(transactionRepository.findById(id).toString());
    }

    public void findAll() throws SQLException {
        for (Transaction t: transactionRepository.findAll()) {
            System.out.println(t);
        }
    }

}
