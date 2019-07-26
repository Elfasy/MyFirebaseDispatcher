package com.example.myfirebasedispatcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {
    Button btnSetScheduler, btnCancelScheduler;

    private String DISPATCHER_TAG = "mydispatcher";
    private String CITY = "Bandung";

    FirebaseJobDispatcher jobDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetScheduler = findViewById(R.id.btn_set_scheduler);
        btnSetScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDispatcher();
                AjatHelper.toast(MainActivity.this,"Dispatcher Created");
            }
        });

        btnCancelScheduler = findViewById(R.id.btn_cancel_scheduler);
        btnCancelScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDispatcher();
                AjatHelper.toast(MainActivity.this,"Dispatcher Cancelled");
            }
        });

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
    }

    public void startDispatcher(){
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString(MyJobService.EXTRAS_CITY,CITY);

        Job myJob =  jobDispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag(DISPATCHER_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0,1))
                .setReplaceCurrent(true)
                .setConstraints(
                        Constraint.ON_ANY_NETWORK,
                        Constraint.DEVICE_IDLE
                )
                .setExtras(myExtrasBundle)
                .build();
        jobDispatcher.mustSchedule(myJob);
    }

    public void cancelDispatcher(){
        jobDispatcher.cancel(DISPATCHER_TAG);
    }
}
