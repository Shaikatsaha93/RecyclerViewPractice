package com.example.dinar.recyclerviewpractice;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dinar.recyclerviewpractice.data.Contact;
import com.example.dinar.recyclerviewpractice.data.MyContacts;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
//override method
    RecyclerView rcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcv = findViewById(R.id.my_list);

        new MyAsyncClass().execute();
        }


//metode
    String getDataFromApi(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
//adapter class
    public class MyAdapter extends RecyclerView.Adapter{

        List<Contact> myList;

        public MyAdapter(List<Contact> myList)
        {
            this.myList = myList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            View v = li.inflate(R.layout.list_item, viewGroup, false);
            MyHolder mh = new MyHolder(v);
            return mh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MyHolder  myHolder = (MyHolder) viewHolder;
            myHolder.bindView(i);
        }
        @Override
        public int getItemCount()
        {
            return myList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            TextView tx;
            TextView tx2;
            TextView tx3;
            ImageView iv;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tx =  itemView.findViewById(R.id.text_view1);
                tx2 =  itemView.findViewById(R.id.text_view2);
                iv =  itemView.findViewById(R.id.image);
                tx3=itemView.findViewById(R.id.text_view3);
            }

            public void bindView(int pos){
                tx.setText(myList.get(pos).getName());
                tx2.setText(myList.get(pos).getPhone().getMobile());
            }
        }
    }

//Async class
    public class MyAsyncClass extends AsyncTask{

    String output;
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            output = getDataFromApi("https://api.androidhive.info/contacts/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        MyContacts myContacts = new Gson().fromJson(output, MyContacts.class);

        rcv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rcv.setAdapter(new MyAdapter(myContacts.getContacts()));
    }
}
}
