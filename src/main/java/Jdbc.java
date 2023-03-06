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
 *  - Statement/PreparedStatement: An object that represents a SQL statement to be executed.
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
 *  * NOTE: Open JDBCWalkthrough.md file for DQL and DML examples
 *      - write SQL logic to create a new song in our songs table
 *      - write SQL logic to retrieve all songs from songs table
 *      - create a method that with the JDBC logic toretrieve a song from songs table
 *      - create a method that with the JDBC logic to delete a songs 
 *      - create a method that with the JDBC logic to update an entire songs 
 *      - create a method that with the JDBC logic to update a songs artist 
 */

public class Jdbc {

    //1. write JDBC logic to create a new song in our songs table
    public void createSong(Song song)  {
        try {
            //retrieve active connection to db
            Connection connection = ConnectionUtil.getConnection();
            //SQL statement we are going to execute, we use ? as placeholders we later set.
            
            //1. write SQL statement here
            String sql = "change me";

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

    //2. write SQL logic to retrieve all songs from songs table
    public List<Song> getAllSongs(){
        List<Song> songs = new ArrayList<>();
        try{
        Connection connection = ConnectionUtil.getConnection();
                
        //2. write SQL statement here
        String sql = "change me";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()){
                songs.add(new Song(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    //3. create a method that with the JDBC logic to retrieve a song from songs table using the song id
    public Song getSong(int id){
        return null;
    }

    //4. create a method that with the JDBC logic to delete a songs, return the number of affected rows 
    public int deleteSong(int id) {
        return 0;
    }

    //5. create a method that with the JDBC logic to update an entire songs, return true if successful  
    public boolean updateSong(Song song, int id) throws SQLException {
        return false;       
    }

    //6. create a method that with the JDBC logic to update a songs artist, return true if successful
    public boolean updateSongArtist(String artistName, int songId) throws SQLException {
       return false;
    }
}
