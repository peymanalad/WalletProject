package wallet;

import java.sql.SQLException;
import java.util.Random;

public class WalletService {

    private WalletRepository walletRepository = new WalletRepository();

    public WalletService() throws SQLException {
    }

    public void createWallet(Integer amount) throws SQLException {
        Wallet wallet = new Wallet(amount);
        walletRepository.insert(wallet);
    }

    public void showBalance(Integer id) throws SQLException {
        System.out.println(walletRepository.findById(id));
    }

    public Wallet findById(Integer id) throws SQLException {
        return walletRepository.findById(id);
    }

    public void update(Wallet wallet) throws SQLException {
        walletRepository.update(wallet);
    }


}
