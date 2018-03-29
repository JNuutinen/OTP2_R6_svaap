package model.db;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Luokka tietokannan käsittelyyn. Tietokannan url, sekä tietokantakäyttäjän nimi ja salasana luetaan
 * config.properties -tiedostosta, sitä ei saa tallentaa versionhallintaan eikä saliksia laittaa
 * lähdekoodiin. Tiedosto pitää olla projectin pathissa, esim. resources kansiossa.
 */
public class Database {

    /**
     * Tietokantayhteys.
     */
    private Connection connection;

    /**
     * Konstruktori. Lataa tietokanta-ajurin ja luo tietokantayhteyden servun tietokantaan.
     */
    public Database() {

        // tietokanta-ajurin lataus
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC-ajurin lataus epäonnistui");
            System.exit(-1);
        }

        // tietokannan url ja käyttäjän tiedot config.properties -tiedostosta
        Properties config = new Properties();
        try {
            config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String URL = config.getProperty("url");
        final String USERNAME = config.getProperty("username");
        final String PASSWORD = config.getProperty("password");

        // tietokantayhteyden luonti
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            do {
                System.err.println("Viesti: " + e.getMessage());
                System.err.println("Virhekoodi: " + e.getErrorCode());
                System.err.println("SQL-tilakoodi: " + e.getSQLState());
            } while (e.getNextException() != null);
        }
    }

    /**
     * Testimetodi tietokannan testaamiseen. Luo rivin save tauluun ja lukee kaikki save taulun tiedot.
     */
    public void dbTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO save VALUES (0, 'testi-nick', 3, 500, 'testi-primary', 'testi-secondary')");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM save");
            while (resultSet.next()) {
                System.out.println("Row " + resultSet.getRow());
                System.out.println("id: " + resultSet.getInt("id") + ", "
                        + "nick: " + resultSet.getString("nick") + ", "
                        + "level: " + resultSet.getInt("level") + ", "
                        + "score: " + resultSet.getInt("score") + ", "
                        + "primary_weapon: " + resultSet.getString("primary_weapon") + ", "
                        + "secondary_weapon: " + resultSet.getString("secondary_weapon") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            do {
                System.err.println("Viesti: " + e.getMessage());
                System.err.println("Virhekoodi: " + e.getErrorCode());
                System.err.println("SQL-tilakoodi: " + e.getSQLState());
            } while (e.getNextException() != null);
        }
    }

    /**
     * TESTIKÄYTTÖÖN. Tyhjentää save taulun tietokannasta.
     */
    public void purgeSaves() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM save");
        } catch (SQLException e) {
            e.printStackTrace();
            do {
                System.err.println("Viesti: " + e.getMessage());
                System.err.println("Virhekoodi: " + e.getErrorCode());
                System.err.println("SQL-tilakoodi: " + e.getSQLState());
            } while (e.getNextException() != null);
        }
    }
}
