package com.mycompany.databaseexample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
 private Parent root;
    private static Scene scene;
    private static Stage stage;
    
    
    @FXML private Text actiontarget;
    
    @FXML
    private TextField user_name, password;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {
        
        
        
        String userNameText = user_name.getText();
        String passwordText = password.getText();
         
         
        System.out.println(userNameText);
        System.out.println(passwordText);

        if (userNameText.equals("a")  && passwordText.equals("z")) {
           actiontarget.setText("Sign in successful");  
            
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        }
        else
        {
            actiontarget.setText("Sign in unsuccessful");
        }

    }

}
