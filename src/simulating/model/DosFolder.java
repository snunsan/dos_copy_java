package simulating.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import simulating.DosCopy;
import simulating.model.impl.DosFileImpl;

/**
 * Model for a DOS folder.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public abstract class DosFolder {

	protected static final Logger LOGGER = Logger.getLogger(DosFolder.class.getName());
	static {
		DosCopy.setLogger(LOGGER);
	}
	
	private DosPath path;

	public DosFolder(DosPath path) {
		this.path = path;
	}
	
	public DosPath getPath() {
		return path;
	}

	public final boolean exists(DosFile file) {
		return checkFile(file.getName());
	}
	
	protected abstract boolean checkFile(String fileName);
	
	public final byte[] read(String fileName) {
		DosPath filePath = getPath().buildNewPath(fileName);
		
		byte[] content = null;
		if(checkFile(fileName)) {
			content = readFile(filePath);
			LOGGER.log(Level.FINE, "\tReading from: " + filePath + " " + (content != null? ">>> [" + new String(content) + "]":"-> NOT FOUND!!!!!!!!!!!!!!"));
		}
		
		return content;
	}
	
	protected abstract byte[] readFile(DosPath filePath);
	
	public final DosFile paste(byte[] content, String fileName, boolean forceWrite) {
		DosFile file = null;
		
		if(content != null) {
			file = new DosFileImpl(getPath(), fileName, content);
			
			if(forceWrite || !exists(file)) {
				LOGGER.log(Level.FINE, "\tWriting to: " + file.getPath().getPathString() + " >>> [" + new String(content) + "]");
			
				writeFile(file);
			}
			else {
				LOGGER.log(Level.FINE, "\tWriting to: " + file.getPath().getPathString() + " -> NOT ALLOWED !!!!!!!!!!");
				
				file = null;
			}
		}
		
		return file;
	}
	
	protected abstract void writeFile(DosFile file);
}
