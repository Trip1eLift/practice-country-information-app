/**appMainInterface.java
 *
 * Description: The GUI of the application
 *
 * Date:11/24/2018
 * @author: Joseph Chang
 */

package edu.psu.bd.cs.jbc5740;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class appMainInterface extends Application
{
    private Controllers controllers;
    private TextField searchName;
    private TextField searchCode;
    private Label result;
    private Label countryNumber;
    private TextField resultName;
    private TextField resultCode;
    private TextField resultHead;
    private Label resultLanguages;
    private Label resultCities;
    private Button prev;
    private Button next;
    private ArrayList<Country> Countries;
    private int CountryIndex = 0;
    private boolean found = false;

    public void start(Stage primaryStage) throws Exception
    {
        controllers = new Controllers();

        Label title = new Label("Countries®");

        Label NameText = new Label("Country Name");
        Label CodeText = new Label("Country Code");
        searchName = new TextField();
        searchCode = new TextField();
        Button search = new Button("Search");
        GridPane searchSection = new GridPane();
        searchSection.setHgap(20);
        searchSection.setPadding(new Insets(10));
        searchSection.add(NameText, 0,0);
        searchSection.add(CodeText, 1,0);
        searchSection.add(searchName, 0,1);
        searchSection.add(searchCode, 1,1);
        searchSection.add(search, 2,1);

        result = new Label("Result:");

        countryNumber = new Label("Country");
        Label countryNameText = new Label("Country Name");
        Label countryCodeText = new Label("Country Code");
        Label countryHeadText = new Label("Head of State");
        Label countryLanguagesText = new Label("Languages");
        Label countryCitiesText = new Label("Cities");
        resultName = new TextField();
        resultCode = new TextField();
        resultHead = new TextField();
        resultLanguages = new Label();
        resultCities = new Label();
        GridPane resultSection = new GridPane();
        resultSection.setHgap(10);
        resultSection.setVgap(10);
        resultSection.setPadding(new Insets(40));
        resultSection.add(countryNumber, 0,0);
        resultSection.add(countryNameText, 0,1);
        resultSection.add(countryCodeText, 0,2);
        resultSection.add(countryHeadText, 0,3);
        resultSection.add(countryLanguagesText, 0,4);
        resultSection.add(countryCitiesText, 0,5);
        resultSection.add(resultName, 1,1);
        resultSection.add(resultCode, 1,2);
        resultSection.add(resultHead, 1,3);
        resultSection.add(resultLanguages, 1,4);
        resultSection.add(resultCities, 1,5);

        prev = new Button("Previous");
        prev.setTextFill(Color.GRAY);
        next = new Button("Next");
        next.setTextFill(Color.GRAY);
        HBox scrollButtons = new HBox(80,prev, next);
        scrollButtons.setAlignment(Pos.CENTER);

        Label explain = new Label("To update information, press Enter key, Previous button, or Next button if\nit is allowed.");

        VBox resultContainer = new VBox(result, resultSection, scrollButtons, explain);
        resultContainer.setAlignment(Pos.TOP_LEFT);



        VBox All = new VBox(20,title, searchSection, resultContainer);
        All.setAlignment(Pos.CENTER);
        All.setPadding(new Insets(30));

        //-------------

        search.setOnAction(new searchButtonHandler());
        prev.setOnAction(new prevButtonHandler());
        next.setOnAction(new nextButtonHandler());

        Scene scene = new Scene(All);

        scene.setOnKeyPressed(new enterButtonHandler());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Countries®");
        primaryStage.show();
    }

    private class searchButtonHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event) {
            found = controllers.Search(searchName.getText(), searchCode.getText());

            if(found)
            {
                Countries = controllers.getCountries();

                CountryIndex = 0;
                displayInfo(CountryIndex);

                if(Countries.size() > 1)
                {
                    next.setTextFill(Color.BLACK);
                    prev.setTextFill(Color.GRAY);
                }
                else
                {
                    next.setTextFill(Color.GRAY);
                    prev.setTextFill(Color.GRAY);
                }
            }
            else
            {
                prev.setTextFill(Color.GRAY);
                next.setTextFill(Color.GRAY);

                resultName.setText("");
                resultCode.setText("");
                resultHead.setText("");
                resultLanguages.setText("");
                resultCities.setText("");
            }

        }
    }

    private class prevButtonHandler implements  EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event) {
            if (!found)
                return;

            if (CountryIndex > 0)
            {
                updateInfo();

                CountryIndex--;
                displayInfo(CountryIndex);
            }

            if (CountryIndex < 1)
            {
                prev.setTextFill(Color.GRAY);
            }
            else
            {
                prev.setTextFill(Color.BLACK);
            }
            if (CountryIndex >= Countries.size()-1)
            {
                next.setTextFill(Color.GRAY);
            }
            else
            {
                next.setTextFill(Color.BLACK);
            }
        }
    }

    private class nextButtonHandler implements  EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event) {
            if (!found)
                return;

            if (CountryIndex < Countries.size()-1)
            {
                updateInfo();

                CountryIndex++;
                displayInfo(CountryIndex);
            }

            if (CountryIndex >= Countries.size()-1)
            {
                next.setTextFill(Color.GRAY);
            }
            else
            {
                next.setTextFill(Color.BLACK);
            }
            if (CountryIndex < 1)
            {
                prev.setTextFill(Color.GRAY);
            }
            else
            {
                prev.setTextFill(Color.BLACK);
            }
        }
    }

    private class enterButtonHandler implements EventHandler<KeyEvent>
    {
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode().equals(KeyCode.ENTER))
            {
                updateInfo();

                displayInfo(CountryIndex);
            }
        }
    }

    private void updateInfo()
    {
        String targetCode = Countries.get(CountryIndex).Code;
        Country tempCountry = new Country(resultCode.getText(), resultName.getText(), resultHead.getText());

        controllers.Update(tempCountry, targetCode);
    }

    private void displayInfo(int index)
    {
        if(0 <= index && index < Countries.size())
        {
            result.setText("Result: " + Countries.size() + " country found");
            countryNumber.setText("Country No." + (index+1));
            resultName.setText(Countries.get(index).Name);
            resultCode.setText(Countries.get(index).Code);
            resultHead.setText(Countries.get(index).HeadOfState);

            int i;
            if (Countries.get(index).Languages.size() > 0)
            {
                String languages = Countries.get(index).Languages.get(0);
                for(i = 1; i < Countries.get(index).Languages.size(); i++)
                {
                    if(i % 4 == 0)
                        languages = languages + "\n" + Countries.get(index).Languages.get(i);
                    else
                        languages = languages + ", " + Countries.get(index).Languages.get(i);
                }
                resultLanguages.setText(languages);
            }
            else
            {
                resultLanguages.setText("");
            }

            if (Countries.get(index).Cities.size() > 0)
            {
                String cities = Countries.get(index).Cities.get(0);
                for(i = 1; i < Countries.get(index).Cities.size(); i++)
                {
                    if(i % 2 == 0)
                        cities = cities + "\n" + Countries.get(index).Cities.get(i);
                    else
                        cities = cities + ", " + Countries.get(index).Cities.get(i);
                }
                resultCities.setText(cities);
            }
            else
            {
                resultCities.setText("");
            }




        }
        else
        {
            System.err.println("Error index of country!");
        }
    }

    public static void main(String args[]) { launch(args); }
}
