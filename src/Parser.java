/**
 * @author Balakrishna Lellapalli
 *
 */
public class Parser {

	// Check if it contains literal value field
	private static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	// Parse instruction and return Instruction Object to be passed into different stages
	public Instruction parseInstruction(String instr) {
		Instruction instruction = null;
		String[] instrArray = instr.split(" ");
		switch (instrArray[0]) {
		case Consts.ADD: // ADD R2 R2 R5
		case Consts.SUB:
		case Consts.MUL:
		case Consts.AND:
		case Consts.OR:
		case Consts.EXOR:
		case Consts.LOAD: // LOAD R5 R2 32
		case Consts.STORE: // STORE R4 R3 20 & STORE R4 R5 R6
			if (isNumeric(instrArray[3])) {
				instruction = new Instruction(instrArray[0],
						new KeyValue<String, Integer>(instrArray[1], null),
						new KeyValue<String, Integer>(instrArray[2], null),
						null, Integer.parseInt(instrArray[3]), instr);
			} else {
				instruction = new Instruction(instrArray[0],
						new KeyValue<String, Integer>(instrArray[1], null),
						new KeyValue<String, Integer>(instrArray[2], null),
						new KeyValue<String, Integer>(instrArray[3], null),
						null, instr);
			}
			break;
		case Consts.MOVC: // MOVC R1 2
		case Consts.MOV: // MOV R2 R1
			if (isNumeric(instrArray[2])) {
				instruction = new Instruction(instrArray[0],
						new KeyValue<String, Integer>(instrArray[1], null),
						null, null, Integer.parseInt(instrArray[2]), instr);
			} else {
				instruction = new Instruction(instrArray[0],
						new KeyValue<String, Integer>(instrArray[1], null),
						new KeyValue<String, Integer>(instrArray[2], null),
						null, null, instr);
			}
			break;
		case Consts.BZ: // BZ 4
		case Consts.BNZ: // BNZ -8
			instruction = new Instruction(instrArray[0], null, null, null,
					Integer.parseInt(instrArray[1]), instr);
			break;
		case Consts.JUMP: // JUMP R1 20075 & JUMP X 0
		case Consts.BAL: // BAL R7 2
			instruction = new Instruction(instrArray[0],
					new KeyValue<String, Integer>(instrArray[1], null), null,
					null, Integer.parseInt(instrArray[2]), instr);
			break;
		case Consts.HALT: // HALT
			instruction = new Instruction(instrArray[0], null, null, null,
					null, instr);
			break;
		default:
			instruction = new Instruction();
			break;
		}
		return instruction;
	}
}