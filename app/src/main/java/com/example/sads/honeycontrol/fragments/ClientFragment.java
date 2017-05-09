package com.example.sads.honeycontrol.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sads.honeycontrol.R;
import com.example.sads.honeycontrol.Utils.Constans;
import com.example.sads.honeycontrol.Utils.ErrorResponse;
import com.example.sads.honeycontrol.adapters.adapterClient;
import com.example.sads.honeycontrol.models.Client;
import com.example.sads.honeycontrol.service.ApiAdapter;
import com.example.sads.honeycontrol.service.response.ResponseClient;
import com.example.sads.honeycontrol.service.response.ResponseInsertClient;
import com.example.sads.honeycontrol.service.response.ResponseLogin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClientFragment extends Fragment {
    private List<Client> client;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayout;
    private int cont =0;
    private String id ;
    private String  pass;
    private FloatingActionButton btnAddClient;
    private String name;
    private String father_surname;
    private String mother_surname;

    public ClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        id=getArguments().getString("id");
        pass=getArguments().getString("pass");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.reciclerViewClient);
        mLayout  = new LinearLayoutManager(getActivity());
        //client  = this.getAllMovies();
        getMyClients(id,pass);
        btnAddClient = (FloatingActionButton) view.findViewById(R.id.FabAddClient);
        btnAddClient.setOnClickListener(addClient);
        return view;
    }

    private View.OnClickListener addClient = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showAlertForCreatigBoard("Add new client","Hello :)");
        }
    };

    private void viewClient(ArrayList<Client> myClient){
        client =myClient;
        mAdapter = new adapterClient(client, R.layout.recycler_view_item_client, getActivity(), new adapterClient.OnItemClickListener() {
            @Override
            public void onItemClik(Client client, int position) {
                //deleteMovie(position);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayout);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getMyClients(String id,String pass){

        Call<ResponseClient> call = ApiAdapter.getApiService().getClient(id,pass);
        call.enqueue( new ResponsableCallBack());

    }

    class ResponsableCallBack implements Callback<ResponseClient> {
        @Override
        public void onResponse(Call<ResponseClient> call, Response<ResponseClient> response) {
            if(response.isSuccessful()){
                ResponseClient responsable= response.body();
                if(responsable.isSuccess()){
                    responsable.getRespuesta();
                    viewClient(responsable.getRespuesta());
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseClient> call, Throwable t) {

        }
    }

    private void showAlertForCreatigBoard(String title, String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if(title!=null) builder.setTitle(title);
        if(message!=null) builder.setMessage(message);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_new_client,null);
        builder.setView(viewInflated);
        final EditText input = (EditText) viewInflated.findViewById(R.id.createNewName);
        final EditText fatherSurname = (EditText) viewInflated.findViewById(R.id.createNewFatherSurname);
        final EditText motherSurname = (EditText) viewInflated.findViewById(R.id.createNewMotherSurname);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 name = input.getText().toString().trim();
                 father_surname  = fatherSurname.getText().toString().trim();
                 mother_surname  = motherSurname.getText().toString().trim();
                if(name.length()>0 && fatherSurname.length()>0) {
                    getDataMyClient(id, pass, name,father_surname,mother_surname);
                }else {
                    Toast.makeText(getContext(), "El nombre es requerido", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }

    private void getDataMyClient(String id, String pass, String name, String father, String mother){
        Call<ResponseInsertClient> call = ApiAdapter.getApiService().insertClient(id,pass,name,father,mother);
        call.enqueue( new InsertCallBack());
    }


    class InsertCallBack implements Callback<ResponseInsertClient>{

        @Override
        public void onResponse(Call<ResponseInsertClient> call, Response<ResponseInsertClient> response) {
            ResponseInsertClient responsable= response.body();

            if(response.isSuccessful()){
                if(responsable.getSuccess()==Constans.SUCCESS){
                    addNewClient(responsable.getId(),name,father_surname,mother_surname);
                    Toast.makeText(getContext(), "Todo un exito", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), ErrorResponse.errorConection(responsable.getSuccess()), Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(getContext(), "Ups, hubo un error...", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseInsertClient> call, Throwable t) {

        }
    }

    public void addNewClient(int id, String name, String father_surname, String mother_surname){
        client.add(0, new Client(id,name,father_surname, mother_surname));
        mAdapter.notifyItemInserted(0);
        mLayout.scrollToPosition(0);
    }
}
