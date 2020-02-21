package com.example.mixit.fragments.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mixit.R;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.interfaces.UpdateCallback;
import com.example.mixit.models.Item;
import com.example.mixit.preferences.SessionPreferences;
import com.example.mixit.services.assets.BlurImages;
import com.example.mixit.services.notifications.ReminderBroadcast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShowFragment extends Fragment implements UpdateCallback, Button.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Item mItem;
    private View mView;
    private Context mContext;
    private ImageView picture;
    private TextView description, tutorial, ingredients;
    private static final String TAG = "ShowFragment";
    private FloatingActionButton mRandom;
    private boolean showFloating = false;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Button mFavourite, mAlarm;

    private OnFragmentInteractionListener mListener;

    private int finalDay, finalMonth, finalYear, currentDay, currentMonth, currentYear;
    private boolean sameDay, sameMonth, sameYear;

    private SessionPreferences mSessionPreferences;

    public ShowFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ShowFragment newInstance() {
        ShowFragment fragment = new ShowFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mItem = (Item) getArguments().getSerializable("item");
            this.mContext = getActivity();
            ((MainActivity) this.mContext).setBackButtonVisibility(true);
            this.showFloating = getArguments().getBoolean("showFloating", false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSessionPreferences = ((MainActivity) mContext).getSessionPreferences();
        mView = inflater.inflate(R.layout.fragment_show, container, false);
        mAlarm = mView.findViewById(R.id.cocktail_alarm);
        mAlarm.setOnClickListener(this);
        mRandom = mView.findViewById(R.id.random);
        if (showFloating) {
            mRandom.setOnClickListener(this);
        } else {
            mRandom.setVisibility(View.INVISIBLE);
        }
        mFavourite = mView.findViewById(R.id.cocktail_favorite);
        mFavourite.setOnClickListener(this);
        updateButtons();
        DisplayMetrics display = getResources().getDisplayMetrics();
        int width = display.widthPixels;
        picture = mView.findViewById(R.id.cocktail_picture);
        picture.getLayoutParams().height =(int) (width*0.6);
        description = mView.findViewById(R.id.cocktail_description);
        tutorial = mView.findViewById(R.id.cocktail_tutorial);
        ingredients = mView.findViewById(R.id.cocktail_ingredients);
        description.setText(this.mItem.getDescription());
        tutorial.setText(this.mItem.getTutorial().get("instructions").toString());
        ingredients.setText(this.mItem.getTutorial().get("ingredients").toString());
        if (this.mItem.getImage() == null) {
            this.mItem.setUpdateCallback(this);
        } else {
            picture.setImageBitmap(this.mItem.getImage());
            Drawable drawablePicture = new BitmapDrawable(getResources(),
                    BlurImages.blur(this.mContext, this.mItem.getImage()));
            picture.setBackground(drawablePicture);
        }
        try {
            tutorial.setText(this.mItem.getTutorial().get("instructions").toString());
            ingredients.setText(this.mItem.getTutorial().get("ingredients").toString());
        }catch (Exception e){
            Log.w(TAG, "Error: "+e);
        }
        picture.setImageBitmap(this.mItem.getImage());
        Drawable drawablePicture = new BitmapDrawable(getResources(),
                BlurImages.blur(this.mContext, this.mItem.getImage()));
        picture.setBackground(drawablePicture);
        getActivity().setTitle(this.mItem.getTitle());
        calendar = Calendar.getInstance();
        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onUpdate(@Nullable String itemId) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
            mView.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            ImageView picture = mView.findViewById(R.id.cocktail_picture);
            picture.setImageBitmap(mItem.getImage());
            picture.setImageBitmap(mItem.getImage());
            Drawable drawablePicture = new BitmapDrawable(getResources(),
                    BlurImages.blur(mContext, mItem.getImage()));
            picture.setBackground(drawablePicture);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.random) {
            ((MainActivity) mContext).fetchRandomItem();
        } else if (id == R.id.cocktail_alarm) {
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);

            datePickerDialog = new DatePickerDialog(mContext, this, currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        } else if (id == R.id.cocktail_favorite) {
            mSessionPreferences.saveFavourite(mItem);
        }
        updateButtons();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        finalYear = year;
        finalMonth = month;
        finalDay = dayOfMonth;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, this, hour, minute,true);
        sameDay = finalDay == currentDay;
        sameMonth = finalMonth == currentMonth;
        sameYear = finalYear == currentYear;
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        boolean sameMinute = minute <= currentMinute;
        boolean sameHour = hourOfDay <= currentHour;
        if (sameDay && sameMonth && sameYear && sameHour && sameMinute) {
            Toast.makeText(mContext, R.string.txt_warning_past_alarm, Toast.LENGTH_SHORT).show();
        } else {
            String strDay = (finalDay < 10) ? ("0" + finalDay) : String.valueOf(finalDay);
            String strMonth = ((finalMonth+1) < 10) ? ("0" + (finalMonth+1)) : String.valueOf(finalMonth+1);
            String strMinute = (minute < 10) ? ("0" + minute) : String.valueOf(minute);
            String strHour = (hourOfDay < 10) ? ("0" + hourOfDay) : String.valueOf(hourOfDay);
            String finalDate = finalYear+"-"+strMonth+"-"+strDay+" "+strHour+":"+strMinute+":00";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = simpleDateFormat.parse(finalDate);
                long millis = date.getTime();

            Toast.makeText(mContext, R.string.txt_confirm_reminder, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ReminderBroadcast.class);
            intent.putExtra("item", mItem);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, millis, pendingIntent);
                mSessionPreferences.saveReminder(mItem);
                updateButtons();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateButtons () {
        if (mSessionPreferences.verifyFavourites(null, mItem)) {
            mFavourite.setBackgroundResource(R.drawable.ic_favorite_red_a700_24dp);
        } else {
            mFavourite.setBackgroundResource(R.drawable.ic_favorite_border_red_a700_24dp);
        }
        if (mSessionPreferences.verifyReminders(null, mItem)) {
            mAlarm.setBackgroundResource(R.drawable.ic_alarm_on_cyan_700_24dp);
        } else {
            mAlarm.setBackgroundResource(R.drawable.ic_alarm_add_cyan_700_24dp);
        }
    }
}
