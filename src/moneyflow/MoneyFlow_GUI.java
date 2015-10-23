/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moneyflow;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author JinHwan
 */
public class MoneyFlow_GUI extends JFrame {

    //Initialize variables
    private JRadioButton rdBtn_income;
    private JRadioButton rdBtn_outgoing;
    private JTextField txtFld_amount;
    private JComboBox cbox_accountType;
    private JComboBox cbox_category;
    private JTextField txtFld_category;
    private JFormattedTextField txtFld_date;
    private JButton btn_register;
    private JButton btn_delete;
    private JButton btn_save;
    private JButton btn_clear;
    private JButton btn_list;
    private JLabel lbl_status;
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenu menu_about;
    private JMenuItem menuItem_open;
    private JMenuItem menuItem_newFile;
    private JMenuItem menuItem_about;

    //Array holds account type selection
    private final String[] accountSelection = {"Cash", "Debit", "Credit", "Saving"};

    //Current working file
    /**
     *
     */
    protected File currentFile;

    /**
     * Constructor for MoneuFlow_GUI.java
     */
    public MoneyFlow_GUI() {

        //Initialize labels
        JLabel lbl_title = new JLabel("Money Flow");
        JLabel lbl_income = new JLabel("Income");
        JLabel lbl_outgoing = new JLabel("Outgoing");
        JLabel lbl_amount = new JLabel("Amount $");
        JLabel lbl_category = new JLabel("Category");
        JLabel lbl_date = new JLabel("Date");
        lbl_status = new JLabel("");

        //Initialize radio buttons
        rdBtn_income = new JRadioButton();
        rdBtn_outgoing = new JRadioButton();

        //Initialize text field
        txtFld_amount = new JTextField(7);
        txtFld_category = new JTextField(10);

        //Initialize formatted text field
        txtFld_date = new JFormattedTextField(new SimpleDateFormat("mm/dd/yyyy"));
        txtFld_date.setColumns(10);
        txtFld_date.setText("mm/dd/yyyy");

        //Initialize comboBox
        cbox_accountType = new JComboBox();
        cbox_category = new JComboBox();

        //Initialize button
        btn_register = new JButton("Register");
        btn_delete = new JButton("del");
        btn_save = new JButton("Save");
        btn_clear = new JButton("Clear");
        btn_list = new JButton("List");
        //Set parent layout
        setLayout(new GridLayout(6, 1));

        //Create the menu bar
        menuBar = new JMenuBar();

        //Menu panel1
        menu_file = new JMenu("File");

        //Menu Item for panel 1
        menuItem_newFile = new JMenuItem("New File");
        menuItem_open = new JMenuItem("Open");

        //Add menu items into file menu
        menu_file.add(menuItem_newFile);
        menu_file.add(menuItem_open);

        //Menu panel2
        menu_about = new JMenu("About");

        //Menu items for panel 2
        menuItem_about = new JMenuItem("About Money-Flow");

        //Add menu items into about menu
        menu_about.add(menuItem_about);

        //Add menus into menu bar
        menuBar.add(menu_file);
        menuBar.add(menu_about);

        //Add menuBar to main frame
        setJMenuBar(menuBar);

        //Add action listener for menu click
        menuItem_newFile.addActionListener(new MenuHandler());
        menuItem_open.addActionListener(new MenuHandler());
        menuItem_about.addActionListener(new MenuHandler());

        //Set up drop-down list of accountType
        for (String accountSelection1 : accountSelection) {
            cbox_accountType.addItem(accountSelection1);
        }

        //set up title
        setTitle("Money Flow - No working file");

        //title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        titlePanel.add(lbl_title); //add to the panel
        add(titlePanel);

        //default layout for each panel
        FlowLayout leftLayout = new FlowLayout(FlowLayout.LEFT, 2, 2);
        FlowLayout centerLayout = new FlowLayout(FlowLayout.CENTER, 2, 2);

        //add inputPanel1
        JPanel inputPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        //Radio Button with its corresponding label
        //1st row of inputPanel
        JPanel incomePanel = new JPanel(centerLayout);
        incomePanel.add(lbl_income);
        incomePanel.add(rdBtn_income);
        rdBtn_income.setSelected(true); //set default selected button

        JPanel outgoingPanel = new JPanel(centerLayout);
        outgoingPanel.add(lbl_outgoing);
        outgoingPanel.add(rdBtn_outgoing);

        //add button group for two radio buttons
        //only one radio button can be selected
        ButtonGroup group = new ButtonGroup();
        group.add(rdBtn_income);
        group.add(rdBtn_outgoing);

        //add income Panel, outgoing Panel to inputPanel
        inputPanel1.add(incomePanel);
        inputPanel1.add(outgoingPanel);
        inputPanel1.add(lbl_date);
        inputPanel1.add(txtFld_date);

        add(inputPanel1);

        //Input panel -> grid layout with 2 x 2
        JPanel inputPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        JPanel amountPanel = new JPanel(leftLayout);
        amountPanel.add(lbl_amount);
        amountPanel.add(txtFld_amount);

        //add amount Panel and comboBox to inputPanel
        inputPanel2.add(amountPanel);
        inputPanel2.add(cbox_accountType);

        add(inputPanel2);

        JPanel inputPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        //Category ComboBox panel
        JPanel cat_cboxPanel = new JPanel(leftLayout);
        cat_cboxPanel.add(lbl_category);
        cat_cboxPanel.add(cbox_category);
        cat_cboxPanel.add(btn_delete);

        //add button listener for btn_delete
        btn_delete.addActionListener(new ButtonHandler());

        //New category Panel
        JPanel cat_newPanel = new JPanel(leftLayout);
        cat_newPanel.add(txtFld_category);
        cat_newPanel.add(btn_register);

        //add btn_register click listener
        btn_register.addActionListener(new ButtonHandler());

        //add cat_cboxPanel and cat_newPanel into inputPanel
        inputPanel3.add(cat_cboxPanel);
        inputPanel3.add(cat_newPanel);

        //add inputPanel to GUI
        add(inputPanel3);

        //Button panel
        JPanel buttonPanel = new JPanel(centerLayout);
        buttonPanel.add(btn_clear);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_list);

