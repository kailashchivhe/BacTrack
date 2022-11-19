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
    String jwtToken;
    String id;
    String customerId;
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
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile_home){
            onLogoutClicked();
            return true;
        }
        else if(id == R.id.action_profile_logout) {
            onHomeClicked();
            return true;
        }
        return false;
    }

    private void onHomeClicked() {
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }


    private void onLogoutClicked() {
//        delete shared preferences
        sharedPreferences.edit().remove("jwtToken").commit();
        sharedPreferences.edit().remove("email").commit();
        sharedPreferences.edit().remove("customerId").commit();
//        goto login page
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_LoginFragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getBooleanMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    displayMessage("Profile Updated");
                    navToHome();
                }
            }
        });
        mViewModel.messageMutableLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    displayMessage(s);
                }
            }
        });
        mViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), new Observer<User>() {
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
        binding.editTextTextProfieEmail.setText(user.getEmail());
        binding.editTextProfileFirstName.setText(user.getFirstName());
        binding.editTextProfileLastName.setText(user.getLastName());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", "");
        id = sharedPreferences.getString("id", "");
        customerId = sharedPreferences.getString("customerId", "");
        spEditor = sharedPreferences.edit();

        mViewModel.retriveProfile(id,jwtToken);
        binding.buttonProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClicked();
            }
        });
        binding.buttonProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    private void onUpdateClicked() {
        String email = binding.editTextTextProfieEmail.getText().toString();
        String firstName = binding.editTextProfileFirstName.getText().toString();
        String lastName = binding.editTextProfileLastName.getText().toString();

        if(!email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()){
            mViewModel.updateProfile(firstName,lastName,id,jwtToken);
        }
        else{
            displayMessage("Please set values to update");
        }
    }

    private void displayMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void onCancelClicked(){
        //GOto profile page
        navToHome();
    }

    private void navToHome() {
        NavHostFragment.findNavController(this).navigate(R.id.action_ProfileFragment_to_HomeFragment);
    }
}