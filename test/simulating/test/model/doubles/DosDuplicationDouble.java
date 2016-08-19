package simulating.test.model.doubles;

import simulating.model.DosDuplication;
import simulating.model.DosFolder;

/**
 * Test double that eases unit testing for the file duplication without any dependencies on the file system.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosDuplicationDouble extends DosDuplication {
	
	public DosDuplicationDouble(DosFolder fromFolder, String fileName1, DosFolder toFolder, String fileName2) {
		super(fromFolder, fileName1, toFolder, fileName2);
	}

	@Override
	public boolean isConfirmationNecessary() {
		return false;
	}
}
