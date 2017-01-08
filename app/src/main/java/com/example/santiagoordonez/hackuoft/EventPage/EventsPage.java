package com.example.santiagoordonez.hackuoft.EventPage;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.santiagoordonez.hackuoft.HackPage.HackSwipeAdapter;
import com.example.santiagoordonez.hackuoft.R;
import com.example.santiagoordonez.hackuoft.dto.EventDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventsPage extends FragmentActivity {

    // initialize a new view pager object
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);

        //get List of Events
        EventTask eventTask = new EventTask();

        //use a given link to get events
        String url = "https://graph.facebook.com/143619579060117/events?access_token=1278772608884108%7CNyvDdjr45c-jtXgQyRG0rkiTq2s&__mref=message_bubble";
        eventTask.execute(url);
    }

    //Remember to check over code to check if all parameters exist, rn it works for prototype only
    public class EventTask extends AsyncTask<String, String, ArrayList<EventDTO>> {

        @Override
        protected void onPostExecute(ArrayList<EventDTO> eventDTOs) {
            //get the view pager object from the view
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            //add swipe adapter
            EventSwipeAdapter eventSwipeAdapter = new EventSwipeAdapter (getSupportFragmentManager(), eventDTOs);

            //set adapter for viewPager
            viewPager.setAdapter(eventSwipeAdapter);

        }

        @Override
        protected ArrayList<EventDTO> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                System.out.println("Hello!");
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                System.out.println("connect?");
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("data");

                ArrayList<EventDTO> EventDTOList = new ArrayList<EventDTO>();

                for (int i = 0; i < parentArray.length(); i++) {
                    EventDTO currentEventDTO = new EventDTO();

                    JSONObject finalObject = parentArray.getJSONObject(i);
                    //Checks for the date if it is in the future or the day of
                    String startTime = "";
                    if(finalObject.has("start_time")){
                        startTime = (finalObject.getString("start_time").substring(0,10));
                    }
                    String endTime = "";
                    if(finalObject.has("endTime")){
                        endTime = (finalObject.getString("end_time").substring(0,10));
                    }
                    if (Integer.parseInt(startTime.substring(0,4))<=2015){
                        System.out.println("dead");
                        break;
                    }
                    String description = finalObject.getString("description");
                    String name = finalObject.getString("name");
                    String eventID = finalObject.getString("id");
                    currentEventDTO.setEventId(eventID);

                    if(finalObject.has("place")){
                        JSONObject placeObj = finalObject.getJSONObject("place");
                        if (placeObj.has("location")){
                            JSONObject locationObj = placeObj.getJSONObject("location");
                            String street = locationObj.getString("street");
                            String address = (street + " " + "ON,"+ "Canada");
                            currentEventDTO.setLocation(address);
                            currentEventDTO.setDesription(description);
                            currentEventDTO.setName(name);
                            currentEventDTO.setStartTime(startTime);
                            EventDTOList.add(currentEventDTO);
                        }
                        else{
                            currentEventDTO.setDesription(description);
                            currentEventDTO.setName(name);
                            currentEventDTO.setLocation("3359 Mississauga Rd, ON,Canada");
                            currentEventDTO.setStartTime(startTime);
                            currentEventDTO.setEndTime(endTime);
                            EventDTOList.add(currentEventDTO);
                        }
                    }else{
                        currentEventDTO.setDesription(description);
                        currentEventDTO.setName(name);
                        currentEventDTO.setLocation("3359 Mississauga Rd, ON,Canada");
                        currentEventDTO.setStartTime(startTime);
                        EventDTOList.add(currentEventDTO);
                    }
                }
                return EventDTOList;
//            for(EventDTO event: EventDTOList){
//                System.out.print(event);
//            }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
