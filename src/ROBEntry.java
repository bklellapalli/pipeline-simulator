/**
 * @author Balakrishna Lellapalli
 *
 */
public class ROBEntry {

	private int instructionAddress;
	private String destinationAddress;
	private int result;
	private boolean status;

	// Set Instruction address that need to be dispatch in ROB
	public void setInstructionAddress(int value) {
		instructionAddress = value;
	}
	// Returns Instruction address for the ROB entry
	public int getInstructionAddress() {
		return instructionAddress;
	}
	// Set destination Operand in destination address field
	public void setDestinationAddress(String value) {
		destinationAddress = value;
	}
	//Return destination operand
	public String getDestinationAddress() {
		return destinationAddress;
	}
	// Set value of destination operand at the time of forward result 
	public void setResult(int value) {
		result = value;
	}
	// Returns result of destination operand
	public int getResult(int value) {
		return result;
	}
	// Set valid if result is forwarded
	public void setStatus(boolean value) {
		status = value;
	}
	// Return status(valid or invalid)
	public boolean getStatus() {
		return status;
	}
}