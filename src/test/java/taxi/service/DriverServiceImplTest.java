package taxi.service;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taxi.dao.DriverDao;
import taxi.lib.Injector;
import taxi.model.Driver;

class DriverServiceImplTest {
    private static final Injector injector = Injector.getInstance("taxi");
    private static final DriverService driverService =
            (DriverService) injector.getInstance(DriverService.class);
    private static final DriverDao driverDao =
            (DriverDao) injector.getInstance(DriverDao.class);

    @Test
    void findByLogin_Ok() {
        Driver driverFromDb;
        if (driverDao.getAll().isEmpty()) {
            Driver driver = new Driver();
            driver.setLogin("demoLogin");
            driver.setPassword("demoPassword");
            driver.setLicenseNumber("DE1234MO");
            driver.setName("demoName");
            driverFromDb = driverDao.create(driver);
        } else {
            driverFromDb = driverDao.getAll().stream().findFirst().get();
        }
        Optional<Driver> byLogin = driverService.findByLogin(driverFromDb.getLogin());
        Assertions.assertTrue(byLogin.isPresent());
        Assertions.assertEquals(driverFromDb, byLogin.get());
    }
}