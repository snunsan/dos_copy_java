package simulating.model.impl;

import simulating.cli.DosCliArgumentsHandler;
import simulating.model.DosDuplication;
import simulating.model.DosFolder;

/**
 * Full implementation for a file duplication, allowing to:
 * + Check if a file exists in the file system
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosDuplicationImpl extends DosDuplication {

	private DosCliArgumentsHandler cliArgsHandler;

	public DosDuplicationImpl(DosFolder fromFolder, String fileName1, DosFolder toFolder, String fileName2, DosCliArgumentsHandler cliArgsHandler) {
		super(fromFolder, fileName1, toFolder, fileName2);

		this.cliArgsHandler = cliArgsHandler;
	}
	
	/**
	 * The confirmation is necessary if the destination file already exists and the confirmation is not suppressed by a CLI modifier
	 */
	@Override
	public boolean isConfirmationNecessary() {
		boolean fileExistsInToFolder = toFolder.exists(new DosFileImpl(toFolder.getPath(), fileName2));

		return fileExistsInToFolder && !cliArgsHandler.confirmationIsSuppressed();
	}
}
