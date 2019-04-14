package com.ayros.iftis_mobapp.autorization;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.R;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;
import com.ayros.iftis_mobapp.network.NetworkFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends DialogFragment implements DownloadCallback<String> {

    private final static String url = "/autorisation/registration";
    private final static String INVALID = "INVALID";
    private NetworkFragment network;

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;
    private EditText mGroupView;
    private EditText mSubgroupView;
    private View mProgressView;
    private View mRegistrationFormView;
    private Student student;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_registration,null);
        // Inflate the layout for this fragment
        mLoginView = (EditText) v.findViewById(R.id.email);

        mPasswordView = (EditText) v.findViewById(R.id.password);
        mGroupView = (EditText) v.findViewById(R.id.group);
        mSubgroupView = (EditText) v.findViewById(R.id.subgroup);

        Button mRegistrationButton = (Button) v.findViewById(R.id.new_account_button);
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegistrationFormView = v.findViewById(R.id.registration_form);
        mProgressView = v.findViewById(R.id.registration_progress);
        network = NetworkFragment.getInstance(getFragmentManager(),getString(R.string.server_url)+url);
        Dialog dialog= new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.title_activity_registration_actitity).create();
        return dialog;
    }
    private void attemptLogin() {

        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);
        mGroupView.setError(null);
        mSubgroupView.setError(null);

        // Store values at the time of the login attempt.
        String email = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();
        String group = mGroupView.getText().toString();
        String subgroup = mSubgroupView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mLoginView.setError(getString(R.string.error_invalid_email));
            focusView = mLoginView;
            cancel = true;
        }
        if (TextUtils.isEmpty(group)) {
            mGroupView.setError(getString(R.string.error_field_required));
            focusView = mGroupView;
            cancel = true;
        }
        if (TextUtils.isEmpty(subgroup)) {
            mSubgroupView.setError(getString(R.string.error_field_required));
            focusView = mSubgroupView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            String[] strings = {email,password,group,subgroup};
            student = new Student();
            student.setLogin(email);
            student.setPassword(password);
            network.setCallback(this);
            network.startDownload(strings);
        }
    }

    private boolean isEmailValid(String login) {
        return login.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistrationFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void updateFromDownload(String result) {
        ObjectMapper mapper = new ObjectMapper();
        String[] response;
        try {
            response = mapper.readValue(result, String[].class);
            if(checkResults(response)){
                addUser(student);
                sendResult(Activity.RESULT_OK,student);
                this.getDialog().cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        showProgress(true);
    }

    @Override
    public void finishDownloading() {
        showProgress(false);
    }

    private boolean checkResults(String[] response){
        boolean success = true;
        if(response[0].equals(INVALID)){
            mLoginView.setError(getString(R.string.error_invalid_login));
            mLoginView.requestFocus();
            success = false;
        }
        if(response[1].equals(INVALID)){
            mPasswordView.setError(getString(R.string.error_wrong_password));
            mPasswordView.requestFocus();
            success = false;
        }
        if(response[2].equals(INVALID)){
            mGroupView.setError(getString(R.string.error_invalid_login));
            mGroupView.requestFocus();
            success = false;
        }
        if(response[3].equals(INVALID)){
            mSubgroupView.setError(getString(R.string.error_invalid_login));
            mSubgroupView.requestFocus();
            success = false;
        }
        return success;
    }

    private void addUser(Student student){
        DataBaseAction action = new UserInsertAction(getContext(), student);
        Data.getInstance(getContext()).getData(action);
        Data.getInstance(getContext()).setStudent(student);
    }

    private void sendResult(int resultCode, Student student){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(LoginFragment.EXTRA_STUDENT,student);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
