package daniels.hackathon.radio.myMediaPlayer;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import daniels.hackathon.radio.MainActivity;
import daniels.hackathon.radio.MediaPlayerFragment;
import daniels.hackathon.radio.programs.ProgramsData;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProgramsReceiver extends AsyncTask<Void,Void, List<ProgramsData>>{
    private static List<ProgramsData> programs;
    private static TreeMap<String,String> vodNames;
    private WeakReference<MainActivity> mainActivityWeakReference;

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    public ProgramsReceiver() {
    }

    public ProgramsReceiver(MainActivity mainActivity) {
        this.mainActivityWeakReference = new WeakReference<>(mainActivity);
    }

    @Override
    protected List<ProgramsData> doInBackground(Void... voids) {
        programs = new ArrayList<>();
        vodNames = new TreeMap<>();
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
//            String streamName = jsonObject.getString("streamName");
            String vodName = jsonObject.getString("vodName");
            String vodNameFixed = vodName.replace("_"," ").replace(".mp4","");
//            String streamId = jsonObject.getString("streamId");
            long creationDate;
            try {
                creationDate = jsonObject.getInt("creationDate");
            } catch (JSONException e) {
                creationDate = jsonObject.getLong("creationDate");
            }
            int duration = jsonObject.getInt("duration");
//            try {
//                int fileSize = jsonObject.getInt("fileSize");
//            } catch (JSONException e) {
//                long fileSize = jsonObject.getLong("fileSize");
//            } //TODO: erase if not needed
//            String filePath = jsonObject.getString("filePath");
            String filePath = "http://be.repoai.com:5080/WebRTCAppEE/streams/home/"+vodName;
            String vodId = jsonObject.getString("vodId");
//            String type = jsonObject.getString("type");

            programs.add(
                    new ProgramsData(vodId,vodNameFixed,"",duration,filePath,0,creationDate));
            vodNames.put(vodId,vodName);
        }
//        MusicLibrary.playingPrograms((ArrayList<ProgramsData>) programs);
    }

    public static List<ProgramsData> getPrograms(){
        return programs;
    }

    public static TreeMap<String,String> getVodNames(){
        return vodNames;
    }

    @Override
    protected void onPostExecute(List<ProgramsData> programsData) {
        System.out.println(programsData);
        Random r = new Random();
        int random = r.nextInt(programsData.size());
        ArrayList<ProgramsData> randomProgram = new ArrayList<>();
        ProgramsData chosenProgram = programsData.get(random);
        randomProgram.add(chosenProgram);
        MediaPlayerFragment.currentlyPlayingProgram = chosenProgram.getProgramName();
        new InitMusicLibrary(randomProgram,mainActivityWeakReference,0).execute();

//        Intent intent = new Intent(mainActivityWeakReference.get(),DrawerActivity.class);
//        mainActivityWeakReference.get().startActivity(intent);
//        mainActivityWeakReference.get().finish();

    }
}
