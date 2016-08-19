package simulating.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import simulating.DosCopy;
import simulating.cli.DosCliArgumentsHandler;
import simulating.model.impl.DosDuplicationImpl;
import simulating.model.impl.DosFolderImpl;

/**
 * Model for a Copy command.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public abstract class DosCopyCommand {
	
	protected static final Logger LOGGER = Logger.getLogger(DosCopyCommand.class.getName());
	static {
		DosCopy.setLogger(LOGGER);
	}
	
	private static final String OVERWRITE_CONFIRMATION_MESSAGE_FORMATTED = "Overwrite %s? (Yes/No/All) ";
	private static final String RESULT_MESSAGE_FORMATTED = "\t%d file(s) copied.";
	
	private int fileCopyCount = 0;
	protected boolean confirmAll;

	public int getFileCopyCount() {
		return fileCopyCount;
	}
	
	public final void execute(String[] args) {
		
		DosCliArgumentsHandler cliArgsHandler = new DosCliArgumentsHandler(args);
		
		printArgs(args);
		
		if(cliArgsHandler.isArgListValid()) {
			
			DosPath currentFolderPath = getCurrentFolderPath();
			LOGGER.log(Level.FINE, "Working Directory = " + currentFolderPath.getPathString());
			
			performDuplications(getDuplications(cliArgsHandler, currentFolderPath));
			
			displayText(String.format(RESULT_MESSAGE_FORMATTED, fileCopyCount));
		}
		else {
			LOGGER.log(Level.SEVERE, "Command parameters are not correct");
		}
	}
	
	protected void displayText(String content) {
		//Does nothing by default
	}
	
	private void printArgs(String[] args)
	{
		LOGGER.log(Level.FINE, "Running command with arguments");

		int i = 0;
		for(String arg:args) {
			LOGGER.log(Level.FINE, "\tArg #" + (i++) + ": " + arg);
		}
	}
	
	protected abstract DosPath getCurrentFolderPath();
	
	private DosDuplication[] getDuplications(DosCliArgumentsHandler cliArgsHandler, DosPath currentFolderPath) {
		List<DosDuplication> duplications = new ArrayList<DosDuplication>();

		String[] eventualOrigins = cliArgsHandler.getEventualOrigins();
		String eventualDestination = cliArgsHandler.getEventualDestination();
		
		if(eventualOrigins != null && eventualDestination != null) {
			boolean multipleFilesCopy = eventualOrigins.length > 1;
			boolean eventualDestinationIsAFolder = exists(new DosFolderImpl(currentFolderPath.buildNewPath(eventualDestination)));
			boolean keepFileName = eventualDestinationIsAFolder || multipleFilesCopy;
			
			int j = 0;
			for(String eventualOrigin:eventualOrigins) {
				
				DosFolder originFolder = buildFolder(currentFolderPath);
				DosFolder destinationFolder = buildFolder(!eventualDestinationIsAFolder? currentFolderPath:currentFolderPath.buildNewPath(eventualDestination));
				
				String fileName1 = eventualOrigin;
				String fileName2 = keepFileName? eventualOrigin:eventualDestination;
				
				LOGGER.log(Level.FINE, "Created duplication #" + (j++) + ": " + new DosPath(originFolder.getPath().getPathString(), fileName1).getPathString() + " -> " + new DosPath(destinationFolder.getPath().getPathString(), fileName2).getPathString());
				
				duplications.add(new DosDuplicationImpl(originFolder, fileName1, destinationFolder, fileName2, cliArgsHandler));
			}
		}
		
		return duplications.toArray(new DosDuplication[duplications.size()]);
	}
	
	public final boolean exists(DosFolder folder) {
		return checkFolder(folder.getPath());
	}
	
	protected abstract boolean checkFolder(DosPath folderPath);
	protected abstract DosFolder buildFolder(DosPath folderPath);
	
	public DosFile[] performDuplications(DosDuplication[] duplications) {
		
		List<DosFile> duplicatedFiles = new ArrayList<DosFile>();
		
		for(DosDuplication duplication:duplications) {
			
			DosFolder fromFolder = duplication.getFromFolder();
			String fileName1 = duplication.getFileName1();
			DosFolder toFolder = duplication.getToFolder();
			String fileName2 = duplication.getFileName2();
			
			if(fromFolder != null && toFolder != null) {
				
				LOGGER.log(Level.FINE, "Performing file duplication");
				
				if(exists(fromFolder) && exists(toFolder))
				{
					boolean necessaryConfirmation = duplication.isConfirmationNecessary();
					boolean forceOverwrite = !necessaryConfirmation || askForConfirmation(fileName2);
					
					if(forceOverwrite) {
						DosFile returnedFile = toFolder.paste(fromFolder.read(fileName1), fileName2, forceOverwrite);
						if(returnedFile != null) {
							duplicatedFiles.add(returnedFile);
							fileCopyCount++;
						}
					}
				}
			}
		}
		
		return duplicatedFiles.toArray(new DosFile[duplicatedFiles.size()]);
	}
	
	private boolean askForConfirmation(String fileName) {
		
		boolean confirmation;
		
		if(!confirmAll) {
			displayText(String.format(OVERWRITE_CONFIRMATION_MESSAGE_FORMATTED, fileName));
			confirmation = readConfirmationInput();
		}
		else {
			confirmation = true;
		}
		 
		return confirmation;
	}
	
	protected abstract boolean readConfirmationInput();
}
