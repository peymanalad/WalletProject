package wallet;

import java.sql.SQLException;
import java.util.Random;

public class TransactionService {

    private TransactionRepository transactionRepository = new TransactionRepository();
    private WalletService walletService = new WalletService();
    private final String NOTEXIST = "Wallet does not exist";
    private final String NOTENOUGH = "Not Enough money to withdraw!";


    public TransactionService() throws SQLException {
    }

    public void withDraw(Integer amount, Integer walletId) throws SQLException {
        Wallet wallet = walletService.findById(walletId);
        if (wallet == null){
            System.out.println(NOTEXIST);
            return;
        }
        if (amount > wallet.getAmount()) {
            System.out.println(NOTENOUGH);
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
            System.out.println(NOTEXIST);
            return;
        }
        wallet.setAmount(wallet.getAmount() + amount);
        walletService.update(wallet);
        Transaction transaction = new Transaction(wallet, amount, Status.ACCEPTED, Type.DEPOSIT);
        transactionRepository.insert(transaction);
    }

}
