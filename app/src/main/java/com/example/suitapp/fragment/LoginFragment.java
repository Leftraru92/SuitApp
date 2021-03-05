package com.example.suitapp.fragment;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.example.suitapp.R;
import com.example.suitapp.activity.MainActivity;
import com.example.suitapp.api.CallWebService;
import com.example.suitapp.api.WebService;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SHA1;
import com.example.suitapp.util.SingletonUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener, CallWebService {
    private static FirebaseAuth mAuth;
    View root;
    static Context context;
    static RelativeLayout loadingPanel;
    static SignInButton signInButton;
    private static GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1, RC_LOGIN = 2;


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

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                String fbToken = task.getResult();
                Log.d(Constants.LOG, "Token device " + fbToken);
                SingletonUser.getInstance(context).setTokenDevice(fbToken);
            }
        }});

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
            callWs();

        } else {
            signInButton.setVisibility(View.VISIBLE);
            loadingPanel.setVisibility(View.GONE);
            Log.i(Constants.LOG, "La cuenta es null");
        }
    }

    private void callWs() {
        WebService webService = new WebService(getContext(), RC_LOGIN);

        JSONObject body = getLoginBody();

        webService.callService(this, Constants.WS_DOMINIO + Constants.WS_LOGIN, null, Request.Method.POST, Constants.JSON_TYPE.OBJECT, body);
    }

    private JSONObject getLoginBody() {
        SingletonUser mUser = SingletonUser.getInstance(context);
        try {
            String hash = SHA1.toSHA1(mUser.getEmail());
            mUser.setHash(hash);
        }catch (UnsupportedEncodingException ex){

        }catch (NoSuchAlgorithmException ex){

        }

        try {
            JSONObject body = new JSONObject();
            body.put("email", mUser.getEmail());
            body.put("device_id", mUser.getDeviceId());
            body.put("project_id", Constants.SERVICE_ID);
            body.put("nombre", mUser.getName());

            return body;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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

    @Override
    public void onResult(int requestCode, JSONObject response) {
        mostrarMensaje("Bienvenido a SuitApp!");
        Navigation.findNavController(root).navigateUp();

    }

    @Override
    public void onResult(int requestCode, JSONArray response) {

    }

    @Override
    public void onError(int requestCode, String message) {
        mostrarMensaje("Error: " + message);
        signInButton.setVisibility(View.VISIBLE);
        loadingPanel.setVisibility(View.GONE);
       //((MainActivity)context).signOut(false);
    }
}