package com.example.sdu.myflag.activity;

import android.content.Intent;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sdu.myflag.base.BaseActivity;
import com.example.sdu.myflag.R;

public class AddFriendMessageActivity extends BaseActivity {
    private Button next;
    private EditText editText;
    private Bundle bundle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_addfriendmessage;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        init();
        setListener();
    }

    private void init(){
        next = (Button) findViewById(R.id.messageNextButton);
        editText = (EditText) findViewById(R.id.messageEditText);
        bundle = this.getIntent().getExtras();
    }

    private void setListener() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                String user = bundle.getString("user");
                Intent intent = new Intent(AddFriendMessageActivity.this, AddFriendActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("user", user);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void friendMsgBack(View view){
        this.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                AddFriendMessageActivity.this.finish();
        }
    }
}
