package nu.annat.simplesqlexample;

import java.sql.SQLException;

import nu.annat.simplesql.android.simplesql.HelperColumnMapping;
import nu.annat.simplesql.android.simplesql.HelperResultSet;
import nu.annat.simplesql.android.simplesql.ObjectMapper;

public class MessageMapper extends ObjectMapper<Message> {

    int idIdx;
    int timestampIdx;
    int userIdx;
    int textIdx;

    @Override
    protected int prepareIndex(HelperResultSet rs) throws SQLException {
        HelperColumnMapping columnMapping = rs.getColumnMapping();
        idIdx = columnMapping.getIdxByName("_id");
        timestampIdx = columnMapping.getIdxByName("timestamp");
        userIdx = columnMapping.getIdxByName("user");
        textIdx = columnMapping.getIdxByName("text");
        return 4;
    }

    @Override
    public Message mapObject(HelperResultSet rs) throws SQLException {
        int id = rs.getInt(idIdx);
        long timestamp = rs.getLong(timestampIdx);
        String user = rs.getString(userIdx);
        String text = rs.getString(textIdx);
        return new Message(id, timestamp, user, text);
    }
}
