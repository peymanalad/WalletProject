package wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    private Connection connection = PostgresConnection.getInstance().getConnection();
    Map<String,List<Transaction>> mapAll = new HashMap<>();

    public TransactionRepository() throws SQLException {
        String transactionTable = "CREATE TABLE IF NOT EXISTS transactions (" +
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
        String insert = "INSERT INTO transactions (wallet_id, amount, status, transaction_type) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1,transaction.getWallet().getId());
        preparedStatement.setInt(2,transaction.getAmount());
        preparedStatement.setString(3,transaction.getStatus().name());
        preparedStatement.setString(4,transaction.getType().name());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void update (Transaction transaction, Integer amount, Status status, Type type) throws SQLException {
        String update = "UPDATE transactions SET " +
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
        String delete = "DELETE FROM transactions WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(delete);
        preparedStatement.setInt(1,transaction.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Transaction findById(Integer id) throws SQLException {
        String findById = "SELECT * FROM transactions WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(findById);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(preparedStatement);
        if (mapAll.containsKey(preparedStatement.toString())) {
            System.out.println("Read from cache");
            List<Transaction> transactionArrayList = new ArrayList<>();
            transactionArrayList.addAll(mapAll.get(preparedStatement.toString()));
            return transactionArrayList.get(0);
        }
        WalletRepository walletRepository = new WalletRepository();
        Transaction transaction = null;
        if (resultSet.next()) {
            transaction = new Transaction(resultSet.getInt("id"),
                    walletRepository.findById(resultSet.getInt("wallet_id")),
                    resultSet.getInt("amount"),
                    Status.valueOf(resultSet.getString("status")),
                    Type.valueOf(resultSet.getString("transaction_type")));
            List<Transaction> transactionsList = new ArrayList<>(1);
            transactionsList.add(transaction);
            mapAll.put(preparedStatement.toString(), transactionsList);
        }
        return transaction;
    }

    public List<Transaction> findAll() throws SQLException {
        String findAll = "SELECT * FROM transactions ";
        PreparedStatement preparedStatement = connection.prepareStatement(findAll);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(preparedStatement);
        if (mapAll.containsKey(preparedStatement.toString())) {
            System.out.println("Read from cache");
            return mapAll.get(preparedStatement.toString());
        }
        WalletRepository walletRepository = new WalletRepository();
        List<Transaction> transactionsList = new ArrayList<>();
        while (resultSet.next()) {
            transactionsList.add(new Transaction(resultSet.getInt("id"),
                    walletRepository.findById(resultSet.getInt("wallet_id")),
                    resultSet.getInt("amount"),
                    Status.valueOf(resultSet.getString("status")),
                    Type.valueOf(resultSet.getString("transaction_type"))));
            mapAll.put(preparedStatement.toString(),transactionsList);
        }
        return transactionsList;
    }

}
