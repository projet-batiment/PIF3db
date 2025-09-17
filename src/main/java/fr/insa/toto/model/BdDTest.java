/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.model;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
public class BdDTest {

    public static void createBdDTest(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("insert into utilisateur (id,surnom) values (?,?)")) {
            pst.setInt(1, 1);
            pst.setString(2, "toto");
            pst.executeUpdate();
            pst.setInt(1, 2);
            pst.setString(2, "titi");
            pst.executeUpdate();
        }
    }
    
    public static void main(String[] args) {
        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
            GestionSchema.razBdd(con);
            createBdDTest(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}
