package wallet;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PostgresConnectionTest {

    @Test
    public void testConnection() {
        assertDoesNotThrow(() -> PostgresConnection.getInstance().getConnection());
    }

}