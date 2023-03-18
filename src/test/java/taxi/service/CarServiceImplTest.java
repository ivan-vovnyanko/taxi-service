package taxi.service;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.dao.CarDao;
import taxi.dao.DriverDao;
import taxi.dao.ManufacturerDao;
import taxi.lib.Injector;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;

class CarServiceImplTest {
    private static Long MANUFACTURER_ID;
    private static final Injector injector = Injector.getInstance("taxi");
    private static final CarService carService =
            (CarService) injector.getInstance(CarService.class);
    private static final CarDao carDao =
            (CarDao) injector.getInstance(CarDao.class);
    private static final ManufacturerDao manufacturerDao =
            (ManufacturerDao) injector.getInstance(ManufacturerDao.class);
    private static final DriverDao driverDao =
            (DriverDao) injector.getInstance(DriverDao.class);

    @BeforeEach
    void setUp() {
        if (manufacturerDao.getAll().isEmpty()) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setName("MAZDA");
            manufacturer.setCountry("ENGLAND");
            manufacturerDao.create(manufacturer);
        }
        MANUFACTURER_ID = manufacturerDao.getAll().stream().findFirst().get().getId();
    }

    @Test
    void createCar_Ok() {
        Car car = new Car();
        car.setModel("Beta Model");
        car.setManufacturer(manufacturerDao.get(MANUFACTURER_ID).get());
        car.setDrivers(new ArrayList<>());
        Car carFromDb = carService.create(car);
        Assertions.assertEquals(car, carDao.get(carFromDb.getId()).get());
    }

    @Test
    void addAndDeleteDriver_Ok() {
        Car car = new Car();
        car.setModel("Beta Model");
        car.setManufacturer(manufacturerDao.get(MANUFACTURER_ID).get());
        car.setDrivers(new ArrayList<>());
        carService.create(car);
        Driver driverFromDb;
        if (driverDao.getAll().isEmpty()) {
            Driver driver = new Driver();
            driver.setLogin("DemoLogin");
            driver.setPassword("password123");
            driver.setLicenseNumber("DE1234MO");
            driver.setName("DemoName");
            driverDao.create(driver);
        }
        driverFromDb = driverDao.getAll().stream().findFirst().get();
        carService.addDriverToCar(driverFromDb, car);
        Assertions.assertEquals(car, carDao.get(car.getId()).get());
        carService.removeDriverFromCar(driverFromDb, car);
        car.getDrivers().remove(driverFromDb);
        Assertions.assertEquals(car, carDao.get(car.getId()).get());
    }
}