        //add buttonPanel to GUI
        add(buttonPanel);

        //add actionListener for clear
        btn_clear.addActionListener(new ButtonHandler());
        btn_save.addActionListener(new ButtonHandler());
        btn_list.addActionListener(new ButtonHandler());

        //Status Panel
        JPanel statusPanel = new JPanel(centerLayout);
        statusPanel.add(lbl_status);

        JPanel StatusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));

        //add statusPanel to GUI
        add(statusPanel);

        //Set up status bar on bottom of appilcation
        //Three panels that are to added to the JFrame
    }

    public String fileToString(File file) {
        String title = file.toString();
        boolean isWindowsOs = false;
        for (int i = 0; i < title.length(); i++) {
            if (title.charAt(i) == '\\') {
                isWindowsOs = true;
            }
        }
        String[] parts;
        if (isWindowsOs) { //Windows OS
            parts = title.split("\\\\");

        } else {
            //Linux or mac OS
            parts = title.split("/");
        }
        return parts[parts.length - 1];
    }

    //Register new category
    /**
     *
     * @param newItem - String
     * @pre newItem should not already registered
     */
    public void registerCategory(String newItem) {
        boolean existingItem = false; //store wheather newItem is exist in the list

        //Check if the newItem entered is already registered
        for (int i = 0; i < cbox_category.getItemCount(); i++) {
            if (cbox_category.getItemAt(i).equals(newItem)) {
                existingItem = true;
                break;
            }
        }
        //if item doesn't exist, add item into comboBox and set status label = ""
        if (!existingItem) {
            cbox_category.addItem(newItem);
            //print status 
            lbl_status.setText("Category \"" + newItem + "\" has successfully registered.");
            lbl_status.setForeground(Color.black);
        } //Otherwise print error message
        else {
            lbl_status.setText("New entered category is already exist");
            lbl_status.setForeground(Color.red);
        }
    }

    //Delete selected category
    /**
     *
     * @param deletingItem
     */
    public void deleteCategory(Object deletingItem) {
        cbox_category.removeItem(deletingItem); //delete item

        //print status
        lbl_status.setText("Category \"" + deletingItem.toString() + "\" has successfully deleted.");
        lbl_status.setForeground(Color.black);
    }

    /**
     * clear all unsaved field
     */
    public void clear() {
        //Set all text fields, buttons, comboBox to its default value
        txtFld_amount.setText("");
        txtFld_date.setText("mm/dd/yyyy");
        txtFld_category.setText("");
        rdBtn_income.setSelected(true);
        cbox_category.setSelectedItem(null);

        lbl_status.setText("Cleared all unsaved data");
        lbl_status.setForeground(Color.black);
    }

    //Check if the amount text_field has valid number
    /**
     *
     * @param input - String
     * @return boolean value of weather the amount-string is valid
     */
    public boolean isValidAmount(String input) {
        boolean result = false;

        //create regular expression for valid amount input
        String withOutDecimal = "^\\d+$";
        String withDecimal = "^\\d*[.]?\\d{1,2}$";

        //create Pattern object
        Pattern pattern1 = Pattern.compile(withOutDecimal);
        Pattern pattern2 = Pattern.compile(withDecimal);

        //Create Matcher object
        Matcher match1 = pattern1.matcher(input);
        Matcher match2 = pattern2.matcher(input);
        if (match1.find() || match2.find()) {
            result = true;
        }

        return result;
    }

    /**
     * Check if the date text_field has valid expression
     *
     * @param input-String
     * @return boolean value of valid date string input
     */
    public boolean isValidDate(String input) {
        //Initlaize variable to use
        boolean result = false;
        int day;
        int month;
        int year;

        //create regular expression for valid amount input
        String regex = "^\\d{1,2}/\\d{1,2}/\\d{4}";

        //create Pattern object
        Pattern pattern = Pattern.compile(regex);

        //Create Matcher object
        Matcher match = pattern.matcher(input);

        //correct format
        if (match.find()) {
            //split date to get day, month, year
            String[] parts = input.split("/");
            month = Integer.parseInt(parts[0]);
            day = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);

            //Check for valid date
            switch (month) {
                //month with 31 days
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    //check for valid date
                    if (day > 0 && day < 32 && year > 0) {
                        result = true;
                    }
                    break;

                //month with 30 days
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 0 && day < 31 && year > 0) {
                        result = true;
                    }
                    break;
                //Feburary
                case 2:
                    if (day > 0 && day < 29 && year > 0) {
                        result = true;
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * Opens saveAsDialog
     */
    public void saveAsDialog() throws FileNotFoundException {
        //Set currentDirectory as working directory
        File currentDirectory = new File(System.getProperty("user.dir"));
        //Create JFileChooser object with parameter value of currentDirectory
        JFileChooser fc = new JFileChooser(currentDirectory);

        //Display the dialog and it will store it in returnVal
        int returnVal = fc.showSaveDialog(null);
        fc.setDialogTitle("New File (Save as)");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fc.getSelectedFile();

            //Check if selected file is exist
            if (currentFile.exists()) {
                //Open confirm dialog if the user wants to over write the file from blank
                if (JOptionPane.showConfirmDialog(null, "File exist, overwrite?", "Warning", JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                    //Make the file empty data file
                    PrintWriter outputStream = null;

                    outputStream = new PrintWriter(new FileOutputStream(currentFile));

                    outputStream.close();

                }
            } else {
                //Make the file empty data file
                PrintWriter outputStream = null;

                outputStream = new PrintWriter(new FileOutputStream(currentFile));

                outputStream.close();
            }
            setTitle("Money Flow-" + fileToString(currentFile));
        }
        
    }

    /**
     * Opens openDialog
     */
    public void openDialog() {
        File currentDirectory = new File(System.getProperty("user.dir"));
        JFileChooser fc = new JFileChooser(currentDirectory);
        int returnVal = fc.showOpenDialog(MoneyFlow_GUI.this);

        //set currentFile = selected file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fc.getSelectedFile();
            setTitle("Money Flow-" + fileToString(currentFile));
        }
        
    }

    class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //If register button is clicked, call registerCategory()
            switch (e.getActionCommand()) {
                case "Register": //Register button click
                    String newItem = txtFld_category.getText(); //get item to add
                    if (newItem != null && !newItem.equals("")) { //check if the item is empty or null
                        //if not, add item into category
                        registerCategory(newItem);

                    } else { // empty or null field
                        //print status 
                        lbl_status.setText("Empty new category field");
                        lbl_status.setForeground(Color.red);
                    }
                    break;

                case "del": // delete button click
                    Object deletingItem = cbox_category.getSelectedItem(); //get item to delete
                    if (deletingItem == null) { //check if item is selected
                        //if there is no selected item, print status and do nothing
                        lbl_status.setText("Nothing to delete in category list");
                        lbl_status.setForeground(Color.red);
                    } else {
                        //otherwise delete the item
                        deleteCategory(deletingItem);
                    }
                    break;

                case "Clear": //clear all unsaved field
                    clear(); //call clear()
                    break;

                case "Save":
                    //check for all valid values
                    boolean isIncome = rdBtn_income.isSelected(); //if false, it's outgoing 
                    //Get all entered values
                    String amountInput = txtFld_amount.getText();
                    double amount = 0.0;
                    String date = txtFld_date.getText();
                    String category = "";
                    StringBuilder errorMsg = new StringBuilder("");

                    int varified = 0; //number of varified entered value
                    if (isValidAmount(amountInput)) { //user entered valid input
                        amount = Double.parseDouble(amountInput);
                        varified++;
                    } else {
                        //Error message to display
                        errorMsg.append("Wrong format of amount<br>");
                    }

                    if (isValidDate(date)) { //validate date
                        varified++;
                    } else {
                        //Error message to display
                        errorMsg.append("Wrong format of Date (mm/dd/yyyy)<br>");
                    }
                    //Get int value of accountType from drop down menu
                    int accountType = cbox_accountType.getSelectedIndex();

                    //Get selected category object
                    Object categoryObj = cbox_category.getSelectedItem();
                    if (categoryObj == null) { //category must be selected
                        //Error message to display
                        errorMsg.append("Select category<br>");
                    } else {
                        //Convert Object of category to String
                        category = categoryObj.toString();
                        varified++;
                    }

                    //varified all inputs
                    if (varified == 3) {
                        //Instantiate MoneyFlow_data with all entered value
                        MoneyFlow_data data = new MoneyFlow_data(date, isIncome, amount, accountType, category);
                        //If file has not selected yet, open openDialog
                        if (currentFile == null) {
                            openDialog();
                        }
                        data.setFile(currentFile);
                        //Display status message with blue color
                        lbl_status.setText("Successfully Saved");
                        lbl_status.setForeground(Color.blue);
                        try {
                            //try data object to use saveFile
                            data.saveFile(data);
                        } catch (IOException ex) {
                            // catch for file does not exist run-time error
                            Logger.getLogger(MoneyFlow_GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else { //If there isn't 3 varified field, display error message
                        lbl_status.setText("<html>" + errorMsg.toString() + "</html>");
                        lbl_status.setForeground(Color.red);
                    }
                    //End of "save" action listener
                    break;

                case "List":
                    //Force to select file if none of file has selected
                    if (currentFile == null) {
                        openDialog();
                    }

                    //Create MoneyFlow_table frame
                    MoneyFlow_table tableFrame;

                    try { // try for file does not exist run time error
                        //Call MoneyFlow_table 
                        tableFrame = new MoneyFlow_table(currentFile);
                        tableFrame.pack(); //Resize aboutFrame to fit all contents
                        tableFrame.setLocationRelativeTo(null); //Bring GUI to center of screen 
                        tableFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); //Close button will hide table
                        tableFrame.setVisible(true); //Set GUI's visible = true
                        tableFrame.setTitle(fileToString(currentFile)); //set title to currently opened file
                    } catch (IOException ex) {
                        Logger.getLogger(MoneyFlow_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

            }

        }

    }

    class MenuHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                //Menu action listeners
                case "Open":
                    openDialog(); // call openDialog() function
                    break;

                case "New File":
                    try {
                        //Check for file does not exist run-time error
                        saveAsDialog();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MoneyFlow_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case "About Money-Flow":
                    //Call About class
                    About aboutFrame = new About();
                    aboutFrame.pack(); //Resize aboutFrame to fit all contents 
                    aboutFrame.setTitle("About"); //Set title of GUI
                    aboutFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Close button just hide the "about" GUI
                    aboutFrame.setLocationRelativeTo(null); //Bring GUI to center of screen
                    aboutFrame.setVisible(true); //set visible as true
            }
        }

    }
}
//Class for About GUI file

