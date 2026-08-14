package com.example.chatapplication.menu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ChatListAdapter;
import com.example.chatapplication.model.ChatList;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    public ChatsFragment(){
        //required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        List<ChatList> list = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       // list.add(new ChatList("11","Jannat","Hello, I'm Jannat","17/04/2022","https://w0.peakpx.com/wallpaper/262/819/HD-wallpaper-female-anonymous-blue-eyes-character-drawings-girl-hat-mask-scary.jpg"));
        // list.add(new ChatList("22","Sumi","Hi, I'm Sumi","25/03/2022","https://dp.profilepics.in/profile_pictures/cartoon_girls/cartoon-profile-pic-03.jpg"));
        //list.add(new ChatList("33","Anik","Yo, I'm Anik","18/03/2022","https://w0.peakpx.com/wallpaper/538/896/HD-wallpaper-neon-mask-abstracto-anime-azul-chaqueta-morado-the-purge.jpg"));
        //list.add(new ChatList("44","Tamanna","Hey I'm Tamanna","09/03/2022","https://images.hdqwalls.com/download/anime-girl-art-kz-1125x2436.jpg"));
        //
        //recyclerView.setAdapter(new ChatListAdapter(list, getContext()));

        return view;
        }
}