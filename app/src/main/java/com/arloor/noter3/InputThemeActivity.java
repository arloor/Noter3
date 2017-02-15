package com.arloor.noter3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_theme);

        Button queding =(Button)findViewById(R.id.queding);
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText theme=(EditText)findViewById(R.id.theme);
                EditText content=(EditText)findViewById(R.id.content);

                Intent intent=new Intent(InputThemeActivity.this,MainActivity.class);
                intent.putExtra("theme",theme.getText().toString());
                intent.putExtra("content",content.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(InputThemeActivity.this,MainActivity.class);
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
