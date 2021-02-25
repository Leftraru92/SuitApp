package com.example.suitapp.fragment;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitapp.R;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static FirebaseAuth mAuth;
    View root;
    static Context context;
    static RelativeLayout loadingPanel;
    static SignInButton signInButton;
    private static GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);
        context = getActivity();

        FloatingActionButton fabBack = root.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(v -> {
            Navigation.findNavController(root).navigateUp();
        });

        signInButton = root.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.WEB_CLIENT)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mAuth = FirebaseAuth.getInstance();

        loadingPanel = root.findViewById(R.id.loadingPanel);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(Constants.LOG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(Constants.LOG, "Google sign in failed", e);
                mostrarMensaje("Fallo de autenticación " + e.getMessage());
                loadingPanel.setVisibility(View.GONE);
                updateUI(null);
            }
        } else {
            loadingPanel.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(((Activity) context), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Constants.LOG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Constants.LOG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(root, "Fallo de autenticación", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(final FirebaseUser account) {

        if (account != null) {
            //mFirebaseAnalytics.setUserProperty("usuario_email", account.getEmail());
            //FirebaseCrashlytics.getInstance().setCustomKey("usuario_email", account.getEmail());
            loadingPanel.setVisibility(View.GONE);
            signInButton.setVisibility(View.GONE);

            SingletonUser.getInstance(context).setData(account);

            mostrarMensaje("Bienvenido a SuitApp!");
            account.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                //Constants.token = task.getResult().getToken();
                                context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("TOKEN", task.getResult().getToken()).apply();

                            } else {
                                //Constantes.token = "NO hay token";
                            }
                            Log.i(Constants.LOG, "Cuenta seleccionada " + account.getDisplayName());
                            Log.i(Constants.LOG, task.getResult().getToken());
                            //callServiceLogin(account);

                        }
                    });
            Navigation.findNavController(root).navigateUp();

        } else {
            //if (!context.getSharedPreferences("_", MODE_PRIVATE).getString("EMAIL", "").equals(""))
            //    signOut(null);
            signInButton.setVisibility(View.VISIBLE);
            loadingPanel.setVisibility(View.GONE);
            Log.i(Constants.LOG, "La cuenta es null");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                loadingPanel.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.GONE);
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void mostrarMensaje(String mensaje) {
        Snackbar.make(loadingPanel, mensaje, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getResources().getColor(R.color.white))
                .setTextColor(context.getResources().getColor(R.color.black))
                .show();
    }
}