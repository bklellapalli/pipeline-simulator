/**
 * @author Balakrishna Lellapalli
 *
 */
public class Parser {

	// Check if it contains literal value field
	private static boolean isNumeric(String str)  
	{  
		try{  
			Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
	}

	// Parse instruction and return Instruction Object to be passed into different stages
	public Instruction parseInstruction(String instr, int pcCounter){
		Instruction instruction = null;
		String [] instrArray = instr.split(" ");
		switch(instrArray[0]){
		case Operations.ADD: //ADD R2 R2 R5
		case Operations.SUB:
		case Operations.MUL: 
		case Operations.AND:
		case Operations.OR:
		case Operations.EXOR:
			instruction = new Instruction(instrArray[0], 
					new KeyValue<String, Integer>(instrArray[1], null), 
					new KeyValue<String, Integer>(instrArray[2], null), 
					new KeyValue<String, Integer>(instrArray[3], null), null, instr);
			break;			
		case Operations.LOAD:  //LOAD R5 R2 32	
		case Operations.STORE: //STORE R4 R3 20 & STORE R4 R5 R6
			if(isNumeric(instrArray[3])){
				instruction = new Instruction(instrArray[0], 
						new KeyValue<String, Integer>(instrArray[1], null), 
						new KeyValue<String, Integer>(instrArray[2], null), 
						null, Integer.parseInt(instrArray[3]), instr);
			}
			else{
				instruction = new Instruction(instrArray[0], 
						new KeyValue<String, Integer>(instrArray[1], null), 
						new KeyValue<String, Integer>(instrArray[2], null), 
						new KeyValue<String, Integer>(instrArray[3], null), null, instr);
			}
			break;	
		case Operations.MOVC: //MOVC R1 2
		case Operations.MOV: //MOV R2 R1
			if(isNumeric(instrArray[2])){
				instruction = new Instruction(instrArray[0], 
						new KeyValue<String, Integer>(instrArray[1], null), 
						null, null, Integer.parseInt(instrArray[2]), instr);
			}
			else{
				instruction = new Instruction(instrArray[0], 
						new KeyValue<String, Integer>(instrArray[1], null), 
						new KeyValue<String, Integer>(instrArray[2], null), null, null, instr);
			}
			break;	
		case Operations.BZ: //BZ 4
		case Operations.BNZ: //BNZ -8
			instruction = new Instruction(instrArray[0], null, null, null, 
					Integer.parseInt(instrArray[1]), instr);
			break;	
		case Operations.JUMP: //JUMP R1 20075	&   JUMP X 0
		case Operations.BAL:  //BAL R7 2
			instruction = new Instruction(instrArray[0], 
					new KeyValue<String, Integer>(instrArray[1], null), null, null, 
					Integer.parseInt(instrArray[2]), instr);
			break;
		case Operations.HALT: //HALT
			instruction = new Instruction(instrArray[0], null, null, null, null, instr);
			break;
		default:instruction = new Instruction();
		break;
		}
		return instruction;
	}
}