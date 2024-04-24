package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.Stage;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.csit228f2_2.MySQLConnection.getConnection;


public class HelloApplication extends Application {
    public static List<User> users;

    public static void main(String[] args) {
        getConnection();
        launch(args);
    }



    @Override
    public void start(Stage stage) throws Exception {





        // LOAD USERS
        //users.add(new User("tsgtest", "123456"));
        //users.add(new User("jayvince", "secret"));
        //users.add(new User("russselll", "palma"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

        AnchorPane pnMain = new AnchorPane();
        GridPane grid = new GridPane();
        pnMain.getChildren().add(grid);
        grid.setAlignment(Pos.CENTER);
        Text sceneTitle = new Text("Log-In to CSIT228");
        sceneTitle.setStrokeType(StrokeType.CENTERED);
        sceneTitle.setStrokeWidth(100);
        sceneTitle.setFill(Paint.valueOf("#325622"));
        sceneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 69));
        grid.add(sceneTitle, 0, 0, 2, 1);

        //-----------USERNAME----------------

        Label lblUsername = new Label("Username: ");
        lblUsername.setTextFill(Paint.valueOf("#c251d5"));
        lblUsername.setFont(Font.font(40));
        grid.add(lblUsername, 0, 1);

        TextField tfUsername = new TextField();
        tfUsername.setFont(Font.font(35));
        grid.add(tfUsername, 1, 1);

        //----------PASSWORD-------------

        Label lblPassword = new Label("Password: ");
        lblPassword.setTextFill(Paint.valueOf("#c251d5"));
        lblPassword.setFont(Font.font(40));
        grid.add(lblPassword, 0, 2);

        TextField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(35));
        grid.add(pfPassword, 1, 2);

        //-----REVEAL PASSWORD BUTTON -------------------

        Button btnShow = new Button("<*>");
        HBox hbShow = new HBox();
        hbShow.getChildren().add(btnShow);
        hbShow.setAlignment(Pos.CENTER);
        hbShow.setMaxWidth(150);
        TextField tfPassword = new TextField();
        tfPassword.setFont(Font.font(35));
        grid.add(tfPassword, 1, 2);
        tfPassword.setVisible(false);
        grid.add(hbShow, 2, 2);

        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent actionEvent) {
                tfPassword.setText(pfPassword.getText());
                tfPassword.setVisible(true);
                pfPassword.setVisible(false);
                grid.add(new Button("Hello"), 4,4);
            }
        });

        btnShow.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tfPassword.setVisible(false);
                pfPassword.setVisible(true);
            }
        });

        Button btnLogIn = new Button("Log In");
        btnLogIn.setFont(Font.font(45));
        HBox hbSignIn = new HBox();
        hbSignIn.getChildren().add(btnLogIn);
        hbSignIn.setAlignment(Pos.CENTER);
        grid.add(hbSignIn, 0, 3, 2, 1);


        btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try (Connection c = getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO tbluser (username,password) Values (?,?)")) {
                    String name = tfUsername.getText();;
                    String pw = pfPassword.getText();
                    statement.setString(1, name);
                    statement.setString(2, pw);
                    int updateCount = statement.executeUpdate();
                    if(updateCount > 0) {
                        System.out.println("Data inserted successfully.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        });







        Scene scene = new Scene(pnMain, 700, 560);
        stage.setScene(scene);
        stage.show();
    }


    private void createTable() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                Statement statement = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "username VARCHAR(50) NOT NULL UNIQUE," +
                        "password VARCHAR(50) NOT NULL)";
                statement.executeUpdate(sql);
                System.out.println("Table created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}