package com.sc.app6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText=null;
    Button button=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                }else{
                    call();
                }
            }
        });

    }

    public void call(){
        String s=editText.getText().toString();
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:"+s));
        startActivity(intent);

       /* getContentResolver().query(Uri.parse("content:com.sc.app6.provider/table1"),
                new String[]{"name","pass","age"},
                "id=?",
                new String[]{"1001"},
                "id desc");*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                call();
            }else{
                Toast.makeText(MainActivity.this,"您已拒绝拨打电话权限，无法拨打电话！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
