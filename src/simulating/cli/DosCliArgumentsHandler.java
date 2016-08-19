package simulating.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Handler for the Command Line Interface arguments of the Copy command
 * 
 * @author Santiago Nuñez
 * @date 17/08/2016 
 */
public class DosCliArgumentsHandler {
	public static final String SUPPRESS_CONFIRMATION_MODIFIER = "/Y";
	
	private static final String[] MODIFIER_LIST = new String[]{
		SUPPRESS_CONFIRMATION_MODIFIER
	};
	
	private String[] args;

	public DosCliArgumentsHandler(String[] args) {
		super();
		this.args = args;
	}
	
	public boolean isArgListValid() {
		return args != null && args.length >= 2;
	}

	public String[] getEventualOrigins() {
		String[] resourceNames = filterResourceNames();
		
		//returning all the elements in the resource name array except for the last one
		return Arrays.copyOfRange(resourceNames, 0, resourceNames.length - 1);
	}

	private String[] filterResourceNames() {
		List<String> resourceNames = new ArrayList<String>();
		
		if(args != null) {
			for(String arg:args) {
				if(!ArrayUtils.contains(MODIFIER_LIST, arg)) {
					resourceNames.add(arg);
				}
			}
		}
		
		return resourceNames.toArray(new String[resourceNames.size()]);
	}
	
	public String getEventualDestination() {
		String[] resourceNames = filterResourceNames();
		
		//returning the last element in the resource name array
		return resourceNames[resourceNames.length - 1];
	}

	public boolean confirmationIsSuppressed() {
		return ArrayUtils.contains(args, SUPPRESS_CONFIRMATION_MODIFIER);
	}
}
