package com.example.tby.test2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tby.test2.tool.DataBase;
import com.example.tby.test2.tool.PrefencesView;
import com.example.tby.test2.tool.massage;


public class Choose extends AppCompatActivity {

    PrefencesView prefencesView;
    private SQLiteDatabase db;
    private String kind;


    private void init(){
        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        massage m= (massage) bundle.getSerializable("message");
        db= DataBase.db;
        kind=m.getMessage();
    }

    private String Response(){
        if(prefencesView!=null){
            if(kind.equals(prefencesView.getResult()))
                return "";
            else
                return prefencesView.getResult();
        }
        return "";
    }

    @Override
    protected void onStop() {
        super.onStop();
        prefencesView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        prefencesView= (PrefencesView) findViewById(R.id.prev);
        init();
        prefencesView.setIsBig(kind);
        Button b= (Button) findViewById(R.id.getButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefencesView.getResult().equals("")||Integer.parseInt(prefencesView.getResult().split("&")[0])>6){
                    Toast.makeText(Choose.this,"请至少选择一个类别已经存在的",Toast.LENGTH_SHORT).show();
                    return ;
                }
                Intent intent=new Intent(Choose.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("kind",Response());
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }
}
