/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is ecole of CoursBeuvron.

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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author francois
 * @author LPe7
 */
public class GestionSchema {

    /**
     * concisely create and destroy sql schemas thanks to abstraction via
     * classes; each instance of the subclasses represents data about the sql
     * elements' structure
     */
    class Beton {

        TableSkeleton[] tables;

        /**
         * Returns: if b then s, else ""
         *
         * @param b boolean
         * @param s String
         * @return
         */
        private static String ifTrue(boolean b, String s) {
            return b ? s : "";
        }

        /**
         * holds the info about an sql constraint
         */
        static class ConstraintSkeleton {

            String name;
            TableSkeleton borrower;
            TableSkeleton lender;
            ColumnSkeleton borrowed;
            ColumnSkeleton lended;

            public ConstraintSkeleton(TableSkeleton borrower, TableSkeleton lender, ColumnSkeleton borrowed, ColumnSkeleton lended) {
                this.name = String.join("_", new String[]{
                    "CONSTRAINT",
                    borrower.name,
                    "FROM",
                    lender.name,}
                );
                this.borrower = borrower;
                this.lender = lender;
                this.borrowed = borrowed;
                this.lended = lended;
            }

            /**
             * generate the constraint creation string based on the contents
             *
             * @return
             */
            public String createString() {
                return String.join(" ", new String[]{
                    "ALTER TABLE",
                    this.borrower.name,
                    "ADD CONSTRAint",
                    this.name,
                    "FOREIGN KEY (",
                    "(" + this.borrowed + ")",
                    "REFERENCES",
                    this.lender.name,
                    "(" + this.lended + ")",}
                );
            }
        }

        /**
         * holds the info about an sql table
         */
        static class TableSkeleton {

            String name;
            ColumnSkeleton[] columns;
            List<ConstraintSkeleton> constraints;

            public TableSkeleton(String name, ColumnSkeleton[] columns) {
                this.name = name;
                this.columns = columns;
                this.constraints = new ArrayList<>();
            }

            /**
             * generate the table creation string based on the contents; the id
             * string is specified from the outside
             *
             * @param idString
             * @return
             */
            public String createString(String idString) {
                return String.join(" ", new String[]{
                    "CREATE TABLE",
                    this.name,
                    "(",
                    Stream.concat(
                        Stream.of(idString),
                        Arrays.stream(this.columns).map(each -> each.createString())
                    ).collect(Collectors.joining(", ")),
                    ")",
                });
            }
        }

        /**
         * holds the info about an sql column, attached to a table
         */
        static class ColumnSkeleton {

            /**
             * the name of an sqltype, without its potential parameters; we
             * admit the only possible parameter is an integer held by the
             * mother class, ColumnSkeleton
             */
            public enum SQLType {
                INTEGER("INTEGER"),
                FLOAT("FLOAT"),
                VARCHAR("VARCHAR");

                private final String name;

                private SQLType(String s) {
                    name = s;
                }

                public String create() {
                    return this.name;
                }
            }

            String name;
            SQLType type;
            int length;
            boolean notNull;
            boolean unique;
            boolean primary;

            /**
             * generate the column creation string based on the contents
             *
             * @return
             */
            public String createString() {
                return String.join(" ", new String[]{
                    name,
                    switch (type) {
                        case VARCHAR ->
                            type + "(" + length + ")";
                        default ->
                            type.create();
                    },
                    ifTrue(notNull, "NOT NULL"),
                    ifTrue(unique, "UNIQUE"),
                    ifTrue(primary, "PRIMARY"),}
                ).trim();
            }

            /**
             * Defaults notNull, unique and primary to false
             * 
             * @param name
             * @param type
             * @param length 
             */
            public ColumnSkeleton(String name, SQLType type, int length) {
                this.name = name;
                this.type = type;
                this.length = length;
                this.notNull = false;
                this.unique = false;
                this.primary = false;
            }

            /**
             * Defaults notNull, unique and primary to false
             * 
             * @param name
             * @param type
             */
            public ColumnSkeleton(String name, SQLType type) {
                this(name, type, 0);
            }

            /**
             * set notNull to true
             * @return 
             */
            public ColumnSkeleton setNotNull() {
                this.notNull = true;
                return this;
            }

            /**
             * set unique to true
             * @return 
             */
            public ColumnSkeleton setUnique() {
                this.unique = true;
                return this;
            }

