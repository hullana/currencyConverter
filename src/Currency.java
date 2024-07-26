import java.util.HashMap;
import java.util.ArrayList;
import org.json.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Currency {
    private String name, currencyChoice;
    private static Double result;
    //similar to arraylist... key-map. every key is mapped to one value. used specifically to attach values to each country
    private HashMap<String, Double> currencyValues = new HashMap<>();
    private static final String API_ENDPOINT = "http://api.exchangeratesapi.io/v1/latest?access_key=e5d2cd431c2a35057d9b93be5e14787e";

    //Currency constructor that has the attribute of the name of the currency
    public Currency(String currencyName){
        this.name = currencyName;
    }

    //values for each currency on a scale of 1 (ex: 1 US Dollar = 49.82 philippine peso), added into hashmap which will be used for getCurrencyValues method
    public void converterValues(){
        currencyChoice = this.name;
        try {
            String jsonResponse = sendHttpRequest(API_ENDPOINT);

            //creates a JSONObject that takes in all the JSON data (turned into string) from the GET request 
            JSONObject exchangeRates = new JSONObject(jsonResponse);

            //gets the keyword "rates" of the jsonobject and then gets the value of the specified currency 3 letter code
            double euroToUSD = exchangeRates.getJSONObject("rates").getDouble("USD");
            double euroToJPY = exchangeRates.getJSONObject("rates").getDouble("JPY");
            double euroToGBP = exchangeRates.getJSONObject("rates").getDouble("GBP");
            double euroToPHP = exchangeRates.getJSONObject("rates").getDouble("PHP");

            //mapping value of each currency onto each other depending on choice of user
            switch(currencyChoice){
                case "US Dollar":
                this.currencyValues.put("US Dollar", 1.00);
                this.currencyValues.put("Euro", convertBaseToWantedCurrency(1, euroToUSD));
                this.currencyValues.put("Japanese Yen", convertWantedToFinalCurrency(1, euroToUSD , euroToJPY));
                this.currencyValues.put("British Pound", convertWantedToFinalCurrency(1, euroToUSD , euroToGBP));
                this.currencyValues.put("Philippine Peso", convertWantedToFinalCurrency(1, euroToUSD , euroToPHP));
                break;
                case "Euro":
                this.currencyValues.put("US Dollar", euroToUSD);
                this.currencyValues.put("Euro", 1.00);
                this.currencyValues.put("Japanese Yen", euroToJPY);
                this.currencyValues.put("British Pound", euroToGBP);
                this.currencyValues.put("Philippine Peso", euroToPHP);
                break;
            
                case "Japanese Yen":
                this.currencyValues.put("US Dollar", convertWantedToFinalCurrency(1, euroToJPY , euroToUSD));
                this.currencyValues.put("Euro", convertBaseToWantedCurrency(1, euroToJPY));
                this.currencyValues.put("Japanese Yen", 1.00);
                this.currencyValues.put("British Pound", convertWantedToFinalCurrency(1, euroToJPY , euroToGBP));
                this.currencyValues.put("Philippine Peso", convertWantedToFinalCurrency(1, euroToJPY , euroToPHP));
                break;
            
                case "British Pound":
                this.currencyValues.put("US Dollar", convertWantedToFinalCurrency(1, euroToGBP , euroToUSD));
                this.currencyValues.put("Euro", convertBaseToWantedCurrency(1, euroToGBP));
                this.currencyValues.put("Japanese Yen", convertWantedToFinalCurrency(1, euroToGBP , euroToJPY));
                this.currencyValues.put("British Pound", 1.00);
                this.currencyValues.put("Philippine Peso", convertWantedToFinalCurrency(1, euroToGBP , euroToPHP));
                break;
            
                case "Philippine Peso":
                this.currencyValues.put("US Dollar", convertWantedToFinalCurrency(1, euroToPHP , euroToUSD));
                this.currencyValues.put("Euro", convertBaseToWantedCurrency(1, euroToPHP));
                this.currencyValues.put("Japanese Yen", convertWantedToFinalCurrency(1, euroToPHP , euroToJPY));
                this.currencyValues.put("British Pound",  convertWantedToFinalCurrency(1, euroToPHP , euroToGBP));
                this.currencyValues.put("Philippine Peso", 1.00);
                break;
                default: 
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //convert from euro (base) to whatever currency was needed
    private static double convertBaseToWantedCurrency(double amountInWantedRate, double baseToWantedRate) {
        return amountInWantedRate / baseToWantedRate;
    }

    //convert from wanted to final currency while using euro as the base currency
    private static double convertWantedToFinalCurrency(double amount, double baseToWanted, double baseToFinal){
        return (amount / baseToWanted) * baseToFinal;
    }

    private static String sendHttpRequest(String urlString){
        try{
            //creates new URL which is the api URL with the api key and latest currency endpoint
             URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //uses HTTP GET method to the JSON data from the api call
            connection.setRequestMethod("GET");
            connection.connect();

            //200 is good meaning it works
            if(connection.getResponseCode() != 200){
                throw new RuntimeException("HTTPResponseCode: " + connection.getResponseCode());
            }else{
                //scanner will read the content of the json
                Scanner sc = new Scanner(url.openStream());
                StringBuffer jsonInfo = new StringBuffer();
                //iterating through the json
                while(sc.hasNext()){
                    jsonInfo.append(sc.nextLine());
                }
                sc.close();
                connection.disconnect();
                return jsonInfo.toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //method used to initialize array with different currencies, method will be used when instaniating currency in window.java
    public static ArrayList<Currency> createCurrencies(){
        ArrayList<Currency> currencies = new ArrayList<Currency>();

        currencies.add(new Currency("US Dollar"));
        currencies.add(new Currency("Euro"));
        currencies.add(new Currency("Japanese Yen"));
        currencies.add(new Currency("British Pound"));
        currencies.add(new Currency("Philippine Peso"));

        //forloop used to get the values of each currency

        int i = 0;
        while(i < currencies.size()){
            currencies.get(i).converterValues();
            i++;
        }

        return currencies;
    }

    //getter method to obtain currencyname which will help in obtaining the names
    public String getCurrencyName(){
        return this.name;
    }

    public HashMap<String, Double> getCurrencyValues(){
        return this.currencyValues;
    }

    //convert method used for the Currency object that will be created in window.java
    public static Double convert(Double convertValue, Double amount){
        result = (double) Math.round((convertValue * amount) * 100) / 100;
        return result;
    }
}
