import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

// Packing Final code
class PackingFile
{
    public static void main(String A[])
    {
        // Scanner object for input
        Scanner sobj = null;

        // Variables to store folder and packed file name
        String FolderName = null;
        String PackedName = null;

        try
        {
            // Create Scanner object
            sobj = new Scanner(System.in);

            // Accept folder name from user
            System.out.println("Enter the name of folder :");
            FolderName = sobj.nextLine();

            // Accept packed file name from user
            System.out.println("Enter the name of Packed file :");
            PackedName = sobj.nextLine();

            // Call packing function
            PackFiles(FolderName, PackedName);
        }
        catch(Exception e)
        {
            // Handle exception in main
            System.out.println("Error occurred : " + e.getMessage());
        }
    }

    // Function to pack files from folder into single file
    public static void PackFiles(String FolderName, String PackedName)
    {
        // Variables for reading data
        int iRet = 0;
        int i = 0, j = 0;

        // Encryption key
        byte key = 0x11;

        // Header string
        String Header = null;

        // File objects
        File fobj = null;
        File packobj = null;
        File fArr[] = null;

        // File streams
        FileInputStream fiobj = null;
        FileOutputStream foobj = null;

        // Buffers for data and header
        byte Buffer[] = null;
        byte bHeader[] = null;

        try
        {
            // Create File object for folder
            fobj = new File(FolderName);

            // Check if folder exists and is directory
            if((fobj.exists()) && (fobj.isDirectory()))
            {
                // Create packed file
                packobj = new File(PackedName);
                packobj.createNewFile();

                // Open output stream for packed file
                foobj = new FileOutputStream(packobj);

                // Get list of files from folder
                fArr = fobj.listFiles();

                // Allocate memory for buffers
                Buffer = new byte[1024];
                bHeader = new byte[100];

                // Traverse all files in folder
                for(i = 0; i < fArr.length; i++)
                {
                    // Open input stream for current file
                    fiobj = new FileInputStream(fArr[i]);

                    // Process only .txt files
                    if(fArr[i].getName().endsWith(".txt"))
                    {
                        // Create header (file name + file size)
                        Header = fArr[i].getName() + " " + fArr[i].length();

                        // Make header of fixed size 100 bytes
                        for(j = Header.length(); j < 100; j++)
                        {
                            Header = Header + " ";
                        }

                        // Convert header to byte array
                        bHeader = Header.getBytes();

                        // Write header into packed file
                        foobj.write(bHeader, 0, 100);

                        // Read data from file
                        while((iRet = fiobj.read(Buffer)) != -1)
                        {
                            // Encrypt data using XOR
                            for(j = 0; j < iRet; j++)
                            {
                                Buffer[j] = (byte)(Buffer[j] ^ key);
                            }

                            // Write encrypted data into packed file
                            foobj.write(Buffer, 0, iRet);
                        }
                    }

                    // Close input stream
                    fiobj.close();
                }

                // Close output stream
                foobj.close();
            }
            else
            {
                // Folder not found
                System.out.println("There is no such folder");
            }
        }
        catch(Exception e)
        {
            // Handle exception in packing logic
            System.out.println("Error occurred : " + e.getMessage());
        }
    }
}
