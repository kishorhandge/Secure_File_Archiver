import java.awt.*;            // AWT library used for GUI layout, colors, fonts
import java.awt.event.*;     // Used to handle events like button click
import java.io.*;            // Used for file handling (read/write files)
import java.util.ArrayList;  // Used to store multiple file names dynamically
import javax.swing.*;        // Swing library used to create GUI components

// This class creates the Packing Server GUI and handles button click events
// JFrame = creates window
// ActionListener = handles button click events
class PackingServerGUI extends JFrame implements ActionListener
{
    JTextField tFile; // Textbox where user enters file name

    JButton bAdd, bDone;
    // bAdd = button to add file into packing list
    // bDone = button to start packing process

    DefaultListModel<String> model;
    // This model stores file names and connects to GUI list

    JList<String> list;
    // This displays file names on screen

    ArrayList<String> files = new ArrayList<>();
    // This stores file names internally for packing process

    Font bigFont = new Font("Arial", Font.PLAIN, 18);
    // Font object used to make text readable and bigger

    // Constructor (runs automatically when object is created)
    PackingServerGUI()
    {
        setTitle("Server - File Packing");
        // Sets title of window

        setSize(600,500);
        // Sets window width and height

        setLocationRelativeTo(null);
        // Opens window at center of screen

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Terminates program when window is closed

        setLayout(new BorderLayout(10,10));
        // Divides window into 5 areas: NORTH, SOUTH, EAST, WEST, CENTER

        // ========================
        //        TOP PANEL
        // ========================
        JPanel top = new JPanel(new BorderLayout(10,10));
        // Creates panel for top section

        JLabel lbl = new JLabel("Enter the file names : ");
        // Creates label to guide user

        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        // Sets label font

        top.add(lbl, BorderLayout.NORTH);
        // Adds label to top of panel

        JPanel inputPanel = new JPanel(new BorderLayout(10,10));
        // Panel for textbox and add button

        tFile = new JTextField();
        // Textbox where user enters file name

        tFile.setFont(bigFont);
        // Sets textbox font

        bAdd = new JButton("+");
        // Button to add file

        bAdd.setFont(new Font("Arial", Font.BOLD, 22));

        bAdd.setBackground(new Color(0,153,76));
        // Sets button background green

        bAdd.setForeground(Color.WHITE);
        // Sets button text color white

        inputPanel.add(tFile, BorderLayout.CENTER);
        // Adds textbox to panel

        inputPanel.add(bAdd, BorderLayout.EAST);
        // Adds Add button to panel

        top.add(inputPanel, BorderLayout.SOUTH);
        // Adds input panel below label

        add(top, BorderLayout.NORTH);
        // Adds top panel to main window

        // ========================
        //      CENTER LIST
        // ========================
        model = new DefaultListModel<>();
        // Creates model to store file names

        list = new JList<>(model);
        // Creates list connected to model

        list.setFont(bigFont);
        // Sets list font

        add(new JScrollPane(list), BorderLayout.CENTER);
        // Adds scrollable list to center

        // ========================
        //      DONE BUTTON
        // ========================
        bDone = new JButton("DONE PACKING");
        // Button to start packing

        bDone.setFont(new Font("Arial", Font.BOLD, 22));

        bDone.setBackground(new Color(0,102,204));
        // Blue color button

        bDone.setForeground(Color.WHITE);

        bDone.setPreferredSize(new Dimension(200,70));
        // Sets button size

        add(bDone, BorderLayout.SOUTH);
        // Adds button to bottom

        bAdd.addActionListener(this);
        // Registers Add button event

        bDone.addActionListener(this);
        // Registers Done button event

        setVisible(true);
        // Makes window visible
    }

    // This function executes when any button is clicked
    public void actionPerformed(ActionEvent e)
    {
        // ========================
        // ADD FILE BUTTON LOGIC
        // ========================
        if(e.getSource() == bAdd)
        {
            String name = tFile.getText().trim();
            // Gets file name from textbox

            if(name.isEmpty())
                return;
            // Stops if empty input

            File f = new File(name);
            // Creates file object to access file properties

            if(!f.exists())
            {
                // Checks if file exists physically in system

                JOptionPane.showMessageDialog(this,
                        "File does not exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            files.add(name);
            // Adds file to internal list

            model.addElement(name);
            // Adds file name to GUI list

            tFile.setText("");
            // Clears textbox
        }

        // ========================
        // DONE PACKING BUTTON LOGIC
        // ========================
        if(e.getSource() == bDone)
        {
            if(files.size() == 0)
            {
                // Prevents packing if no files added

                JOptionPane.showMessageDialog(this,
                        "No files added!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            String output = JOptionPane.showInputDialog("Enter Packed File Name");
            // Takes packed file name from user

            if(output == null || output.trim().isEmpty())
                return;

            PackFiles(files, output);
            // Calls packing function

            JOptionPane.showMessageDialog(this,
                    "Packing Completed Successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===============================
    // PACK FILE FUNCTION
    // ===============================
    public static void PackFiles(ArrayList<String> fileList, String PackedName)
    {
        try
        {
            byte key = 0x11;
            // XOR encryption key
            // XOR is used because it can encrypt and decrypt using same key

            FileOutputStream foobj = new FileOutputStream(PackedName);
            // Creates packed file where all data will be stored

            byte Buffer[] = new byte[1024];
            // Buffer improves performance by reading chunks instead of byte-by-byte

            byte bHeader[] = new byte[100];
            // Header stores metadata (file name and file size)
            // Fixed size header helps unpacking process

            //This loop is used to access each file one by one from 
            // the fileList so that every file can be packed
            for(String fname : fileList)
            {
                File f = new File(fname);

                if(f.getName().endsWith(".txt"))
                {
                    // Only packs text files

                    FileInputStream fiobj = new FileInputStream(f);
                    // Opens file for reading

                    String Header = f.getName() + " " + f.length();
                    // Header format: filename + filesize
                    // Example: abc.txt 200

                    while(Header.length() < 100)
                        Header += " ";
                    // Makes header fixed size for easy unpacking

                    bHeader = Header.getBytes();
                    // Converts header into byte format

                    foobj.write(bHeader,0,100);
                    // Writes header into packed file

                    int iRet = 0;

                    while((iRet = fiobj.read(Buffer)) != -1)
                    {
                        // Reads file data into buffer

                        for(int j=0;j<iRet;j++)
                            Buffer[j] ^= key;
                        // Encrypts data using XOR encryption

                        foobj.write(Buffer,0,iRet);
                        // Writes encrypted data into packed file
                    }

                    fiobj.close();
                    // Closes input file
                }
            }

            foobj.close();
            // Closes packed file
        }
        catch(Exception e){}
    }

    public static void main(String[] args)
    {
        new PackingServerGUI();
        // Starts GUI application
    }
}
