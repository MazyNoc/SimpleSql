package nu.annat.simplesql;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Mapper implementation for a list as result
 *
 * @param <E>
 */
public abstract class ListMapper<E> {
	private List<E> list;
	private boolean stopped = false;

	public boolean isStopped() {
		return stopped;
	}

	public void stop() {
		this.stopped = true;
	}

	public void initResult() {
		list = new LinkedList<E>();
	}

	protected abstract ObjectMapper<E> getObjectMapper();

	public void doRow(HelperResultSet rs, ObjectMapper<E> mapper) throws SQLException {
		E object = mapper.mapObject(rs);
		row(rs, object, list);
	}

	public void row(HelperResultSet rs, E object, List<E> list) throws SQLException {
		list.add(object);
	}

	public List<E> getResult() {
		if (list == null) {
			return Collections.emptyList();
		} else {
			return list;
		}
	}
}
