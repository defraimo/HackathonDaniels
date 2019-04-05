package daniel.rad.radiotabsdrawer.myMediaPlayer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.DrawerActivity;
import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgramsReceiver extends AsyncTask<Void,Void, List<ProgramsData>>{
    private static List<ProgramsData> programs;
//    View view;
//
//    public ProgramsReceiver(View view) {
//        this.view = view;
//    }

    @Override
    protected List<ProgramsData> doInBackground(Void... voids) {
        programs = new ArrayList<>();
        String link = "http://be.repoai.com:5080/WebRTCAppEE/rest/broadcast/getVodList/0/100?fbclid=IwAR1c2x7Pa5nSOL3i4oCvq4Ji_-JNv8DNuTLkqo1sy1h-mPyQCZptUkGQl_E";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(link).build();
        Call call = client.newCall(request);

        try (Response response = call.execute()) {
            if (response.body() == null){
                return programs;
            }
            String json = response.body().string();
            try {
                parseJson(programs,json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return programs;
    }

    private void parseJson(List<ProgramsData> programs, String json) throws JSONException { //TODO: erase unneeded lines
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String streamName = jsonObject.getString("streamName");
            String vodName = jsonObject.getString("vodName");
            String vodNameFixed = vodName.replace("_"," ").replace(".mp4","");
            String streamId = jsonObject.getString("streamId");
            long creationDate;
            try {
                creationDate = jsonObject.getInt("creationDate");
            } catch (JSONException e) {
                creationDate = jsonObject.getLong("creationDate");
            }
            int duration = jsonObject.getInt("duration");
            try {
                int fileSize = jsonObject.getInt("fileSize");
            } catch (JSONException e) {
                long fileSize = jsonObject.getLong("fileSize");
            } //TODO: erase if not needed
//            String filePath = jsonObject.getString("filePath");
            String filePath = "http://be.repoai.com:5080/WebRTCAppEE/streams/home/"+vodName;
            String vodId = jsonObject.getString("vodId");
            String type = jsonObject.getString("type");
            programs.add(
                    new ProgramsData(vodId,vodNameFixed,"",duration,filePath,0,creationDate));
        }
    }

    public static ArrayList<ProgramsData> getPrograms(){
        return (ArrayList<ProgramsData>) programs;
    }

    @Override
    protected void onPostExecute(List<ProgramsData> programsData) {
        System.out.println(programsData);
//        DrawerActivity drawerActivity = new DrawerActivity();
//        drawerActivity.initLoadingPic();
//        drawerActivity.stopLoadingPic();
    }
}
