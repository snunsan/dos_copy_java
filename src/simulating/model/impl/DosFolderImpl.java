package simulating.model.impl;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;

import simulating.model.DosFile;
import simulating.model.DosFolder;
import simulating.model.DosPath;

/**
 * Full implementation for a DOS folder, allowing to:
 * + Access to the file system
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosFolderImpl extends DosFolder {
	
	public DosFolderImpl(DosPath path) {
		super(path);
	}

	@Override
	protected boolean checkFile(String fileName) {
		return new File(getPath().buildNewPath(fileName).getPathString()).exists();
	}

	@Override
	protected byte[] readFile(DosPath filePath) {	
		byte[] content = null;
		try {
			content = FileUtils.readFileToByteArray(new File(filePath.getPathString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content;
	}

	@Override
	protected void writeFile(DosFile file) {
		try {
			FileUtils.writeByteArrayToFile(new File(file.getPath().getPathString()), file.getContent());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.SEVERE, "Exception thrown");
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return getPath().getPathString();
	}
}
