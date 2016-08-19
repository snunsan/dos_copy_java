package simulating;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import simulating.model.DosCopyCommand;
import simulating.model.DosFile;
import simulating.model.DosFolder;
import simulating.model.DosPath;
import simulating.model.impl.DosCopyCommandImpl;
import simulating.model.impl.DosFileImpl;
import simulating.model.impl.DosFolderImpl;

/**
 * DOS copy command simulated in Java.
 * + REQ01: The command copies computer files from one directory to another.
 * + REQ02: The destination defaults to the current working directory.
 * + REQ03: If more than one source file is indicated, the destination must be a directory.
 * 
 * Examples of usage:
 * + java executable.DosCopy ThisIsAFile.txt FolderB
 * + java executable.DosCopy ThisIsAFile.txt ThisIsADuplicatedFile.txt
 * + java executable.DosCopy ThisIsAFile.txt ThisIsASecondFile.txt FolderB
 * 
 * TODO Improvements:
 * - DosCopyCommand implementing an interface
 * - DosCopy should explicitly implement a dependence on the file system (FileSystemHelper) that doesn't happen in DosCopyDouble
 * - Don't use the folder string from the args as a folder id (the only folder id is the folder path)
 * - performDuplications() tested separately
 * - Introduce Path
 * - Logger
 * - Allow relative paths
 * - Clean up
 * + Allow CLI modifiers in args: /Y, ETC!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * + Remove TODOs
 * + Eclemma
 * + Check error/exception execution flows
 * + SOLID
 * + Javadoc
 * + No magic numbers/strings
 * + DIP
 * 
 * Behaviour described at:
 * @see https://en.wikipedia.org/wiki/Copy_(command)
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosCopy {
	
	private static final boolean TESTING_MODE = false;
	
	private static final Logger LOGGER = Logger.getLogger(DosCopy.class.getName());
	static {
		setLogger(LOGGER);
	}
	
	public static void main(String[] args)
	{		
		if(TESTING_MODE) {
			final String ORIGIN_FOLDER = "C:\\_\\_TEMP\\DosCopy\\testing\\FromFolder_A";
			
			DosFile originFile = new DosFileImpl(new DosPath(ORIGIN_FOLDER), "MyFile.txt",
				"This is the 1st line of the file\n\r\n\rThis is the 2nd line of the file".getBytes()
			);
			DosFolder fromFolder = new DosFolderImpl(new DosPath(ORIGIN_FOLDER));
			DosFolder toFolder = new DosFolderImpl(new DosPath("C:\\_\\_TEMP\\DosCopy\\testing\\ToFolder_B"));
			
			prepareTesting(fromFolder, toFolder, originFile);
			
			DosCopyCommand copyCommand = new DosCopyCommandImpl();
			copyCommand.execute(new String[]{originFile.getName(), "ThisIsADuplicatedFile.txt"});
		}
		else {
			DosCopyCommand copyCommand = new DosCopyCommandImpl();
			copyCommand.execute(args);
		}
	}
	
	private static void prepareTesting(DosFolder fromFolder, DosFolder toFolder, DosFile file) {
		try {
			FileUtils.deleteDirectory(new File(fromFolder.getPath().getPathString()));
			FileUtils.deleteDirectory(new File(toFolder.getPath().getPathString()));
			
			FileUtils.forceMkdir(new File(fromFolder.getPath().getPathString()));
			FileUtils.writeByteArrayToFile(new File(file.getPath().getPathString()), file.getContent());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.SEVERE, "Exception thrown");
			e.printStackTrace();
		}
	}
	
	public static void setLogger(Logger logger) {
		if(true) {
			logger.setLevel(Level.ALL);
			ConsoleHandler ch = new ConsoleHandler();
	        ch.setLevel(Level.FINEST);
	        logger.addHandler(ch);
	        logger.setLevel(Level.FINEST);
		}
	}
}
