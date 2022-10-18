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
import com.example.opscgroupprojectapp.Dashboard;
import com.example.opscgroupprojectapp.R;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.OptionViewHolder>{
    private final InterfaceRV interfaceRV;//(Practical Coding, 2021)
    final LayoutInflater inflater;
    final String[] CountryList;
    final FragmentManager fm;
    public CountryListAdapter(FragmentManager f, InterfaceRV _interfaceRecyclerView, Context context, String[] CountryList)
    {
        fm = f;
        this.interfaceRV = _interfaceRecyclerView;
        inflater = LayoutInflater.from(context);
        this.CountryList = CountryList;
    }


    @NonNull
    @Override
    public CountryListAdapter.OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dashboard,parent,false);
        return new CountryListAdapter.OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.OptionViewHolder holder, int position) {
        holder.country.setText(CountryList[position]);
    }

    @Override
    public int getItemCount() {
        return CountryList.length;
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder{

        ImageView countryImage;
        Button country;

        public OptionViewHolder(@NonNull View itemView)
        {
            super(itemView);
            countryImage = itemView.findViewById(R.id.flagPic);
            country = itemView.findViewById(R.id.selectedCountryBtn);
            country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.beginTransaction().setReorderingAllowed(true).replace(R.id.WelcomeFrag, CityPageFragment.class,null).addToBackStack(null).commit();

                }
            });
            itemView.setOnClickListener(v -> {
                if(interfaceRV != null)
                {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        interfaceRV.onItemClick(pos);//(Practical Coding, 2021)
                    }
                }
            });
        }
    }
}
