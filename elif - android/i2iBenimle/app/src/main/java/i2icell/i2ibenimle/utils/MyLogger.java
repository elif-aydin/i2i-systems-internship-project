package i2icell.i2ibenimle.utils;

/**
 * Created by elif on 26-Jul-17.
 */

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class MyLogger
{
    public static PrintWriter pw = null;
    public static FileOutputStream f = null;

    public static void init() throws IOException
    {
        try
        {
            File root = android.os.Environment.getExternalStorageDirectory();
            File file = new File(root.getAbsolutePath(), "i2i_log.txt");
            f = new FileOutputStream(file, true);
            pw = new PrintWriter(f);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.err.println(">>> init done");
    }

    public static void writeToLog(String text)
    {
        try
        {
            init();
            pw.println((new Date()) + " -- " + text);
            pw.flush();
            pw.close();
            f.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
