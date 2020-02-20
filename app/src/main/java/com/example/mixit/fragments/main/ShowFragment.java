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
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import com.example.mixit.R;
import com.example.mixit.activities.MainActivity;
import com.example.mixit.interfaces.UpdateCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.assets.BlurImages;
import com.example.mixit.services.notifications.ReminderBroadcast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment implements UpdateCallback, Button.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Item mItem;
    private View mView;
    private Context mContext;
    private ImageView picture;
    private TextView description, tutorial, ingredients;
    private FloatingActionButton mRandom;
    private boolean showFloating = false;
    private Button mAlarm;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private OnFragmentInteractionListener mListener;

    private int finalDay, finalMonth, finalYear, currentDay, currentMonth, currentYear;
    private boolean sameDay, sameMonth, sameYear;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        mView = inflater.inflate(R.layout.fragment_show, container, false);
        mAlarm = mView.findViewById(R.id.cocktail_alarm);
        mAlarm.setOnClickListener(this);
        mRandom = mView.findViewById(R.id.random);
        if (showFloating) {
            mRandom.setOnClickListener(this);
        } else {
            mRandom.setVisibility(View.INVISIBLE);
        }

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
            Drawable drawablePicture = new BitmapDrawable(getResources(), BlurImages.blur(this.mContext, this.mItem.getImage()));
            picture.setBackground(drawablePicture);
        }
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
        }

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
            Toast.makeText(mContext, "Can't set reminder for the past!", Toast.LENGTH_SHORT).show();
        } else {
            String strDay = (finalDay < 10) ? ("0" + finalDay) : String.valueOf(finalDay);
            String strMonth = (finalMonth < 10) ? ("0" + finalMonth) : String.valueOf(finalMonth);
            String strMinute = (minute < 10) ? ("0" + minute) : String.valueOf(minute);
            String strHour = (hourOfDay < 10) ? ("0" + hourOfDay) : String.valueOf(hourOfDay);
            String finalDate = finalYear+"-"+strMonth+"-"+strDay+"T"+strHour+":"+strMinute+":00.500Z";
            long millisFromEpoch = Instant.parse(finalDate).toEpochMilli();

            Toast.makeText(mContext, "Reminder set!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ReminderBroadcast.class);
            intent.putExtra("item", mItem);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, millisFromEpoch, pendingIntent);
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
}
