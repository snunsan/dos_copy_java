package simulating.model.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import simulating.model.DosCopyCommand;
import simulating.model.DosFolder;
import simulating.model.DosPath;

/**
 * Full implementation for a Copy command, allowing to have:
 * + Access to the file system
 * + Access to the console
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosCopyCommandImpl extends DosCopyCommand {

	private static final String CONFIRMATION_YES = "Y";
	private static final String CONFIRMATION_NO = "N";
	private static final String CONFIRMATION_ALL = "A";
	
	@Override
	protected void displayText(String content) {
		System.out.println(content);
	}
	
	@Override
	protected DosPath getCurrentFolderPath() {
		return new DosPath(System.getProperty("user.dir"));
	}
	
	@Override
	protected boolean checkFolder(DosPath folderPath) {
		return new File(folderPath.getPathString()).exists();
	}
	
	@Override
	protected DosFolder buildFolder(DosPath folderPath) {
		return new DosFolderImpl(folderPath);
	}
	
	@Override
	protected boolean readConfirmationInput() {
		
		boolean read = false;
		
		BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String input = br.readLine().toUpperCase();

                if(CONFIRMATION_YES.equals(input) || CONFIRMATION_ALL.equals(input)) {
                	read = true;
                	if(CONFIRMATION_YES.equals(input)) {
                		LOGGER.log(Level.FINE, "\t-> YES inputted");
                	}
                	else if(CONFIRMATION_ALL.equals(input)) {
                		LOGGER.log(Level.FINE, "\t-> ALL inputted");
                		confirmAll = true;
                	}
                	break;
                }
                else if(CONFIRMATION_NO.equals(input)) {
                	read = false;
                	LOGGER.log(Level.FINE, "\t-> NO inputted");
                	break;
                }
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return read;
	}
}
