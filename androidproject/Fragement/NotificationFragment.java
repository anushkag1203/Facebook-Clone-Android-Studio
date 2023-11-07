package com.example.androidproject.Fragement;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidproject.Adapter.NotificationAdapter;
import com.example.androidproject.Model.Notification;
import com.example.androidproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Notification> list;

    FirebaseDatabase database;
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            database=FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.notification2RV);
        list=new ArrayList<>();

        // convert profile to deaf
/*        list.add(new NotificationModel(R.drawable.profile,"<b>Mayuri Gadhave</b> mention you in comment: nice try","just now"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Anushka</b>  Commented on your post","1 hour"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Shivanjali</b> liked your post","2 hour"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Sakshi</b> liked your post","3 hour"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Mayuri</b> liked your post","4 hour"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Shivanjali</b> commented on your photo","5 hour"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Shivanjali</b> commented on your photo","6 hour"));

        list.add(new NotificationModel(R.drawable.profile,"<b>Shivanjali</b> commented on your photo","7 hour"));
        list.add(new NotificationModel(R.drawable.profile,"<b>Mayuri Gadhave</b> mention you in comment: nice try","just now"));*/



        NotificationAdapter adapter=new NotificationAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference().child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            Notification notification=dataSnapshot.getValue(Notification.class);
                            notification.setNotificationId(dataSnapshot.getKey());
                            list.add(notification);

                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}