/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.model;

import fr.insa.beuvron.utils.database.ClasseMiroir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author francois
 */
public class Utilisateur extends ClasseMiroir {

    private String surnom;
    private String pass;
    private int role;

    /** pour nouvel utilisateur en mémoire */
    public Utilisateur(String surnom, String pass,int role) {
        super();
        this.surnom = surnom;
        this.pass = pass;
        this.role = role;
    }

    /** pour utilisateur récupéré de la base de données */
    public Utilisateur(int id,String surnom, String pass,int role) {
        super(id);
        this.surnom = surnom;
        this.pass = pass;
        this.role = role;
    }

    @Override
    public Statement saveSansId(Connection con) throws SQLException {
        PreparedStatement insert = con.prepareStatement(
                "insert into utilisateur (surnom,pass,role) values (?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        insert.setString(1, this.getSurnom());
        insert.setString(2, this.getPass());
        insert.setInt(3, role);
        insert.executeUpdate();
        return insert;
    }
    
    public static List<Utilisateur> tousLesUtilisateur(Connection con) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement("select id,surnom,pass,role from utilisateur")) {
            try (ResultSet allU = pst.executeQuery()) {
                while (allU.next()) {
                    res.add(new Utilisateur(allU.getInt("id"), allU.getString("surnom"),
                            allU.getString("pass"),allU.getInt("role")));
                }
            }
        }
        return res;
    }

    /**
     * @return the surnom
     */
    public String getSurnom() {
        return surnom;
    }

    /**
     * @param surnom the surnom to set
     */
    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

}
