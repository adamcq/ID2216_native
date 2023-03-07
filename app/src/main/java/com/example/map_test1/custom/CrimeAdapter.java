package com.example.map_test1.custom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map_test1.R;
import com.example.map_test1.model.CrimeItem;
import com.example.map_test1.viewModel.SharedViewModel;

import java.util.ArrayList;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder>{
    private final ArrayList<CrimeItem> crimeItemArrayList;
    SharedViewModel mSharedViewModel;

    // Constructor
    public CrimeAdapter(Context context, ArrayList<CrimeItem> crimeItemArrayList, SharedViewModel sharedViewModel) {
        this.crimeItemArrayList = crimeItemArrayList;
        mSharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public CrimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crime_item_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeAdapter.ViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        CrimeItem model = crimeItemArrayList.get(position);
        Log.d("CrimeAdapter", ""+position);

        if (mSharedViewModel.getCurrentCrimes().getValue()[position])
            holder.checkButton.setImageResource(R.drawable.ic_checked);
        else
            holder.checkButton.setImageResource(R.drawable.ic_unchecked);

        holder.crimeName.setText(model.getName());
        setButtonListener(holder, position);
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return crimeItemArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton checkButton;
        private final TextView crimeName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkButton = itemView.findViewById(R.id.uncheckedButton);
            crimeName = itemView.findViewById(R.id.name);
        }
    }

    private void setButtonListener(CrimeAdapter.ViewHolder holder, int position) {
        holder.checkButton.setOnClickListener(btn ->  {
            boolean[] activeCrimes = mSharedViewModel.getCurrentCrimes().getValue();

            activeCrimes[position] = !activeCrimes[position];
            mSharedViewModel.updateCurrentCrimes(activeCrimes);

            if (activeCrimes[position])
                holder.checkButton.setImageResource(R.drawable.ic_checked);
            else
                holder.checkButton.setImageResource(R.drawable.ic_unchecked);
        });
    }
}
