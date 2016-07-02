/**
 * Created by Evan on 7/2/2016.
 */
public class Instruction
{
    public int V;
    public int LO;
    public int HI;

    public Instruction(int V, int LO, int HI)
    {
        this.V = V;
        this.LO = LO;
        this.HI = HI;
    }

    public boolean isLeafInstruction()
    {
        if(LO == 0 || LO == 1)
        {
            if(HI == 0 || HI == 1)
            {
                return true;
            }
        }

        return false;
    }
}
