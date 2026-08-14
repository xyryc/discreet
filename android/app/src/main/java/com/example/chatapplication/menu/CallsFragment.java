package com.example.chatapplication.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.CallListAdapter;
import com.example.chatapplication.model.CallList;

import java.util.ArrayList;
import java.util.List;

public class  CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calls, container, false);

        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CallList> lists = new ArrayList<>();

       /* lists.add(new CallList(
                "001",
                "Jannat",
                "12/04/2022, 9:33 pm",
                "https://w0.peakpx.com/wallpaper/262/819/HD-wallpaper-female-anonymous-blue-eyes-character-drawings-girl-hat-mask-scary.jpg",
                "income"));


        lists.add(new CallList(
                "001",
                "Sumi",
                "12/04/2022, 10:03 am",
                "https://dp.profilepics.in/profile_pictures/cartoon_girls/cartoon-profile-pic-03.jpg",
                "missed"));

        lists.add(new CallList(
                "001",
                "Anik",
                "12/04/2022, 02:49 pm",
                "https://w0.peakpx.com/wallpaper/538/896/HD-wallpaper-neon-mask-abstracto-anime-azul-chaqueta-morado-the-purge.jpg",
                "out"));

        lists.add(new CallList(
                "001",
                "Tamanna",
                "12/04/2022, 07:43 am",
                "https://images.hdqwalls.com/download/anime-girl-art-kz-1125x2436.jpg",
                "income"));*/

        //recyclerView.setAdapter(new CallListAdapter(lists,getContext()));
        return view;
    }
}