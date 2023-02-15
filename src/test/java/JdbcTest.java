
import util.ConnectionUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTest {
    Jdbc jdbc = new Jdbc();
    
    @Before
    public void beforeEach(){
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "CREATE TABLE songs (id SERIAL PRIMARY KEY, title varchar(100), artist varchar(100));";
            String sql2 = "INSERT INTO songs (title, artist) VALUES ('Let it be', 'Beatles');";
            String sql3 = "INSERT INTO songs (title, artist) VALUES ('Imagine', 'Beatles');";
            String sql4 = "INSERT INTO songs (title, artist) VALUES ('Kashmir', 'Led Zeppelin');";
            PreparedStatement ps = connection.prepareStatement(sql + sql2 + sql3 + sql4);
            ps.executeUpdate();
            System.out.println(jdbc.getAllSongs());
        } catch (SQLException e) {
            System.out.println("dropping table");
        }
    }

    /**
     * The @After annotation runs after every test so that way we drop the tables to avoid conflicts in future tests
     */
    @After
    public void cleanup(){
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DROP TABLE songs;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("dropping table");
        }
    }

    @Test
    public void createSongTest(){
        int counter = 0;
        Song song = new Song("song1", "artist1");
        jdbc.createSong(song);
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "select * from songs;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                counter++;
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        Assert.assertEquals(4,counter);
    }

    @Test
    public void selectAllSongsTest(){
        List<Song> expectedResult = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "select * from songs;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                expectedResult.add(new Song(rs.getString(1), rs.getString(2)));
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        List<Song> actualResult = jdbc.getAllSongs();
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test
    public void selectSongTest() throws Exception {
        Song expectedResult = new Song();
        expectedResult.setId(1);
        expectedResult.settitle("Let it be");
        expectedResult.setArtist("Beatles");

        Class jdbcClass = Class.forName("Jdbc");
        Object jdbcObject = jdbcClass.getDeclaredConstructor().newInstance();
        Method getSongMethod;
        try {
            getSongMethod = jdbcClass.getDeclaredMethod("getSong", int.class);
        } catch (NoSuchMethodException e) {
            Assert.fail("getSong method not found in Jdbc.java");
            return;
        }
        getSongMethod.setAccessible(true);
        try {
            Song actualResult = (Song) getSongMethod.invoke(jdbcObject, 1);
            Assert.assertEquals(expectedResult, actualResult);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteSongTest() throws Exception {
        int expectedResult = 1; //affected rows
        Class jdbcClass = Class.forName("Jdbc");
        Object jdbcObject = jdbcClass.getDeclaredConstructor().newInstance();
        Method deleteSongMethod;
        try {
            deleteSongMethod = jdbcClass.getDeclaredMethod("deleteSong", int.class);
        } catch (NoSuchMethodException e) {
            Assert.fail("deleteSong method not found in Jdbc.java");
            return;
        }
        deleteSongMethod.setAccessible(true);
        int actualResult = (int)deleteSongMethod.invoke(jdbcObject, 1);
        System.out.println(jdbc.getAllSongs());
        Assert.assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void updateSongTest() throws Exception {
        boolean expectedResult = true;
        boolean actualResult = false;
        Song song = new Song("Led Zeppelin", "Stairway to Heaven");
        Class jdbcClass = Class.forName("Jdbc");
        Object jdbcObject = jdbcClass.getDeclaredConstructor().newInstance();
        Method updateSongMethod;
        try {
            updateSongMethod = jdbcClass.getDeclaredMethod("updateSong", Song.class, int.class);
        } catch (NoSuchMethodException e) {
            Assert.fail("updateSong method not found in Jdbc.java");
            return;
        }
        updateSongMethod.setAccessible(true);
        try {
            actualResult = (boolean) updateSongMethod.invoke(jdbcObject, song, 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(jdbc.getAllSongs());
        Assert.assertEquals(expectedResult, actualResult);
    }


    @Test
    public void updateSongArtistTest() throws Exception {
        Class jdbcClass = Class.forName("Jdbc");
        Object jdbcObject = jdbcClass.getDeclaredConstructor().newInstance();
        Method updateSongArtistMethod;
        try {
            updateSongArtistMethod = jdbcClass.getDeclaredMethod("updateSongArtist", String.class, int.class);
        } catch (NoSuchMethodException e) {
            Assert.fail("updateSongArtist method not found in Jdbc.java");
            return;
        }
        updateSongArtistMethod.setAccessible(true);
    
        boolean expectedResult = true;
        boolean actualResult = false;
        try {
            actualResult = (boolean) updateSongArtistMethod.invoke(jdbcObject, "MJ", 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(jdbc.getAllSongs());
        Assert.assertEquals(expectedResult, actualResult);
    }
}
