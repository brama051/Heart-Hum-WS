package brama.com.aws;


import brama.com.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by vedra on 09.08.2015..
 */
public class RDS {

    Connection connection;

    public RDS(){
        //System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        //System.out.println("MySQL JDBC Driver Registered!");
        connection = null;
        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://hearthum-db.c2mjqvzjslso.us-west-2.rds.amazonaws.com/hearthumdb", "hearthumroot", "hearthumamigosi");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            //System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    //check if user exists
    public int getUserId(String email){
        if (connection!=null){
            int id=0;
            PreparedStatement preparedStatement = null;
            String selectSQL = "SELECT ID FROM User WHERE EMAIL LIKE ?";
            try {
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, email);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    id = rs.getInt("ID");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return id;
        }else {
            return -1;
        }
    }

    public void insertUser(String email){
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO User"
                + "(EMAIL, SENDRESULTS) VALUES"
                + "(?,?)";
        try {
            preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, 1);

            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into User table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    public void insertFiles(int userId, String[] files){
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO File"
                + "(AWSURL, USERID) VALUES";
        for(int i=0;i<files.length;i++){
            if(i==0){
                insertTableSQL = insertTableSQL + "(?,?)";
            }else{
                insertTableSQL = insertTableSQL + ",(?,?)";
            }
        }

        try {
            preparedStatement = connection.prepareStatement(insertTableSQL);
            for(int i=1; i<=files.length*2; i++){
                if(i%2==0){
                    preparedStatement.setInt(i, userId);
                }else{
                    preparedStatement.setString(i, files[(i - 1) / 2]);
                }
            }
            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into FILE table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setSendResults(int userId, int sendResults){
        PreparedStatement preparedStatement = null;

        String updateTableSQL = "UPDATE User SET SENDRESULTS = ? "
                + " WHERE ID = ?";

        try {
            preparedStatement = connection.prepareStatement(updateTableSQL);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, sendResults);

            // execute update SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record is updated to User table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            try{
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    public List<String> getPositiveResults(String email){
        if (connection!=null){
            List<String> adrese = new ArrayList<String>();
            PreparedStatement preparedStatement = null;
            String selectSQL = "SELECT hearthumdb.File.AWSURL FROM hearthumdb.User INNER JOIN hearthumdb.File ON User.ID=File.UserID INNER JOIN hearthumdb.Result ON File.ID = Result.FileID WHERE Result.POSITIVE=1 AND User.EMAIL LIKE ?";
            try {
                preparedStatement = connection.prepareStatement(selectSQL);
                preparedStatement.setString(1, email);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    adrese.add(rs.getString("AWSURL"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return adrese;
        }else {
            return null;
        }
    }


    public void close(){
        try {
            connection.close();
        }catch (Exception e){

        }

    }
}
