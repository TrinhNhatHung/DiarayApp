package com.example.diaryapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class CreateFragment extends Fragment {

    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
    private int mHour;
    private int mMinute;
    private int mColor;
    private BottomNavigationView bottomNavigationMenu;
    private Toolbar toolbar;
    private EditText edTitle;
    private EditText edContent;
    public CreateFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        edTitle = view.findViewById(R.id.pt_title);
        edContent = view.findViewById(R.id.pt_content);

        toolbar = view.findViewById(R.id.tb_menu);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR,year);
                mCalendar.set(Calendar.MONTH,month);
                mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            }
        };
        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mHour = hour;
                mMinute =minute;
            }
        };
        mColor = Color.parseColor("#FFFFFF");
        bottomNavigationMenu = view.findViewById(R.id.bottom_nav);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_date:
                        new DatePickerDialog(getContext(),dateListener,mCalendar.get(Calendar.YEAR),
                                    mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    case R.id.nav_time:
                        new TimePickerDialog(getContext(),timeListener,mHour,mMinute,true).show();
                        return true;
                    case R.id.nav_color:
                        ColorPicker colorPicker = new ColorPicker(getActivity());
                        colorPicker.setRoundColorButton(true);
                        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                mColor = color;
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).setDefaultColorButton(mColor);
                        colorPicker.show();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done,menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.ic_done:
                Intent intent = new Intent();
                intent.putExtra("color",mColor);
                intent.putExtra("title",edTitle.getText().toString());
                intent.putExtra("content",edContent.getText().toString());
                mCalendar.set(Calendar.HOUR_OF_DAY,mHour);
                mCalendar.set(Calendar.MINUTE,mMinute);
                intent.putExtra("time",mCalendar.getTimeInMillis());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}