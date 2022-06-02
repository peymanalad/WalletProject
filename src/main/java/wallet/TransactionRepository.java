package wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private Connection connection = PostgresConnection.getInstance().getConnection();

    public TransactionRepository() throws SQLException {
        String transactionTable = "CREATE TABLE IF NOT EXISTS tranaction (" +
                "    id serial primary key," +
                "    wallet_id int," +
                "    amount int," +
                "    status varchar(50)," +
                "    transaction_type varchar(50)," +
                "    constraint fk_walletid foreign key (wallet_id) references wallet(id)" +
                ")";
        PreparedStatement preparedStatement = connection.prepareStatement(transactionTable);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insert(Transaction transaction) throws SQLException {
        String insert = "INSERT INTO transaction (wallet_id, amount, status, transaction_type) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1,transaction.getWallet().getId());
        preparedStatement.setInt(2,transaction.getAmount());
        preparedStatement.setString(3,transaction.getStatus().name());
        preparedStatement.setString(4,transaction.getType().name());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update (Transaction transaction, Integer amount, Status status, Type type) throws SQLException {
        String update = "UPDATE transaction SET " +
                "wallet_id = ? " +
                "AND amount = ?" +
                "AND status = ?" +
                "AND transaction_type = ?" +
                "WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(update);
        preparedStatement.setInt(1,transaction.getWallet().getId());
        preparedStatement.setInt(2,amount);
        preparedStatement.setString(3,status.name());
        preparedStatement.setString(4,type.name());
        preparedStatement.setInt(5,transaction.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete (Transaction transaction) throws SQLException {
        String delete = "DELETE FROM transaction WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(delete);
        preparedStatement.setInt(1,transaction.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Transaction findById(Integer id) throws SQLException {
        String findById = "SELECT * FROM transaction WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(findById);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        WalletRepository walletRepository = new WalletRepository();
        Transaction transaction = null;
        if (resultSet.next()) {
            transaction = new Transaction(resultSet.getInt("id"),
                    walletRepository.findById(resultSet.getInt("wallet_id")),
                    resultSet.getInt("amount"),
                    Status.valueOf(resultSet.getString("status")),
                    Type.valueOf(resultSet.getString("transaction_type")));
        }
        return transaction;
    }

    public List<Transaction> findAll() throws SQLException {
        String findAll = "SELECT * FROM transaction ";
        PreparedStatement preparedStatement = connection.prepareStatement(findAll);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        WalletRepository walletRepository = new WalletRepository();
        List<Transaction> transactionsList = new ArrayList<>();
        while (resultSet.next()) {
            transactionsList.add(new Transaction(resultSet.getInt("id"),
                    walletRepository.findById(resultSet.getInt("wallet_id")),
                    resultSet.getInt("amount"),
                    Status.valueOf(resultSet.getString("status")),
                    Type.valueOf(resultSet.getString("transaction_type"))));
        }
        return transactionsList;
    }

}
