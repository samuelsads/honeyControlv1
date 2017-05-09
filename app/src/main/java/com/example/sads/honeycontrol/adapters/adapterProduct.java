package com.example.sads.honeycontrol.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sads.honeycontrol.R;
import com.example.sads.honeycontrol.Utils.Constans;
import com.example.sads.honeycontrol.Utils.ErrorResponse;
import com.example.sads.honeycontrol.models.Client;
import com.example.sads.honeycontrol.models.Products;
import com.example.sads.honeycontrol.service.ApiAdapter;
import com.example.sads.honeycontrol.service.response.ResponseDeleteClient;
import com.example.sads.honeycontrol.service.response.ResponseDeleteProduct;
import com.example.sads.honeycontrol.service.response.ResponseUpdateProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sads on 14/04/17.
 */
public class adapterProduct  extends RecyclerView.Adapter<adapterProduct.ViewHolder> {
    private List<Products> product;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;
    private Activity activity;
    private String id="1";
    private String pass="aduLvDJ7Lk74c";
    private String priceUpdate;
    private String sizeUpdate;

    public adapterProduct(List<Products> product, int layout, Activity activity, OnItemClickListener listener) {
        this.product = product;
        this.layout = layout;
        this.itemClickListener = listener;
        this.activity = activity;

    }

    @Override
    public adapterProduct.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        context = parent.getContext();
        ViewHolder vh  = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(adapterProduct.ViewHolder holder, int position) {
        holder.bind(product.get(position),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewPrice;
        public TextView textViewSize;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textViewSize = (TextView) itemView.findViewById(R.id.textViewSize);
            itemView.setOnCreateContextMenuListener(this);

        }

        public void bind(final Products product, final OnItemClickListener listener) {
            // this.TextViewName.setText(name);
            textViewPrice.setText(product.getPrice());
            textViewSize.setText(product.getSize());
            //imageView.setImageResource(movies.getPoster());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClik(product, getAdapterPosition());
                }
            });
        }

        private void showAlertForEditClient(String title, String message, final Products product, final int position){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if(title!=null) builder.setTitle(title);
            if(message!=null) builder.setMessage(message);
            View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_create_new_product,null);
            builder.setView(viewInflated);
            final EditText size = (EditText) viewInflated.findViewById(R.id.createNewSize);
            final EditText price = (EditText) viewInflated.findViewById(R.id.createNewPrice);
            size.setText(product.getSize());
            price.setText(product.getPrice());
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sizeUpdate = size.getText().toString().trim();
                    priceUpdate  = price.getText().toString().trim();

                    if(sizeUpdate.length()>0 && priceUpdate.length()>0) {
                        updateProduct(sizeUpdate,priceUpdate,product.getId(),position);
                       // updateProducById(id,pass,size,price,product.getId(),position);

                    }else {
                        Toast.makeText(activity, "El nombre es requerido", Toast.LENGTH_LONG).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel",null);
            builder.create().show();
        }



        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Products myproducts  = product.get(this.getAdapterPosition());
            contextMenu.setHeaderTitle(myproducts.getSize());
            MenuInflater inflater  = activity.getMenuInflater();
            inflater.inflate(R.menu.menu_options,contextMenu);
            for (int i =0; i<contextMenu.size();i++){
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.delete:
                   deleteMyProduct(product.get(this.getAdapterPosition()).getId());
                    product.remove(this.getAdapterPosition());
                    notifyItemRemoved(this.getAdapterPosition());
                    Toast.makeText(activity,"eliminar el id ",Toast.LENGTH_LONG).show();
                    return  true;
                case R.id.edit:
                    showAlertForEditClient("Edit product","Change size and price in products",product.get(getAdapterPosition()),getAdapterPosition());
                    return true;
                default:
                    return  false;

            }
        }
    }

    private void updateProduct(String size, String price, int idProduct, int position){
        Call<ResponseUpdateProduct> call = ApiAdapter.getApiService().updateProduct(id,pass,size,price,idProduct);
        call.enqueue( new ResponseUpdateProducts(product.get(position)));
    }

    class ResponseUpdateProducts implements Callback<ResponseUpdateProduct>{
        Products product;
        public ResponseUpdateProducts(Products product) {
            this.product = product;
        }

        @Override
        public void onResponse(Call<ResponseUpdateProduct> call, Response<ResponseUpdateProduct> response) {
            if(response.isSuccessful()){
                editProductAdapter(sizeUpdate,priceUpdate,product);
            }
        }

        @Override
        public void onFailure(Call<ResponseUpdateProduct> call, Throwable t) {

        }
    }

    private void editProductAdapter(String size,String price, Products product){
        product.setSize(size);
        product.setPrice(price);
        notifyDataSetChanged();
    }

    private void deleteMyProduct(int idProduct){
        Call<ResponseDeleteProduct> call = ApiAdapter.getApiService().deleteProduct(id,pass,idProduct);
        call.enqueue( new ResponsableCallBack());
    }

    class ResponsableCallBack implements Callback<ResponseDeleteProduct>{

        @Override
        public void onResponse(Call<ResponseDeleteProduct> call, Response<ResponseDeleteProduct> response) {
            if(response.isSuccessful()){
                ResponseDeleteProduct deletes = response.body();
                if(deletes.getSuccess()== Constans.SUCCESS){
                    Toast.makeText(context,"Se ha elimiado el producto con exito",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, ErrorResponse.errorConection(deletes.getSuccess()),Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseDeleteProduct> call, Throwable t) {

        }
    }
    public interface OnItemClickListener{
        void onItemClik(Products product, int position);
    }
}
