package com.example.sads.honeycontrol.fragments;


import android.content.DialogInterface;
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
import com.example.sads.honeycontrol.adapters.adapterClient;
import com.example.sads.honeycontrol.adapters.adapterProduct;
import com.example.sads.honeycontrol.models.Client;
import com.example.sads.honeycontrol.models.Products;
import com.example.sads.honeycontrol.service.ApiAdapter;
import com.example.sads.honeycontrol.service.response.ResponseClient;
import com.example.sads.honeycontrol.service.response.ResponseInsertClient;
import com.example.sads.honeycontrol.service.response.ResponseInsertProduct;
import com.example.sads.honeycontrol.service.response.ResponseProduct;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {
    private List<Products> products;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayout;
    private String id="1";
    private String pass="aduLvDJ7Lk74c";
    private FloatingActionButton btnAddProduct;
    private String insertPrice;
    private String insertSize;
    private String insertamount;


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.reciclerViewProduct);
        mLayout  = new LinearLayoutManager(getActivity());
        getMyProducts(id,pass);
        btnAddProduct = (FloatingActionButton) view.findViewById(R.id.FabAddProduct);
        btnAddProduct.setOnClickListener(addProduct);
        return view;
    }

    private View.OnClickListener addProduct  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showAlertForCreatigBoard("Add new product", "");
        }
    };

    private void showAlertForCreatigBoard(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if(title!=null) builder.setTitle(title);
        if(message!=null) builder.setMessage(message);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_new_product,null);
        builder.setView(viewInflated);
        final EditText size = (EditText) viewInflated.findViewById(R.id.createNewSize);
        final EditText price = (EditText) viewInflated.findViewById(R.id.createNewPrice);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String price1 = price.getText().toString().trim();
                String size1  = size.getText().toString().trim();
                if(price.length()>0 && price1.length()>0 && size1.length()>0) {
                    //createNewBoard(boardName);
                    insertProduct(id,pass, size1,price1);
                }else {
                    Toast.makeText(getContext(), "Tiene campos vacios", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }

    public void insertProduct(String id, String pass, String size, String price){
        insertPrice= price;
        insertSize  = size;
        Call<ResponseInsertProduct> call = ApiAdapter.getApiService().insertProduct(id,pass,size,price);
        call.enqueue( new InsertProductCallBack());
    }

    class InsertProductCallBack implements Callback<ResponseInsertProduct>{

        @Override
        public void onResponse(Call<ResponseInsertProduct> call, Response<ResponseInsertProduct> response) {
            if(response.isSuccessful()){
                 ResponseInsertProduct insertProduct = response.body();
                if(insertProduct.getSuccess()== Constans.SUCCESS){
                    addNewProduct(insertProduct.getId(),insertPrice,insertSize);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseInsertProduct> call, Throwable t) {

        }
    }

    public void addNewProduct(int id,String size,String price){
        products.add(0, new Products(id,size,price));
        mAdapter.notifyItemInserted(0);
        mLayout.scrollToPosition(0);
    }


    public void viewProducts(ArrayList<Products> product){
        products = product;
        mAdapter = new adapterProduct(products, R.layout.recycler_view_item_product, getActivity(), new adapterProduct.OnItemClickListener() {
            @Override
            public void onItemClik(Products product, int position) {
                //deleteMovie(position);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayout);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void getMyProducts(String id, String pass){
        Call<ResponseProduct> call = ApiAdapter.getApiService().getProduct(id,pass);
        call.enqueue( new ResponsableCallBack());
    }


    class ResponsableCallBack implements Callback<ResponseProduct>{

        @Override
        public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
            if(response.isSuccessful()){
                ResponseProduct product  = response.body();
                if(product.isSuccess()){
                    viewProducts(product.getRespuesta());
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseProduct> call, Throwable t) {

        }
    }

}
