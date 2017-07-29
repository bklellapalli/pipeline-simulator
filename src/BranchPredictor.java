/**
 * @author Balakrishna Lellapalli
 *
 */
public class BranchPredictor {
	
	private static boolean isTaken = false;
	private static boolean isPredictionCorrect = false;
	
	// Make Prediction as "taken" if literal value of the previous instruction is negative 
	//Make prediction as "not taken" if literal value of the previous instruction is positive
	public static boolean predictionTaken(Instruction instruction){
		isTaken = instruction.getLiteral() < 0;
		return isTaken;
	}
	//Update current PC address in case of branch instruction
	// so that it could read instruction from the target address from next cycle
	public static int getUpdatedPCAddress(Instruction instruction){
		int currentPC = 0;
		switch(instruction.getOperation()){
		//Control Flow Instruction	
		case Consts.BZ:
		case Consts.BNZ: 
			currentPC = instruction.getInstructionAddress() + 
			instruction.getLiteral();
			break;
		}
		return currentPC;
	}
	//EX Stage: Verify if prediction taken for the branch instruction is correct or incorrect
	public static boolean isPredictionCorrect(Instruction instruction) {
		
		switch(instruction.getOperation()) {
		case Consts.BZ :
			if((isTaken && instruction.getSrc1().getValue() == 0) || 
					(!isTaken && instruction.getSrc1().getValue() != 0))
				isPredictionCorrect = true;
			break;
		case Consts.BNZ :
			if((isTaken && instruction.getSrc1().getValue() != 0) || 
					(!isTaken && instruction.getSrc1().getValue() == 0))
				isPredictionCorrect = true;
			break;
		}
		return isPredictionCorrect;
	}
}