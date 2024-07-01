package com.anarul.skwhatsapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.anarul.skwhatsapp.Adapters.FragmentsAdapter;
import com.anarul.skwhatsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;
   FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();

         binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
         binding.tablayout.setupWithViewPager(binding.viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    //menu code add in main activity
        MenuInflater inflater=getMenuInflater();//inflate karna hai menu ko
        inflater.inflate(R.menu.menu,menu);//resource file.menu folder.nenu file
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                Intent i=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(i);
                 break;

            case R.id.logout:
                auth.signOut();//direct method provided in a.s
                Intent intent =new Intent(MainActivity.this,SignInActivity.class);//sign out ke bad kon face me control transfer hoga
                startActivity(intent);
                break;
            case R.id.groupChat:
                Intent intentt =new Intent(MainActivity.this,GroupChatActivity.class);//Main to group chat activity pe control transfer hoga
                startActivity(intentt);
                break;
        }
        return true;
    }
}