package simulating.model;

/**
 * Model for a file duplication.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public abstract class DosDuplication {

	private DosFolder fromFolder;
	private String fileName1;
	protected DosFolder toFolder;
	protected String fileName2;

	public DosDuplication(DosFolder fromFolder, String fileName1, DosFolder toFolder, String fileName2) {
		this.fromFolder = fromFolder;
		this.fileName1 = fileName1;
		this.toFolder = toFolder;
		this.fileName2 = fileName2;
	}
	
	public DosFolder getFromFolder() {
		return fromFolder;
	}

	public String getFileName1() {
		return fileName1;
	}

	public DosFolder getToFolder() {
		return toFolder;
	}
	
	public String getFileName2() {
		return fileName2;
	}
	
	public abstract boolean isConfirmationNecessary();
}
