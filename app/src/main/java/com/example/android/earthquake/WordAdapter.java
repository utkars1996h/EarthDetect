package com.example.android.earthquake;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;
import java.util.ArrayList;
import java.util.Set;

import static java.lang.Math.floor;

public class WordAdapter extends ArrayAdapter<Word> {


    String PrimaryLocation;
    String SecondaryLocation;




    private static final String LocationSplitter = "of";

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        return dateFormatter.format(dateObject);
    }

    private String formatTime(Date dateObject){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm, a");
        return dateFormatter.format(dateObject);
    }

    private String formatMagnitude(Double magnitudeObject){
        DecimalFormat magnitudeFormatter = new DecimalFormat("0.0");
        return magnitudeFormatter.format(magnitudeObject);
    }

    private int  getMagnitudeColor(double magnitude){
        int MagnitudeReturn;
        int MagnitudeFloor = (int)magnitude;
        switch (MagnitudeFloor){
            case 0:
            case 1:
                MagnitudeReturn = R.color.magnitude1;
                break;
            case 2:
                MagnitudeReturn = R.color.magnitude2;
                break;
            case 3:
                MagnitudeReturn = R.color.magnitude3;
                break;
            case 4:
                MagnitudeReturn = R.color.magnitude4;
                break;
            case 5:
                MagnitudeReturn = R.color.magnitude5;
                break;
            case 6:
                MagnitudeReturn = R.color.magnitude6;
                break;
            case 7:
                MagnitudeReturn = R.color.magnitude7;
                break;
            case 8:
                MagnitudeReturn = R.color.magnitude8;
                break;
            case 9:
                MagnitudeReturn = R.color.magnitude9;
                break;
            default:
                MagnitudeReturn = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(),MagnitudeReturn);



    }






    public WordAdapter(Context context, ArrayList<Word> word){
        super(context,0,word);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentView = convertView;
        if(currentView == null){
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Word currentWord = getItem(position);




        Double magnitudeValue = currentWord.getmMagnitude();

        String formattedMagnitude = formatMagnitude(magnitudeValue);

        TextView textView = currentView.findViewById(R.id.mag);
        textView.setText(formattedMagnitude);




        String originalLocation = currentWord.getmArea();

        if(originalLocation.contains(LocationSplitter)){
           String[] stringArray =  originalLocation.split(LocationSplitter);
           SecondaryLocation = stringArray[0] +LocationSplitter;
           PrimaryLocation = stringArray[1];
        }else{
            SecondaryLocation = getContext().getString(R.string.near_the);
            PrimaryLocation = originalLocation;
        }

        TextView textView1 = currentView.findViewById(R.id.secondary_place);
        textView1.setText(SecondaryLocation);

        TextView textView4 = currentView.findViewById(R.id.primary_place);
        textView4.setText(PrimaryLocation);




        Date dateObject = new Date(currentWord.getmTimeinMilli());

        TextView textView2 = currentView.findViewById(R.id.date);
        String formatDate = formatDate(dateObject);
        textView2.setText(formatDate);


        TextView textView3 = currentView.findViewById(R.id.timeFormat);
        String formattedTime = formatTime(dateObject);
        textView3.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentWord.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);















        return currentView;
    }
}
