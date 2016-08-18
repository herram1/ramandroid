package com.example.herram.loginjoin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by herram on 2016-08-04.
 */
public class JoinActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String dbName="st_file.db";
    int dbVersion = 3;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    String tag="SQLite";
    String tableName="student";
    String test;
    String test1;
    String test2;

    EditText etName;
    EditText etEmail;
    EditText etPassword;
    TextView tv;
    CheckBox cb;

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private static final int REQUEST_READ_CONTACTS = 0;

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private static final String[] DUMMY_CREDENTIALS = new String[]{
                "foo@example.com:hello", "bar@example.com:world"
        };
        /**
         * Keep track of the login task to ensure we can cancel it if requested.
         */
        private UserLoginTask mAuthTask = null;

        // UI references.
        private AutoCompleteTextView mEmailView;
        private EditText mPasswordView;
        private View mProgressView;
        private View mLoginFormView;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_join);

            cb=(CheckBox)findViewById(R.id.checkBox);
            etName=(EditText)findViewById(R.id.name);
            etEmail=(EditText)findViewById(R.id.email);
            etPassword=(EditText)findViewById(R.id.password);
            tv=(TextView)findViewById(R.id.textView4);

            helper = new MySQLiteOpenHelper(
                    this,
                    dbName,
                    null,
                    dbVersion);

            try{
                db=helper.getWritableDatabase();
            } catch (SQLiteException e){
                e.printStackTrace();
                Log.e(tag,"데이터 베이스를 열 수 없음");
                finish();
            }
            // Set up the login form.
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            populateAutoComplete();

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cb.isChecked()) {
                        attemptLogin();
                    }
                    else {
                        Toast.makeText(JoinActivity.this, "체크박스에 체크를 해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }

        private void populateAutoComplete() {
            if (!mayRequestContacts()) {
                return;
            }

            getLoaderManager().initLoader(0, null, this);
        }

        private boolean mayRequestContacts() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true;
            }
            if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                            }
                        });
            } else {
                requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
            }
            return false;
        }

        /**
         * Callback received when a permissions request has been completed.
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            if (requestCode == REQUEST_READ_CONTACTS) {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    populateAutoComplete();
                }
            }
        }


        /**
         * Attempts to sign in or register the account specified by the login form.
         * If there are form errors (invalid email, missing fields, etc.), the
         * errors are presented and no actual login attempt is made.
         */
        private void attemptLogin() {



            if (mAuthTask != null) {
                return;
            }

            // Reset errors.
            mEmailView.setError(null);
            mPasswordView.setError(null);

            // Store values at the time of the login attempt.
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            String name = etName.getText().toString();
            String email1 = etEmail.getText().toString();
            String password1 = etPassword.getText().toString();
            String email2 = null;
            boolean cancel = false;
            View focusView = null;
            Cursor c = db.rawQuery("SELECT email FROM student WHERE email='"+email1+"'",null);
            Log.d(tag,"로그1");
            while (c.moveToNext()) {
                email2 = c.getString(0);
                Log.d(tag, "" + email2);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            c.close();
                if (email1.equals(email2)) {
                    mEmailView.setError("중복");
                }
                else {
                    // Check for a valid password, if the user entered one.
                    if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
                        mPasswordView.setError(getString(R.string.error_invalid_password));
                        focusView = mPasswordView;
                        cancel = true;
                    }

                    // Check for a valid email address.
                    if (TextUtils.isEmpty(email)) {
                        mEmailView.setError(getString(R.string.error_field_required));
                        focusView = mEmailView;
                        cancel = true;
                    } else if (!isEmailValid(email)) {
                        mEmailView.setError(getString(R.string.error_invalid_email));
                        focusView = mEmailView;
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
                        mAuthTask = new UserLoginTask(email, password);
                        mAuthTask.execute((Void) null);
                        insert(name, email1, password1);
                    }
                }
        }
        private boolean isEmailValid(String email) {
            //TODO: Replace this with your own logic
            return email.contains("@");
        }

        private boolean isPasswordValid(String password) {
            //TODO: Replace this with your own logic
            return password.length() > 5;
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
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(this,
                    // Retrieve data rows for the device user's 'profile' contact.
                    Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                            ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                    // Select only email addresses.
                    ContactsContract.Contacts.Data.MIMETYPE +
                            " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                    .CONTENT_ITEM_TYPE},

                    // Show primary email addresses first. Note that there won't be
                    // a primary email address if the user hasn't specified one.
                    ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            List<String> emails = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                emails.add(cursor.getString(ProfileQuery.ADDRESS));
                cursor.moveToNext();
            }

            addEmailsToAutoComplete(emails);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {

        }

        private interface ProfileQuery {
            String[] PROJECTION = {
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
            };

            int ADDRESS = 0;
            int IS_PRIMARY = 1;
        }


        private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
            //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(JoinActivity.this,
                            android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

            mEmailView.setAdapter(adapter);
        }

        /**
         * Represents an asynchronous login/registration task used to authenticate
         * the user.
         */
        public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

            private final String mEmail;
            private final String mPassword;

            UserLoginTask(String email, String password) {
                mEmail = email;
                mPassword = password;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                // TODO: attempt authentication against a network service.

                try {
                    // Simulate network access.
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return false;
                }
                finally {
                    Intent intent=new Intent(
                            getApplicationContext(),
                            LoginActivity.class);
                    startActivity(intent);
                }

                for (String credential : DUMMY_CREDENTIALS) {
                    String[] pieces = credential.split(":");
                    if (pieces[0].equals(mEmail)) {
                        // Account exists, return true if the password matches.
                        return pieces[1].equals(mPassword);
                    }
                }

                // TODO: register the new account here.
                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                mAuthTask = null;
                showProgress(false);

                if (success) {
                    finish();
                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }

            @Override
            protected void onCancelled() {
                mAuthTask = null;
                showProgress(false);
            }

        }
        void insert (String name, String email, String password){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("email",email);
        values.put("password",password);
        long result = db.insert(tableName,null,values);
        tv.setText(result+"--회원가입성공");
        //select();
    }
    void select(){
        Cursor c = db.query(tableName,null,null,null,null,null,null);
        while(c.moveToNext()){
            int _id=c.getInt(0);
            String name = c.getString(1);
            String email = c.getString(2);
            String password = c.getString(3);
            tv.setText("\n"+"_id : "+_id+", name : "+name+", email : "+email+", password : "+password);
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
