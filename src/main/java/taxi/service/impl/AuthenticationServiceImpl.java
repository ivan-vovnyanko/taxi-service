package taxi.service.impl;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import taxi.exception.AuthenticationException;
import taxi.lib.Inject;
import taxi.lib.Service;
import taxi.model.Driver;
import taxi.service.AuthenticationService;
import taxi.service.DriverService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
    @Inject
    private DriverService driverService;

    @Override
    public Driver login(String login, String password) {
        Optional<Driver> optionalDriver = driverService.findByLogin(login);
        if (optionalDriver.isEmpty()
                || !optionalDriver.get().getPassword().equals(password)) {
            logger.error("Incorrect login or password! Login: " + login);
            throw new AuthenticationException("Invalid login or password");
        }
        return optionalDriver.get();
    }
}
