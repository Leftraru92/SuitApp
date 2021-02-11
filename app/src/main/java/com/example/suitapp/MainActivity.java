package com.example.suitapp;

import android.app.Activity;
import android.content.Context;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suitapp.fragment.LoginFragment;
import com.example.suitapp.util.Constants;
import com.example.suitapp.util.SingletonUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    InputMethodManager imm;
    MenuItem logoutItem;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        toolbar = findViewById(R.id.toolbar);
        Toolbar subToolbar = findViewById(R.id.subToolbar);
        TextInputLayout etSearch = findViewById(R.id.etSearch);
        setSupportActionBar(toolbar);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //oculta el teclado

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_shopping, R.id.nav_notifications, R.id.nav_favs,
                R.id.nav_account, R.id.nav_categories, R.id.nav_stores, R.id.nav_article, R.id.nav_article_detail)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        logoutItem = navigationView.getMenu().findItem(R.id.nav_logout);
        logoutItem.setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener) item -> {
            signOut();
            updateMenu();
            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.action_move_to_home);
            DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer1.closeDrawer(GravityCompat.START);
            return true;
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                boolean isLogued = SingletonUser.getInstance(getBaseContext()).isLogued();
                updateMenu();

                if (!isLogued && (destination.getId() == R.id.nav_cart || destination.getId() == R.id.nav_account ||
                        destination.getId() == R.id.nav_favs || destination.getId() == R.id.nav_shopping)) {
                    controller.navigateUp();
                    destination = controller.getCurrentDestination();
                    mostrarMensaje();
                    Log.d(Constants.LOG, "No está loguado");
                }

                //muestro oculto toolbar
                if (destination.getId() == R.id.nav_login)
                    toolbar.setVisibility(View.GONE);
                else
                    toolbar.setVisibility(View.VISIBLE);

                //muestro oculto searchview
                if (destination.getId() == R.id.nav_home || destination.getId() == R.id.nav_article)
                    toolbar.findViewById(R.id.etSearch).setVisibility(View.VISIBLE);
                else
                    toolbar.findViewById(R.id.etSearch).setVisibility(View.GONE);

                //Muestro oculto subtoolbar
                if (destination.getId() == R.id.nav_article)
                    subToolbar.setVisibility(View.VISIBLE);
                else
                    subToolbar.setVisibility(View.GONE);

                //si el teclado esta visible se oculta al cambiar de pantalla
                if (imm.isAcceptingText() && getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void signOut() {
        Log.i(Constants.LOG, "Log Out");

        SingletonUser.getInstance(this).clearData();

        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.WEB_CLIENT)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        if (mGoogleSignInClient != null)
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    task -> Toast.makeText(getBaseContext(), "Se ha deslogueado", Toast.LENGTH_LONG).show());
        //throw new RuntimeException("Test Crash"); // Force a crash
    }

    private void updateMenu() {

        boolean isLogued = SingletonUser.getInstance(this).isLogued();
        //Bind header data
        if (navigationView == null)
            navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView navImage = (ImageView) headerView.findViewById(R.id.navImage);
        TextView navName = (TextView) headerView.findViewById(R.id.navName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navEmail);

        String email = SingletonUser.getInstance(this).getEmail();
        String name = SingletonUser.getInstance(this).getName();
        String image = SingletonUser.getInstance(this).getUri();

        if (isLogued) {
            logoutItem.setVisible(true);
            navName.setText("Hola " + name + "!");
            navEmail.setText(email);
            Picasso.with(MainActivity.this).load(image).into(navImage);
        } else {
            logoutItem.setVisible(false);
            navName.setText("Hola Usuario!");
            navEmail.setText("");
            navImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    public void mostrarMensaje() {
        Snackbar.make(toolbar, "Primero debe loguarse", Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.black))
                .setTextColor(getResources().getColor(R.color.white))
                .setAction("INGRESAR", v ->
                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment)
                                .navigate(R.id.action_move_to_login))
                .show();
    }
}