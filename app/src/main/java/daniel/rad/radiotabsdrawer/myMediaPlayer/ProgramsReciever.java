package daniel.rad.radiotabsdrawer.myMediaPlayer;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgramsReciever extends AsyncTask<Void,Void, List<ProgramsData>>{
    private static final List<ProgramsData> programs = new ArrayList<>();
//    private static final HashMap<String, String> musicFileName = new HashMap<>();


    @Override
    protected List<ProgramsData> doInBackground(Void... voids) {
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
            String filePath = jsonObject.getString("filePath");
            filePath = "http://localhost:5080/WebRTCAppEE/"+filePath;
            String vodId = jsonObject.getString("vodId");
            String type = jsonObject.getString("type");

//            programs.put(vodName,
//                    new MediaMetadataCompat.Builder().
//                            putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,vodId).
//                            putString(MediaMetadataCompat.METADATA_KEY_TITLE, vodName).
//                            putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI,filePath).
//                            build());
//
//            musicFileName.put(vodId,filePath);

            programs.add(
                    new ProgramsData(
                            vodName,"student name",0,creationDate,duration,vodId,filePath));
        }
    }

    public static List<ProgramsData> getPrograms(){
        return programs;
    }
}
