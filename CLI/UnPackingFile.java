import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

// Unpacking Final code
class UnpackingFile
{
    public static void main(String A[]) throws Exception
    {   
        // Variables for file size, loop counter and read count
        int FileSize = 0;
        int i = 0;
        int iRet = 0;

        // Encryption key
        byte key = 0x11;

        // Scanner object for input
        Scanner sobj = null;

        // Variable to store packed file name
        String FileName = null;

        // File objects
        File fpackobj = null;
        File fobj = null;

        // File streams
        FileInputStream fiobj = null;
        FileOutputStream foobj = null;

        // Header buffer and data buffer
        byte bHeader[] = new byte[100];
        byte Buffer[] = null;

        // Header string and tokens
        String Header = null;
        String Tokens[] = null;

        // Create Scanner object
        sobj = new Scanner(System.in);

        // Accept packed file name
        System.out.println("Enter the name of packed file :");
        FileName  = sobj.nextLine();

        // Create File object for packed file
        fpackobj = new File(FileName);

        // Check if packed file exists
        if(fpackobj.exists() == false)
        {
            System.out.println("Error : There is no such packed file");
            return;
        }
         
        // Open packed file for reading
        fiobj = new FileInputStream(fpackobj);

        // Read header and file data
        while((iRet = fiobj.read(bHeader,0,100)) != -1)
        {
            // Convert header bytes to string
            Header = new String(bHeader);

            // Remove extra spaces
            Header = Header.trim();

            // Split header into file name and file size
            Tokens = Header.split(" ");

            System.out.println("File Name : "+Tokens[0]);
            System.out.println("File Size : "+Tokens[1]);

            // Create new file using extracted name
            fobj = new File(Tokens[0]);
            fobj.createNewFile();

            // Open output stream for extracted file
            foobj = new FileOutputStream(fobj);

            // Convert file size to integer
            FileSize = Integer.parseInt(Tokens[1]);

            // Allocate buffer for file data
            Buffer = new byte[FileSize];
            
            // Read encrypted data from packed file
            fiobj.read(Buffer,0,FileSize);

            // Decrypt the data using XOR
            for(i = 0; i < FileSize; i++)
            {
                Buffer[i] = (byte)(Buffer[i] ^ key);
            }

            // Write decrypted data into extracted file
            foobj.write(Buffer,0,FileSize);
        }
    }
}
