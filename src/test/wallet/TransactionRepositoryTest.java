package wallet;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private Wallet wallet;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;


    @BeforeAll
    public static void setUp() {
        Connection connection = PostgresConnection.getInstance().getConnection();
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        wallet = new Wallet(1000);
        walletRepository = new WalletRepository();
        Integer id = walletRepository.insert(wallet);
        wallet.setId(id);
        transactionRepository = new TransactionRepository();


    }

    @AfterEach
    public void cleanUp() throws SQLException {
        String delete = "DELETE FROM wallet";
        PreparedStatement preparedStatement1 = PostgresConnection.getInstance().getConnection().prepareStatement(delete);
        preparedStatement1.execute();
        preparedStatement1.close();

        delete = "DELETE FROM transactions";
        PreparedStatement preparedStatement2 = PostgresConnection.getInstance().getConnection().prepareStatement(delete);
        preparedStatement2.execute();
        preparedStatement2.close();
    }

    @Test
    public void insertTest() throws SQLException {
        //Arrange
        Transaction transaction = new Transaction(wallet, 500, Status.ACCEPTED, Type.WITHDRAW);


        //Act
        Integer transactionId = transactionRepository.insert(transaction);

        //Assert
        Transaction loadedTransaction = transactionRepository.findById(transactionId);
        assertThat(loadedTransaction).isNotNull();
        assertThat(loadedTransaction.getAmount()).isEqualTo(500);

    }


}