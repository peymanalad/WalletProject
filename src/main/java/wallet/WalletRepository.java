package wallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletRepository {
    private Connection connection = PostgresConnection.getInstance().getConnection();
    private Map<String,List<Wallet>> mapAll = new HashMap<>();

    public WalletRepository() throws SQLException {
        String walletTable = "CREATE TABLE IF NOT EXISTS wallet (" +
                "    id serial primary key," +
                "    amount bigint" +
                ")";
        PreparedStatement preparedStatement = connection.prepareStatement(walletTable);
        preparedStatement.execute();
        preparedStatement.close();
    }
    //CRUD
    public void insert(Wallet wallet) throws SQLException {
        String insert = "INSERT INTO wallet (amount) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1,wallet.getAmount());
        preparedStatement.executeUpdate();
        mapAll.clear();
        preparedStatement.close();
    }

    public void update(Wallet wallet) throws SQLException {
        String update = "UPDATE wallet SET amount = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(update);
        preparedStatement.setInt(1, wallet.getAmount());
        preparedStatement.setInt(2, wallet.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void delete (Wallet wallet) throws SQLException {
        String delete = "DELETE FROM wallet WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(delete);
        preparedStatement.setInt(1,wallet.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Wallet findById(Integer id) throws SQLException {
        String findById = "SELECT * FROM wallet WHERE id = " + id;
//        PreparedStatement preparedStatement = connection.prepareStatement(findById);
//        preparedStatement.setInt(1,id);
//        System.out.println(preparedStatement);

        Statement statement = connection.createStatement();
//        statement.execute(findById);
        System.out.println(findById);
        if (mapAll.containsKey(findById)) {
            System.out.println("reading from cache");
            List<Wallet> walletList = new ArrayList<>(mapAll.get(findById));
            return walletList.get(0);
        }
//        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSet resultSet = statement.executeQuery(findById);
        Wallet wallet = null;
        if (resultSet.next()) {
            wallet = new Wallet(resultSet.getInt("id"), resultSet.getInt("amount"));
            List<Wallet> walletList = new ArrayList<>(1);
            walletList.add(wallet);
            mapAll.put(findById, walletList);
        }
        return wallet;
    }

    public List<Wallet> findAll() throws SQLException {
        String findAll = "SELECT * FROM wallet";
        PreparedStatement preparedStatement = connection.prepareStatement(findAll);
        System.out.println(preparedStatement);
        if (mapAll.containsKey(preparedStatement.toString())) {
            System.out.println("Reading from cache");
            return mapAll.get(preparedStatement.toString());
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Wallet> walletList = new ArrayList<>();
        while (resultSet.next()) {
            walletList.add(new Wallet(resultSet.getInt("id"), resultSet.getInt("amount")));
            mapAll.put(preparedStatement.toString(),walletList);
        }
        return walletList;
    }

}
