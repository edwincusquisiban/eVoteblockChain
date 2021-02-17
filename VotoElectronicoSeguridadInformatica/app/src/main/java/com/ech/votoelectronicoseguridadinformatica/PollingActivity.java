package com.ech.votoelectronicoseguridadinformatica;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PollingActivity extends AppCompatActivity {
    private List<Candidate> Candidates;
    private AppAdapter candidatesAdapter;
    private ListView candidateListView;
    private TextView textView;
    private Button button;
    private String candidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);
        button=findViewById(R.id.btn_endPolling);
        candidateListView = (ListView) findViewById(R.id.candidates_list);
        Candidates = getCandidates();
        candidatesAdapter = new AppAdapter(PollingActivity.this, Candidates);
        candidateListView.setAdapter(candidatesAdapter);
        textView=findViewById(R.id.candidate_choosen);
        candidateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                textView.setText("Elección :   "+Candidates.get(i).getName());
                candidate=Candidates.get(i).getName();



            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ScreenLockCredencials();


                    }
                }).start();

            }
        });



    }





    @SuppressLint("UseCompatLoadingForDrawables")
    public List<Candidate> getCandidates() {
        List<Candidate> candidatelist = new ArrayList<Candidate>();
        candidatelist.add(new Candidate("CANDIDATE A", getApplicationContext().getResources().getDrawable(R.drawable.ic_candidate_icon, null),""));
        candidatelist.add(new Candidate("CANDIDATE B", getApplicationContext().getResources().getDrawable(R.drawable.ic_candidate_icon, null),""));
        candidatelist.add(new Candidate("CANDIDATE C", getApplicationContext().getResources().getDrawable(R.drawable.ic_candidate_icon, null),""));
        candidatelist.add(new Candidate("CANDIDATE D", getApplicationContext().getResources().getDrawable(R.drawable.ic_candidate_icon, null),""));
        candidatelist.add(new Candidate("CANDIDATE E", getApplicationContext().getResources().getDrawable(R.drawable.ic_candidate_icon, null),""));
        return candidatelist;
    }



    public class AppAdapter extends BaseAdapter implements Filterable {

        public LayoutInflater layoutInflater;
        public List<Candidate> listStorage;
        private ValueFilter valueFilter;


        public AppAdapter(Context context, List<Candidate> customizedListView) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
            getFilter();
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }

            return valueFilter;
        }


        @Override
        public int getCount() {
            return listStorage.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ValueFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                listStorage = getCandidates();
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<Candidate> filterList = new ArrayList<Candidate>();
                    for (int i = 0; i < listStorage.size(); i++) {
                        if ((listStorage.get(i).getName().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            Candidate contacts = new Candidate();
                            contacts.setName(listStorage.get(i).getName());
                            contacts.setIcon(listStorage.get(i).getIcon());
                            contacts.setDescription(listStorage.get(i).getDescription());
                            filterList.add(contacts);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = listStorage.size();
                    results.values = listStorage;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listStorage = (ArrayList<Candidate>) results.values;
                notifyDataSetChanged();
            }

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder listViewHolder;
            if (convertView == null) {
                listViewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.candidates_item, parent, false);

                listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.candidate_name);
                listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.candidate_icon);

                listViewHolder.PackageInListView = (TextView) convertView.findViewById(R.id.candidate_description);

                convertView.setTag(listViewHolder);
            } else {
                listViewHolder = (ViewHolder) convertView.getTag();
            }
            listViewHolder.textInListView.setText(listStorage.get(position).getName());
            listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
            listViewHolder.PackageInListView.setText(listStorage.get(position).getDescription());
            return convertView;
        }

        private class ViewHolder {
            TextView textInListView;
            ImageView imageInListView;

            TextView PackageInListView;

        }
    }

    public void ScreenLockCredencials(){
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        assert km != null;
        if (km.isDeviceSecure()) {
            // finish();
            Intent authIntent = km.createConfirmDeviceCredentialIntent("Por favor Autentifíquese", "Ingrese sus credenciales");
            startActivityForResult(authIntent,100);
        }else {



        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                MainActivity.block.addTransaction(candidate);
                Toast.makeText(getApplicationContext(), "Voto registrado  ", Toast.LENGTH_SHORT).show();
                textView.setText("Elección :   ");
            }
            else {
                ScreenLockCredencials();
            }
        }
    }



}