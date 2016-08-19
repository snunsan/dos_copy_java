package simulating.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a DOS path.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosPath {
	private static final String FOLDER_SEPARATOR = "\\";
	private static final String ESCAPING_CHARACTER = "\\";
	
	private String[] names;

	public DosPath(String[] names) {
		super();
		this.names = names;
	}
	
	public DosPath(String name1, String name2) {
		this(new String[]{name1, name2});
	}
	
	public DosPath(String name) {
		//the separator has to be escaped in order to not behave as a Regular Expression
		this(name.split(ESCAPING_CHARACTER + FOLDER_SEPARATOR));
	}
	
	public String[] getNames() {
		return names;
	}

	public String getPathString() {
		return String.join(FOLDER_SEPARATOR, names);
	}
	
	/**
	 * Builds a new path by combining another path and a file name
	 * @param fileName
	 * @return
	 */
	public DosPath buildNewPath(String fileName) {
		List<String> list = new ArrayList<String>();
		for(String name:names) {
			list.add(name);
		}
		list.add(fileName);
		return new DosPath(list.toArray(new String[list.size()]));
	}
	
	/**
	 * Allows to compare 2 path objects according to their path strings
	 */
	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof DosPath && getPathString().equals(((DosPath)o).getPathString());
	}
	
	@Override
	public String toString() {
		return getPathString();
	}
}
