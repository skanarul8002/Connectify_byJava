package com.anarul.skwhatsapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anarul.skwhatsapp.Models.Calls;
import com.anarul.skwhatsapp.R;

import java.util.ArrayList;
import java.util.List;


public class CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_calls, container, false);

        RecyclerView recyclerView=view.findViewById(R.id.callsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Calls> list=new ArrayList<>();



        return view;
    }
}