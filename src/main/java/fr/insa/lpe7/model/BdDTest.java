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
package fr.insa.lpe7.model;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author francois
 */
public class BdDTest {

    public static void createBdDTestV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("insert into utilisateur (id,surnom) values (?,?)")) {
            pst.setInt(1, 1);
            pst.setString(2, "toto");
            pst.executeUpdate();
            pst.setInt(1, 2);
            pst.setString(2, "titi");
            pst.executeUpdate();
        }
    }

    public static void createBdDTestV2(Connection con) throws SQLException {
        List<Utilisateur> users = List.of(
                new Utilisateur("toto", "p1", 1),
                new Utilisateur("titi", "p2", 2),
                new Utilisateur("tutu", "p2", 2)
        );
        for (var u : users) {
            u.saveInDB(con);
        }
        List<Loisir> loisirs = List.of(
                new Loisir("tennis", "c'est fatiguant"),
                new Loisir("sieste", "c'est reposantm"),
                new Loisir("lecture", "trop intello")
        );
        for (var lo : loisirs) {
            lo.saveInDB(con);
        }
        int[][] apprecient = new int[][]{
            {0, 1},
            {1, 1},
            {1, 2},
            {2, 1},};
        try (PreparedStatement app = con.prepareStatement(
                "insert into apprecie (u1,u2) values (?,?)")) {
            for (int[] a : apprecient) {
                app.setInt(1, users.get(a[0]).getId());
                app.setInt(2, users.get(a[1]).getId());
                app.executeUpdate();
            }
        }
        int[][] pratiques = new int[][]{
            {0, 1, 1},
            {1, 0, 2},
            {1, 2, -2},
            {2, 1, -1},};
        try (PreparedStatement pra = con.prepareStatement(
                "insert into pratique (idutilisateur,idloisir,niveau) values (?,?,?)")) {
            for (int[] p : pratiques) {
                pra.setInt(1, users.get(p[0]).getId());
                pra.setInt(2, loisirs.get(p[1]).getId());
                pra.setInt(3, p[2]);
                pra.executeUpdate();
            }
        }
    }

    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            GestionSchema.razBdd(con);
            createBdDTestV2(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}
