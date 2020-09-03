package com.fawzy.waitlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
   private RecyclerView recyclerView ;
   private FloatingActionButton floatingActionButton ;
   private Adapter adapter ;
   private Cursor cursor ;
   private ImageView imageView ;
   private Wait_DB wait_db ;
   private SQLiteDatabase sqLiteDatabase ;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                  //  Toast.makeText(MainActivity.this, "loaded", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                 //  Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                //  Toast.makeText(MainActivity.this, "opened", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }
            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }
            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        recyclerView = findViewById(R.id.recycler);
        floatingActionButton = findViewById(R.id.floating);
        imageView = findViewById(R.id.img);

        wait_db = new Wait_DB(getApplicationContext());
        sqLiteDatabase = wait_db.getWritableDatabase();
        cursor = getAllGuests();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,cursor);
        recyclerView.setAdapter(adapter);

        if (cursor.getCount() != 0){
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialogue();
            }
        });

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);




    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
              long id = (long)viewHolder.itemView.getTag();
              removeGuest(id);
              adapter.SwapCursor(getAllGuests());
            if (getAllGuests().getCount() == 0)
            {
                imageView.setVisibility(View.VISIBLE);
            }
        }
    };

    private void showCustomDialogue() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.guest_dialogue);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT ;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT ;

         final EditText guest_name = dialog.findViewById(R.id.guest_name);
         final EditText number = dialog.findViewById(R.id.size_number);
        final Button add = dialog.findViewById(R.id.add);
        final Button back = dialog.findViewById(R.id.back);
        final Toast toast = new Toast(this);

        final View view = getLayoutInflater().inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = guest_name.getText().toString() ;
                String Number = number.getText().toString() ;

                if (Name.length() == 0|| Number.length() == 0){
                     toast.setDuration(Toast.LENGTH_LONG);
                     toast.setView(view);
                     toast.show();
                }else {
                    AddnewGuest(Name,Number);
                    adapter.SwapCursor(getAllGuests());
                    imageView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
               toast.cancel();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    private long AddnewGuest(String name, String number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WaitListContract.WaitListEntry.COULUMN_GUEST_NAME,name);
        contentValues.put(WaitListContract.WaitListEntry.COULUMN_PARTY_SIZE,number);
          return sqLiteDatabase.insert(WaitListContract.WaitListEntry.TABLE_NAME,null,contentValues);
    }

    private boolean removeGuest(long id){
        return sqLiteDatabase.delete(WaitListContract.WaitListEntry.TABLE_NAME,
                WaitListContract.WaitListEntry._ID + "=" + id, null) > 0;
    }


    private Cursor getAllGuests() {
        return sqLiteDatabase.query(
                WaitListContract.WaitListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitListContract.WaitListEntry.COULUMN_TIMESTAP
        );
    }
}