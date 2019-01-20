/**Controllers.java
 *
 * Description: To connect the GUI application with database
 *
 * Date:11/24/2018
 * @author: Joseph Chang
 */

package edu.psu.bd.cs.jbc5740;

import java.sql.*;
import java.util.ArrayList;

public class Controllers
{
    private ArrayList<Country> countries;

    public boolean Search(String name, String code)
    {
        boolean found = false;

        Connection connection;
        ResultSet resultSet = null;

        ArrayList<Country> Countries = new ArrayList<Country>();

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Joseph Chang\\OneDrive\\Source\\Cmpsc221\\HW13\\src\\main\\resources\\World");
            PreparedStatement preparedStatement;
            String query;

            if(name.equals("") && code.equals("")) {
                return found;
            }
            else if(!name.equals("") && code.equals(""))
            {
                name = "%" + name + "%";

                query = "SELECT Country.Code, Country.Name, Country.HeadOfState\n" +
                        "FROM Country\n" +
                        "WHERE Name LIKE ?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,name);
            }
            else if(!code.equals("") && name.equals(""))
            {
                query = "SELECT Country.Code, Country.Name, Country.HeadOfState\n" +
                        "FROM Country\n" +
                        "WHERE Code = ?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,code);
            }
            else
            {
                name = "%" + name + "%";

                query = "SELECT Country.Code, Country.Name, Country.HeadOfState\n" +
                        "FROM Country\n" +
                        "WHERE Code = ?\n" +
                        "AND Name LIKE ?";

                preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1,code);
                preparedStatement.setString(2,name);
            }



            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                Country tempCountry = new Country(resultSet.getString("Code"), resultSet.getString("Name"), resultSet.getString("HeadOfState"));
                tempCountry.Languages = new ArrayList<String>();
                tempCountry.Cities = new ArrayList<String>();
                Countries.add(tempCountry);

                found = true;
            }

            //Languages
            String queryForLanguage = "SELECT CountryLanguage.language\n" +
                    "FROM CountryLanguage\n" +
                    "WHERE CountryCode = ?";
            preparedStatement = connection.prepareStatement(queryForLanguage);

            for (int i = 0; i < Countries.size(); i++)
            {
                preparedStatement.setString(1,Countries.get(i).Code);
                resultSet = preparedStatement.executeQuery();

                ArrayList<String> tempLanguages = new ArrayList<String>();

                while(resultSet.next())
                {
                    String tempLanguage = resultSet.getString("Language");
                    tempLanguages.add(tempLanguage);
                }

                Countries.get(i).Languages = tempLanguages;
            }

            //Cities
            String queryForCities = "SELECT City.Name\n" +
                    "FROM City\n" +
                    "WHERE CountryCode = ?";
            preparedStatement = connection.prepareStatement(queryForCities);

            for (int i = 0; i < Countries.size(); i++)
            {
                preparedStatement.setString(1,Countries.get(i).Code);
                resultSet = preparedStatement.executeQuery();

                ArrayList<String> tempCities = new ArrayList<String>();

                while(resultSet.next())
                {
                    String tempCity = resultSet.getString("Name");
                    tempCities.add(tempCity);
                }

                Countries.get(i).Cities = tempCities;
            }

            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        countries = Countries;
        return found;
    }


//--------------------------------------UPDATE---------------------------------------
    public boolean Update(Country updateInfo, String code)
    {
        boolean update = false;

        int i;

        for(i = 0; i < countries.size(); i++)
        {
            if(code.equals(countries.get(i).Code))
            {
                update = true;

                countries.get(i).Code = updateInfo.Code;
                countries.get(i).Name = updateInfo.Name;
                countries.get(i).HeadOfState = updateInfo.HeadOfState;

                break;
            }
        }

        if(update)
        {
            //query update here
            Connection connection;
            ResultSet resultSet = null;

            try{
                connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Joseph Chang\\OneDrive\\Source\\Cmpsc221\\HW13\\src\\main\\resources\\World");

                String query = "UPDATE Country\n" +
                        "SET Code = ?,\n" +
                        "Name = ?,\n" +
                        "HeadOfState = ?\n" +
                        "WHERE Code = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1,updateInfo.Code);
                preparedStatement.setString(2,updateInfo.Name);
                preparedStatement.setString(3,updateInfo.HeadOfState);
                preparedStatement.setString(4,code);

                preparedStatement.executeUpdate();

                String queryForLanguage = "UPDATE CountryLanguage\n" +
                        "SET CountryCode = ?\n" +
                        "WHERE CountryCode = ?";
                preparedStatement = connection.prepareStatement(queryForLanguage);
                preparedStatement.setString(1,updateInfo.Code);
                preparedStatement.setString(2,code);
                preparedStatement.executeUpdate();

                String queryForCity = "UPDATE City\n" +
                        "SET CountryCode = ?\n" +
                        "WHERE CountryCode = ?";
                preparedStatement = connection.prepareStatement(queryForCity);
                preparedStatement.setString(1,updateInfo.Code);
                preparedStatement.setString(2,code);
                preparedStatement.executeUpdate();

                preparedStatement.close();
                //resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        return update;
    }

    public ArrayList<Country> getCountries()
    {
        return countries;
    }

    public static void main(String args[])
    {
        Controllers cont = new Controllers();
        cont.Search("China", "CHN");

        ArrayList<Country> countriesMain = cont.getCountries();


        for(int i = 0; i < countriesMain.size(); i++)
        {
            System.out.println(countriesMain.get(i).Code + " " + countriesMain.get(i).Name + " " + countriesMain.get(i).HeadOfState);

//            int j = 0;
//            while(j < countriesMain.get(i).Languages.size())
//            {
//                System.out.println(" " + countriesMain.get(i).Languages.get(j));
//                j++;
//            }
//
//            j = 0;
//            while(j < countriesMain.get(i).Cities.size())
//            {
//                System.out.println("  " + countriesMain.get(i).Cities.get(j));
//                j++;
//            }
        }

        Country TempCountry = new Country("ROC", "Formosa", "KMT");
        cont.Update(TempCountry,"CHN");
        cont.Search("", "ROC");
        countriesMain = cont.getCountries();
        for(int i = 0; i < countriesMain.size(); i++) {
            System.out.println(countriesMain.get(i).Code + " " + countriesMain.get(i).Name + " " + countriesMain.get(i).HeadOfState);
        }

        TempCountry = new Country("CHN", "China", "Jiang Zemin");
        cont.Update(TempCountry,"ROC");

        cont.Search("", "CHN");
        countriesMain = cont.getCountries();
        for(int i = 0; i < countriesMain.size(); i++) {
            System.out.println(countriesMain.get(i).Code + " " + countriesMain.get(i).Name + " " + countriesMain.get(i).HeadOfState);
        }
    }

}
