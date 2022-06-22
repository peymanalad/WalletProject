package wallet;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WalletRepositoryTest {
    private Integer id;
    private WalletRepository walletRepository;

    @BeforeAll
    public static void setUp() {
        Connection connection = PostgresConnection.getInstance().getConnection();
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        walletRepository = new WalletRepository();
    }

    @Test
    public void insertTest() throws SQLException {
        //Arrange
        Wallet wallet = new Wallet(3000);

        //Act
        Integer id = walletRepository.insert(wallet);
        this.id = id;
        //Assert
        Wallet loadedWallet = walletRepository.findById(id);
        assertAll(
                () -> assertNotNull(id),
                () -> assertNotNull(loadedWallet),
                () -> assertEquals(id,loadedWallet.getId()),
                () -> assertEquals(3000,loadedWallet.getAmount())
        );
    }

    @Test
    public void findAll() throws SQLException {
        //Arrange
        Wallet wallet = new Wallet(1000);
        Wallet wallet2 = new Wallet(2000);
        Wallet wallet3 = new Wallet(3000);
        walletRepository.insert(wallet);
        walletRepository.insert(wallet2);
        walletRepository.insert(wallet3);

        //Act
        List<Wallet> walletList = walletRepository.findAll();

        //Assert
        assertEquals(3,walletList.size());
        org.assertj.core.api.Assertions.assertThat(walletList.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    public void update() throws SQLException {
        //Arrange
        Wallet wallet = new Wallet(6900);
        Integer id = walletRepository.insert(wallet);
        wallet.setId(id);

        //Act
        Wallet newWallet = new Wallet(id, 20000);
        walletRepository.update(newWallet);

        //Assert
        Wallet loadedWallet = walletRepository.findById(id);
        assertThat(loadedWallet.getAmount()).isEqualTo(20000);
        assertThat(loadedWallet.getId()).isEqualTo(id);
    }

    @Test
    public void deleteTest() throws SQLException {
        //Arrange
        Wallet wallet = new Wallet(1000);
        wallet.setId(walletRepository.insert(wallet));

        //Act
        walletRepository.delete(wallet);

        //Assert
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(3);

    }


    @AfterEach
    public void cleanUp() throws SQLException {
        //delete all records in table
        String delete = "DELETE FROM wallet where id";
        PreparedStatement preparedStatement = PostgresConnection.getInstance().getConnection().prepareStatement(delete);
        preparedStatement.execute();
        preparedStatement.close();
    }

}