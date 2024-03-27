package com.example.and103_lab5;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.and103_lab5.adapter.DistributorAdapter;
import com.example.and103_lab5.databinding.ActivityMainBinding;
import com.example.and103_lab5.databinding.DialogAddBinding;
import com.example.and103_lab5.model.CongTy;
import com.example.and103_lab5.model.Response;
import com.example.and103_lab5.services.HttpRequest;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements DistributorAdapter.DistributorClick {
    private ActivityMainBinding binding;
    private HttpRequest httpRequest;
    private ArrayList<CongTy> list = new ArrayList<>();
    private DistributorAdapter adapter;
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        TextView Load = findViewById(R.id.txtLoadLai);
        fetchAPI();
        userListener();
        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAPI();
            }
        });
    }

    private void fetchAPI() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        httpRequest = new HttpRequest();
        httpRequest.callAPI()
                .getData()
                .enqueue(getDistributorAPI);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 3000);


    }

    private void userListener() {
        binding.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String key = binding.edSearch.getText().toString().trim();
                    httpRequest.callAPI()
                            .searchCongTy(key)
                            .enqueue(getDistributorAPI);
                    Log.d(TAG, "onEditorAction: " + key);
                    return true;
                }
                return false;
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });
    }

    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();
        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding1.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "you must enter name", Toast.LENGTH_SHORT).show();
                } else {
                    CongTy congTy = new CongTy();
                    congTy.setName(name);
                    httpRequest.callAPI()
                            .addStudent(congTy)
                            .enqueue(responseDistributorAPI);
                    fetchAPI();
                    alertDialog.dismiss();
                    progressDialog.dismiss();

                }
            }
        });
        alertDialog.show();
    }


    public void getData() {
        adapter = new DistributorAdapter(list, this, this);
        binding.rcvDistributor.setAdapter(adapter);
        progressDialog.dismiss();
    }

    Callback<Response<ArrayList<CongTy>>> getDistributorAPI = new Callback<Response<ArrayList<CongTy>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<CongTy>>> call, retrofit2.Response<Response<ArrayList<CongTy>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    getData();
                    Log.d(TAG, "onResponse: " + list.size());
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<CongTy>>> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());
            progressDialog.dismiss();

        }


    };


    Callback<Response<CongTy>> responseDistributorAPI = new Callback<Response<CongTy>>() {
        @Override
        public void onResponse(Call<Response<CongTy>> call, retrofit2.Response<Response<CongTy>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    httpRequest.callAPI()
                            .getData()
                            .enqueue(getDistributorAPI);
                    fetchAPI();
                    Toast.makeText(MainActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<CongTy>> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());
            progressDialog.dismiss();

        }
    };

    private void showDialogEdit(CongTy congTy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit distributor");
        DialogAddBinding binding1 = DialogAddBinding.inflate(LayoutInflater.from(this));
        builder.setView(binding1.getRoot());
        AlertDialog alertDialog = builder.create();

        binding1.etName.setText(congTy.getName());

        binding1.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = congTy.getName();

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "you must enter name", Toast.LENGTH_SHORT).show();
                } else {

                    CongTy congTy1 = new CongTy();
                    congTy1.setName(binding1.etName.getText().toString().trim());
                    httpRequest.callAPI()
                            .updateStudent(congTy.getId(), congTy1)
                            .enqueue(responseDistributorAPI);
                    fetchAPI();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void delete(CongTy congTy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            httpRequest.callAPI()
                    .deleteStudent(congTy.getId())
                    .enqueue(responseDistributorAPI);
            fetchAPI();
        });
        builder.setNegativeButton("no", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();


    }

    @Override
    public void edit(CongTy congTy) {
        showDialogEdit(congTy);
    }
}

