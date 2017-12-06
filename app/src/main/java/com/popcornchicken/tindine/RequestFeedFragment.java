package com.popcornchicken.tindine;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFeedFragment extends Fragment {

    private ArrayList<Request> mRequests;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private RequestAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private String clickedRequestId;

    public RequestFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RequestFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFeedFragment newInstance() {
        RequestFeedFragment fragment = new RequestFeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_requestfeed, container, false);

        mListView = (ListView) view.findViewById(R.id.request_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);

        return view;
    }

    public void updateListView() {
        if (mRequests.equals(RequestTracker.getInstance().getNearbyRequests())) {
            return;
        }
        mRequests.clear();
        mRequests.addAll(RequestTracker.getInstance().getNearbyRequests());
        Collections.reverse(mRequests);
        mAdapter.notifyDataSetChanged();
    }

    public void initListView() {
        mProgressBar.setVisibility(View.GONE);

        mRequests = RequestTracker.getInstance().getNearbyRequests();
        Collections.reverse(mRequests);

        mAdapter = new RequestAdapter(getActivity(), mRequests);
        mListView.setAdapter(mAdapter);
        mListView.setVisibility(View.VISIBLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Request request = (Request) adapterView.getItemAtPosition(i);
                final RequestData requestData = request.getRequestData();
                clickedRequestId = request.getRequestID();
                final String requesterId = request.getRequesterID();
                final String requestStatus = request.getRequestState();
                final String restaurantName = requestData.getRestaurant().getRestaurantName();
                final String restaurantAddress = requestData.getRestaurant().getRestaurantAddress();
                final String lunchTopic1 = requestData.getTopic1();
                final String lunchTopic2 = requestData.getTopic2();

                final Intent intent = new Intent(getActivity(), RequestInfoActivity.class);
                intent.putExtra("requesterId", requesterId);
                intent.putExtra("requestStatus", requestStatus);
                intent.putExtra("restaurantName", restaurantName);
                intent.putExtra("restaurantAddress", restaurantAddress);
                intent.putExtra("lunchTopic1", lunchTopic1);
                intent.putExtra("lunchTopic2", lunchTopic2);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                // claim request
                break;
            case 2:
                // un-claim request
                break;
            case 3:
                // un-match request
                break;
            case 4:
                // remove request
                break;
            case 5:
                // complete request
                break;
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
        void onFragmentInteraction(String TAG);
    }
}
