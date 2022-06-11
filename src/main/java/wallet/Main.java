package wallet;

import customexception.NotEnoughMoneyException;
import customexception.NotExistException;

import java.io.NotActiveException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection connection = PostgresConnection.getInstance().getConnection();
        WalletService walletService = new WalletService();
        TransactionService transactionService=new TransactionService();
//        try {
//            transactionService.withDraw(6000, 2);
//        }
//        catch (NotEnoughMoneyException e) {
//            e.printStackTrace();
//        }
//        catch (NotExistException e) {
//            e.printStackTrace();
//        }
        walletService.showBalance(3);
        walletService.findById(3);
        for (Wallet w: walletService.findAll()) {
            System.out.println(w);
        }


    }
}
