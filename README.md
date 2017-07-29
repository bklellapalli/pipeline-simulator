# pipeline-simulator
Implement a cycle-by-cycle simulator for the simple 5-stage instruction pipeline. Assume that the code to be simulated is stored in a text file with one ascii string representing an instruction (in the symbolic form, such as ADD R1, R4, R6) in each line of the file. 

# The instructions supported are:
1. Register-to-register instructions: ADD, SUB, MOVC, MUL, AND, OR, EX-OR (all done on the ALU in a single cycle). You can assume that the result of multiplying two registers will fi into a single register.
2. Memory instructions: LOAD, STORE
3. Control flow instructions: BZ, BNZ, JUMP, BAL, HALT

Assume that there are 8 architectural registers, R0 through R7. Memory is viewed as a linear array of integer values (4 Bytes wide) numbered 0 through 9999.

The BAL register implements function calls, saves the return address in a special register X and transfers control to a specified target address:

BAL <register>, literal: saves address of next instruction in X and then sets fetch PC to contents of <reg> plus the literal.
MOVC <register> <literal>, moves literal value into specified register

The HALT instruction stops execution. JUMP specifies a register and a literal and transfers control to the address obtained by adding the contents of the register to the literal.

To invoke the simulator, specify the name of executable and name of the ascii file that contains the code to be simulated.

# Initialize 
Initializes the simulator state, sets the PC of the fetch stage to point to the first instruction in the ascii code file, which is assumed to be at address 20000. Each instruction takes 4 bytes of space, so the next instruction is at address 20001, as memory words are 4 Bytes long, just like the integer data items.

# Simulate <n>
Simulates the number of cycles specified as <n> and waits. Simulation can stop earlier if a HALT instruction is encountered and when the HALT instruction is in the WB stage.

# Display
Displays the contents of each stage in the pipeline and the contents of the first 100 memory locations containing data, starting with address 0.

# CLARIFICATIONS TO PART 1:
1. This is the in-order APEX pipeline with 5 stages.
2. Instructions following a branch instruction in the pipeline should be flushed on a taken branch.
3. Forwarding is not used in this version.
4. Any one of the following languages can be used to implement BOTH parts: C, C++ or Java.
5. Must implement in-order execution correctly.

# Function units are present:
1. INT (implements integer and logical instructions) - one stage, single cycle latency.
2. MUL (implements the MUL operation, non-pipelined, latency 4 cycles,).
3. Memory (implements LOAD and STORE instruction, 3 pipeline stages with a single cycle latency per stage.)

The issue queue has 8 entries, the LSQ has 4 entries and the ROB has 16 entries. The physical register file has 16 registers. You can assume that a tag based forwarding mechanism is used.

# CLARIFICATIONS for PART 2:
1. Must implement forwarding to IQ entries as well as to instructions in the decode/rename stages.
2. Must implement tag-based forwarding and register renaming.
3. You can assume cache hits. The cache does not need to be modeled.
4. Assume that all instructions that can alter the control flow use the integer FU to evaluate the branch condition and branching decisions are made at the end of the cycle they spend in the integer FU.
5. BZ, BNZ use a simple prediction based on the sign of the offset; if the offset is negative the prediction is taken, if it is positive the branch is predicted to be not taken. The sign of the offset is NOT known till the branch instruction has just entered the first decode/rename stage. Assume that the target address is available when the sign of the offset is detected. Thus, even with a correct prediction of taken, there is a 2-cycle bubble.
6. On incorrect predictions and on executing the BALR and JUMP instructions, the pipeline needs to be flushed and rename table  entries altered by the flushed instructions have to be correctly restored. On correct predictions of taken, the two instructions following the BZ or BNZ also need to be flushed.
7. The HALT instruction is processed as soon as it enters the first decode/rename stage, Then all instructions ahead of it are allowed to complete and commit. The the pipeline clock is frozen (that is the clock does not advance).
8. Assume that the tag associated with a result is broadcasted one cycle before the actual data. Instructions that are waiting for their last operand can become ready for selection in the same cycle when the data is broadcasted. If the appropriate FU is available, then the instruction is selected and execution begins in the immediately following cycle. 
9. If an instruction is ready and selected for execution, execution can begin only when the FU is free. 
   
