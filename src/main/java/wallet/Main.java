package wallet;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        Connection connection = PostgresConnection.getInstance().getConnection();
        WalletService walletService = new WalletService();
        TransactionService transactionService=new TransactionService();


        transactionService.withDraw(6000,2);




    }
}