            /**
             * set primary to true
             * @return 
             */
            public ColumnSkeleton setPrimary() {
                this.primary = true;
                return this;
            }
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        try {
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                // creation des tables
                st.executeUpdate("create table utilisateur ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " surnom varchar(30) not null unique,"
                        + " pass varchar(20) not null,"
                        + " role integer not null "
                        + ") "
                );
                st.executeUpdate("create table loisir ( "
                        + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ","
                        + " nom varchar(20) not null unique,"
                        + " description text not null"
                        + ") "
                );
                st.executeUpdate("create table pratique ( "
                        + " idutilisateur integer not null,"
                        + " idloisir integer not null,"
                        + " niveau integer not null "
                        + ") "
                );
                con.commit();
                st.executeUpdate("create table apprecie ( "
                        + " u1 integer not null,"
                        + " u2 integer not null"
                        + ") "
                );

                st.executeUpdate("alter table apprecie\n"
                        + "  add constraint fk_apprecie_u1\n"
                        + "  foreign key (u1) references utilisateur(id)"
                );
                st.executeUpdate("alter table apprecie\n"
                        + "  add constraint fk_apprecie_u2\n"
                        + "  foreign key (u2) references utilisateur(id)"
                );
                st.executeUpdate("alter table pratique\n"
                        + "  add constraint fk_pratique_idutilisateur\n"
                        + "  foreign key (idutilisateur) references utilisateur(id)"
                );

                st.executeUpdate("alter table pratique\n"
                        + "  add constraint fk_pratique_idloisir\n"
                        + "  foreign key (idloisir) references loisir(id)"
                );

                con.commit();
            }
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            try {
                st.executeUpdate(
                        "alter table utilisateur "
                        + "drop constraint fk_utilisateur_u1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table utilisateur "
                        + "drop constraint fk_utilisateur_u2");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table pratique "
                        + "drop constraint fk_pratique_idutilisateur");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate(
                        "alter table pratique "
                        + "drop constraint fk_pratique_idloisir");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table apprecie");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table pratique");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table loisir");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table utilisateur");
            } catch (SQLException ex) {
            }
        }
    }

    /**
     *
     * @param con
     * @throws SQLException
     */
    public static void razBdd(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Beton.TableSkeleton joueur = new Beton.TableSkeleton(
                "joueur",
                new Beton.ColumnSkeleton[]{
                    new Beton.ColumnSkeleton(
                            "surnom",
                            Beton.ColumnSkeleton.SQLType.VARCHAR,
                            24
                    ).setUnique(),
                    new Beton.ColumnSkeleton(
                            "categorie",
                            Beton.ColumnSkeleton.SQLType.VARCHAR,
                            1
                    ),
                    new Beton.ColumnSkeleton(
                            "taillecm",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),}
        );

        Beton.TableSkeleton matchs = new Beton.TableSkeleton(
                "matchs",
                new Beton.ColumnSkeleton[]{
                    new Beton.ColumnSkeleton(
                            "ronde",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),
                    new Beton.ColumnSkeleton(
                            "idEquipeA",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),
                    new Beton.ColumnSkeleton(
                            "idEquipeB",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),}
        );

        Beton.TableSkeleton equipe = new Beton.TableSkeleton(
                "equipe",
                new Beton.ColumnSkeleton[]{
                    new Beton.ColumnSkeleton(
                            "nom",
                            Beton.ColumnSkeleton.SQLType.VARCHAR,
                            24
                    ),}
        );

        Beton.TableSkeleton scoresMatchs = new Beton.TableSkeleton(
                "scoresMatchs",
                new Beton.ColumnSkeleton[]{
                    new Beton.ColumnSkeleton(
                            "score",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),
                    new Beton.ColumnSkeleton(
                            "numEquipe",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),
                    new Beton.ColumnSkeleton(
                            "idMatch",
                            Beton.ColumnSkeleton.SQLType.INTEGER
                    ),}
        );

        System.out.println("joueur: " + joueur.createString("##id"));
        System.out.println("matchs: " + matchs.createString("##id"));
        System.out.println("equipe: " + equipe.createString("##id"));
        System.out.println("scoresMatchs: " + scoresMatchs.createString("##id"));

//        try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
//            razBdd(con);
//        } catch (SQLException ex) {
//            throw new Error(ex);
//        }
    }
}
