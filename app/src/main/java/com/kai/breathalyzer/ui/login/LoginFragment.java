package com.kai.breathalyzer.ui.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.kai.breathalyzer.databinding.FragmentLoginBinding;
import com.kai.breathalyzer.model.LoginDetails;
import com.kai.breathalyzer.ui.results.GraphViewModel;


public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    FragmentLoginBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(LoginViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getMessageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()){
                    displayMessage(s);
                }
            }
        });

        mViewModel.getLoginDetailsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LoginDetails>() {
            @Override
            public void onChanged(LoginDetails loginDetails) {
                if(loginDetails != null){
                    //save in shared preference
                    spEditor.putString("id", loginDetails.getId());
                    spEditor.putString("jwtToken", loginDetails.getJwtToken());
                    spEditor.putString("customerId", loginDetails.getCustomerId());
                    spEditor.apply();
                    navigateToHome();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", "");
        String id = sharedPreferences.getString("id", "");
        String customerId = sharedPreferences.getString("customerId", "");
        spEditor = sharedPreferences.edit();

        if(jwtToken.length() != 0 && id.length() != 0 && customerId.length() != 0){
            navigateToHome();
        }
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });
        binding.buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClicked();
            }
        });
    }

    private void navigateToHome() {
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_HomeFragment);
    }

    private void onLoginClicked() {
        String email = binding.editTextLoginEmail.getText().toString();
        String password = binding.editTextLoginPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            //call login
            mViewModel.login(email,password);
        }
        else{
            displayMessage("Please enter credentials to login");
        }
    }

    private void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onRegisterClicked(){
        //goto registration fragment
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_RegisterFragment);
    }
}