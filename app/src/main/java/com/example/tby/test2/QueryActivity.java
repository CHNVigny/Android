package com.example.tby.test2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.tby.test2.tool.DataBase;

public class QueryActivity extends Activity {
	Cursor cursor;
	ListView listView;
	ImageButton imageButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect);

		String[] from = {"title","content","src"};
		cursor=DataBase.db.query("collection", null, null, null, null, null, null);
		int[] to = {R.id.tv1,R.id.tv2,R.id.tv3};
		SimpleCursorAdapter adapter =
				new SimpleCursorAdapter(this, R.layout.itemnull, cursor, from, to);

		listView= (ListView)findViewById(R.id.listview1);
		if(adapter!=null)
			listView.setAdapter(adapter);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		imageButton=(ImageButton) findViewById(R.id.imgBtnBack);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent in=new Intent(QueryActivity.this,webView.class);
				//Log.d(position+"",beanList.get(position).url);
				Bundle b=new Bundle();
				cursor.moveToPosition(position);
				b.putString("url",cursor.getString(cursor.getColumnIndex("url")));
				in.putExtras(b);
				QueryActivity.this.startActivity(in);
			}
		});

		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				// TODO Auto-generated method stub
				final long temp = arg3;
				final int position=arg2;
				builder.setMessage("真的要删除吗").
						setPositiveButton("是", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								cursor.moveToPosition(position);
								DataBase.db.delete("collection","_id=?",new String[]{""+cursor.getInt(cursor.getColumnIndex("_id"))});
								cursor=DataBase.db.query("collection", null, null, null, null, null, null);
								String[] from = {"title","content","src"};
								int[] to = {R.id.tv1,R.id.tv2,R.id.tv3};
								SimpleCursorAdapter adapter =
										new SimpleCursorAdapter(getApplicationContext(), R.layout.itemnull, cursor, from, to);
								listView.setAdapter(adapter);
							}
						}).setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				AlertDialog ad  = builder.create();
				ad.show();
				return true;
			}
		});
	}
}

	   
	   
	   
	   
	   
	   
	   