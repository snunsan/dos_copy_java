package simulating.test.model.doubles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulating.model.DosCopyCommand;
import simulating.model.DosDuplication;
import simulating.model.DosFile;
import simulating.model.DosFolder;
import simulating.model.DosPath;

/**
 * Test double that eases unit testing for the Copy command without any dependencies on the file system.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosCopyCommandDouble extends DosCopyCommand {

	private Map<String, DosFolder> existingFolders;
	private DosPath currentFolderPath;
	private boolean confirmOverwrite;
	
	public DosCopyCommandDouble(DosFolder[] existingFolders, DosPath currentFolderPath) {
		super();
		
		this.existingFolders = new HashMap<String, DosFolder>();
		if(existingFolders != null) {
			for(DosFolder existingFolder:existingFolders) {
				if(existingFolder != null && existingFolder.getPath() != null) {
					this.existingFolders.put(existingFolder.getPath().getPathString(), existingFolder);
				}
			}
		}
		
		this.currentFolderPath = currentFolderPath;
	}
	
	public DosCopyCommandDouble(DosFolder[] existingFolders, DosPath currentFolderPath, boolean confirmOverwrite) {
		this(existingFolders, currentFolderPath);
		this.confirmOverwrite = confirmOverwrite;
	}
	
	public DosCopyCommandDouble(DosFolder existingFolder, DosPath currentFolderPath) {
		this(existingFolder != null? new DosFolder[]{existingFolder}:null, currentFolderPath);
	}
	
	public DosCopyCommandDouble(DosFolder existingFolder) {
		this(existingFolder, null);
	}
	
	@Override
	public DosFile[] performDuplications(DosDuplication[] duplications) {
		DosFile[] duplicatedFiles = super.performDuplications(duplications);
		
		updateExistingFiles(duplicatedFiles);
		
		return duplicatedFiles;
	}
	
	private void updateExistingFiles(DosFile[] files) {
		if(files != null) {
			for(DosFile file:files) {
				if(file != null) {
					List<DosFile> allExistingFiles = getAllExistingFiles();
					for(DosFile existingFile:allExistingFiles) {
						if(existingFile != null && file.getPath().equals(existingFile.getPath())) {
							existingFile.setContent(file.getContent());
						}
					}
				}
			}
		}
	}
	
	private List<DosFile> getAllExistingFiles() {
		List<DosFile> list = new ArrayList<DosFile>();
		
		for(DosFolderDouble existingFolderDouble:getAllExistingFolderDoubles()) {
    		Map<String, DosFile> existingFiles = existingFolderDouble.getExistingFiles();
    		if(existingFiles != null) {
    			for(Map.Entry<String, DosFile> fileEntry:existingFiles.entrySet()) {
    				if(fileEntry != null) {
    					DosFile existingFile = fileEntry.getValue();
    					if(existingFile != null) {
    						list.add(existingFile);
    					}
    				}
    			}
    		}
		}
		
		return list;
	}
	
	private List<DosFolderDouble> getAllExistingFolderDoubles() {
		List<DosFolderDouble> list = new ArrayList<DosFolderDouble>();
		
		for(Map.Entry<String, DosFolder> folderEntry:existingFolders.entrySet()) {
		    if(folderEntry != null) {
		    	DosFolder existingFolder = folderEntry.getValue();
		    	if(existingFolder != null && existingFolder instanceof DosFolderDouble) {
		    		list.add((DosFolderDouble)existingFolder);
		    	}
		    }
		}
		
		return list;
	}

	@Override
	protected DosPath getCurrentFolderPath() {
		return currentFolderPath;
	}
	
	@Override
	protected boolean readConfirmationInput() {
		return confirmOverwrite;
	}
	
	@Override
	protected boolean checkFolder(DosPath folderPath) {
		return folderPath != null && existingFolders.get(folderPath.getPathString()) != null;
	}
	
	@Override
	protected DosFolder buildFolder(DosPath folderPath) {
		return folderPath != null? existingFolders.get(folderPath.getPathString()):null;
	}
}
