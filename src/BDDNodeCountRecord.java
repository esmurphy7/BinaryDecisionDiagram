/**
 * Created by Evan on 7/2/2016.
 */
public class BDDNodeCountRecord
{
    Instruction instruction;
    int count;
    boolean hasBeenCounted;

    public BDDNodeCountRecord(Instruction instr)
    {
        instruction = instr;
        count = 0;
        hasBeenCounted = false;
    }
}
