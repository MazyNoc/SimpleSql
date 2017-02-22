package nu.annat.simplesqlexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.sql.Types;
import java.util.List;

import nu.annat.simplesql.android.simplesql.DataHelper;
import nu.annat.simplesql.android.simplesql.MappedParameters;
import nu.annat.simplesql.android.simplesql.sqlite.SqliteHelperConnection;

public class MainActivity extends AppCompatActivity {

    private DataHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDatabase();

        findViewById(R.id.insert_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMessage("test");
            }
        });

        List<Message> messages = dh.queryList("Select * from messages order by timestamp DESC", new MessageMapper(), null);
        for (Message message : messages) {
            Log.d("message", message.toString());
        }

    }

    private void insertMessage(String text) {
        MappedParameters parameters = new MappedParameters();
        parameters.add("timestamp", System.currentTimeMillis(), Types.BIGINT);
        parameters.add("user", "unknown", Types.VARCHAR);
        parameters.add("text", text, Types.VARCHAR);
        Long aLong = dh.insertAutoKey("Insert into messages (timestamp, user, text) values (:timestamp, :user, :text)", parameters);
        Log.d("new key", "" + aLong);
    }

    private void openDatabase() {
        DataBaseExample db = new DataBaseExample(this);
        SqliteHelperConnection conn = new SqliteHelperConnection(db.getWritableDatabase());
        dh = new DataHelper(conn);


    }
}
