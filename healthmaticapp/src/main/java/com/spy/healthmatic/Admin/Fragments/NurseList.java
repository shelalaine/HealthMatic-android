package com.spy.healthmatic.Admin.Fragments;

//Team Name: Team SPY

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.spy.healthmatic.API.StaffAPI;
import com.spy.healthmatic.Admin.Adapters.NurseListAdapter;
import com.spy.healthmatic.Admin.AdminAddNurse;
import com.spy.healthmatic.Admin.AdminAddStaff;
import com.spy.healthmatic.Global.GlobalConst;
import com.spy.healthmatic.Global.GlobalFunctions;
import com.spy.healthmatic.Model.Nurse;
import com.spy.healthmatic.Model.Staff;
import com.spy.healthmatic.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NurseList extends Fragment implements GlobalConst, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyler_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.progress_dialog)
    ProgressBar mProgressDialog;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private OnNurseListFragmentInteractionListener mListener;

    //RecyclerView objects
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    ArrayList<Staff> nurses;

    public NurseList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nurse_list, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.circlePVRim),

                getResources().getColor(R.color.circlePVBar),

                getResources().getColor(R.color.appBarScrim),

                getResources().getColor(R.color.yellow));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    public void onStart(){
        super.onStart();
        getNurseList(false);
    }

    private void getNurseList(final boolean isRefresh){
        Call<ArrayList<Staff>> call = STAFF_API.getAllNurses();
        call.enqueue(new Callback<ArrayList<Staff>>() {
            @Override
            public void onResponse(Call<ArrayList<Staff>> call, Response<ArrayList<Staff>> response) {
                if (!response.isSuccessful()) {
                    Log.d("RETROFIT", "RETROFIT FAILURE - RESPONSE FAIL >>>>> " + response.errorBody());
                    Toast.makeText(getActivity(), "Was not able to fetch data. Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                nurses = response.body();
                loadRecyclerViewElements();
                if (isRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Staff>> call, Throwable t) {
                Log.d("RETROFIT", "RETROFIT FAILURE >>>>> " + t.toString());
                Toast.makeText(getActivity(), "Was not able to fetch data. Please try again.", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadRecyclerViewElements(){
        mProgressDialog.setVisibility(View.GONE);
        mAdapter = new NurseListAdapter(nurses, mListener, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NurseList.OnNurseListFragmentInteractionListener) {
            mListener = (NurseList.OnNurseListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @OnClick(R.id.fab)
    public void addNurse(){
        Toast.makeText(getActivity(), "Add Nurse clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AdminAddStaff.class);
        intent.putExtra(ROLE, "nurse");
        intent.putExtra(ACTION, "create");
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        getNurseList(true);
    }

    public interface OnNurseListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNurseListFragmentInteraction(Staff nurse, int position);
    }

}
