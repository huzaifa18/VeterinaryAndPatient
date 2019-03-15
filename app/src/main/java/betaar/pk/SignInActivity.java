package betaar.pk;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import betaar.pk.Models.Chat;
import betaar.pk.Preferences.Prefs;

import static java.lang.System.err;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton mSignInButton;
    private Button btnAnon;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseApp firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Contact");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SignInActivity.this ,R.color.colorBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();

        final String userName = Prefs.getEmailFromPref(SignInActivity.this);
        String password = Prefs.getPasswordFromPref(SignInActivity.this);
        AuthCredential credential = EmailAuthProvider.getCredential(userName, password);

        /*mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "CredentialExist:success");
                    FirebaseUser user = task.getResult().getUser();
                    Log.e("tag" , "User: "+user);
                    startActivity(new Intent(SignInActivity.this, ChatActivityMain.class));
                    finish();
                    //updateUI(user);
                } else {
                    Log.e(TAG, "CredentialExist:failure", task.getException());
                    Toast.makeText(SignInActivity.this, "CredentialExist Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });*/



        /*// Sign in existing user
        firebase.auth().signInWithEmailAndPassword(email, password)
                .catch(function(err) {
            // Handle errors
        });*/

    }

    private void init(){

        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        btnAnon = (Button) findViewById(R.id.bt_sign_in_anon);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Set click listeners
        mSignInButton.setOnClickListener(this);
        btnAnon.setOnClickListener(this);

    }

    private void handleFirebaseAuthResult(AuthResult authResult) {
        if (authResult != null) {
            // Welcome the user
            FirebaseUser user = authResult.getUser();
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

            // Go back to the main activity
            //startActivity(new Intent(this, ChatActivityMain.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.bt_sign_in_anon:
                //signInAnon();
                createEmail();
                break;
            default:
                return;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInAnon() {
        firebaseAuthAnon();
        //Intent signInAnonIntent = mFirebaseAuth.createUserWithEmailAndPassword(userName,password);

    }

    private void signInEmail() {

        final String userName = Prefs.getEmailFromPref(SignInActivity.this);
        String password = Prefs.getPasswordFromPref(SignInActivity.this);

        mFirebaseAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.e(TAG, "CredentialExist:success");
                    FirebaseUser user = task.getResult().getUser();
                    Log.e("tag" , "User: "+user);
                    startActivity(new Intent(SignInActivity.this, ChatActivityMain.class));
                    finish();
                } else {
                    Log.e(TAG, "CredentialExist:failure", task.getException());
                    Toast.makeText(SignInActivity.this, "CredentialExist Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createEmail(){

        final String userName = Prefs.getEmailFromPref(SignInActivity.this);
        String password = Prefs.getPasswordFromPref(SignInActivity.this);

        mFirebaseAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.e(TAG, "NewUserWithEmail:success");
                    FirebaseUser user = task.getResult().getUser();
                    user.getEmail();

                    signInEmail();


                } else {
                    Log.e(TAG, "NewUserWithEmail:failure", task.getException());
                    Toast.makeText(SignInActivity.this, "NewUserWithEmail Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                    signInEmail();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("tag" , "here is result : "+result);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthAnon(){

        Log.e("tag" , "Anon ");

        mFirebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Log.e("tag" , "User: "+user);
                            //startActivity(new Intent(SignInActivity.this, ChatActivityMain.class));
                            //finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

        String userName = Prefs.getEmailFromPref(SignInActivity.this);
        String password = Prefs.getPasswordFromPref(SignInActivity.this);
        AuthCredential credential = EmailAuthProvider.getCredential(userName, password);
        FirebaseUser prevUser = mFirebaseAuth.getCurrentUser();

        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.e("tag" , "User: "+user);
                            startActivity(new Intent(SignInActivity.this, ChatActivityMain.class));
                            finish();
                            //updateUI(user);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Link Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignInActivity.this, ChatActivityMain.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

}
