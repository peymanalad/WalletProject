package wallet;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection connection = PostgresConnection.getInstance().getConnection();

    }
}