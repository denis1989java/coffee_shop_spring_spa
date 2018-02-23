package ru.mail.system.configs.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.mail.coffee.model.Coffee;
import ru.mail.system.configs.listener.impl.ScriptService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * this class launch files or MySQL DB, fill tables
 */
public class ApplicationListenerBean implements ApplicationListener {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("root");
    private static final Logger logger = Logger.getLogger(ApplicationListenerBean.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        if (applicationEvent instanceof ContextRefreshedEvent) {
            logger.debug("Initialization started");
            logger.debug("check orders saving method");

            if (resourceBundle.getString("method.of.orders.saving").equals("mysql")) {

                logger.debug("initialization of mysql DB");
                ScriptService.getInstance().process("init.sql");

            } else if (resourceBundle.getString("method.of.orders.saving").equals("file")) {

                logger.debug("initialization of files");
                iniDataForTesting();
            }
            logger.debug("Initialization finished");
        }
    }

    /**
     * creating coffees and write them to file
     */
    private void iniDataForTesting() {


        logger.debug("writing the file with coffees");
        List<Coffee> coffees = new ArrayList<>();
        coffees.add(new Coffee((long) 1, "Americano", "bitter", BigDecimal.valueOf(8.56)));
        coffees.add(new Coffee((long) 2, "Cappuccino", "sweet", BigDecimal.valueOf(10.89)));
        coffees.add(new Coffee((long) 3, "Espresso", "5 stars", BigDecimal.valueOf(9.54)));
        coffees.add(new Coffee((long) 4, "Latte", "milk", BigDecimal.valueOf(10.31)));
        coffees.add(new Coffee((long) 5, "Lungo", "half strong", BigDecimal.valueOf(10.26)));
        coffees.add(new Coffee((long) 6, "Doppiio", "dig glass", BigDecimal.valueOf(12.78)));
        coffees.add(new Coffee((long) 7, "Glace", "eggs", BigDecimal.valueOf(12.87)));
        coffees.add(new Coffee((long) 8, "Raf", "with tube", BigDecimal.valueOf(11.45)));
        try {

            File file = new File(resourceBundle.getString("coffeeRoot"));
            Files.deleteIfExists(file.toPath());

            File file1 = new File(resourceBundle.getString("orderRoot"));
            Files.deleteIfExists(file1.toPath());

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(resourceBundle.getString("coffeeRoot")));
            out.writeObject(coffees);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
