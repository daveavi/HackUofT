package com.example.santiagoordonez.hackuoft.HackPage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.santiagoordonez.hackuoft.dto.HackathonDTO;

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

/**
 * Created by Ose on 1/7/2017.
 */

public class HackSwipeAdapter extends FragmentPagerAdapter {
    ArrayList<HackathonDTO> finalHackathons;

    //TODO: This constructor will have to accept an array of HackathonEvent classes that it will iterate through as swipe objects.
    public HackSwipeAdapter(FragmentManager fm , ArrayList<HackathonDTO> hack) {
        super(fm);
        finalHackathons = hack;
    }
    @Override
    //TODO: This method will give us the current HackathonEvent object using a position as it's index in the list.
    public Fragment getItem(int position) {

        HackPageFragment fragment = new HackPageFragment();

        //get current hackathon event
        HackathonDTO hackathon = finalHackathons.get(position);

        //This will allow us to pass variables from the HackathonEvent Objects to the fragments
        Bundle bundle = new Bundle();

        //Right now we are passing a variable called "position" that gives the position of an object
        bundle.putInt("position", position);

        bundle.putInt("year",Integer.parseInt(hackathon.getYear()));

        bundle.putString("cost", hackathon.getCost());

        bundle.putString("facebook_url", hackathon.getFacebookURL());

        bundle.putString("city",hackathon.getCity());

        bundle.putString("picture_url", hackathon.getPictureURL());

        bundle.putString("school_name",hackathon.getHost());

        bundle.putString("title",hackathon.getTitle());


        //add bundle as an argument
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    //TODO: This method will get the total number of elements in the HackathonEvent List.
    public int getCount() {

        // we will leave this as static for now
        return finalHackathons.size();
    }


}
