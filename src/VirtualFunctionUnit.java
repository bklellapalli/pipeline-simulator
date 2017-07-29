/**
 * @author Balakrishna Lellapalli
 *
 */
public class VirtualFunctionUnit {

	//Integer Virtual function
	public static boolean intFUExecution() {	
		Instruction instruction = Queue.pullIQInstruction(Consts.INTEGER);	
		
		if(instruction.isNOP())
			return true;
		
		if(Consts.CONDITIONAL_BRANCH.contains(instruction.getOperation())) {
			if(!BranchPredictor.isPredictionCorrect(instruction)) {
				//Correct prediction. Do nothing
				return true;
			} else {
				System.out.println("--Incorrect Prediction--");
				//instruction.getrenamedSlot();
				//instruction.getSrc1().getValue();
			}
			
		} else {
			
			instruction = FunctionUnit.executeInstruction(instruction);	
			Simulator.addUpdateInstruction(Consts.STAGES_INT, instruction);
		}
		return false;
	}	
	
	// MUL Virtual Function Unit
	public static boolean mulFUExecution(Instruction instruction) {

		if (instruction == null) {
			if (!Simulator.retrieveInstruction(Consts.STAGES_MUL1).isNOP()) {
				Simulator.addUpdateInstruction(Consts.STAGES_MUL2,
						Simulator.retrieveInstruction(Consts.STAGES_MUL1));
				Simulator.addUpdateInstruction(Consts.STAGES_MUL1,
						new Instruction());

			} else if (!Simulator.retrieveInstruction(Consts.STAGES_MUL2).isNOP()) {
				Simulator.addUpdateInstruction(Consts.STAGES_MUL3,
						Simulator.retrieveInstruction(Consts.STAGES_MUL2));
				Simulator.addUpdateInstruction(Consts.STAGES_MUL2,
						new Instruction());

			} else if (!Simulator.retrieveInstruction(Consts.STAGES_MUL3).isNOP()) {
				Simulator.addUpdateInstruction(Consts.STAGES_MUL4,
						Simulator.retrieveInstruction(Consts.STAGES_MUL3));
				Simulator.addUpdateInstruction(Consts.STAGES_MUL3,
						new Instruction());

			}
		} else {
			if (!instruction.isNOP()) {
				instruction = FunctionUnit.executeInstruction(instruction);
				Simulator.addUpdateInstruction(Consts.STAGES_MUL1, instruction);
				return false;
			}
		}
		return true;
	}
	
	// MEMORY Virtual Function Unit
	public static void memFUExecution(){
		Instruction instruction = Queue.pullLSQInstruction();
		if(!instruction.isNOP()) {
			instruction = FunctionUnit.executeInstruction(instruction);
		}
		Simulator.addUpdateInstruction(Consts.STAGES_MEM1, instruction);
		Simulator.moveInstruction(Consts.STAGES_MEM2, Consts.STAGES_MEM1);
		Simulator.moveInstruction(Consts.STAGES_MEM3, Consts.STAGES_MEM2);
	}
}