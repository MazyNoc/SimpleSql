package nu.annat.simplesqlexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.sql.Types;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nu.annat.simplesql.DataHelper;
import nu.annat.simplesql.MappedParameters;
import nu.annat.simplesql.NamedStatement;
import nu.annat.simplesql.sqliteandroid.SqliteHelperConnection;


public class MainActivity extends AppCompatActivity {

    private DataHelper dh;
    private NamedStatement namedStatement;

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


        // Auto mapping requires an empty constructor and set methods for all values.
        List<Message> autpMappedMessageList = dh.queryArrayListAuto("Select * from messages order by timestamp DESC", Message.class, null);
        for (Message message : autpMappedMessageList) {
            Log.d("message", message.toString());
        }

        // you can also prepare a statement with or without named parameters for later reuse.
        // internally this uses the databases preparedStatements and can be optimized by the database engine
        namedStatement = dh.getNamedStatement("Select * from messages where timestamp>:timestamp order by timestamp DESC");
        MappedParameters parameters = new MappedParameters();
        parameters.add("timestamp", System.currentTimeMillis()* TimeUnit.MINUTES.toMillis(-1), Types.BIGINT);
        List<Message> preparedMessageList = dh.queryList(namedStatement, new MessageMapper(), parameters);
        for (Message message : preparedMessageList) {
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
