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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.R;
import com.ayros.iftis_mobapp.db.DataBaseAction;
import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;
import com.ayros.iftis_mobapp.network.NetworkFragment;

import androidx.navigation.Navigation;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends DialogFragment implements DownloadCallback<String> {

    public static final String EXTRA_STUDENT = "com.ayros.iftis_mobapp.model.student";

    private final static String url = "/autorisation/login";
    private static final String DIALOG_REGISTRATION = "Registration";
    private NetworkFragment fragment;

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Student student;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login,null);
        // Set up the login form.
        mLoginView = (EditText) v.findViewById(R.id.login);

        mPasswordView = (EditText) v.findViewById(R.id.password_autorization);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) v.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) v.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                RegistrationFragment fragment = new RegistrationFragment();
                fragment.setCancelable(false);
                fragment.setTargetFragment(getTargetFragment(),getTargetRequestCode());
                fragment.show(manager,DIALOG_REGISTRATION);
                getDialog().cancel();
            }
        });
        mLoginFormView = v.findViewById(R.id.autorization_form);
        mProgressView = v.findViewById(R.id.conncetion_progress);
        fragment = NetworkFragment.getInstance(getFragmentManager(), getString(R.string.server_url) + url);
        Dialog dialog= new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.title_activity_login).create();
        return dialog;
    }

    private void attemptLogin() {

        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_wrong_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isEmailValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
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
            student = new Student();
            student.setLogin(login);
            student.setPassword(password);
            String[] str = {login,password};
            fragment.setCallback(this);
            fragment.startDownload(str);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void updateFromDownload(String result) {
        if(result == null){
            Toast.makeText(getContext(),"Conncetion Failed", Toast.LENGTH_LONG).show();
            return;
        }
        if(result.equals("Valid")){
            addUser(student);
            sendResult(Activity.RESULT_OK,student);
            this.getDialog().cancel();
            return;
        }
        if(result.equals("Invalid")){
            mPasswordView.setError(getString(R.string.error_wrong_password));
            mPasswordView.requestFocus();
            return;
        }
        else {
            Toast.makeText(getContext(),"Conncetion Failed", Toast.LENGTH_LONG).show();
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
        intent.putExtra(EXTRA_STUDENT,student);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
