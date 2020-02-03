package com.example.mixit.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.mixit.R;
import com.example.mixit.interfaces.VolleyCallback;
import com.example.mixit.models.Item;
import com.example.mixit.services.network.JSONAPIRequest;
import com.example.mixit.utilities.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link ListFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link ListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ItemListFragment extends Fragment implements VolleyCallback,
        AbsListView.OnScrollListener, SearchView.OnQueryTextListener {

    private View mView;
    private ListView listView;
    private ViewStub stubList;
    private ListViewAdapter listViewAdapter = null;
    private List<Item> itemList = new ArrayList<>();
    private JSONArray APIResponse;
    private Context mContext;
    private FragmentManager mFragmentManager;

    private OnFragmentInteractionListener mListener;

    private Integer itemsByDefault = 10;
    private Integer itemWindows;
    private Integer windowCount = 0;
    private Integer currentPosition = 0;

    public ItemListFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ListFragment.
//     */
    // TODO: Rename and change types and number of parameters
    public static ItemListFragment newInstance() {
//    public static ItemListFragment newInstance(String param1, String param2) {
        ItemListFragment fragment = new ItemListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
        this.mFragmentManager = ((Activity) mContext).getFragmentManager();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setRetainInstance(true);
        if (mView == null) {
            // Inflate the layout for this fragment
            mView = inflater.inflate(R.layout.fragment_list, container, false);
            stubList = mView.findViewById(R.id.stub_list);
            stubList.inflate();
            listView = mView.findViewById(R.id.my_list_view);
            listView.setOnScrollListener(this);
            setAdapters();
            Random random = new Random();
            char randomChar = (char)(random.nextInt(26) + 'a');
            searchItems(Character.toString(randomChar));
        }

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
    public void onSuccess(JSONArray response) {
        if (response != null) {
            itemWindows = (int) Math.ceil(response.length() / itemsByDefault);
            APIResponse = response;
            paginateItems();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int currentItemCount = firstVisibleItem+visibleItemCount;
        if ((currentItemCount == totalItemCount) && (currentItemCount > 0)) {
            paginateItems();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchItems(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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

    private void setAdapters() {
        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(mFragmentManager, mContext, R.layout.list_item, itemList);
            listView.setAdapter(listViewAdapter);
        } else {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private void searchItems (String search) {
        JSONAPIRequest APIService = new JSONAPIRequest(mContext, this);

        HashMap params = new HashMap();
        params.put("glass", null);
        params.put("alcohol", "Alcoholic");
        params.put("category", null);
        params.put("ingredient", null);

        HashMap query = new HashMap();
        query.put("type", "search");
        query.put("search", search);

        currentPosition = 0;
        windowCount = 0;
        itemWindows = null;
        itemList.clear();

        APIService.execute(query);
    }

    private void paginateItems () {
        if ((itemWindows == windowCount) && (itemList.size() < APIResponse.length())) {

            for (int i = currentPosition; i < APIResponse.length(); i++) {
                try {
                    itemList.add(new Item((JSONObject) APIResponse.get(i)));
                    setAdapters();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (itemWindows > windowCount) {
            windowCount++;
            for (int i = currentPosition; i < windowCount*itemsByDefault; i++) {
                try {
                    itemList.add(new Item((JSONObject) APIResponse.get(i)));
                    setAdapters();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                currentPosition++;
            }
            if (windowCount == itemWindows-1) windowCount++;
        }
    }
}
