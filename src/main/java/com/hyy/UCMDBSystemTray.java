package com.hyy;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author youyouhuang
 * @create 2019-05-17
 * @desc
 **/

public class UCMDBSystemTray {

    private static final Image red = createImage("images/tingzhi.png", "tray icon");
    private static final Image blue = createImage("images/yuxing.png", "tray icon");
    private static ScheduledExecutorService scheduleService = Executors.newScheduledThreadPool(1);
    private static ExecutorService service = Executors.newFixedThreadPool(5);
    private static volatile String version;
    private static volatile Consts.ServiceState serviceState = Consts.ServiceState.STOPPED;
    private static volatile Image trayIconImage = red;

    //    private static MenuItem aboutItem;
    private static MenuItem startUcmdb;
    private static MenuItem stopUcmdb;
    //    private static CheckboxMenuItem cb1;
//    private static CheckboxMenuItem cb2;
//    private static Menu displayMenu;
//    private static MenuItem errorItem;
//    private static MenuItem warningItem;
//    private static MenuItem infoItem;
//    private static MenuItem noneItem;
    private static MenuItem exitItem;
    private static TrayIcon trayIcon;

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });


    }


    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(red);
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
//        aboutItem = new MenuItem("About");
        startUcmdb = new MenuItem("Start UCMDB");
        stopUcmdb = new MenuItem("Stop UCMDB");
//        cb1 = new CheckboxMenuItem("Set auto size");
//        cb2 = new CheckboxMenuItem("Set tooltip");
//        displayMenu = new Menu("Display");
//        errorItem = new MenuItem("Error");
//        warningItem = new MenuItem("Warning");
//        infoItem = new MenuItem("Info");
//        noneItem = new MenuItem("None");
        exitItem = new MenuItem("Exit");

        //Add components to popup menu
//        popup.add(aboutItem);
        popup.add(startUcmdb);
        popup.add(stopUcmdb);
//        popup.addSeparator();
//        popup.add(cb1);
//        popup.add(cb2);
//        popup.addSeparator();
//        popup.add(displayMenu);
//        displayMenu.add(errorItem);
//        displayMenu.add(warningItem);
//        displayMenu.add(infoItem);
//        displayMenu.add(noneItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });

//        aboutItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null,
//                        "This dialog box is run from the About menu item");
//            }
//        });

        startUcmdb.addActionListener(e -> {
            ServiceControl.startUcmdbServer();
        });

        stopUcmdb.addActionListener(e -> {
            ServiceControl.stopUcmdbServer();
        });

//        cb1.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                int cb1Id = e.getStateChange();
//                if (cb1Id == ItemEvent.SELECTED) {
//                    trayIcon.setImageAutoSize(true);
//                } else {
//                    trayIcon.setImageAutoSize(false);
//                }
//            }
//        });
//
//        cb2.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                int cb2Id = e.getStateChange();
//                if (cb2Id == ItemEvent.SELECTED) {
//                    trayIcon.setToolTip("Sun TrayIcon");
//                } else {
//                    trayIcon.setToolTip(null);
//                }
//            }
//        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Error".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);

                } else if ("Warning".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);

                } else if ("Info".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.INFO;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);

                } else if ("None".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.NONE;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);
                }
            }
        };

//        errorItem.addActionListener(listener);
//        warningItem.addActionListener(listener);
//        infoItem.addActionListener(listener);
//        noneItem.addActionListener(listener);

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });

        scheduleService.scheduleWithFixedDelay(() -> {
            try {
                version = HttpClient.checkUcmdbVersion().toString();
                SystemTray.getSystemTray().getTrayIcons()[0].setToolTip(version);

                serviceState = ServiceControl.checkServiceStatus(new String[]{"sc", "query", "UCMDB_Server"});
            } catch (Exception e) {

            }
        }, 0, 20L, TimeUnit.SECONDS);

        enableButton();
    }

    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    private static void enableButton() {
        service.submit(() -> {
            while (true) {
                try {
                    if (serviceState == Consts.ServiceState.RUNNING) {
                        stopUcmdb.setEnabled(true);
                    } else {
                        stopUcmdb.setEnabled(false);
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });


        service.submit(() -> {
            while (true) {
                if (serviceState == Consts.ServiceState.STOPPED) {
                    startUcmdb.setEnabled(true);
                } else {
                    startUcmdb.setEnabled(false);
                }
            }
        });

        service.submit(() -> {
            while (true) {
                if (serviceState == Consts.ServiceState.STOPPED && trayIconImage == blue) {
                    trayIconImage = red;
                    trayIcon.setImage(trayIconImage);
                }

                if (serviceState == Consts.ServiceState.RUNNING && trayIconImage == red && !version.contains("Starting")) {
                    trayIconImage = blue;
                    trayIcon.setImage(trayIconImage);
                }
            }
        });
    }
}
