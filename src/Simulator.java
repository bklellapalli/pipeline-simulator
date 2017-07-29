/**
 * @author Balakrishna Lellapalli
 *
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Simulator {
	
	//region Private Member(s)
	
	private static int currentPC;
	private static boolean isFetchInstruction;
	private static int currentFilePointer;
	private static boolean isIntFUAvailable;
	private static boolean isMemFUAvailable;
	private static boolean isMulFUAvailable;
	private static Map<String, Instruction> stages;
	private static Map<String, Instruction> latches;
	private static int[] memory;
	private static Map<String, Integer> architecturalRegisterFile;
	private static Instruction nextInstruction;
	private static boolean stopExecution;
		
	//endregion
	
	//region Private Method(s)
	
	// Sets default value
	private static void Initialize() {
		currentPC = 20000;
		currentFilePointer = 0;
		isFetchInstruction = true;
		isIntFUAvailable = true;
		isMulFUAvailable = true;
		isMemFUAvailable = true;
		memory = new int[10000];
		stages = new HashMap<String, Instruction>();
		latches = new HashMap<String, Instruction>();
		architecturalRegisterFile = new HashMap<String, Integer>(8);
		clearFUStages();
	}
	
	// Flush register values(Fill Fetch 1 and Fetch 2 stage with NOP instruction)
	private static void flushFetchStages(){
		stages.put(Consts.STAGES_FETCH1, new Instruction());
		stages.put(Consts.STAGES_FETCH2, new Instruction());
		latches.put(Consts.STAGES_FETCH1, new Instruction());
		latches.put(Consts.STAGES_FETCH2, new Instruction());
	}
	
	//Clear Functional unit stages by adding NOP instruction
	private static void clearFUStages() {
		stages.put(Consts.STAGES_INT, new Instruction());

		stages.put(Consts.STAGES_MUL1, new Instruction());
		stages.put(Consts.STAGES_MUL2, new Instruction());
		stages.put(Consts.STAGES_MUL3, new Instruction());
		stages.put(Consts.STAGES_MUL4, new Instruction());

		stages.put(Consts.STAGES_MEM1, new Instruction());
		stages.put(Consts.STAGES_MEM2, new Instruction());
		stages.put(Consts.STAGES_MEM3, new Instruction());
	}

	// Set rename slot in case of Branch Instruction, Update PC counter based on branch prediction
	private static Instruction updateBranchInFetchI(Instruction instruction) {
		if (instruction != null && !instruction.isNOP() 
				&& Consts.CONDITIONAL_BRANCH.contains(instruction.getOperation())) {
			
			int currentPC = instruction.getInstructionAddress();	
			instruction = new Instruction(instruction.getOperation(), null,
					nextInstruction.getDestination(), null, instruction.getLiteral(),
					instruction.getContent());
			
			instruction.setInstructionAddress(currentPC);
			for(Entry<String, String> renameEntry : Rename.getRenameTable().entrySet()){				
				if(nextInstruction.getDestination().getKey().equals(renameEntry.getKey())) {
					String[] rntValue = renameEntry.getValue().split(" ");
					instruction.setRenamedSlot(Integer.valueOf(rntValue[0]));
					break;
				}
			}			

			stages.put(Consts.STAGES_FETCH1, new Instruction());
			stages.put(Consts.STAGES_FETCH2, new Instruction());
			if(BranchPredictor.predictionTaken(instruction)) {
				System.out.println("Taken");
				currentPC = BranchPredictor.getUpdatedPCAddress(instruction);
				currentFilePointer = currentPC - 20000;
			} else {
				System.out.println("NotTaken");
				currentPC = instruction.getInstructionAddress() + 1;
				currentFilePointer = currentPC - 20000;
			}
		}
		return instruction;
	}
	
	// FETCH 1 Stage (Fetch instruction from File)
	private static void simulateFetchIStage() throws IOException {
		Parser parser = new Parser();
		
		if(stopExecution) // In case of Halt, break
			return;
		
		if (isFetchInstruction) {
			String instructionStatement = FileOperation.getContent(currentFilePointer);
			Instruction instruction = parser.parseInstruction(instructionStatement);
			instruction.setInstructionAddress(currentPC);
			currentFilePointer++;
			currentPC++;
			if (stages.containsKey(Consts.STAGES_FETCH1)) {
				latches.put(Consts.STAGES_FETCH1, stages.get(Consts.STAGES_FETCH1));
			}
			stages.put(Consts.STAGES_FETCH1, instruction);
		}
	}
	
	// FETCH 2 Stage
	private static void simulateFetchIIStage() throws IOException {
		moveInstruction(Consts.STAGES_FETCH2, Consts.STAGES_FETCH1);
	}
	
	// DECODE 1 Stage
	private static void simulateDecodeIStage() throws IOException {
		nextInstruction = stages.get(Consts.STAGES_DECODE1);
		moveInstruction(Consts.STAGES_DECODE1, Consts.STAGES_FETCH2);
		Instruction instruction = updateBranchInFetchI(stages.get(Consts.STAGES_DECODE1));
		stages.put(Consts.STAGES_DECODE1, instruction);
		
		Instruction instr = stages.get(Consts.STAGES_DECODE1);
		if(instr != null && !instr.isNOP() && instr.getOperation().equals(Consts.HALT)){
			stopExecution = true;
			flushFetchStages();
			stages.put(Consts.STAGES_DECODE1, new Instruction());
		}
	}
	
	// DECODE 2 Stage
	private static void simulateDecodeIIStage() {	
		moveInstruction(Consts.STAGES_DECODE2, Consts.STAGES_DECODE1);
		Instruction instruction = stages.get(Consts.STAGES_DECODE2);		
		if (instruction != null && !instruction.isNOP()) {	
			isFetchInstruction = false;			
			
			if (instruction.getOperation().equals(Consts.STORE) || 
					Consts.CONDITIONAL_BRANCH.contains(instruction.getOperation())) {
				
				instruction = Rename.readSourceOperands(instruction);
				isFetchInstruction = Queue.addToQueue(instruction);
			
			} else {
				if (Rename.isROBAvailable()) {
					instruction = Rename.renameInstruction(instruction);
					isFetchInstruction = Queue.addToQueue(instruction);
				}
			}
		}
		// Remove read if valid bit is set as 1
		Rename.retireROBEntry();
	}
	
	// Execute Stage
	private static void dispatchInstruction() {
		if (isMulFUAvailable) {
			stages.put(Consts.STAGES_MUL4, new Instruction());
			isMulFUAvailable = VirtualFunctionUnit.mulFUExecution(Queue
					.pullIQInstruction(Consts.MULTIPLIER));
		} else {
			// Add NOP Instruction
			VirtualFunctionUnit.mulFUExecution(null);
		}
		if (isMemFUAvailable) {
			stages.put(Consts.STAGES_MEM3, new Instruction());
			latches.put(Consts.STAGES_MEM1, stages.get(Consts.STAGES_MEM1));
			VirtualFunctionUnit.memFUExecution();
		}
		if (isIntFUAvailable) {
			stages.put(Consts.STAGES_INT, new Instruction());
			isIntFUAvailable = VirtualFunctionUnit.intFUExecution();
		}
	}
			
	//Forward result to ROB using ROB index
	private static void forwardTagBasedResult() {
		if (!stages.get(Consts.STAGES_MUL4).isNOP()) {
			Rename.forwardResult(stages.get(Consts.STAGES_MUL4));	
			isMulFUAvailable = true;
			
			if(!stages.get(Consts.STAGES_MEM3).isNOP())
				isMemFUAvailable = false;
			
		} else if (!stages.get(Consts.STAGES_INT).isNOP()) {
			
			Rename.forwardResult(stages.get(Consts.STAGES_INT));
			isIntFUAvailable = true;
			
			if(!stages.get(Consts.STAGES_MEM3).isNOP())
				isMemFUAvailable = false;
			
		} else if (!stages.get(Consts.STAGES_MEM3).isNOP()) {
			Rename.forwardResult(stages.get(Consts.STAGES_MEM3));
		}
	}
	
	// Simulate instructions for n cycle
	private static void Simulate(int n) throws IOException {
		for (int i = 0; i < n; i++) {
			simulateFetchIStage();
			simulateFetchIIStage();
			simulateDecodeIStage();
			dispatchInstruction();
			simulateDecodeIIStage();
			forwardTagBasedResult();
		}
	}
	
	//Display Result at the end of n cycle
	private static void Display(){
		
		StringBuilder stageValues = new StringBuilder();
		for(Entry<String, Instruction> stage : stages.entrySet()){
			if(stage.getValue() != null)
				stageValues.append(stage.getKey() + " : " + stage.getValue().getContent() + " | ");
		}
		System.out.println("\nPipleline Stages: \n" + stageValues);
		
		StringBuilder arValues = new StringBuilder();
		for(Entry<String, Integer> register : architecturalRegisterFile.entrySet()){
			arValues.append(register.getKey() + " : " + register.getValue() + " | ");
		}
		System.out.println("\nArchitectural Register File: \n" + arValues);
		
		StringBuilder prfValues = new StringBuilder();
		int index = 0;
		for(int registerValue : Rename.getPhysicalRegFile()){
			prfValues.append("P"+ index + " : " + registerValue + " | ");
			index++;
		}
		System.out.println("\nPhysical Register File: \n" + prfValues);

		StringBuilder rntValues = new StringBuilder();
		for(Entry<String, String> renameEntry : Rename.getRenameTable().entrySet()){
			rntValues.append(renameEntry.getKey() + " : " + renameEntry.getValue() + " | ");
		}
		System.out.println("\nRename Register File: \n" + rntValues);	
		
		StringBuilder memValues = new StringBuilder();
		for(int i = 0;i < 100; i++){
			if(memory[i] != 0)
				memValues.append("Mem[" + i +"]" + " : " + memory[i] + " | ");
		}
		System.out.println("\nMemory: \n" + memValues);	
		
		System.out.println("\nIssue Queue: ");
		for(Instruction instruction : Queue.retrieveIsssueQueue()){
			System.out.println(instruction.getContent());
		}
		System.out.println("\nLoad Store Queue: ");
		for(Instruction instruction : Queue.retrieveLoadStoreQueue()){
			System.out.println(instruction.getContent());
		}	
	}
		
	//endregion
	
	//region Public Method(s)
	
	//Return value of architectural register
	public static int getValueFromARF(String key){
		return architecturalRegisterFile.get(key);
	}	
	
	//Insert value in architectural register
	public static void saveValueInARF(String key, int value){
		architecturalRegisterFile.put(key, value);
	}
	
	//Return value of Memory
	public static int getValueFromMemory(int index){
		return memory[index];
	}
	
	//Insert value in Memory
	public static void saveValueInMemory(int index, int value){
		memory[index] = value;
	}
			
	//Insert Instruction in Stage specified by key parameter
	public static void addUpdateInstruction(String key, Instruction value) {
		stages.put(key, value);
	}
	
	//Retrieve Instruction from Stages
	public static Instruction retrieveInstruction(String key) {
		return stages.get(key);
	}
	
	// Move Instruction to next stage using latches
	public static void moveInstruction(String cStage, String pStage) {
		if (stages.containsKey(cStage)) {
			latches.put(cStage, stages.get(cStage));
		}
		if (latches.containsKey(pStage)) {
			stages.put(cStage, latches.get(pStage));
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			System.out.println("Enter cyle to Simulate:");
			String[] arg = scanner.nextLine().split(" ");
			
			Initialize();
			Simulate(Integer.parseInt(arg[0]));
			Display();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		finally{
			if(scanner != null)
				scanner.close();
		}
	}
	//endregion
}