package commands;

import javax.swing.JOptionPane;

/**
 * Command wrapper to represent the jump tags in the player. Values consist of
 * any arbitrary string to be used as a descriptor
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-04-01
 */
public class GoHereCommand implements PlayerCommand {

	private String jumpTag = "";

	/**
	 * Construct a new instance with the value set to the given value
	 *
	 * @param jumpTag
	 *            Desired jump tag label
	 */
	public GoHereCommand(String jumpTag) {
		this.jumpTag = jumpTag;
	}

	@Override
	public String toString() {
		return "Location: " + jumpTag;
	}

	@Override
	public String serialize() {
		return "/~" + jumpTag;
	}

	@Override
	public String getEditLabel() {
		return "Enter name of location";
	}

	@Override
	public String getCurrentValue() {
		return jumpTag;
	}

	@Override
	public void setCurrentValue(String jumpTag) {
		this.jumpTag = jumpTag;
	}
	
	@Override
	public void editCommand() {
		Object value;
		value = JOptionPane.showInputDialog(null, "Enter location to go to", "Edit Item Details",
				JOptionPane.PLAIN_MESSAGE, null, null, this.jumpTag);
		if (value != null && value != "") {
			this.jumpTag = (String) value;
		
		}
		
	}
	

}
