/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.webui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.toto.model.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author francois
 */
@Route(value = "newUser")
public class NewUserPanel extends VerticalLayout {

    private TextField tfSurnom;
    private Grid<Utilisateur> selApprecies;

    public NewUserPanel() {
        this.tfSurnom = new TextField("votre surnom :");
        Button bValidate = new Button("Sauvegarder");
        bValidate.addSingleClickListener((t) -> {
            String surnom = this.tfSurnom.getValue();
            try (Connection con = ConnectionPool.getConnection()) {
                this.provisoireSaveInDB(con, surnom, this.selApprecies.getSelectedItems());
                Notification.show("Utilisateur " + surnom + " sauvegardé");
                this.tfSurnom.setValue("");
                this.selApprecies.setItems(Utilisateur.tousLesUtilisateur(con));
            } catch (SQLException ex) {
                Notification.show("Problem : " + ex.getLocalizedMessage());
            }
        });
        try (Connection con = ConnectionPool.getConnection()) {
            this.selApprecies = new Grid<>(Utilisateur.tousLesUtilisateur(con));
            this.selApprecies.addColumn(Utilisateur::getSurnom);
            this.selApprecies.setSelectionMode(Grid.SelectionMode.MULTI);
            this.add(new H3("Nouvel Utilisateur"));
            this.add(this.tfSurnom);
            this.add(new H4("selectionnez les personnes appréciées :"));
            this.add(this.selApprecies, bValidate);
        } catch (SQLException ex) {
            Notification.show("Problem : " + ex.getLocalizedMessage());
        }
    }

    public void provisoireSaveInDB(Connection con, String surnom, Set<Utilisateur> apprecies) throws SQLException {
        try {
            con.setAutoCommit(false);
            int id = new Utilisateur(surnom, Long.toHexString(new Random().nextLong()), 2).saveInDB(con);
            try (PreparedStatement pst = con.prepareStatement(
                    "insert into apprecie (u1,u2) values (?,?)")) {
                for(var u : apprecies) {
                    pst.setInt(1, id);
                    pst.setInt(2, u.getId());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

}
