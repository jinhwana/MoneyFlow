/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moneyflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author JinHwan
 */
public class MoneyFlow_data{

    //Initialize variables
    private String date;
    private boolean isIncome;
    private double amount;
    private int accountType;
    private String category;
    private File file;

    //Constructor
    public MoneyFlow_data(String date, boolean isIncome, double amount, int accountType, String category) {
        this.date = date;
        this.isIncome = isIncome;
        this.amount = amount;
        this.accountType = accountType;
        this.category = category;
    }

    //Getter methods
    public String getDate() {
        return date;
    }

    public boolean isIsIncome() {
        return isIncome;
    }

    public double getAmount() {
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getCategory() {
        return category;
    }

    public File getFile(){
        return file;
    }

    //Setter method
    public void setFile(File file) {
        this.file = file;
    }

    //converts boolean value of isIncome to its corresponding income/outgoing type
    public String inOutType(boolean isIncome) {
        String result;
        if (isIncome) {
            result = "Income";
        } else {
            result = "Outgoing";
        }
        return result;
    }

    //converts int value of accountType to its corresponding String value
    public String accountTypeToString(int accountType) {
        String accountTypeString;
        switch (accountType) {
            //Coresponding int accountType to String
            case 0:
                accountTypeString = "Cash";
                break;

            case 1:
                accountTypeString = "Debit";
                break;

            case 2:
                accountTypeString = "Credit";
                break;

            case 3:
                accountTypeString = "Saving";
                break;

            default: 
                accountTypeString = "N/A";

        }
        return accountTypeString;
    }

    //Save data to external file
    public void saveFile(MoneyFlow_data data) throws IOException {
        //Store values to current file with semi-colon seperator of each data
        PrintWriter outputStream = null;

        outputStream = new PrintWriter(new FileOutputStream(file, true));

        outputStream.println(data.getDate() + ";" + inOutType(data.isIsIncome()) + ";"
                + data.getAmount() + ";" + accountTypeToString(data.getAccountType()) + ";"
                + data.getCategory() + ";");
        outputStream.close();
    }

}
