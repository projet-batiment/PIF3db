package fr.insa.lpe7.webui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.lpe7.model.BdDTest;
import fr.insa.lpe7.model.GestionSchema;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Theme("default")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        // seulement si bdd h2 en memoire
        try (Connection con = ConnectionPool.getConnection()) {
            GestionSchema.razBdd(con);
            BdDTest.createBdDTestV2(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
        SpringApplication.run(Application.class, args);
    }

}
