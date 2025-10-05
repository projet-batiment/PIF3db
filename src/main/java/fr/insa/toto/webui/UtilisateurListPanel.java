/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
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
