package com.kai.breathalyzer.ui.home;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.kai.breathalyzer.R;
import com.kai.breathalyzer.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    HomeViewModel viewModel;
    ObjectAnimator rotate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.connectButton.setOnClickListener(v -> {
            initConnecting();
            viewModel.connectToNearestBreathalyzer();
        });
        binding.history.setOnClickListener( v ->{
            viewModel.setBackFlag( true );
            navigateToGraph();
        });
        viewModel.getConnectionLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                Log.d("HomeFragment", "onChanged: "+aBoolean);
                if( viewModel.getBackFlag() ){
                    viewModel.setBackFlag(false);
                    return;
                }
                connectionSuccessAnimation();
            }
            else{
                Toast.makeText( requireContext(), "Disconnected", Toast.LENGTH_LONG ).show();
            }
        });
    }

    private void navigateToGraph() {
        NavHostFragment.findNavController( this ).navigate( R.id.action_homeFragment_to_graphFragment );
    }

    private void initConnecting() {
        binding.animationView.setVisibility(View.VISIBLE);
        binding.animationView.setImageDrawable(getResources().getDrawable( R.drawable.connecting ) );
        rotate = ObjectAnimator.ofFloat(binding.animationView, "rotation", 359f, 0f);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(500);
        rotate.start();
    }

    private void connectionSuccessAnimation(){
        rotate.cancel();
        rotate.end();
        rotate.removeAllListeners();
        binding.animationView.setImageDrawable(getResources().getDrawable( R.drawable.success ) );
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToTest();
            }
        }, 500 );
    }

    private void navigateToTest(){
        NavHostFragment.findNavController( this ).navigate( R.id.action_homeFragment_to_testFragment );
    }
}