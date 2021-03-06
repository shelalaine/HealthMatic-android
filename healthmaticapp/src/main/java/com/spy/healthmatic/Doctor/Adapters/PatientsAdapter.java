package com.spy.healthmatic.Doctor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.spy.healthmatic.Model.Patient;
import com.spy.healthmatic.Model.Staff;
import com.spy.healthmatic.R;

import java.util.List;

/**
 * Team Name: Team SPY
 * Created by shelalainechan on 2016-10-26.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {

    private Staff mDoctor;
    private List<Patient> mPatients;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mivPatient;
        private TextView mtvPatientName;
        private TextView mtvPatientCondition;
        private TextView mtvRoomNum;

        public ViewHolder(View view) {
            super(view);
            mivPatient = (ImageView) view.findViewById(R.id.ivPatient);
            mtvPatientName = (TextView) view.findViewById(R.id.tvPatientName);
            mtvRoomNum = (TextView) view.findViewById(R.id.tvRoomNum);
            mtvPatientCondition = (TextView) view.findViewById(R.id.tvPatientCondition);
        }
    }

    public PatientsAdapter(Context context, List<Patient> patients, Staff doctor) {
        mPatients = patients;
        mContext = context;
        mDoctor = doctor;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public PatientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_patient, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PatientsAdapter.ViewHolder holder, int position) {
        Patient patient = mPatients.get(position);

        ImageView imageView = holder.mivPatient;
        if (patient.getGender() == Patient.FEMALE) {
            imageView.setBackgroundResource(R.mipmap.user_female);
        } else {
            imageView.setBackgroundResource(R.mipmap.user_male);
        }
        TextView textViewName = holder.mtvPatientName;
        textViewName.setText(patient.getFirstName() + " " + patient.getLastName());
        TextView textViewRoom = holder.mtvRoomNum;
        textViewRoom.setText(Integer.toString(patient.getRoom()));
        TextView textViewCondition = holder.mtvPatientCondition;
        textViewCondition.setText(patient.getCondition());

        int lastPosition = - 1;
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}
