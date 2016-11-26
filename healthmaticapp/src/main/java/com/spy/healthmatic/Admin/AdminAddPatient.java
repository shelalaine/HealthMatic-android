package com.spy.healthmatic.Admin;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.spy.healthmatic.API.StaffAPI;
import com.spy.healthmatic.Admin.Fragments.PatientDoctorFragment;
import com.spy.healthmatic.Admin.Fragments.PatientHealthFragment;
import com.spy.healthmatic.Admin.Fragments.PatientList;
import com.spy.healthmatic.Admin.Fragments.PatientNurseFragment;
import com.spy.healthmatic.Admin.Fragments.PatientPersonalFragment;
import com.spy.healthmatic.Global.GlobalConst;
import com.spy.healthmatic.Model.Doctor;
import com.spy.healthmatic.Model.Nurse;
import com.spy.healthmatic.Model.Patient;
import com.spy.healthmatic.Model.Staff;
import com.spy.healthmatic.Model.Tab;
import com.spy.healthmatic.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddPatient extends AppCompatActivity implements GlobalConst {

    private int currentTabPosition;
    private Patient patient;

    ArrayList<Tab> tabs;
    public static ArrayList<Staff> doctors;
    public static ArrayList<Staff> nurses;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_patient);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            currentTabPosition = bundle.getInt(CURRENTTABPOSITION);
//            patient = (Patient) bundle.getSerializable(PATIENT);
//            tabs = (ArrayList<Tab>) bundle.getSerializable(TABS);
//        }
        if(patient==null){
            patient = new Patient();
            tabs = new ArrayList<>(4);
            tabs.add(0, new Tab("Personal", 0));
            currentTabPosition = 0;
        }

        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentTabPosition, true);

        tabLayout.setupWithViewPager(mViewPager);
        downloadDoctorAndNurse();
    }

    public ViewPager getViewPagerObject(){
        return mViewPager;
    }

    private void downloadDoctorAndNurse(){
        Call<ArrayList<Staff>> call = STAFF_API.getAllDoctors();
        call.enqueue(new Callback<ArrayList<Staff>>() {
            @Override
            public void onResponse(Call<ArrayList<Staff>> call, Response<ArrayList<Staff>> response) {
                if (!response.isSuccessful()) {
                    doctors = null;
                    return;
                }
                doctors = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Staff>> call, Throwable t) {
                doctors = null;
            }
        });
        Call<ArrayList<Staff>> call1 = STAFF_API.getAllNurses();
        call1.enqueue(new Callback<ArrayList<Staff>>() {
            @Override
            public void onResponse(Call<ArrayList<Staff>> call, Response<ArrayList<Staff>> response) {
                if (!response.isSuccessful()) {
                    nurses = null;
                    return;
                }
                nurses = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Staff>> call, Throwable t) {
                nurses = null;
            }
        });
    }

    public ArrayList<Staff> getDoctors(){
        return doctors;
    }

    public ArrayList<Staff> getNurses(){
        return nurses;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return PatientPersonalFragment.newInstance(patient, tabs);
            }
            else if (position == 1) {
                return PatientHealthFragment.newInstance(patient, tabs);
            }
            else if (position == 2) {
                return PatientDoctorFragment.newInstance(patient, tabs);
            }
            return PatientNurseFragment.newInstance(patient, tabs);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Personal";//steps.get(position).getTitle();
            } else if (position == 1) {
                return "Health";
            } else if (position == 2) {
                return "Docotor";
            } else {
                return "Nurse";
            }
        }
    }

}
