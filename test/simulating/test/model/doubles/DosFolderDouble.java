package simulating.test.model.doubles;

import java.util.HashMap;
import java.util.Map;

import simulating.model.DosFile;
import simulating.model.DosFolder;
import simulating.model.DosPath;

/**
 * Test double that eases unit testing for the DOS folder without any dependencies on the file system.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosFolderDouble extends DosFolder {

	private Map<String, DosFile> existingFiles;

	public DosFolderDouble(DosPath path, DosFile[] existingFiles) {
		super(path);
		this.existingFiles = new HashMap<String, DosFile>();
		if(existingFiles != null) {
			for(DosFile existingFile:existingFiles) {
				if(existingFile != null && existingFile.getPath() != null) {
					this.existingFiles.put(existingFile.getPath().getPathString(), existingFile);
				}
			}
		}
	}
	
	public DosFolderDouble(DosPath path, DosFile existingFile) {
		this(path, existingFile != null? new DosFile[]{existingFile}:null);
	}
	
	public DosFolderDouble(DosPath path) {
		this(path, (DosFile)null);
	}
	
	public Map<String, DosFile> getExistingFiles() {
		return existingFiles;
	}

	@Override
	protected boolean checkFile(String fileName) {
		return existingFiles.get(getPath().buildNewPath(fileName).getPathString()) != null;
	}
	
	@Override
	protected byte[] readFile(DosPath filePath) {		
		DosFile existingFile = filePath != null? existingFiles.get(filePath.getPathString()):null;
		return existingFile != null? existingFile.getContent():null;
	}
	
	@Override
	protected void writeFile(DosFile file) {
		if(file != null && file.getPath() != null) {
			existingFiles.put(file.getPath().getPathString(), file);
		}
	}
}
