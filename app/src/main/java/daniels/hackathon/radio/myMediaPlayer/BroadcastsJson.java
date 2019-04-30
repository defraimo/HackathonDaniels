package daniels.hackathon.radio.myMediaPlayer;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BroadcastsJson extends AsyncTask<Void,Void,String> {

    @Override
    protected String doInBackground(Void... voids) {
        System.out.println("function is working");

        URLConnection urlConnection = null;
        try {
            URL url = new URL("http://be.repoai.com:5080/WebRTCAppEE/rest/broadcast/getVodList/0/100?fbclid=IwAR1OrsxSLBLJ86ZZ3DGxFC0Ym4iq8c1ndiVEtNMeUuQSoQKo7GDpn9sFqAY");
            urlConnection = url.openConnection();
            urlConnection.connect();

            File storage = Environment.getExternalStorageDirectory();
            File file = new File(storage,"broadcasts.json");

            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.flush();
            fileOutput.close();
            System.out.println("json was saved");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }
}
