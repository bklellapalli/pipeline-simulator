/**
 * @author Balakrishna Lellapalli
 *
 */
public class FunctionUnit {

	//In EX stage:  Perform Arithmetic operation on input operands and store in destination field of Instruction Object
	public static Instruction executeInstruction(Instruction instruction) {

		switch (instruction.getOperation()) {

		// Register to Register Instruction
		case Consts.ADD:
			instruction.setDestination(instruction.getSrc1().getValue()
					+ instruction.getSrc2().getValue());
			break;
		case Consts.SUB:
			instruction.setDestination(instruction.getSrc1().getValue()
					- instruction.getSrc2().getValue());
			break;
		case Consts.MUL:
			instruction.setDestination(instruction.getSrc1().getValue()
					* instruction.getSrc2().getValue());
			break;
		case Consts.AND:
			instruction.setDestination(instruction.getSrc1().getValue()
					& instruction.getSrc2().getValue());
			break;
		case Consts.OR:
			instruction.setDestination(instruction.getSrc1().getValue()
					| instruction.getSrc2().getValue());
			break;
		case Consts.EXOR:
			instruction.setDestination(instruction.getSrc1().getValue()
					^ instruction.getSrc2().getValue());
			break;
		//Move literal/ Source value into destination field 
		case Consts.MOV:
		case Consts.MOVC:
			if (instruction.getSrc1() != null)
				instruction.setDestination(instruction.getSrc1().getValue());
			else
				instruction.setDestination(instruction.getLiteral());
			break;
		//Memory Instruction(Load from memory/ Store in memory)
		case Consts.LOAD:
		case Consts.STORE:
			if (instruction.getSrc2() != null)
				instruction.setMemoryAddress(instruction.getSrc1().getValue()
						+ instruction.getSrc2().getValue());
			else
				instruction.setMemoryAddress(instruction.getSrc1().getValue()
						+ instruction.getLiteral());
			break;
		//Control Flow Instruction(unconditional jump instruction)	
		case Consts.JUMP:
			instruction.setSrc1(instruction.getSrc1().getValue() + 
					instruction.getLiteral());
			break;
		default:
			break;
		}
		return instruction;
	}
}