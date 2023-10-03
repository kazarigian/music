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


public class DatabaseSQLiteController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private VBox vBox;

    @FXML
    private TextField titleTextField, artistTextField;//where data gets entered by user

    @FXML
    Label footerLabel;
    @FXML
    TableColumn id = new TableColumn("ID");

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        try {
            loadData();
        } catch (SQLException ex) {
            
            System.out.println(ex.toString());
        }
        intializeColumns();
        CreateSQLiteTable();
    }

    String databaseURL = "jdbc:sqlite:src/main/resources/com/mycompany/databaseexample/songs";

    /* Connect to a sample database
     */
    private ObservableList<Song> data; //object of records in DB

    /*
       ArrayList: Resizable-array implementation of the List interface. 
       Implements all optional list operations, and permits all elements, including null.
       ObservableList: A list that allows listeners to track changes when they occur
    */
    
    
    public DatabaseSQLiteController() throws SQLException {
        this.data = FXCollections.observableArrayList(); //a moveable list of songs
    }

    private void intializeColumns() {
        id = new TableColumn("ID");
        id.setMinWidth(50);
        id.setCellValueFactory(new PropertyValueFactory<Song, Integer>("id"));

        TableColumn title = new TableColumn("Title");
        title.setMinWidth(450);
        title.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));

        TableColumn artist = new TableColumn("Artist");
        artist.setMinWidth(100);
        artist.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));

        
        //tableView.setOpacity(0.3);
        /* Allow for the values in each cell to be changable */
    }

    public void loadData() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
 
        try {

            // create a connection to the database
            conn = DriverManager.getConnection(databaseURL);

            System.out.println("Connection to SQLite has been established.");
            String sql = "SELECT * FROM songs;"; //selects everything, can change to only show a few feilds (important fields)
            // Ensure we can query the actors table
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Song song;
                song = new Song(rs.getInt("id"), rs.getString("title"), rs.getString("artist")); //CHANGE ~ how to deal with artists object
                System.out.println(song.getId() + " - " + song.getTitle() + " - " + song.getArtist());
                data.add(song); //adding to record(DB) of songs
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
        Text text = new Text("Music Library");

        //Setting the font of the text 
        text.setFont(Font.font("Edwardian Script ITC", 55));

        //Setting the position of the text 
//        text.setX(600);
//        text.setY(100);
        //Setting the linear gradient 
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
     * @param title
     * @param artist
     * @throws java.sql.SQLException
     */
    public void insert(String title, String artist) throws SQLException {
        int last_inserted_id = 0;
        Connection conn = null;
        try {
            // create a connection to the database

            conn = DriverManager.getConnection(databaseURL);

            System.out.println("Connection to SQLite has been established.");

            System.out.println("Inserting one record!");

            String sql = "INSERT INTO songs(title,artist) VALUES(?,?)"; //values of question marks come from the variables

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
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
        System.out.println("last_inserted_id " + last_inserted_id);
  
        data.add(new Song(last_inserted_id, title, artist));
    }

    @FXML
    public void handleAddSong(ActionEvent actionEvent) {

        System.out.println("Title: " + titleTextField.getText() + "\nArtist: " + artistTextField.getText());//only prints in console (user doesn't see)

        try {
            // insert a new rows
            insert(titleTextField.getText(), artistTextField.getText()); //these are the objects made to hold the input (from line 44 or 47)

            System.out.println("Data was inserted Successfully");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        titleTextField.setText("");
        artistTextField.setText("");
        //re-seting fields after sucessfully inputing info
        footerLabel.setText("Record inserted into table successfully!");
    }

    private void CreateSQLiteTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS songs (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	title text NOT NULL,\n"
                + "	artist string NOT NULL,\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(databaseURL);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRecord(int id, int selectedIndex) { //id to remove from DB, selectedIndex to remove from table

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(databaseURL);

            String sql = "DELETE FROM Songs WHERE id=" + Integer.toString(id); //id has to be made a string to concotinate it with the other statement
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
    private void handleDeleteAction(ActionEvent event) throws IOException {
        System.out.println("Delete Song");
        //Check whether item is selected and set value of selected item to Label
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);

            if (selectedIndex >= 0) {

                System.out.println("Handle Delete Action");
                System.out.println(index);
                Song song = (Song) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + song.getId());
                System.out.println("Title: " + song.getTitle());
                System.out.println("Artist: " + song.getArtist());
                deleteRecord(song.getId(), selectedIndex);
            }

        }
    }

    Integer index = -1;

    @FXML
    private void showRowData() {

        index = tableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }

        System.out.println("showRowData");
        System.out.println(index);
        Song song = (Song) tableView.getSelectionModel().getSelectedItem(); //making empty object of song then casting song type to the selectedItem
        System.out.println("ID: " + song.getId());
        System.out.println("Title: " + song.getTitle());
        System.out.println("Artist: " + song.getArtist());

        titleTextField.setText(song.getTitle());
        artistTextField.setText(song.getArtist());

        String content = "Id= " + song.getId() + "\nTitle= " + song.getTitle() + "\nArtist= " + song.getArtist();

    }


    @SuppressWarnings("empty-statement")
    public ObservableList<Song> search(String _title, String _artist) throws SQLException {
        ObservableList<Song> searchResult = FXCollections.observableArrayList();
        // read data from SQLite database
        CreateSQLiteTable();
        String sql = "Select * from Songs where true";

        if (!_title.isEmpty()) {
            sql += " and title like '%" + _title + "%'";
        }
        if (!_artist.isEmpty()) {
            sql += " and artist ='" + _artist + "'";
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

                    int recordId = rs.getInt("id");
                    String title = rs.getString("title");
                    String artist = rs.getString("artist");
                    
                    Song song = new Song(recordId, title, artist);
                    searchResult.add(song);
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return searchResult;
    }

    @FXML
    private void handleSearchAction(ActionEvent event) throws IOException, SQLException {
        String _title = titleTextField.getText().trim();
        String _artist = artistTextField.getText().trim();
        ObservableList<Song> tableItems = search(_title, _artist);
        tableView.setItems(tableItems);

    }

    @FXML
    private void handleShowAllRecords(ActionEvent event) throws IOException, SQLException {
        tableView.setItems(data);

    }

    /**
     * Update a record in the songs table
     *
     * @param title
     * @param artist
     * @throws java.sql.SQLException
     */
    public void update(String title, String artist, int selectedIndex, int id) throws SQLException {

        Connection conn = null;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(databaseURL);
            String sql = "UPDATE songs SET title = ?, artist =? Where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, artist);;
            pstmt.setString(4, Integer.toString(id)); //CHANGE there was a line before it that was three, should 4 be changed to three, if so does anything else need to be updated 

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
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
    private void handleUpdateRecord(ActionEvent event) throws IOException, SQLException {

        //Check whether item is selected and set value of selected item to Label
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);

            if (selectedIndex >= 0) {

                System.out.println(index);
                Song song = (Song) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + song.getId());

                try {
                    // insert a new rows
                    update(titleTextField.getText(), artistTextField.getText(), selectedIndex, song.getId());

                    System.out.println("Record updated successfully!");
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }

                titleTextField.setText("");
                artistTextField.setText("");

                footerLabel.setText("Record updated successfully!");
                data.clear();
                loadData();
                tableView.refresh();
            }

        }

    }

    @FXML
    private void sidebarShowAllRecords() {
        tableView.setItems(data);
    }

    @FXML
    private void sidebarAddNewRecord() {
        System.out.println("Title: " + titleTextField.getText() + "\nArtist: " + artistTextField.getText());

        try {
            // insert a new rows
            insert(titleTextField.getText(), artistTextField.getText());

            System.out.println("Data was inserted Successfully");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        titleTextField.setText("");
        artistTextField.setText("");

        footerLabel.setText("Record inserted into table successfully!");

    }

    @FXML
    private void sidebarDeleteRecord() {
        System.out.println("Delete Song");
        //Check whether item is selected and set value of selected item to Label
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);

            if (selectedIndex >= 0) {

                System.out.println("Handle Delete Action");
                System.out.println(index);
                Song song = (Song) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + song.getId());
                System.out.println("Title: " + song.getTitle());
                System.out.println("Artist: " + song.getArtist());
                deleteRecord(song.getId(), selectedIndex);
            }

        }
    }

    @FXML
    private void sidebarSearch() throws SQLException {
        String _title = titleTextField.getText().trim();
        String _artist = artistTextField.getText().trim();
        ObservableList<Song> tableItems = search(_title, _artist);
        tableView.setItems(tableItems);
    }

    
     @FXML
    private void sidebarUpdateRecord() throws SQLException {
        //Check whether item is selected and set value of selected item to Label
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: " + selectedIndex);

            if (selectedIndex >= 0) {

                System.out.println(index);
                Song movie = (Song) tableView.getSelectionModel().getSelectedItem();
                System.out.println("ID: " + movie.getId());

                try {
                    // insert a new rows
                    update(titleTextField.getText(), artistTextField.getText(), selectedIndex, movie.getId());

                    System.out.println("Record updated successfully!");
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }

                titleTextField.setText("");
                artistTextField.setText("");

                footerLabel.setText("Record updated successfully!");
                data.clear();
                loadData();
                tableView.refresh();
            }
        }
    }   
    
}
