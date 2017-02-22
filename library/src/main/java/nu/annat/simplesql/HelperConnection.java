package nu.annat.simplesql;

import java.sql.SQLException;

public interface HelperConnection {

	HelperStatement prepareStatement(String sql, int totalNumberOfParameters) throws SQLException;

}
