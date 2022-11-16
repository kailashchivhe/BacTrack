package com.kai.breathalyzer.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.kai.breathalyzer.R;
import com.kai.breathalyzer.databinding.FragmentProfileBinding;
import com.kai.breathalyzer.model.User;


public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    FragmentProfileBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile_update) {
            onProfileUpdateClicked();
            return true;
        }
        else if (id == R.id.action_profile_logout) {
            onLogoutClicked();
            return true;
        }
        else if (id == R.id.action_profile_home) {
            onHomeClicked();
            return true;
        }
        return false;
    }

    private void onHomeClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }

    private void onLogoutClicked() {
        //delete shared preference
        sharedPreferences.edit().remove("jwtToken").commit();
        sharedPreferences.edit().remove("email").commit();
        sharedPreferences.edit().remove("customerId").commit();
        //        goto login page
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_LoginFragment);
    }

    private void onProfileUpdateClicked() {
//        goto profileupdate fragment
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_ProfileUpdateFragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getMessageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    displayMessage(s);
                }
            }
        });
        mViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    displayMessage("Profile Found");
                    setUserDetails(user);
                }
            }
        });
    }

    private void setUserDetails(User user) {
        binding.textViewProfileEmail.setText(user.getEmail());
        binding.textViewProfileFirstName.setText(user.getFirstName());
        binding.textViewProfileLastName.setText(user.getLastName());
    }

    private void displayMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", "");
        String id = sharedPreferences.getString("id", "");
        String customerId = sharedPreferences.getString("customerId", "");
        spEditor = sharedPreferences.edit();

        mViewModel.retriveProfile(id,jwtToken);
    }

}