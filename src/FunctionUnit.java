/**
 * @author Balakrishna Lellapalli
 *
 */
public class FunctionUnit {

	//In EX stage:  Perform Arithmetic operation on input operands and store in destination field of Instruction Object
	public Instruction executeInstruction(Instruction instruction){

		switch(instruction.getOperation()){

		//Register to Register Instruction (Source1 and Source2)
		case Operations.ADD: 
			instruction.setDestination(instruction.getSrc1().getValue() +  instruction.getSrc2().getValue());
			break;
		case Operations.SUB:
			instruction.setDestination(instruction.getSrc1().getValue() - instruction.getSrc2().getValue());
			break;
		case Operations.MUL:
			instruction.setDestination(instruction.getSrc1().getValue() * instruction.getSrc2().getValue());
			break;
		case Operations.AND:
			instruction.setDestination(instruction.getSrc1().getValue() & instruction.getSrc2().getValue());
			break;
		case Operations.OR:
			instruction.setDestination(instruction.getSrc1().getValue() | instruction.getSrc2().getValue());
			break;
		case Operations.EXOR:
			instruction.setDestination(instruction.getSrc1().getValue() ^ instruction.getSrc2().getValue());
			break;
		//Move literal/ Source value into destination field 
		case Operations.MOV:
		case Operations.MOVC:
			if(instruction.getSrc1() != null)
				instruction.setDestination(instruction.getSrc1().getValue());
			else
				instruction.setDestination(instruction.getLiteral());
			break;

		//Memory Instruction(Load from memory/ Store in memory)
		case Operations.LOAD:
		case Operations.STORE:
			if(instruction.getSrc2() != null)
				instruction.setMemoryAddress(instruction.getSrc1().getValue() +  instruction.getSrc2().getValue());
			else 
				instruction.setMemoryAddress(instruction.getSrc1().getValue() +  instruction.getLiteral());
			break;
		}
		return instruction;
	}

	public Integer predictBranch(Instruction instruction, Integer pDestination, Integer currentPC, Integer registerValue, Integer specialRegister){
		switch(instruction.getOperation()){
		//Control Flow Instruction(Conditional/ unconditional jump instruction)	
		case Operations.BNZ:
			if(pDestination != 0)
				currentPC = currentPC + instruction.getLiteral() - 2; //Relative address
			break;
		case Operations.BZ:
			if(pDestination == 0)
				currentPC = currentPC + instruction.getLiteral() - 2; //Relative address
			break;
		case Operations.JUMP:
			// X is Special register used to store next PC address in case of BAL instruction
			if(!instruction.getDestination().getKey().equals("X"))
				currentPC =  registerValue + instruction.getLiteral() - 2;
			else
				currentPC = specialRegister + instruction.getLiteral();
			break;
		case Operations.BAL:
			currentPC = registerValue +  instruction.getLiteral() - 2;	
			break;
		}
		return currentPC;
	}
}