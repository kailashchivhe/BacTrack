package com.kai.breathalyzer.ui.test;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import com.kai.breathalyzer.R;
import com.kai.breathalyzer.databinding.FragmentTestBinding;

public class TestFragment extends Fragment {
    FragmentTestBinding binding;
    TestViewModel testViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentTestBinding.inflate(inflater,container,false);
        testViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(TestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        testViewModel.getBatteryPercentage();
        testViewModel.getSerialNumber();
        testViewModel.getUseCount();

        binding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.result.setVisibility(View.GONE);
                testViewModel.startTest();
                binding.message.setText(R.string.get_ready_to_blow_in_12_seconds);
                binding.message.setVisibility( View.VISIBLE );
            }
        });
        binding.disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewModel.disconnect();
            }
        });
        initObservers();
    }

    @SuppressLint("SetTextI18n")
    private void initObservers() {
        testViewModel.getBatteryLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if( integer != null ) {
                    binding.batteryTextView.setText(integer.toString());
                }
            }
        });

        testViewModel.getCountdownLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if( integer != null ) {
                    if( integer == 1 ){
                        binding.message.setText("Please blow!");
                    }
                    else {
                        binding.message.setText("Get Ready to blow in " + integer + " seconds.");
                    }
                }
            }
        });

        testViewModel.getResultLiveData().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float aFloat) {
                if( aFloat != null ) {
                    binding.result.setVisibility( View.VISIBLE );
                    binding.message.setText("Analysis Completed");
                    if(aFloat <= 0.01f){
                        binding.result.setTextColor(Color.GREEN);
                    }
                    else if( aFloat > 0.01f && aFloat <= 0.16f ){
                        binding.result.setTextColor(Color.YELLOW);
                    }
                    else if( aFloat > 0.16f ){
                        binding.result.setTextColor(Color.RED);
                    }
                    binding.result.setText("Result : " + aFloat.toString());
                }
            }
        });

        testViewModel.getProgressLiveData().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float aFloat) {
                if( aFloat != null ) {
                    if(aFloat >= 100 ){
                        binding.message.setText("Analyzing Data...");
                    }
                    else {
                        binding.message.setText("Keep Blowing " + aFloat + "/100 Remaining.");
                    }
                }
            }
        });

        testViewModel.getSerialLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if( s != null ) {
                    binding.serialTextView.setText(""+Integer.parseInt(s, 16));
                }
            }
        });

        testViewModel.getUseCountLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if( integer != null ) {
                    binding.useTextView.setText(integer.toString());
                }
            }
        });

        testViewModel.getConnectionLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                testViewModel.restrictExtraEvent();
                navigateHome();
            }
        });
    }

    private void navigateHome() {
        NavHostFragment.findNavController( this ).navigateUp();
    }
}