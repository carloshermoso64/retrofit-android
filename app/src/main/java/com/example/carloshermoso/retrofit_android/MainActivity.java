package com.example.carloshermoso.retrofit_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Button bt = (Button) findViewById(R.id.button);
        final TextView tv = (TextView) findViewById(R.id.textView);
        final EditText et = (EditText) findViewById(R.id.editText1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

                GitHubService service = retrofit.create(GitHubService.class);
                

                    final String user = et.getText().toString();


                    Call<List<Repo>> repos = service.listRepos(user);


                    repos.enqueue(new Callback<List<Repo>>() {
                        @Override
                        public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                            List<String> input = new ArrayList<>();
                            List<Repo> repos = response.body();

                            try{
                                for (Repo r : repos) {

                                    input.add(r.full_name);

                                }            } catch (NullPointerException e) {
                                tv.setText("El usuario introducido es incorrecto");
                            }
                            mAdapter = new MyAdapter(input);
                            recyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<Repo>> call, Throwable t) {
                            tv.setText("Fallo");
                        }
                    });

            }
        });


    }
}
