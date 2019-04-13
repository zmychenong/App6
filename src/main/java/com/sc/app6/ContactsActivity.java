package com.sc.app6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {
    List<Map<String,String>> list=new ArrayList<>();
    SimpleAdapter simpleAdapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);




        ListView listView=findViewById(R.id.listView);
        simpleAdapter=new SimpleAdapter(ContactsActivity.this,
                list,
                R.layout.acticity_contacts_item,
                new String[]{"name","number"},
                new int[]{R.id.textView,R.id.textView2});
        listView.setAdapter(simpleAdapter);



        //申请权限
        if(ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactsActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            readContacts();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                    call(list.get(position).get("number"));
                }
            }
        });
    }

    public void call(String number){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);

    }

    public  void readContacts(){
        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(cursor!=null&&cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Map<String,String> map=new HashMap<>();
            map.put("name",name);
            map.put("number",number);
            list.add(map);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("perssion", Arrays.toString(grantResults));
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                readContacts();
            }else{
                Toast.makeText(ContactsActivity.this,"您已拒绝读取联系人权限，无法读取联系人！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
