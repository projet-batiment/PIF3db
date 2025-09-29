/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.webui;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@Route("users")
public class UtilisateurListPanel extends VerticalLayout {

    public UtilisateurListPanel() {
        try (Connection con = ConnectionPool.getConnection(); PreparedStatement pst1 = con.prepareStatement(
                "select surnom, ("
                + "  select count(*) from apprecie where apprecie.u2 = utilisateur.id"
                + ") as \"apprecie par\""
                + "  from utilisateur "
                + "  order by \"apprecie par\" desc"
        //                "select * from apprecie"
        )) {
            this.add(new Paragraph("Les utilisateurs"));
            this.add(new ResultSetGrid(pst1));
        } catch (SQLException ex) {
            Notification.show("Problem : " + ex.getLocalizedMessage());
        }

    }

}
