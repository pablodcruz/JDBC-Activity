# Class Activity: Build Demo JDBC Application with DAO

#### Contributors:
{ Pablo De La Cruz } 

### Scope:
The main goal of the activity is to teach students how to use a DAO pattern to perform CRUD operations in a JDBC application. By implementing a DAO class and using it in a Main class to perform CRUD operations, students will learn the following:
- The purpose and benefits of using the DAO pattern in a JDBC application.
- How to create a DAO class that includes methods for performing CRUD operations.
- How to use the DAO class to interact with a database.


### Project Setup
1. Divide the class into groups of 2-3 students.
2. Have each group fork your activity repo -> open locally -> follow instrustions in Jdbc.java
3. Tell students how many tests out of the total they should complete with the time you provide (I recommend 2 per 60min). Can use VSCode or Intellij to run tests
4. Give the groups time to work on the activity and provide assistance as needed.
5. Once the groups have completed the activity, rank them by time and accuracy.
6. Demo/Explain any concept the class struggled with. (check solution branch for answers)
7. Finally, provide a summary of the main takeaways from the activity and how the DAO pattern can be used in real-world applications.

## How to Complete this Activity

1. Follow Jdbc.java instructions
2. Pass tests  

### Discussion & FAQ
- Refer to JDBCWalkthrough.MD 
### Evaluation Criteria:
- 2/6 tests in 60min
- Proper implementation of the DAO pattern
- Proper implementation of CRUD operations
- Code quality and organization
- Presentation and demonstration of the demo application (optional)

## Solution
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.ConnectionUtil;

public class Jdbc {

    //1. write JDBC logic to create a new song in our songs table
    public void createSong(Song song)  {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "insert into songs (title, artist) values (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, song.getTitle());
            pstmt.setString(2, song.getArtist());
            
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
            String sql = "select * from songs;";
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
        Song song = new Song();
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "select * from songs where id = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                song = new Song(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return song;
    }

    //4. create a method that with the JDBC logic to delete a songs, return the number of affected rows 
    public int deleteSong(int id) {
        int affectedrows = 0;
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM songs WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);

            affectedrows = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(affectedrows);
        return affectedrows;
    }

    //5. create a method that with the JDBC logic to update an entire songs, return true if successful.  
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

    //6. create a method that with the JDBC logic to update a songs artist 
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
