package simulating.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import simulating.cli.DosCliArgumentsHandler;
import simulating.model.DosCopyCommand;
import simulating.model.DosDuplication;
import simulating.model.DosFile;
import simulating.model.DosFolder;
import simulating.model.DosPath;
import simulating.model.impl.DosFileImpl;
import simulating.test.model.doubles.DosCopyCommandDouble;
import simulating.test.model.doubles.DosDuplicationDouble;
import simulating.test.model.doubles.DosFolderDouble;

/**
 * Unit tests for the DOS copy command simulated in Java.
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosCopyTest {
	
	@Test
	public void test_CopyOneFileToAnotherFolderSucceeds_NoOverwrite() {
		final String FILE_NAME = "File";
		final String CURRENT_FOLDER = "From";
		final String DESTINATION_FOLDER_NAME = "To";

		DosPath fromFolderPath = new DosPath(CURRENT_FOLDER);
		DosFile file = new DosFileImpl(fromFolderPath, FILE_NAME, "XxXxX".getBytes());
		DosFolder fromFolder = new DosFolderDouble(fromFolderPath, file);
		DosFolder toFolder = new DosFolderDouble(fromFolderPath.buildNewPath(DESTINATION_FOLDER_NAME));

		assertThat(fromFolder.exists(file), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file), equalTo(Boolean.FALSE));
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{fromFolder, toFolder}, fromFolderPath);
		copyCommand.execute(new String[]{FILE_NAME, DESTINATION_FOLDER_NAME});
		
		assertThat(copyCommand.getFileCopyCount(), equalTo(1));
		assertThat(fromFolder.exists(file), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file), equalTo(Boolean.TRUE));
		assertThat(fromFolder.read(file.getName()), equalTo(toFolder.read(file.getName())));
	}

	@Test
	public void test_CopyOneFileToAnotherFolderSucceeds_OverwriteIsConfirmed() {
		final String FILE_NAME = "File";
		final String CURRENT_FOLDER = "From";
		final String DESTINATION_FOLDER_NAME = "To";

		DosPath fromFolderPath = new DosPath(CURRENT_FOLDER);
		DosFile file1 = new DosFileImpl(fromFolderPath, FILE_NAME, "XxXxX".getBytes());
		DosFolder fromFolder = new DosFolderDouble(fromFolderPath, file1);
		DosPath toFolderPath = fromFolderPath.buildNewPath(DESTINATION_FOLDER_NAME);
		DosFile file2 = new DosFileImpl(toFolderPath, FILE_NAME, "YyYyYy".getBytes());
		DosFolder toFolder = new DosFolderDouble(toFolderPath, file2);

		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{fromFolder, toFolder}, fromFolderPath, true);
		copyCommand.execute(new String[]{FILE_NAME, DESTINATION_FOLDER_NAME});
		
		assertThat(copyCommand.getFileCopyCount(), equalTo(1));
		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(fromFolder.read(file1.getName()), equalTo(toFolder.read(file1.getName())));
	}
	
	@Test
	public void test_CopyOneFileToAnotherFolderSucceeds_OverwriteConfirmationIsSuppressed() {
		final String FILE_NAME = "File";
		final String CURRENT_FOLDER = "From";
		final String DESTINATION_FOLDER_NAME = "To";

		DosPath fromFolderPath = new DosPath(CURRENT_FOLDER);
		DosFile file1 = new DosFileImpl(fromFolderPath, FILE_NAME, "XxXxX".getBytes());
		DosFolder fromFolder = new DosFolderDouble(fromFolderPath, file1);
		DosPath toFolderPath = fromFolderPath.buildNewPath(DESTINATION_FOLDER_NAME);
		DosFile file2 = new DosFileImpl(toFolderPath, FILE_NAME, "YyYyYy".getBytes());
		DosFolder toFolder = new DosFolderDouble(toFolderPath, file2);

		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{fromFolder, toFolder}, fromFolderPath);
		copyCommand.execute(new String[]{FILE_NAME, DESTINATION_FOLDER_NAME, DosCliArgumentsHandler.SUPPRESS_CONFIRMATION_MODIFIER});
		
		assertThat(copyCommand.getFileCopyCount(), equalTo(1));
		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(fromFolder.read(file1.getName()), equalTo(toFolder.read(file1.getName())));
	}
	
	@Test
	public void test_CopyOneFileToAnotherFolderFails_NoFileOverwriteConfirmation() {
		final String FILE_NAME = "File";
		final String CURRENT_FOLDER = "From";
		final String DESTINATION_FOLDER_NAME = "To";
		final String FILE_CONTENT1 = "XxXxX";

		DosPath fromFolderPath = new DosPath(CURRENT_FOLDER);
		DosFile file1 = new DosFileImpl(fromFolderPath, FILE_NAME, FILE_CONTENT1.getBytes());
		DosFolder fromFolder = new DosFolderDouble(fromFolderPath, file1);
		DosPath toFolderPath = fromFolderPath.buildNewPath(DESTINATION_FOLDER_NAME);
		DosFile file2 = new DosFileImpl(toFolderPath, FILE_NAME, "YyYyYy".getBytes());
		DosFolder toFolder = new DosFolderDouble(toFolderPath, file2);

		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{fromFolder, toFolder}, fromFolderPath, false);
		copyCommand.execute(new String[]{FILE_NAME, DESTINATION_FOLDER_NAME});
		
		assertThat(copyCommand.getFileCopyCount(), equalTo(0));
		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(fromFolder.read(file1.getName()), equalTo(FILE_CONTENT1.getBytes()));
	}
	
	@Test
	public void test_CopyMultipleFilesToAnotherFolderSucceeds() {
		final String FILE_NAME1 = "File1";
		final String FILE_NAME2 = "File2";
		final String CURRENT_FOLDER = "From";
		final String DESTINATION_FOLDER_NAME = "To";
		
		DosPath fromFolderPath = new DosPath(CURRENT_FOLDER);
		DosFile file1 = new DosFileImpl(fromFolderPath, FILE_NAME1, "XxXxX".getBytes());
		DosFile file2 = new DosFileImpl(fromFolderPath, FILE_NAME2, "YyYyYy".getBytes());
		DosFolder fromFolder = new DosFolderDouble(fromFolderPath, new DosFile[]{file1, file2});
		DosFolder toFolder = new DosFolderDouble(fromFolderPath.buildNewPath(DESTINATION_FOLDER_NAME));

		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(fromFolder.exists(file2), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.FALSE));
		assertThat(toFolder.exists(file2), equalTo(Boolean.FALSE));
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{fromFolder, toFolder}, fromFolderPath);
		copyCommand.execute(new String[]{FILE_NAME1, FILE_NAME2, DESTINATION_FOLDER_NAME});
		
		assertThat(copyCommand.getFileCopyCount(), equalTo(2));
		assertThat(fromFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(fromFolder.exists(file2), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file1), equalTo(Boolean.TRUE));
		assertThat(toFolder.exists(file2), equalTo(Boolean.TRUE));
		assertThat(fromFolder.read(file1.getName()), equalTo(toFolder.read(file1.getName())));
		assertThat(fromFolder.read(file2.getName()), equalTo(toFolder.read(file2.getName())));
	}
	
	@Test
	public void test_DuplicateAFileInTheSameFolderSucceeds() {
		final String FILE_NAME1 = "File1";
		final String FILE_NAME2 = "File2";
		final String CURRENT_FOLDER = "CurrentFolder";
		
		DosPath currentFolderPath = new DosPath(CURRENT_FOLDER);
		DosFile originalFile = new DosFileImpl(currentFolderPath, FILE_NAME1, "XxXxX".getBytes());
		DosFile duplicatedFile = new DosFileImpl(currentFolderPath, FILE_NAME2);
		DosFolder folder = new DosFolderDouble(currentFolderPath, originalFile);

		assertThat(folder.exists(originalFile), equalTo(Boolean.TRUE));
		assertThat(folder.exists(duplicatedFile), equalTo(Boolean.FALSE));

		DosCopyCommand copyCommand = new DosCopyCommandDouble(folder, currentFolderPath);
		copyCommand.execute(new String[]{FILE_NAME1, FILE_NAME2});
		
		assertThat(copyCommand.getFileCopyCount(), equalTo(1));
		assertThat(folder.exists(originalFile), equalTo(Boolean.TRUE));
//		assertThat(folder.exists(duplicatedFile), equalTo(Boolean.TRUE));
		assertThat(folder.read(originalFile.getName()), equalTo(folder.read(duplicatedFile.getName())));
	}

	@Test
	public void test_FileDuplicationSucceeds() {
		final String ORIGINAL_FILE_NAME = "OriginalFile";
		final String DESTINATION_FILE_NAME = "DestinationFile";

		DosPath folderPath = new DosPath("OriginFolder");
		DosFile originalFile = new DosFileImpl(folderPath, ORIGINAL_FILE_NAME, "XxXxX".getBytes());
		DosFolder originFolder = new DosFolderDouble(folderPath, originalFile);
		DosFolder destinationFolder = new DosFolderDouble(new DosPath("DestinationFolder"), originalFile);
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{originFolder, destinationFolder}, folderPath);
		DosFile[] duplicatedFiles = copyCommand.performDuplications(new DosDuplication[]{new DosDuplicationDouble(originFolder, ORIGINAL_FILE_NAME, destinationFolder, DESTINATION_FILE_NAME)});
		
		assertThat(duplicatedFiles.length, equalTo(1));
		assertThat(duplicatedFiles[0].getName(), equalTo(DESTINATION_FILE_NAME));
		assertThat(duplicatedFiles[0].getFolderPath(), equalTo(destinationFolder.getPath()));
		assertThat(duplicatedFiles[0].getContent(), equalTo(originalFile.getContent()));
	}
	
	@Test
	public void test_FileDuplicationFails_OriginFolderDoesntExist() {		
		DosPath folderPath = new DosPath("OriginFolder");
		DosFile originalFile = new DosFileImpl(folderPath, "FileName1", "XxXxX".getBytes());
		DosFolder originFolder = new DosFolderDouble(folderPath, originalFile);
		DosFolder destinationFolder = new DosFolderDouble(new DosPath("DestinationFolder"), originalFile);
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{destinationFolder}, folderPath);
		DosFile[] duplicatedFiles = copyCommand.performDuplications(new DosDuplication[]{new DosDuplicationDouble(originFolder, "FileName1", destinationFolder, "FileName2")});
		
		assertThat(duplicatedFiles.length, equalTo(0));
	}

	@Test
	public void test_FileDuplicationFails_DestinationFolderDoesntExist() {
		DosPath folderPath = new DosPath("OriginFolder");
		DosFile originalFile = new DosFileImpl(folderPath, "FileName1", "XxXxX".getBytes());
		DosFolder originFolder = new DosFolderDouble(folderPath, originalFile);
		DosFolder destinationFolder = new DosFolderDouble(new DosPath("DestinationFolder"), originalFile);
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(originFolder);
		DosFile[] duplicatedFiles = copyCommand.performDuplications(new DosDuplication[]{new DosDuplicationDouble(originFolder, "FileName1", destinationFolder, "FileName2")});
		
		assertThat(duplicatedFiles.length, equalTo(0));
	}
	
	@Test
	public void test_FileDuplicationFails_OriginalFileDoesntExist() {
		DosPath folderPath = new DosPath("OriginFolder");
		DosFile originalFile = new DosFileImpl(new DosPath("AnotherFolder"), "FileName1", "XxXxX".getBytes());
		DosFolder originFolder = new DosFolderDouble(folderPath, originalFile);
		DosFolder destinationFolder = new DosFolderDouble(new DosPath("DestinationFolder"), originalFile);
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(new DosFolder[]{originFolder, destinationFolder}, folderPath);
		DosFile[] duplicatedFiles = copyCommand.performDuplications(new DosDuplication[]{new DosDuplicationDouble(originFolder, "FileName1", destinationFolder, "FileName2")});
		
		assertThat(duplicatedFiles.length, equalTo(0));
	}
	
	@Test
	public void test_FolderExists() {
		DosPath currentFolderPath = new DosPath("CurrentFolder");
		DosFolder folder = new DosFolderDouble(currentFolderPath);
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(folder, currentFolderPath);
		
		assertThat(copyCommand.exists(folder), equalTo(Boolean.TRUE));
	}
	
	@Test
	public void test_FolderDoesntExist() {
		DosPath currentFolderPath = new DosPath("CurrentFolder");
		DosFolder folder1 = new DosFolderDouble(currentFolderPath);
		DosFolder folder2 = new DosFolderDouble(new DosPath("AnotherFolder"));
		
		DosCopyCommand copyCommand = new DosCopyCommandDouble(folder2, currentFolderPath);
		
		assertThat(copyCommand.exists(folder1), equalTo(Boolean.FALSE));
	}
	
	@Test
	public void test_FileExistsInFolder() {
		DosPath folderPath = new DosPath("CurrentFolder");
		DosFile file = new DosFileImpl(folderPath, "FileName", "XxXxX".getBytes());
		DosFolder folder = new DosFolderDouble(folderPath, file);
		
		assertThat(folder.exists(file), equalTo(Boolean.TRUE));
	}
	
	@Test
	public void test_FileDoesntExistInFolder() {
		DosPath folderPath = new DosPath("CurrentFolder");
		DosFile file1 = new DosFileImpl(new DosPath("AnotherFolder"), "FileName1", "XxXxX".getBytes());
		DosFile file2 = new DosFileImpl(folderPath, "FileName2", "XxXxX".getBytes());
		DosFolder folder = new DosFolderDouble(folderPath, file2);
		
		assertThat(folder.exists(file1), equalTo(Boolean.FALSE));
	}
}
