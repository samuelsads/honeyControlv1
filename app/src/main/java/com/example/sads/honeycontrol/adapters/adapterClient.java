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
import com.example.sads.honeycontrol.Utils.Util;
import com.example.sads.honeycontrol.activities.DashActivity;
import com.example.sads.honeycontrol.models.Client;
import com.example.sads.honeycontrol.service.ApiAdapter;
import com.example.sads.honeycontrol.service.response.ResponseClient;
import com.example.sads.honeycontrol.service.response.ResponseDeleteClient;
import com.example.sads.honeycontrol.service.response.ResponseUpdateCliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sads on 31/03/17.
 */
public class adapterClient extends RecyclerView.Adapter<adapterClient.ViewHolder> {

    private List<Client> client;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;
    private Activity activity;
    private String id="1";
    private String pass="aduLvDJ7Lk74c";
    private String name;
    private String father_surname;
    private String mother_surname;



    public adapterClient(List<Client> client, int layout, Activity activity, OnItemClickListener listener) {
        this.client = client;
        this.layout = layout;
        this.itemClickListener = listener;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        context = parent.getContext();
        ViewHolder vh  = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(client.get(position),itemClickListener);
    }


    @Override
    public int getItemCount() {
        return client.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewTitle);
            itemView.setOnCreateContextMenuListener(this);

        }

        public void bind(final Client client, final OnItemClickListener listener) {
            // this.TextViewName.setText(name);
            textViewName.setText(Util.formatName(client.getName(),client.getFather_surname(),client.getMother_surname()));
            //imageView.setImageResource(movies.getPoster());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClik(client, getAdapterPosition());
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Client myclient  = client.get(this.getAdapterPosition());
            contextMenu.setHeaderTitle(myclient.getName());
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
                    deleteMyClient(client.get(getAdapterPosition()).getId());
                    client.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Toast.makeText(activity,"El cliente fue eliminado con exito",Toast.LENGTH_LONG).show();
                    return  true;
                case R.id.edit:

                    showAlertForEditClient("Edit client","You can edit name, father and mother surname",client.get(getAdapterPosition()), getAdapterPosition());
                    return true;
                default:
                    return  false;

            }
        }
    }

    private void showAlertForEditClient(String title, String message, final Client client, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(title!=null) builder.setTitle(title);
        if(message!=null) builder.setMessage(message);
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialog_create_new_client,null);
        builder.setView(viewInflated);
        final EditText namePerson = (EditText) viewInflated.findViewById(R.id.createNewName);
        final EditText fatherSurname = (EditText) viewInflated.findViewById(R.id.createNewFatherSurname);
        final EditText motherSurname = (EditText) viewInflated.findViewById(R.id.createNewMotherSurname);
        namePerson.setText(client.getName());
        fatherSurname.setText(client.getFather_surname());
        motherSurname.setText(client.getMother_surname());
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = namePerson.getText().toString().trim();
                father_surname  = fatherSurname.getText().toString().trim();
                mother_surname  = motherSurname.getText().toString().trim();
                if(name.length()>0 && father_surname.length()>0) {

                    updateClienteById(id,pass,name,father_surname,mother_surname,client.getId(),position);

                }else {
                    Toast.makeText(activity, "El nombre es requerido", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }


    private void editClientAdapter(String name,String father, String mother, Client client){

        client.setName(name);
        client.setFather_surname(father);
        client.setMother_surname(mother);
        notifyDataSetChanged();
    }

        public interface OnItemClickListener{
            void onItemClik(Client client, int position);
        }


    private void updateClienteById(String id, String pass, String name, String father_surname, String mother_surname, int idClient,int position){

        Call<ResponseUpdateCliente> call = ApiAdapter.getApiService().updateCliente(id,pass,idClient,name,father_surname,mother_surname);
        call.enqueue( new ResponsableUpdateCliente(client.get(position)));
    }

     class ResponsableUpdateCliente implements Callback<ResponseUpdateCliente>{
        private  Client client;
         public ResponsableUpdateCliente(Client client) {
            this.client  = client;
         }

         @Override
         public void onResponse(Call<ResponseUpdateCliente> call, Response<ResponseUpdateCliente> response) {
            if(response.isSuccessful()){
                ResponseUpdateCliente updateClient =  response.body();
                if(updateClient.getRespuesta()== Constans.SUCCESS){
                    editClientAdapter(name,father_surname,mother_surname,  client);
                    Toast.makeText(context,"Los datos fueron actualizados con exito", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, ErrorResponse.errorConection(updateClient.getRespuesta()), Toast.LENGTH_LONG).show();
                }
            }
         }

         @Override
         public void onFailure(Call<ResponseUpdateCliente> call, Throwable t) {

         }
     }

    private void deleteMyClient(int idClient){
        Call<ResponseDeleteClient> call = ApiAdapter.getApiService().deleteClient(id,pass,idClient);
        call.enqueue( new ResponsableCallBack());
    }
    class ResponsableCallBack implements Callback<ResponseDeleteClient>{

        @Override
        public void onResponse(Call<ResponseDeleteClient> call, Response<ResponseDeleteClient> response) {
            if(response.isSuccessful()){
                ResponseDeleteClient responsable= response.body();
                if(responsable.getSuccess()==Constans.SUCCESS){
                    Toast.makeText(context,"Los datos fueron eliminados con exito", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,ErrorResponse.errorConection(responsable.getSuccess()), Toast.LENGTH_LONG).show();
                }
            }
        }


        @Override
        public void onFailure(Call<ResponseDeleteClient> call, Throwable t) {

        }
    }


}
