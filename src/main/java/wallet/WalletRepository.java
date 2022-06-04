package wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WalletRepository {
    private Connection connection = PostgresConnection.getInstance().getConnection();

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
        String findById = "SELECT * FROM wallet WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(findById);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Wallet wallet = null;
        if (resultSet.next()) {
            wallet = new Wallet(resultSet.getInt("id"), resultSet.getInt("amount"));
        }
        return wallet;
    }

    public List<Wallet> findAll() throws SQLException {
        String findAll = "SELECT * FROM wallet";
        PreparedStatement preparedStatement = connection.prepareStatement(findAll);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        List<Wallet> walletList = new ArrayList<>();
        while (resultSet.next()) {
            walletList.add(new Wallet(resultSet.getInt("id"), resultSet.getInt("amount")));
        }
        return walletList;
    }

}
