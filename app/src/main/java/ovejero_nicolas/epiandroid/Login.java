package ovejero_nicolas.epiandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private String login;
    private String password;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void makeRequestLogin(String path)
    {
        final TextView msg = (TextView) findViewById(R.id.message_co);

        final Context view = this;
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        msg.setText("Welcome in Epitech's intranet\nCurrent connexion");
                        try {
                            Intent intent = new Intent(view, principal.class);
                            intent.putExtra("token", response.getString("token"));
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 401)
                        {
                            msg.setText("The login and / or password are invalid.");
                        }
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void checkLogin(View view)
    {
        EditText logint = (EditText) findViewById(R.id.loginInput);
        EditText pass = (EditText) findViewById(R.id.PassInput);
        login = logint.getText().toString();
        password = pass.getText().toString();
        login = "ovejer_n";
        password = "n1JjiJYN";
        makeRequestLogin("login?login=" + login + "&password=" + password);
    }
}
