import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class Window implements ActionListener{

    //all instance variables that will be used
    private JPanel infoPanel;
    private JLabel titleLabel, fromLabel, toLabel, enterLabel, convertedLabel;
    private JComboBox<String> fromCountries, toCountries;
    private JTextField beforeconvert, afterconvert;
    private JButton convertButton;
    private String fromCurrencyChosen, toCurrencyChosen;
    private ArrayList<Currency> currencies = Currency.createCurrencies();
    private JFrame frame = new JFrame();
    
    //Window constructor that contains all the information, which then will be called in Main
    public Window(){
        //create frame
        frame.setTitle("Currency Converter");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //create panel to put all information on
        infoPanel = new JPanel();
        infoPanel.setBackground(Color.gray);
        infoPanel.setLayout(null); //used to remove layout that is automatically given
        frame.setContentPane(infoPanel);

        //creates large title
        titleLabel = new JLabel("Currency Converter");
        titleLabel.setBounds(80, 0, 400, 100);
        titleLabel.setFont(new Font("Palatino Linotype", Font.BOLD, 34));
        infoPanel.add(titleLabel);

        fromLabel = new JLabel("From:");
        fromLabel.setBounds(40, 80, 92, 50);
        fromLabel.setFont(new Font("Palatino Linotype", Font.PLAIN, 26));
        infoPanel.add(fromLabel);
        
        //different countries that you can convert currency from
        fromCountries = new JComboBox<String>();
        fromCountries.setBounds(110, 80, 92, 40);
        fillList(fromCountries, currencies);
        infoPanel.add(fromCountries);
        

        toLabel = new JLabel("To:");
        toLabel.setBounds(300, 81, 92, 50);
        toLabel.setFont(new Font("Palatino Linotype", Font.PLAIN, 26));
        infoPanel.add(toLabel);

        toCountries = new JComboBox<String>();
        toCountries.setBounds(340, 80, 92, 40);
        fillList(toCountries, currencies);
        infoPanel.add(toCountries);

        enterLabel = new JLabel("Enter amount: ");
        enterLabel.setBounds(80, 110, 202, 100);
        enterLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        infoPanel.add(enterLabel);


        convertedLabel = new JLabel("Converted to: ");
        convertedLabel.setBounds(80, 180, 202, 100);
        convertedLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        infoPanel.add(convertedLabel);

        beforeconvert = new JTextField();
        beforeconvert.setBounds(210, 150, 200, 25);
        frame.add(beforeconvert);

        afterconvert = new JTextField();
        afterconvert.setBounds(210, 218, 200, 25);
        frame.add(afterconvert);

        convertButton = new JButton("Convert");
        convertButton.setBounds(140, 280, 202, 30);
        convertButton.addActionListener(this);
        frame.add(convertButton);

        //this certain method needs to be placed at the end to prevent layout problems
        frame.setVisible(true); 

    }
    //method used to fill the combobox with countries of currencies I will use
    public static void fillList(JComboBox<String> currencyList, ArrayList<Currency> currencies){
        for(int i = 0; i < currencies.size(); i++){
            currencyList.addItem(currencies.get(i).getCurrencyName());
        }
    }
    //actionlistener used so that when convert button is clicked, currency is converted
    @Override
    public void actionPerformed(ActionEvent e) {
        fromCurrencyChosen = (String) fromCountries.getSelectedItem();
        toCurrencyChosen = (String) toCountries.getSelectedItem();
        Double amount = 0.0;
        Double result;
        String changeResult;

        amount = Double.parseDouble(beforeconvert.getText()); //turning the string of the textfield into a double
        result = convertCurrency(fromCurrencyChosen, toCurrencyChosen, currencies, amount);
        changeResult = String.valueOf(result);

        afterconvert.setText(changeResult);

    }

    public Double convertCurrency(String fromCurrency, String toCurrency, ArrayList<Currency> currencies, Double amount){
        String findToCurrency = ""; //finding the toCurrency variable name
        Double convertValue = 0.0;
        Double result = 0.0;
        
        //foreach loop to get the toCurrency string and assign it to findToCurrency
        for(Currency c: currencies){
            if(c.getCurrencyName() == toCurrency){
                findToCurrency = c.getCurrencyName();
            }
        }

        //forloop to find the the value that we are converting from and then using convert method to convert it
        if(findToCurrency != ""){
            for(int i = 0; i < currencies.size(); i++){
                if(currencies.get(i).getCurrencyName().equals(fromCurrency)){
                    convertValue = currencies.get(i).getCurrencyValues().get(findToCurrency);
                    result = Currency.convert(convertValue, amount);
                }
            }

        }
        return result;
    }
}