/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moneyflow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JinHwan
 */
public class MoneyFlow_table extends JFrame implements ActionListener {

    JButton deleteButton;
    JButton saveButton;
    JButton summaryButton;
    DefaultTableModel model;
    JTable table;
    File file;
    JLabel lbl_status;
    double currentAmount;
    double spentAmount;
    double savedAmount;

    //Load data from external file and display in GUI
    public MoneyFlow_table(File currentFile) throws FileNotFoundException, IOException {
        file = currentFile;
        BufferedReader inputStream = new BufferedReader(new FileReader(currentFile));

        //Intialize header of table
        String[] header = {"Date", "In/Out", "Amount", "Account Type", "Category"};

        int numLine = 0;
        //Get number of line from the file to create 2-d array
        while (inputStream.readLine() != null) {
            numLine++;
        }
        // go back to beginning of line
        inputStream.close();
        inputStream = new BufferedReader(new FileReader(currentFile));

        Object[][] data = new Object[numLine][5]; //store data of columns
        String line = null;
        int count = 0;

        while ((line = inputStream.readLine()) != null) {
            //Read each line

            String[] parts = line.split(";");

            //Save into data
            for (int i = 0; i < parts.length; i++) {

                if (i == 2) {
                    // $ Sign
                    data[count][i] = String.format("$%.2f", Double.parseDouble(parts[i]));
                } else {
                    data[count][i] = parts[i];
                }
            }

            count++;
        }
        inputStream.close();
        //DefaultTableModel
        model = new DefaultTableModel(data, header);

        //Create JTable
        table = new JTable(model);
        table.setMinimumSize(new Dimension(400, 400)); //Set size of table GUI

        setLayout(new BorderLayout()); //Parent layout
        JScrollPane scrollPane = new JScrollPane(table); //Create JScroolPane which includes table

        //Create tablePanel
        JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
        tablePanel.add(scrollPane); //Add scrollPane into tablePanel

        //Add table into Table GUI
        add(tablePanel, BorderLayout.NORTH);

        //Add 2 buttons
        deleteButton = new JButton("Del");
        saveButton = new JButton("Save");
        summaryButton = new JButton("Summary");

        //Add action listener for 3 buttons
        deleteButton.addActionListener(this);
        saveButton.addActionListener(this);
        summaryButton.addActionListener(this);

        //Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(summaryButton);
        add(buttonPanel, BorderLayout.CENTER);

        //Add JLabel status to GUI frame
        lbl_status = new JLabel("Double click a data to edit.");
        add(lbl_status, BorderLayout.SOUTH);
    }

    public boolean isValidData(DefaultTableModel model) {
        boolean result = false;

        //Create MoneyFlow_GUI object for validation check for new input
        MoneyFlow_GUI check = new MoneyFlow_GUI();

        int num_valid_data = 0;

        // Go through all data from DefaultTableModel model
        for (int i = 0; i < model.getRowCount(); i++) { //all rows
            for (int j = 0; j < model.getColumnCount(); j++) { //all columns
                //Get Object of value at (i, j)
                Object stringObj = model.getValueAt(i, j);

                //Convert stringObj into string
                String string = (String) stringObj;
                if (j == 0) { //Date column
                    //Check for valid date value
                    if (check.isValidDate(string)) {
                        num_valid_data++;
                    } else {
                        //invalid amount
                        lbl_status.setText("Wrong format of date");
                        lbl_status.setForeground(Color.red);
                    }
                } else if (j == 1) { //in/out column
                    //Check if the string is either income or outgoing
                    if (string.equals("Income") || string.equals("Outgoing")) {
                        num_valid_data++;
                    } else {
                        //invalid string
                        lbl_status.setText("in/out field must be either \"Income\" or \"Outgoing\"");
                        lbl_status.setForeground(Color.red);
                    }
                } else if (j == 2) {  //amount column
                    //Get the value where column is index of 2 and ignore character of $ 
                    string = string.substring(1);

                    //Check for valid amount input
                    if (check.isValidAmount(string)) {
                        num_valid_data++;
                    } else {
                        //invalid amount
                        lbl_status.setText("Invalid amount format");
                        lbl_status.setForeground(Color.red);
                    }
                } else if (j == 3) { //account type column
                    if (string.equals("Cash") || string.equals("Debit") || string.equals("Credit")
                            || string.equals("Saving")) {
                        num_valid_data++;
                    } else {
                        //invalid string
                        lbl_status.setText("Invalid string of account type");
                        lbl_status.setForeground(Color.red);
                    }
                } else { //category column -> no validation check
                    num_valid_data++;
                }
            }
            if (num_valid_data == (model.getColumnCount() * model.getRowCount())) {
                result = true;
            }
        }
        return result;
    }

    private void updateBalance(DefaultTableModel model) {
        //Reset amounts
        savedAmount = 0.0;
        currentAmount = 0.0;
        spentAmount = 0.0;

        // Go through all data from DefaultTableModel model
        for (int i = 0; i < model.getRowCount(); i++) { //all rows

            //Get Object of value at (i, in/out column)
            Object inOutObj = model.getValueAt(i, 1);
            Object amountObj = model.getValueAt(i, 2);

            //Convert objects to string
            String inOut = (String) inOutObj;
            String amountString = (String) amountObj;
            amountString = amountString.substring(1);

            //Cast string amount to double
            double amount = Double.parseDouble(amountString);

            if (inOut.equals("Income")) {
                //Add amount
                savedAmount += amount;
                currentAmount += amount;
            } else {
                //Minus amount
                spentAmount += amount;
                currentAmount -= amount;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        PrintWriter outputStream = null;
        switch (e.getActionCommand()) {

            //Del button clicked
            case "Del":

                if (selectedRow == -1) { // not selected any row
                    JOptionPane.showMessageDialog(null, "Select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int rowCount = model.getRowCount();
                    //Delete selected row
                    model.removeRow(selectedRow);
                    //Reset table view
                    model.fireTableDataChanged();
                }

                break;

            //Save button clicked -> Save current data into the file
            case "Save":

                //Check weather all input values are valid
                if (isValidData(model)) {
                    //Try-Catch for opening valid file
                    try {
                        outputStream = new PrintWriter(new FileOutputStream(file));
                    } catch (FileNotFoundException ex) {
                        ex.getMessage();
                    }
                    //Go through all rows
                    for (int i = 0; i < model.getRowCount(); i++) {
                        //Go through all columns
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            if (j == 2) { //Amount column
                                Object stringObj = model.getValueAt(i, j);
                                String string = (String) stringObj;
                                //Get rid of $ 
                                string = string.substring(1);
                                outputStream.print(string + ";");
                            } else {
                                outputStream.print(model.getValueAt(i, j) + ";");
                            }
                        }

                        // After each row, a new line starts
                        outputStream.println();

                    }
                    lbl_status.setText("Saved all values");
                    lbl_status.setForeground(Color.blue);
                    //Close outputStream
                    outputStream.close();
                }
                break;

            case "Summary":
                updateBalance(model);
                //add image icon
                ImageIcon icon = new ImageIcon(getClass().getResource("/money.png"));

                //Message to display
                String message = String.format("Current balance: $%.2f\n"
                        + "Total income: $%.2f\n"
                        + "Total spent: $%.2f", currentAmount, savedAmount, spentAmount);
                //Call message dialog of JOptionPane
                JOptionPane.showMessageDialog(null, message, "Summary", JOptionPane.INFORMATION_MESSAGE, icon);
                break;
        }
    }

}
