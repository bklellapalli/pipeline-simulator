/**
 * @author Balakrishna Lellapalli
 *
 */
public class Instruction {

	// region Private Member(s)

	private int instructionAddress;
	private String operation;
	private KeyValue<String, Integer> destination;
	private KeyValue<String, Integer> src1;
	private KeyValue<String, Integer> src2;
	private Integer literal;
	private int memAddress;
	private String content;
	private boolean isNOP;
	private int destinationRobIndex;
	private int source1RobIndex;
	private int source2RobIndex;
	private boolean sourceValid;
	private int renamedSlot;

	// endregion

	// region Public Constructor

	public Instruction() {
		this.content = "NOP";
		this.isNOP = true;
	}

	public Instruction(String operation, KeyValue<String, Integer> destination,
			KeyValue<String, Integer> src1, KeyValue<String, Integer> src2,
			Integer literal, String content) {
		this.operation = operation;
		this.destination = destination;
		this.src1 = src1;
		this.src2 = src2;
		this.literal = literal;
		this.content = content;
		this.destinationRobIndex = -1;
		this.source1RobIndex = -1;
		this.source2RobIndex = -1;
	}

	// endregion

	// region Public Method(s)

	// Set Instruction address(current PC Address)
	public void setInstructionAddress(int value) {
		instructionAddress = value;
	}

	// Return Instruction address(current PC Address)
	public int getInstructionAddress() {
		return instructionAddress;
	}
	
	// Returns Instruction's Operation type (ADD/SUB/MUL/AND/OR/EXOR/MOV/LOAD/STORE .. etc)
	public String getOperation() {
		return this.operation;
	}

	// Set value in Instruction's Destination field
	public void setDestination(Integer value) {
		this.destination.setValue(value);
	}

	// Returns Instruction's Destination <key,value> field (Example: <R1, 100>)
	public KeyValue<String, Integer> getDestination() {
		return this.destination;
	}

	// Set value in Instruction's Source1 field
	public void setSrc1(int value) {
		this.src1.setValue(value);
	}

	// Returns Instruction's Source1 <key,value> field (Example: <R1, 100>)
	public KeyValue<String, Integer> getSrc1() {
		return this.src1;
	}

	// Set value in Instruction's Source2 field
	public void setSrc2(int value) {
		this.src2.setValue(value);
	}

	// Returns Instruction's Source2 <key,value> field (Example: <R1, 100>)
	public KeyValue<String, Integer> getSrc2() {
		return this.src2;
	}

	// Returns Instruction's literal value
	public Integer getLiteral() {
		return this.literal;
	}

	// Sets memory address for Store Instruction
	public void setMemoryAddress(int value) {
		this.memAddress = value;
	}

	// Returns memory address of Instruction if any
	public int getMemoryAddress() {
		return this.memAddress;
	}
	
	// Returns NOP (in case of halt instruction)
	public boolean isNOP() {
		return this.isNOP;
	}

	//Returns Instruction string(Example: MOVC R0 1)
	public String getContent() {
		return content;
	}

	// Set ROB slot for instruction on "Instruction Renaming"
	public void setDestinationRobIndex(int value) {
		destinationRobIndex = value;
	}

	// Get ROB slot for instruction destination field
	public int getDestinationRobIndex() {
		return this.destinationRobIndex;
	}

	// Set ROB slot for Source 1 on Instruction dispatching
	public void setSource1RobIndex(int value) {
		source1RobIndex = value;
	}

	// Get ROB slot for Source 1
	public int getSource1RobIndex() {
		return this.source1RobIndex;
	}

	// Set ROB slot for Source 2 on Instruction dispatching
	public void setSource2RobIndex(int value) {
		source2RobIndex = value;
	}

	// Get ROB slot for Source 2
	public int getSource2RobIndex() {
		return this.source2RobIndex;
	}

	// Returns true if source 1 and 2 are valid
	public boolean isSourceValid() {
		return this.sourceValid;
	}

	// Set if source 1 and 2 are valid
	public void isSourceValid(boolean value) {
		sourceValid = value;
	}
	
	// Set rename slot on Instruction dispatching
	public void setRenamedSlot(int value) {
		renamedSlot = value;
	}

	// Get rename slot on Instruction dispatching
	public int getrenamedSlot() {
		return this.renamedSlot;
	}

	// Return TYPE of "virtual function unit" (VFU) needed based on operation
	public String getFUType() {
		if (this.operation.equals(Consts.LOAD)
				|| this.operation.equals(Consts.STORE)) {
			return Consts.MEMORY;
		}
		if (this.operation.equals(Consts.MUL))
			return Consts.MULTIPLIER;
		else
			return Consts.INTEGER;
	}
	// endregion
}