class About extends JFrame {

    public About() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        String content = "<html>Application for keep track of your money flow<br>"
                + "Copyright © Jin Hwan Oh, Sheridan College Institute of Technology and Advanced Learning | All Rights Reserved<br>"
                + "Version: 1.0<br>"
                + "Project: PROG24178 Final Project<br>"
                + "Professor: Paul Bonenfant<br><br>"
                + "Features<br>"
                + "-Select in/out as money flow (Radio-button) <br>"
                + "-Date picker (formatted-text-field) <br>"
                + "-Set amount (text field) <br>"
                + "-Set account type (drop-down menu) <br>"
                + "-Register category (text-field, button) <br>"
                + "-Select category (drop-down menu) <br>"
                + "-Delete category (button) <br>"
                + "-Clear all unsaved field (button) <br>"
                + "-Save all data to external file (file) <br>"
                + "-Show table of money flow from external file (file, table) <br>"
                + "-Save to new file (save dialog) <br>"
                + "-Open saved database (reader) <br>"
                + "-Show GUI's title for each opened file (string) <br>"
                + "-Count amounts <br>"
                + "-Edit/delete from saved data <br>"
                + "<br><br><br>"
                + "Submitted by: Jin Hwan Oh<br>"
                + "Honour: I have completed this assignment on my own.<br>"
                + "In researching the assignment I got help/ideas from the textbook<br>"
                + "File name: MoneyFlow.java<br>"
                + "Description: This is an pplication for keep track of your money flow<br>"
                + "@author Jin Hwan Oh<br></html>";
        JLabel lbl_content = new JLabel(content, SwingConstants.CENTER);
        add(lbl_content);
    }
}
