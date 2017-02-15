package com.arloor.noter3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arloor.noter3.model.Note;
import com.arloor.noter3.model.RecyclerNoteAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static List<Note> notes = new ArrayList<>();
    private static EditText noteContent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Note note = new Note();

            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat ("HH:mm:ss");
            String time = formatter.format(new Date());

            note.setTime(time);
            note.setTheme(data.getStringExtra("theme"));
            note.setContent(data.getStringExtra("theme"));
            note.save();
            initNotes();
            RecyclerNoteAdapter.setNoteLastClicked(note);
            noteContent.setHint("成功创建新便签，现在可以继续编辑");
            noteContent.setText(data.getStringExtra("content"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteContent = (EditText) findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,InputThemeActivity.class);
                startActivityForResult(intent,1);
            }
        });



        LitePal.getDatabase();


        //显示notes清单并且更新EditText
        initNotes();

        //保存按键
        Button save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是第一条便签的话需要定义主题
                if(notes.size()==0){
                    Note note = new Note();

                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat ("HH:mm:ss");
                    String time = formatter.format(new Date());

                    note.setTime(time);
                    note.setTheme("第一条便签");
                    note.setContent(noteContent.getText().toString());
                    note.save();
                    initNotes();
                }else{
                    Note lastClicked=RecyclerNoteAdapter.getNoteLastClicked();
                    if(lastClicked!=null) {
                        lastClicked.setContent(noteContent.getText().toString());
                        lastClicked.updateAll("theme = ? and time = ?",lastClicked.getTheme(),lastClicked.getTime());
                        initNotes();
                        noteContent.setHint("保存成功，请重新选择便签");
                    }else{
                        AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("没有选定便签");
                        alert.setMessage("请选择便签再进行修改");
                        alert.setCancelable(true);
                        alert.show();
                    }
                    }

            }
        });

        //删除按钮
        final Button delete=(Button)findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Note lastClicked=RecyclerNoteAdapter.getNoteLastClicked();
                if(lastClicked!=null) {
                    DataSupport.deleteAll(Note.class, "theme = ? and time = ?", lastClicked.getTheme(), lastClicked.getTime());
                    initNotes();
                    RecyclerNoteAdapter.setNoteLastClicked(null);
                }else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("没有选定要删除的便签");
                    alert.setMessage("请选择便签再删除");
                    alert.setCancelable(true);
                    alert.show();
                }
            }
        });

        //取消按钮
        Button cancel=(Button)findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNotes();
                noteContent.setHint("已撤销一切修改，请重新选择便签");
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initNotes() {
        notes.clear();
        /*
        for(int i=1;i<=10;i++){
            Note note=new Note(String.valueOf(i),"第"+i+"条便签","第"+i+"条便签的内容");
            notes.add(note);
        }*/
        notes = DataSupport.findAll(Note.class);
        Log.i(TAG, "onCreate: 便签数量的总数是——" + notes.size());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerNoteAdapter adapter = new RecyclerNoteAdapter(notes);
        recyclerView.setAdapter(adapter);

        if (notes.size() > 0) {
            noteContent.setText("");
            noteContent.setHint("请选择便签");
        }else{
            noteContent.setText("");
            noteContent.setHint("请编辑你的第一条便签");
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
