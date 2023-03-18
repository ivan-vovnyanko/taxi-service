package taxi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.exception.AuthenticationException;
import taxi.lib.Injector;
import taxi.model.Driver;

class AuthenticationServiceImplTest {
    private final static String LOGIN = "bob1234";
    private final static String PASSWORD = "QWERTYqwerty123";
    private final static String SALT = "SALT";
    private static final Injector injector = Injector.getInstance("taxi");
    private final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private final DriverService driverService =
            (DriverService) injector.getInstance(DriverService.class);
    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = new Driver();
        driver.setName("bob");
        driver.setLicenseNumber("AB1234CD");
        driver.setLogin(LOGIN);
        driver.setPassword(PASSWORD);
        if (driverService.findByLogin(LOGIN).isEmpty()) {
            driverService.create(driver);
        }
    }

    @Test
    void loginUser_Ok() {
        Assertions.assertEquals(driver, authenticationService.login(LOGIN, PASSWORD));
    }

    @Test
    void loginUSer_invalidPassword_NotOK() {
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(LOGIN, PASSWORD + SALT));
    }

    @Test
    void loginUSer_invalidLogin_NotOK() {
        Assertions.assertThrows(AuthenticationException.class, () ->
                authenticationService.login(LOGIN + SALT, PASSWORD));
    }
}