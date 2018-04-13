/**
 * Created by BartowB on 3/28/2016.
 * Simple Program that will monitor services required to run powerschool and database
 * it will start or shutdown services that are set to manual, and will also monitor all
 * services, this can be seen through colored status icons, green=started, yellow=starting
 * orange=stopping, red=stopped
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import java.lang.Runtime;
import javax.swing.Timer;

import static java.lang.System.exit;



public class PSguiFrame {
    //private GUI elements
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JLabel messageLabel;
    private JPanel statusPanel1;
    private JPanel statusPanel2;
    private JPanel statusPanel3;
    private JPanel statusPanel4;
    private JPanel controlPanel;
    private JPanel exitPanel;

    //private ImageIcons that are initialized as gray icons for place holders for statuses
    private ImageIcon psConnection = new ImageIcon(new ImageIcon("src/main/resources/Images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
    private ImageIcon psInstaller = new ImageIcon(new ImageIcon("src/main/resources/Images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
    private ImageIcon psMessenger = new ImageIcon(new ImageIcon("src/main/resources/Images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
    private ImageIcon rwConnection = new ImageIcon(new ImageIcon("src/main/resources/Images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
    private ImageIcon dbListener = new ImageIcon(new ImageIcon("src/main/resources/Images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
    private ImageIcon dbConnection = new ImageIcon(new ImageIcon("src/main/resources/Images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));


    //constuctor that will initialize our swing GUI
    public PSguiFrame() {
        prepareGUI();
    }

    //main function
    public static void main(String[] args) {
        PSguiFrame psguiFrame = new PSguiFrame();
        psguiFrame.showEventFrame();
    }

    private void prepareGUI() {
        //setup mainframe Jframe that will hold all of the elements within it
        //gridlayout to keep everything organized and a title
        mainFrame = new JFrame("PS Database and Application Service Controller");
        mainFrame.setSize(800, 700);
        mainFrame.setLayout(new GridLayout(10, 3, 10, 10));


        //initialize labels for the panel
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        messageLabel = new JLabel("", JLabel.CENTER);

        //setup mainframe with a window listener
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });

        //set up status panel JPanel
        statusPanel1 = new JPanel();
        statusPanel1.setLayout(new GridLayout(1, 3, 10, 5));
        statusPanel1.setSize(600, 200);

        statusPanel2 = new JPanel();
        statusPanel2.setLayout(new GridLayout(1, 3, 10, 5));
        statusPanel2.setSize(600, 200);

        statusPanel3 = new JPanel();
        statusPanel3.setLayout(new GridLayout(1, 3, 10, 5));
        statusPanel3.setSize(600, 200);

        statusPanel4 = new JPanel();
        statusPanel4.setLayout(new GridLayout(1, 3, 10, 5));
        statusPanel4.setSize(600, 200);

        //set up control panel JPanel
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 3, 5, 5));
        controlPanel.setSize(600, 200);

        exitPanel = new JPanel();
        exitPanel.setLayout(new FlowLayout());


        //add elements to the main JFrame
        mainFrame.add(headerLabel);
        mainFrame.add(statusPanel1);
        mainFrame.add(statusPanel2);
        mainFrame.add(statusPanel3);
        mainFrame.add(statusPanel4);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.add(messageLabel);
        mainFrame.add(exitPanel);


    }

    private void showEventFrame() {
        //simple header text
        headerLabel.setText("Start or Stop Services");


        //new timer object to run the actionlistener that sets services statues every 15 secs
        Timer timer = new Timer(1000, taskPerformer);
        timer.start();//start timer


        boolean check = timer.isRunning();//boolean to see if timer is running


        //creation of buttons for control panel, each starts or stops a service
        JButton startPSButton = new JButton("Start PS");
        JButton stopPSButton = new JButton("Stop PS");
        JButton startRWButton = new JButton("Start ReportWorks");
        JButton stopRWButton = new JButton("Stop ReportWorks");
        JButton startDBButton = new JButton("Start Database");
        JButton stopDBButton = new JButton("Stop Database");
        JButton startPSIButton = new JButton("Start PS Installer");
        JButton stopPSIButton = new JButton("Stop PS Installer");
        JButton startMessButton = new JButton("Start PS Messenger");
        JButton stopMessButton = new JButton("Stop PS Messenger");
        JButton startDBListButton = new JButton("Start DB Listener");
        JButton stopDBListButton = new JButton("Stop DB Listener");
        JButton stopAppButton = new JButton("Stop App");

        //action command associated with each button that's pushed
        startPSButton.setActionCommand("startPS");
        stopPSButton.setActionCommand("stopPS");
        startRWButton.setActionCommand("startRW");
        stopRWButton.setActionCommand("stopRW");
        startDBButton.setActionCommand("startDB");
        stopDBButton.setActionCommand("stopDB");
        startPSIButton.setActionCommand("startPSI");
        stopPSIButton.setActionCommand("stopPSI");
        startMessButton.setActionCommand("startMS");
        stopMessButton.setActionCommand("stopMS");
        startDBListButton.setActionCommand("startDBL");
        stopDBListButton.setActionCommand("stopDBL");
        stopAppButton.setActionCommand("stopApp");

        //listener for each button
        startPSButton.addActionListener(new ButtonClickListener());
        stopPSButton.addActionListener(new ButtonClickListener());
        startRWButton.addActionListener(new ButtonClickListener());
        stopRWButton.addActionListener(new ButtonClickListener());
        startDBButton.addActionListener(new ButtonClickListener());
        stopDBButton.addActionListener(new ButtonClickListener());
        startPSIButton.addActionListener(new ButtonClickListener());
        stopPSIButton.addActionListener(new ButtonClickListener());
        startMessButton.addActionListener(new ButtonClickListener());
        stopMessButton.addActionListener(new ButtonClickListener());
        startDBListButton.addActionListener(new ButtonClickListener());
        stopDBListButton.addActionListener(new ButtonClickListener());
        stopAppButton.addActionListener(new ButtonClickListener());


        //status panel that holds our status icons
        statusPanel1.add(new JLabel(psConnection));
        statusPanel1.add(new JLabel(psInstaller));
        statusPanel1.add(new JLabel(psMessenger));
        statusPanel2.add(new JLabel("PS Service", JLabel.CENTER));
        statusPanel2.add(new JLabel("PS Installer Service", JLabel.CENTER));
        statusPanel2.add(new JLabel("PS Messenger Service", JLabel.CENTER));
        statusPanel3.add(new JLabel(rwConnection));
        statusPanel3.add(new JLabel(dbListener));
        statusPanel3.add(new JLabel(dbConnection));
        statusPanel4.add(new JLabel("ReportWorks Service", JLabel.CENTER));
        statusPanel4.add(new JLabel("DBListener Service", JLabel.CENTER));
        statusPanel4.add(new JLabel("PSPRODDB Service", JLabel.CENTER));


        //control panel that holds the buttons
        controlPanel.add(startPSButton);
        controlPanel.add(startRWButton);
        controlPanel.add(startPSIButton);
        controlPanel.add(startMessButton);
        controlPanel.add(startDBButton);
        controlPanel.add(startDBListButton);
        controlPanel.add(stopPSButton);
        controlPanel.add(stopRWButton);
        controlPanel.add(stopPSIButton);
        controlPanel.add(stopMessButton);
        controlPanel.add(stopDBButton);
        controlPanel.add(stopDBListButton);

        exitPanel.add(stopAppButton);

        //message to show when the services are querying on command line
        if (check) {
            messageLabel.setText("Services are up to date.");
        } else {
            messageLabel.setText("Services are not up to date.");
        }


        //run this last to make sure that everything shows when the app starts
        mainFrame.setVisible(true);


    }

    //listener function that will interpert the button click to run command line bat files based on
    //what button is clicked.
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("startPS")) {
                statusLabel.setText("Starting PowerSchool Service");
                try {
                    Process p = Runtime.getRuntime().exec("sc start PearsonPowerSchool");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopPS")) {
                statusLabel.setText("Stopping PowerSchool Service");
                try {
                    Process p = Runtime.getRuntime().exec("sc stop PearsonPowerSchool");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("startDB")) {
                statusLabel.setText("Starting Database");
                try {
                    Process p = Runtime.getRuntime().exec("sc start OracleServicePSPRODDB");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopDB")) {
                statusLabel.setText("Stopping Database");
                try {
                    Process p = Runtime.getRuntime().exec("sc stop OracleServicePSPRODDB");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("startRW")) {
                statusLabel.setText("Starting ReportWorks");
                try {
                    Process p = Runtime.getRuntime().exec("sc start PearsonReportWorks");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopRW")) {
                statusLabel.setText("Stopping ReportWorks");
                try {
                    Process p = Runtime.getRuntime().exec("sc stop PearsonReportWorks");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("startPSI")) {
                statusLabel.setText("Starting PowerSchool Installer");
                try {
                    Process p = Runtime.getRuntime().exec("sc start PearsonPowerSchoolInstaller");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopPSI")) {
                statusLabel.setText("Stopping PowerSchool Installer");
                try {
                    Process p = Runtime.getRuntime().exec("sc stop PearsonPowerSchoolInstaller");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("startDBL")) {
                statusLabel.setText("Starting DataBase Listener");
                try {
                    Process p = Runtime.getRuntime().exec("sc start OracleOH12102TNSListener");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopDBL")) {
                statusLabel.setText("Stopping DataBase Listener");
                try {
                    Process p = Runtime.getRuntime().exec("sc stop OracleOH12102TNSListener");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("startMS")) {
                statusLabel.setText("Starting PowerSchool Messenger Service");
                try {
                    Process p = Runtime.getRuntime().exec("sc start PearsonPowerSchoolMessageService");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopMS")) {
                statusLabel.setText("Stopping PowerSchool Messenger Service");
                try {
                    Process p = Runtime.getRuntime().exec("sc stop PearsonPowerSchoolMessageService");
                    p.waitFor();
                } catch (IOException ex) {
                    messageLabel.setText(ex.getMessage());
                } catch (InterruptedException ex) {
                    messageLabel.setText(ex.getMessage());
                }
            } else if (command.equals("stopApp")) {
                statusLabel.setText("Shutting Down");
                exit(1);
            }
        }
    }

    //function that takes in a string that is the name of the service to check
    //it returns an int that directly relates to the service status
    //ex 1=stopped, 2=stopping, 3=starting, 4=started, 0 is the default if there is an error or issue
    private int checkConnection(String service) {
        int num = 0;
        try {
            Process p = Runtime.getRuntime().exec("sc query " + service);

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            while (line != null) {
                if (line.trim().startsWith("STATE"))

                {

                    if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("1"))
                        num = 1;
                    else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("2"))
                        num = 2;
                    else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("3"))
                        num = 3;
                    else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("4"))
                        num = 4;

                }
                line = reader.readLine();
            }

        } catch (IOException e1) {
        }

        return num;

    }

    //private function that takes in a int and imageicon that is related to a service
    //returns the proper color for the imageicon depending on the num related to the service status
    private ImageIcon statusColor(int num, ImageIcon name) {
        if (num == 0) {
            name =
                    new ImageIcon(new ImageIcon("images/gray.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        } else if (num == 1) {
            statusLabel.removeAll();
            name =
                    new ImageIcon(new ImageIcon("images/red.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            statusLabel.setText("Service is Stopped");
        } else if (num == 2) {
            statusLabel.removeAll();
            name =
                    new ImageIcon(new ImageIcon("images/yellow.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            statusLabel.setText("Service is starting up.");
        } else if (num == 3) {
            statusLabel.removeAll();
            name =
                    new ImageIcon(new ImageIcon("images/orange.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            statusLabel.setText("Service is stopping.");
        } else if (num == 4) {
            statusLabel.removeAll();
            name =
                    new ImageIcon(new ImageIcon("images/green.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            statusLabel.removeAll();
        }

        return name;
    }

    ActionListener taskPerformer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JLabel psService = new JLabel(psConnection);
            JLabel psInstallService = new JLabel(psInstaller);
            JLabel psMessengerService = new JLabel(psMessenger);
            JLabel rwConnService = new JLabel(rwConnection);
            JLabel dbListService = new JLabel(dbListener);
            JLabel dbConnService = new JLabel(dbConnection);

            //int variables that pertain to each service, will call checkconnection function to determine the int
            //that is associated with the status
            int psConn = checkConnection("PearsonPowerSchool");
            int psInstall = checkConnection("PearsonPowerSchoolInstaller");
            int psMess = checkConnection("PearsonPowerSchoolMessageService");
            int rwConn = checkConnection("PearsonReportWorks");
            int dbList = checkConnection("OracleOH12102TNSListener");
            int dbConn = checkConnection("OracleServicePSPRODDB");

            statusPanel1.removeAll();
            //initialization of imageicons for status of required services

            psConnection = statusColor(psConn, psConnection);
            statusPanel1.add(psService);


            psInstaller = statusColor(psInstall, psInstaller);
            statusPanel1.add(psInstallService);


            psMessenger = statusColor(psMess, psMessenger);
            statusPanel1.add(psMessengerService);

            statusPanel3.removeAll();

            rwConnection = statusColor(rwConn, rwConnection);
            statusPanel3.add(rwConnService);


            dbListener = statusColor(dbList, dbListener);
            statusPanel3.add(dbListService);


            dbConnection = statusColor(dbConn, dbConnection);
            statusPanel3.add(dbConnService);


            statusPanel1.revalidate();
            statusPanel1.repaint();
            statusPanel3.revalidate();
            statusPanel3.repaint();
            mainFrame.setVisible(true);

        }
    };


}