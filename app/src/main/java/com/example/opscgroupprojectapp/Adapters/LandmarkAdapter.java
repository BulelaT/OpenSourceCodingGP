package com.example.opscgroupprojectapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opscgroupprojectapp.CityPageFragment;
import com.example.opscgroupprojectapp.Models.Landmark_Model;
import com.example.opscgroupprojectapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//(Hagos, 2018)
public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.OptionViewHolder>{
    private final InterfaceRV interfaceRV;//(Practical Coding, 2021)
    final LayoutInflater inflater;
    final ArrayList<Landmark_Model> lmArrayList;
    final FragmentManager fm;


    public LandmarkAdapter(FragmentManager f, InterfaceRV _interfaceRecyclerView, Context context, ArrayList<Landmark_Model> lmArrayList)
    {
        fm = f;
        this.interfaceRV = _interfaceRecyclerView;
        inflater = LayoutInflater.from(context);
        this.lmArrayList = lmArrayList;
    }


    @NonNull
    @Override
    public LandmarkAdapter.OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_r_v_template,parent,false);
        return new LandmarkAdapter.OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandmarkAdapter.OptionViewHolder holder, int position) {
        //holder.country.setText(CountryList[position]);
        Landmark_Model lm = lmArrayList.get(position);
        Picasso.get().load(lm.getLandmarkPicture()).resize(100,100).centerCrop().into(holder.myImage);
        holder.myName.setText(lm.getLandmarkName());
        holder.myLocation.setText(lm.getLandmarkLocation());
    }

    @Override
    public int getItemCount() {
        return lmArrayList.size();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder{

        private final ImageView myImage;
        private final TextView myName, myLocation;
        public OptionViewHolder(@NonNull View itemView)
        {
            super(itemView);
            myImage = (ImageView) itemView.findViewById(R.id.rvImage);
            myName = itemView.findViewById(R.id.rvName);;
            myLocation = itemView.findViewById(R.id.rvLocation);

        }
    }
}

