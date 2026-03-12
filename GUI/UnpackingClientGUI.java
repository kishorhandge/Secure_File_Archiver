import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

class UnpackingClientGUI extends JFrame implements ActionListener
{
    JTextField tPacked, tDest;
    JButton bDecrypt;

    Font bigFont = new Font("Arial", Font.PLAIN, 18);

    UnpackingClientGUI()
    {
        setTitle("Client - File Unpacking");
        setSize(600,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(5,1,10,10));

        // ========================
        // LABEL 1
        // ========================
        JLabel lbl1 = new JLabel("Enter packed file name : ");
        lbl1.setFont(new Font("Arial", Font.BOLD, 20));
        add(lbl1);

        // source field
        tPacked = new JTextField();
        tPacked.setFont(bigFont);
        add(tPacked);

        // ========================
        // LABEL 2
        // ========================
        JLabel lbl2 = new JLabel("Enter destination folder : ");
        lbl2.setFont(new Font("Arial", Font.BOLD, 20));
        add(lbl2);

        // destination field
        tDest = new JTextField();
        tDest.setFont(bigFont);
        add(tDest);

        // ========================
        // BUTTON
        // ========================
        bDecrypt = new JButton("DECRYPT FILE");
        bDecrypt.setFont(new Font("Arial", Font.BOLD, 22));
        bDecrypt.setBackground(new Color(204,0,0));
        bDecrypt.setForeground(Color.WHITE);
        bDecrypt.setPreferredSize(new Dimension(200,60));
        add(bDecrypt);

        bDecrypt.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        String packed = tPacked.getText().trim();
        String dest   = tDest.getText().trim();

        if(packed.isEmpty() || dest.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        File f = new File(packed);

        if(!f.exists())
        {
            JOptionPane.showMessageDialog(this,
                    "Packed file not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        File folder = new File(dest);

        // create destination folder automatically
        if(!folder.exists())
            folder.mkdirs();

        UnpackFiles(packed, dest);

        JOptionPane.showMessageDialog(this,
                "Files Decrypted Successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ===============================
    //          STARTING
    // ===============================
    public static void UnpackFiles(String PackedName, String DestFolder)
    {
        try
        {
            byte key = 0x11;

            FileInputStream fiobj = new FileInputStream(PackedName);

            byte header[] = new byte[100];
            byte Buffer[] = new byte[1024];

            int iRet;

            while(fiobj.read(header) == 100)
            {
                String head = new String(header).trim();

                String parts[] = head.split(" ");

                String fname = parts[0];
                int size = Integer.parseInt(parts[1]);

                // only change → save inside destination
                FileOutputStream foobj =
                        new FileOutputStream(DestFolder + File.separator + fname);

                int remaining = size;

                while(remaining > 0)
                {
                    iRet = fiobj.read(Buffer,0,Math.min(1024,remaining));

                    for(int j=0;j<iRet;j++)
                        Buffer[j] ^= key;

                    foobj.write(Buffer,0,iRet);

                    remaining -= iRet;
                }

                foobj.close();
            }

            fiobj.close();
        }
        catch(Exception e){}
    }

    public static void main(String[] args)
    {
        new UnpackingClientGUI();
    }
}
