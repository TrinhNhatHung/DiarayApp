package com.example.diaryapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.diaryapp.model.PostMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private FloatingActionButton fabCreate;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private RecyclerView rvDiary;
    private DiaryAdapter adapter;
    private ArrayList<PostMessage> mPostMessage;
    private final int RETURN_CODE = 123;
    private long countPost;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_list_24);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabCreate = view.findViewById(R.id.fab_create);
        fabCreate.setColorFilter(Color.WHITE);
        rvDiary = view.findViewById(R.id.rv_note);
        rvDiary.setLayoutManager(new LinearLayoutManager(getContext()));

        mPostMessage = new ArrayList<>();


        mDatabase.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPostMessage = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                        PostMessage data = item.getValue(PostMessage.class);
                        mPostMessage.add(data);
                }
                adapter = new DiaryAdapter(mPostMessage);
                rvDiary.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFragment fragment = new CreateFragment();
                fragment.setTargetFragment(DashboardFragment.this, RETURN_CODE);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RETURN_CODE) {
                String title = data.getStringExtra("title");
                String content = data.getStringExtra("content");
                int color = data.getIntExtra("color", 0);
                long time = data.getLongExtra("time", 0);
                PostMessage postMessage = new PostMessage(title,content,color,time);
                mDatabase.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            countPost = snapshot.getChildrenCount();
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mDatabase.child(firebaseAuth.getUid()).child("" + (countPost + 1)).setValue(postMessage);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_logout,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, loginFragment);
                transaction.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}