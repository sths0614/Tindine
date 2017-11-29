package com.popcornchicken.tindine;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "NewRequestFragment";

    private static final String ARG_TEXT = "arg_text";
    private Request mRequest;
    private OnFragmentInteractionListener mListener;

    private PlaceAutocompleteFragment mAutocompleteFragment;
    private Button mSubmitButton;
    private EditText mEditStartTime;
    private EditText mEditEndTime;
    private EditText mEditTopic1;
    private EditText mEditTopic2;
    private int mStartHour;
    private int mStartMinute;
    private int mEndHour;
    private int mEndMinute;
    private Place mPlaceSelected;



    public NewRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    public static NewRequestFragment newInstance() {
        NewRequestFragment fragment = new NewRequestFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_TEXT, text);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newrequest, container, false);
        mSubmitButton = (Button) view.findViewById(R.id.submitButton);
        mEditStartTime = (EditText) view.findViewById(R.id.editStartTime);
        mEditEndTime = (EditText) view.findViewById(R.id.editEndTime);
        mEditTopic1 = (EditText) view.findViewById(R.id.editTopic1);
        mEditTopic2 = (EditText) view.findViewById(R.id.editTopic2);

        mAutocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();

        mAutocompleteFragment.setFilter(typeFilter);
        mAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mPlaceSelected = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, status.getStatusMessage());
            }
        });

        mEditStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar cal = Calendar.getInstance();
                mStartHour = cal.get(Calendar.HOUR_OF_DAY);
                mStartMinute = cal.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(
                                    TimePicker view,
                                    int hourOfDay,
                                    int minute) {
                                mStartHour = hourOfDay;
                                mStartMinute = minute;
                                String pad = minute < 10 ? "0" : "";
                                mEditStartTime.setText("" + hourOfDay + ":" + pad + minute);
                            }
                        }, mStartHour, mStartMinute,false);
                timePickerDialog.show();
            }
        });

        mEditEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get current time then add 30 minutes
                final Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 30);
                mEndHour = cal.get(Calendar.HOUR_OF_DAY);
                mEndMinute = cal.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(
                                    TimePicker view,
                                    int hourOfDay,
                                    int minute) {
                                mEndHour = hourOfDay;
                                mEndMinute = minute;
                                String pad = minute < 10 ? "0" : "";
                                mEditEndTime.setText("" + hourOfDay + ":" + pad + minute);
                            }
                        }, mEndHour, mEndMinute,false);
                timePickerDialog.show();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = Integer.toString(mStartHour) + ":" + Integer.toString(mStartMinute);
                String endTime = Integer.toString(mEndHour) + ":" + Integer.toString(mEndMinute);
                String topic1 = mEditTopic1.getText().toString();
                String topic2 = mEditTopic2.getText().toString();
                Restaurant restaurant = createRestaurantFromPlace(mPlaceSelected);

                Toast.makeText(getContext(), "Request Submitted", Toast.LENGTH_SHORT).show();

                mAutocompleteFragment.setText("");
                mEditStartTime.setText("");
                mEditEndTime.setText("");
                mEditTopic1.setText("");
                mEditTopic2.setText("");

                mRequest = createNewRequest(restaurant, startTime, endTime, topic1, topic2);
                mListener.onSubmitButtonPressed(TAG);
            }
        });

        return view;
    }

    private Restaurant createRestaurantFromPlace(Place place) {
        String id = place.getId();
        String name = place.getName().toString();
        String phoneNumber = place.getPhoneNumber().toString();
        String address = place.getAddress().toString();
        String city = LocationUtil.getCityFromLatLng(getActivity().getApplicationContext(), place.getLatLng());
//        city = "Los Angeles";

        return new Restaurant(id, name, phoneNumber, address, city);
    }

    public Request createNewRequest(
            Restaurant restaurant,
            String startTime,
            String endTime,
            String topic1,
            String topic2) {
        RequestData requestData = new RequestData(startTime, endTime, topic1, topic2,
                restaurant);
        // requesterID is the user's FBID
        Request newRequest = new Request(User.getUserFBID(), requestData);
        FirebaseManager.getInstance().writeRequest(newRequest);
        return newRequest;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString(ARG_TEXT, mText);
        super.onSaveInstanceState(outState);
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
        void onFragmentInteraction(String TAG);
        void onSubmitButtonPressed(String TAG);
    }
}
