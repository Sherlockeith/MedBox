package com.example.jwtapplication.module;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.example.jwtapplication.R;
import com.example.jwtapplication.dao.Data_dashboard;
import com.example.jwtapplication.dao.Message;
import com.example.jwtapplication.util.OkHttpUtils;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String account ;
    String jwt;
    String id;
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    Button re;
    public Fragment3() {
        // Required empty public constructor
    }
    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        new NetWork().execute();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            account=getArguments().getString("account");
            jwt=getArguments().getString("jwt");
            id=getArguments().getString("id");
        }
        progressDialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        listView=view.findViewById(R.id.list);
        re=view.findViewById(R.id.refresh);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Requesting");
        new NetWork().execute();
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NetWork().execute();
            }
        });
        //view.setBackgroundColor(R.color.b2);
        // Inflate the layout for this fragment
        return view;
    }
    OkHttpUtils okHttpUtils = new OkHttpUtils();

    String ResponseString;
    class NetWork extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String, String> map = new HashMap<>();
            map.put("username", account);
            map.put("medboxID",id);

            HashMap<String,String> head=new HashMap<>()                    ;
            head.put("jwt",jwt);

            try {
                ResponseString = okHttpUtils.PostResult("/logs/getLogs", map, head);
            } catch (Exception e) {
            }
            data=  JSON.parseObject(ResponseString, Data_dashboard.class);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Adapter adapter=new Adapter(getActivity(),R.layout.item,data.data)                    ;
            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
    Data_dashboard data;



    class Adapter extends ArrayAdapter<Message> {

        private int resourceId;
        private List<Message> objects;
        public Adapter(@NonNull Context context, int resource, @NonNull List<Message> objects) {
            super(context, resource, objects);
            resourceId=resource;
            this.objects=objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Message msg=objects.get(position);

            View view;
            Adapter.ViewHolder viewHolder;
            if (convertView==null){

                view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);


                viewHolder=new Adapter.ViewHolder();
                viewHolder.ID=view.findViewById(R.id.ID);
                viewHolder.txt=view.findViewById(R.id.txt);
                viewHolder.time=view.findViewById(R.id.time);


                view.setTag(viewHolder);
            } else{
                view=convertView;
                viewHolder=(Adapter.ViewHolder) view.getTag();
            }


            viewHolder.ID.setText(msg.medicine_name);
            switch (msg.taken){
                case "1":
                    viewHolder.ID.setBackground(getResources().getDrawable(R.drawable.background_1));
                    break;
                case "0":
                    viewHolder.ID.setBackground(getResources().getDrawable(R.drawable.background_2));
                    break;
                default:
            }
            viewHolder.txt.setText("Quantity:"+msg.quantity+"\nTime snoozed:"+msg.quantity);
            viewHolder.time.setText(msg.time_slot);
            return view;
        }


        class ViewHolder{
            TextView ID;
            TextView txt;
            TextView time;
        }
    }
}