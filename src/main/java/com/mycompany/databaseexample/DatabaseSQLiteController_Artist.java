package com.mycompany.databaseexample;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class DatabaseSQLiteController_Artist implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private VBox vBox;

    @FXML
    private TextField nameTextField, genreTextField, songsTextField; //where data gets entered by user

    @FXML
    Label footerLabel;
    @FXML
    //TableColumn id = new TableColumn("ID");

    @Override
     public void initialize(URL location, ResourceBundle rb) {
        try {
            loadData();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        intializeColumns();
        createSQLiteTable();
    }

    String databaseURL = "jdbc:sqlite:src/main/resources/com/mycompany/databaseexample/artists.db";

    /* Connect to a sample database
     */
    private ObservableList<Artist> data; //object of records in DB

    /*
       ArrayList: Resizable-array implementation of the List interface. 
       Implements all optional list operations, and permits all elements, including null.
       ObservableList: A list that allows listeners to track changes when they occur
    */
    
    
    public DatabaseSQLiteController_Artist() throws SQLException {
        this.data = FXCollections.observableArrayList(); //a moveable list of artists
    }

   // /*
    private void intializeColumns() {
        //make sure to add comments  here
        /*id = new TableColumn("ID");
        id.setMinWidth(50);
        //id.setCellValueFactory(new PropertyValueFactory<Artist, Integer>("id"));
        id.setCellValueFactory(new PropertyValueFactory<Artist, Integer>("id"));
        
        TableColumn name = new TableColumn("Name");
        name.setMinWidth(450);
        name.setCellValueFactory(new PropertyValueFactory<Artist, String>("name"));

        TableColumn genre = new TableColumn("Genre");
        genre.setMinWidth(100);
        genre.setCellValueFactory(new PropertyValueFactory<Artist, String>("genre"));
        
        TableColumn songs = new TableColumn("Songs");
        songs.setMinWidth(100);
        songs.setCellValueFactory(new PropertyValueFactory<Artist, String>("song"));
        
       tableView.setItems(data);
       tableView.getColumns().addAll(id, name, genre, songs); */

        
     
    TableColumn id = new TableColumn("ID");
    id.setMinWidth(50);
    id.setCellValueFactory(new PropertyValueFactory<Artist, Integer>("ID"));
    
    TableColumn name = new TableColumn("Name");
    name.setMinWidth(450);
    name.setCellValueFactory(new PropertyValueFactory<Artist, String>("name"));

    TableColumn genre = new TableColumn("Genre");
    genre.setMinWidth(100);
    genre.setCellValueFactory(new PropertyValueFactory<Artist, String>("genre"));
    
    TableColumn songs = new TableColumn("Songs");
    songs.setMinWidth(100);
    songs.setCellValueFactory(new PropertyValueFactory<Artist, String>("song"));
    
    tableView.setItems(data);
    tableView.getColumns().addAll(id, name, genre, songs);
}

        
        
        //tableView.setOpacity(0.3);
        ///* Allow for the values in each cell to be changable 
    
//*/
    
    /*
    private void intializeColumns() {
    // Make sure to add comments here
    TableColumn<Artist, Integer> id = new TableColumn<>("ID");
    id.setMinWidth(50);
    id.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Artist, String> name = new TableColumn<>("Name");
    name.setMinWidth(450);
    name.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Artist, String> genre = new TableColumn<>("Genre");
    genre.setMinWidth(100);
    genre.setCellValueFactory(new PropertyValueFactory<>("genre"));

    TableColumn<Artist, String> songs = new TableColumn<>("Songs");
    songs.setMinWidth(100);
    songs.setCellValueFactory(new PropertyValueFactory<>("songs"));

    tableView.setItems(data);
    tableView.getColumns().addAll(id, name, genre, songs);

    // tableView.setOpacity(0.3); // Allow for the values in each cell to be changable 
}
    */



    public void loadData() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
 
        try {

            // create a connection to the database
            conn = DriverManager.getConnection(databaseURL);

            System.out.println("Connection to SQLite has been established.");
            String sql = "SELECT * FROM artists;"; //selects everything, can change to only show a few feilds (important fields)
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

         while (rs.next()) {
                Artist artist;
                artist = new Artist(rs.getInt("id"), rs.getString("name"), rs.getString("genre"), rs.getString("songs"));
                System.out.println(artist.getID() + " - " + artist.getName() + " - " + artist.getGenre() + " - " + artist.getSong());
                data.add(artist);
            }
            rs.close(); //if you open a connection should close it too
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void drawText() {
        //Drawing a text 
        Text text = new Text("The Artist Database");
 
        text.setFont(Font.font("Edwardian Script ITC", 55));

        Stop[] stops = new Stop[]{
            new Stop(0, Color.DARKSLATEBLUE),
            new Stop(1, Color.RED)
        };
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        //Setting the linear gradient to the text 
        text.setFill(linearGradient);
        // Add the child to the grid
        vBox.getChildren().add(text);

    }

    /**
     * Insert a new row into the songs table
     *
     * @param name
     * @param genre
     * @param songs

     * @throws java.sql.SQLException
     */
    public void ainsert(String name, String genre, String songs) throws SQLException {
        int last_inserted_id = 0;
        Connection conn = null;
        try {
            // create a connection to the database

            conn = DriverManager.getConnection(databaseURL);

            System.out.println("Connection to SQLite has been established.");

            System.out.println("Inserting one record!");

            //String sql = "INSERT INTO songs(title,artist) VALUES(?,?)"; //values of question marks come from the variables
            String sql = "INSERT INTO artists(name,genre,songs) VALUES(?,?,?)";
            
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, genre);
            pstmt.setString(3, songs);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                last_inserted_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        //System.out.println("last_inserted_id " + last_inserted_id);
  
        System.out.println("last_inserted_id " + last_inserted_id);
        data.add(new Artist(last_inserted_id, name, genre, songs));
    }

    @FXML
    public void handleAAddArtist(ActionEvent actionEvent) {

        //System.out.println("Title: " + titleTextField.getText() + "\nArtist: " + artistTextField.getText());//only prints in console (user doesn't see)
       
        System.out.println("Name: " + nameTextField.getText() + "\nGenre: " + genreTextField.getText() + "\nSongs: " + songsTextField.getText());
        
        try {
            // insert a new rows
            ainsert(nameTextField.getText(), genreTextField.getText(), songsTextField.getText());
            System.out.println("Data was inserted Successfully");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        nameTextField.setText("");
        genreTextField.setText("");
        songsTextField.setText("");
        footerLabel.setText("Record inserted into table successfully!");
    }

      private void createSQLiteTable() {
        String sql = "CREATE TABLE IF NOT EXISTS artists (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " genre text NOT NULL,\n"
                + " song text NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(databaseURL);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteARecord(int id, int selectedIndex) { //id to remove from DB, selectedIndex to remove from table

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(databaseURL);
            String sql = "DELETE FROM Artists WHERE id=" + Integer.toString(id);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

            tableView.getItems().remove(selectedIndex); //selectedIndex is row from table view (id won't match the current row)
            System.out.println("Record Deleted Successfully");
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    @FXML
    private void handleADeleteAction(ActionEvent event) throws IOException {
        System.out.println("Delete Artist");
        //Check whether item is selected and set value of selected item to Label
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);

            if (selectedIndex >= 0) {

                System.out.println(index);
                Artist artist = (Artist) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + artist.getID());
                System.out.println("Name: " + artist.getName());
                System.out.println("Genre: " + artist.getGenre());
                System.out.println("Songs: " + artist.getSong());
                deleteARecord(artist.getID(), selectedIndex);
            }

        }
    }

    Integer index = -1;

    @FXML
    private void showARowData() {

        index = tableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }

        System.out.println("showRowData");
        System.out.println(index);
        Artist artist = (Artist) tableView.getSelectionModel().getSelectedItem();
        System.out.println("ID: " + artist.getID());
        System.out.println("Name: " + artist.getName());
        System.out.println("Genre: " + artist.getGenre());
        System.out.println("Songs: " + artist.getSong());
        nameTextField.setText(artist.getName());
        genreTextField.setText(artist.getGenre());
        songsTextField.setText(artist.getSong());
        String content = "Id= " + artist.getID() + "\nName= " + artist.getName() + "\nGenre= " + artist.getGenre() + "\nSongs= " + artist.getSong();

    }


    @SuppressWarnings("empty-statement")
    public ObservableList<Artist> asearch(String _name, String _genre, String _song) throws SQLException {
        ObservableList<Artist> searchResult = FXCollections.observableArrayList();
        // read data from SQLite database
        createSQLiteTable();
      String sql = "Select * from Artists where true";
        if (!_name.isEmpty()) {
            sql += " and name like '%" + _name + "%'";
        }
        if (!_genre.isEmpty()) {
            sql += " and genre ='" + _genre + "'";
        }
          if (!_song.isEmpty()) {
            sql += " and song ='" + _song + "'";
        }
        System.out.println(sql);
        try (Connection conn = DriverManager.getConnection(databaseURL);
                Statement stmt = conn.createStatement()) {
            // create a ResultSet

            ResultSet rs = stmt.executeQuery(sql);
            // checking if ResultSet is empty
            if (rs.next() == false) {
                System.out.println("ResultSet in empty");
            } else {
                // loop through the result set
                do {

                    /*int recordId = rs.getInt("id");
                    String title = rs.getString("title");
                    String artist = rs.getString("artist");
                    */
                    
                    /*int recordId = rs.getInt("id");
                    String name = rs.getString("name");
                    String genre = rs.getString("genre");
                    String songs = rs.getString("songs");
                    */
                    
                    int recordId = rs.getInt("id");
                    String name = rs.getString("name");
                    String genre = rs.getString("genre");
                    String songs = rs.getString("songs");

                    
                    Artist artist = new Artist(recordId, name, genre, songs);
                    searchResult.add(artist);
                    
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return searchResult;
    }

    @FXML
    private void handleASearchAction(ActionEvent event) throws IOException, SQLException {
        String _name = nameTextField.getText().trim();
        String _genre = genreTextField.getText().trim();
        String _song = songsTextField.getText().trim();
        ObservableList<Artist> tableItems = asearch(_name, _genre, _song);
        tableView.setItems(tableItems);
    }

    @FXML
    private void handleAShowAllRecords(ActionEvent event) throws IOException, SQLException {
        tableView.setItems(data);

    }

    /**
     * Update a record in the songs table
     *
     * @param name
     * @param genre
     * @param songs

     * @throws java.sql.SQLException
     */
    public void aupdate(String name, String genre, String songs, int selectedIndex, int id) throws SQLException {

       //public void update(int id, String title, String artist) throws SQLException {

    Connection conn = null;
    try {
        // create a connection to the database
conn = DriverManager.getConnection(databaseURL);
            String sql = "UPDATE Artists SET name = ?, genre = ?, song = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, genre);
            pstmt.setString(3, songs);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}


   

    @FXML
    private void handleAUpdateRecord(ActionEvent event) throws IOException, SQLException {

         if (tableView.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);
            if (selectedIndex >= 0) {
                System.out.println(index);
                Artist artist = (Artist) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + artist.getID());
                try {
                    aupdate(nameTextField.getText(), genreTextField.getText(), songsTextField.getText(), selectedIndex, artist.getID());
                    System.out.println("Record updated successfully!");
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }
                
                nameTextField.setText("");
                genreTextField.setText("");
                songsTextField.setText("");
                footerLabel.setText("Record updated successfully!");
                data.clear();
                loadData();
                tableView.refresh();
            }
        }
    }

    @FXML
    private void sidebarAShowAllRecords() {
        tableView.setItems(data);
    }

    @FXML
    private void sidebarAAddNewRecord() {
System.out.println("Name: " + nameTextField.getText() + "\nGenre: " + genreTextField.getText() + "\nSongs: " + songsTextField.getText());
        try {
            ainsert(nameTextField.getText(), genreTextField.getText(), songsTextField.getText());
            System.out.println("Data was inserted Successfully");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        nameTextField.setText("");
        genreTextField.setText("");
        songsTextField.setText("");
        footerLabel.setText("Record inserted into table successfully!");
    }  
      
    @FXML
    private void sidebarADeleteRecord() {
         System.out.println("Delete Artist");
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);
            if (selectedIndex >= 0) {
                System.out.println(index);
                Artist artist = (Artist) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + artist.getID());
                System.out.println("Name: " + artist.getName());
                System.out.println("Genre: " + artist.getGenre());
                System.out.println("Songs: " + artist.getSong());
                deleteARecord(artist.getID(), selectedIndex);
            }
        }
    }

    @FXML
    private void sidebarASearch() throws SQLException {
        String _name = nameTextField.getText().trim();
        String _genre = genreTextField.getText().trim();
        String _song = songsTextField.getText().trim();
        ObservableList<Artist> tableItems = asearch(_name, _genre, _song);
        tableView.setItems(tableItems);
    }

    
     @FXML
    private void sidebarAUpdateRecord() throws SQLException {
        //Check whether item is selected and set value of selected item to Label
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);
            if (selectedIndex >= 0) {
                System.out.println(index);
                Artist artist = (Artist) tableView.getSelectionModel().getSelectedItem();
                System.out.println("id: " + artist.getID());
                try {
                    aupdate(nameTextField.getText(), genreTextField.getText(), songsTextField.getText(), selectedIndex, artist.getID());
                    System.out.println("Record updated successfully!");
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }
                nameTextField.setText("");
                genreTextField.setText("");
                songsTextField.setText("");
                footerLabel.setText("Record updated successfully!");
                data.clear();
                loadData();
                tableView.refresh();
            }
        }
    }   
    
}
