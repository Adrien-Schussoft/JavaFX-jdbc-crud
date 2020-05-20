package org.adrien.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientDAO {

    private Connection conn;

    public ClientDAO(){
        conn = Connexion.getConnexion();
    }

    public void Insert(Client cli) throws SQLException {

        PreparedStatement stm = conn.prepareStatement("INSERT INTO client (cli_nom, cli_prenom, cli_ville) VALUES (?, ?, ?)");
        stm.setString(1, cli.getNom());
        stm.setString(2, cli.getPrenom());
        stm.setString(3, cli.getVille());
        stm.execute();
        stm.close();
        conn.close();
    }

    public void Update(Client cli) {

        try{
            String query = "UPDATE client SET cli_nom = (?),cli_prenom = (?),cli_ville = (?) WHERE cli_id = (?)";
            PreparedStatement pmt = conn.prepareStatement(query);
            pmt.setString(1, cli.getNom());
            pmt.setString(2, cli.getPrenom());
            pmt.setString(3, cli.getVille());
            pmt.setInt(4,cli.getId());
            pmt.executeUpdate();
            pmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void Delete(Client cli) {

        try {
            conn = Connexion.getConnexion();
            String query = "DELETE FROM client WHERE cli_id = (?)";
            PreparedStatement pmt = conn.prepareStatement(query);
            pmt.setInt(1,cli.getId());
            pmt.execute();
            pmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return resultat
     */
    public ArrayList List() {
        conn = Connexion.getConnexion();
        ResultSet rs = null;
        Client client;
        ArrayList<Client> resultat = new ArrayList<>();
        String query = "SELECT * FROM client";
        try {
            PreparedStatement pmt = conn.prepareStatement(query);
            rs = pmt.executeQuery();
            while (rs.next()) {
                client = new Client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                resultat.add(client);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultat;
    }
}
