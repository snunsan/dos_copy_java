package simulating.model;

/**
 * Model for a DOS file.
 * 
 * @author Santiago Nu�ez
 * @date 17/08/2016 
 */
public interface DosFile {

	public DosPath getFolderPath();
	public String getName();
	public DosPath getPath();
	public byte[] getContent();
	public void setContent(byte[] content);
}
