package com.kai.breathalyzer.ui.registration;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.kai.breathalyzer.R;
import com.kai.breathalyzer.databinding.FragmentRegistrationBinding;
import com.kai.breathalyzer.ui.login.LoginViewModel;


public class RegistrationFragment extends Fragment {

    private RegistrationViewModel mViewModel;
    FragmentRegistrationBinding binding;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(RegistrationViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getBooleanMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    displayMessage("User Registration Complete");
                    navToLogin();
                }
            }
        });

        mViewModel.getMessageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    displayMessage(s);
                }
            }
        });
    }

    private void displayMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.buttonRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });
        binding.buttonRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    private void onSubmitClicked() {
        String email = binding.editTextRegisterEmail.getText().toString();
        String password = binding.editTextRegisterPassword.getText().toString();
        String firstName = binding.editTextRegisterFirstName.getText().toString();
        String lastName = binding.editTextRegisterLastName.getText().toString();
         if(!email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()){
            mViewModel.registration(email,password,firstName,lastName);
         }
         else{
             displayMessage("Please enter user details before registering");
         }
    }
    private void onCancelClicked(){
        navToLogin();
    }

    private void navToLogin() {
        //goto login Fragment
        NavHostFragment.findNavController(this).navigate(R.id.action_RegisterFragment_to_LoginFragment);
    }

}