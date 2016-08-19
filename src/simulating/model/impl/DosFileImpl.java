package simulating.model.impl;

import simulating.model.DosFile;
import simulating.model.DosPath;

/**
 * Full implementation for a DOS file.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosFileImpl implements DosFile {

	private DosPath folderPath;
	private String name;
	private byte[] content;
	
	public DosFileImpl(DosPath folderPath, String name, byte[] content) {
		this.folderPath = folderPath;
		this.name = name;
		this.content = content;
	}
	
	public DosFileImpl(DosPath folderPath, String name) {
		this(folderPath, name, null);
	}
	
	@Override
	public DosPath getFolderPath() {
		return folderPath;
	}

	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Builds the path for this file according to its folder path and its file name
	 */
	@Override
	public DosPath getPath() {
		return folderPath != null? folderPath.buildNewPath(name):null;
	}

	@Override
	public byte[] getContent() {
		return content;
	}
	
	@Override
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
