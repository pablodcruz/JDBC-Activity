

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.ConnectionUtil;

/**
 * JDBC stands for Java DataBase Connectivity.  It is utilized to connect our java code with a database.
 * JDBC will allow us to execute SQL statements from java and retrieve the result set of that query to be utilized in java
 *
 * JDBC datatypes to know:
 *  - Connection: Creates an active connection to the database.
 *  - Statement: An object that represents a SQL statement to be executed.
 *  - ResultSet: An object that represents the virtual table return from a query (Only needed for executing DQL statements)
 *
 * Background:
 * Assume we have the following table:
 *      songs table
 *      |   id  |      title        |        artist         |
 *      -----------------------------------------------------
 *      |1      |'Let it be'        |'Beatles'              |
 *      |2      |'Hotel California' |'Eagles'               |
 *      |3      |'Kashmir'          |'Led Zeppelin'         |
 *
 * Assignment: Write JDBC logic in the methods below to achieve the following in our database
 *      - write jdbc logic to create a new song in our songs table
 *      - write jdbc logic to retrieve all songs from songs table
 *      - create a method that with the jdbc logic toretrieve a song from songs table
 *      - create a method that with the jdbc logic to delete a songs 
 *      - create a method that with the jdbc logic to update an entire songs 
 *      - create a method that with the jdbc logic to update a songs artist 
 */

public class Jdbc {

    //write jdbc logic to create a new song in our songs table
    public void createSong(Song song)  {
        // 1. write jdbc code here
        try {
            //retrieve active connection to db
            Connection connection = ConnectionUtil.getConnection();
            //SQL statement we are going to execute, we use ? as placeholders we later set.
            String sql = "insert into songs (title, artist) values (?, ?)";
            //create PrepareStatement object (better than Statement object)
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //Add the song data to fill in the placeholders ? in the sql statement. 1 for the first ?, 2 for the second
            pstmt.setString(1, song.getTitle());
            pstmt.setString(2, song.getArtist());
            // execute the statement to db
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //write jdbc logic to retrieve all songs from songs table
    public List<Song> getAllSongs(){
        List<Song> songs = new ArrayList<>();
        //2. write jdbc code here
        try{
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from songs;";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()){
                songs.add(new Song(rs.getString(1), rs.getString(2)));
            }
            //NOTE: in the above statement, rs.getString(1) is retrieving column 1 as a string from a record. in our situation, that will get the firstname. Since lastname is in the second column in the table, we retrieve that value by using rs.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    //3. create a method that with the jdbc logic to retrieve a song from songs table using the song id
    public Song getSong(int id){
        Song song = new Song();
        //3. write jdbc code here
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "select * from songs where id = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                song = new Song(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return song;
    }

    //4. create a method that with the jdbc logic to delete a songs, return the number of affected rows 
    public int deleteSong(int id) {
        int affectedrows = 0;
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM songs WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, id);
            affectedrows = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(affectedrows);
        return affectedrows;
    }

    //5. create a method that with the jdbc logic to update an entire songs, return true if successful.  
    public boolean updateSong(Song song, int id) throws SQLException {
        int numberOfRecordsUpdated = 0;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE songs SET title = ?, artist = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, song.getTitle());
            pstmt.setString(2, song.getArtist());
            pstmt.setInt(3, id);
            numberOfRecordsUpdated = pstmt.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }   
        return numberOfRecordsUpdated == 1;
    }

    //6. create a method that with the jdbc logic to update a songs artist 
    public boolean updateSongArtist(String artistName, int songId) throws SQLException {
        int numberOfRecordsUpdated = 0;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE songs SET artist = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, artistName);
            pstmt.setInt(2, songId);

            numberOfRecordsUpdated = pstmt.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }   
        return numberOfRecordsUpdated == 1;
    }

}
