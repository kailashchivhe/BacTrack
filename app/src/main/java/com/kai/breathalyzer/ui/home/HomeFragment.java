package com.kai.breathalyzer.ui.home;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home_logout){
            onLogoutClicked();
            return true;
        }
        else if(id == R.id.action_home_profile) {
            onProfileClicked();
            return true;
        }
        return false;

    }

    private void onProfileClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_ProfileFragment);
    }

    private void onLogoutClicked() {
        sharedPreferences.edit().remove("jwtToken").commit();
        sharedPreferences.edit().remove("email").commit();
        sharedPreferences.edit().remove("customerId").commit();
        //        goto login page
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_LoginFragment);
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