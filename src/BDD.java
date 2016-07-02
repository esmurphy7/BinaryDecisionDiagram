import java.util.*;

public class BDD
{
    private List<Instruction> instructionSet = new ArrayList<Instruction>();

    public int n;   // number of variables
    public int s;   // number of nodes

    BDD ( Scanner in )
    {
        // read header (n & s)
        n = Integer.parseInt(in.next());
        s = Integer.parseInt(in.next());

        // for each line of s lines
        for(int i = 0; i < s; i++)
        {
            // parse line into instruction object
            int var = Integer.parseInt(in.next());
            int lo = Integer.parseInt(in.next());
            int hi = Integer.parseInt(in.next());

            Instruction I = new Instruction(var, lo, hi);
            instructionSet.add(I);
        }

        // reverse the instruction set since they are read from the file in opposite order (I0 is last)
        Collections.reverse(instructionSet);

        // make the list readonly since order matters
        instructionSet = Collections.unmodifiableList(instructionSet);
    }

    /*
    *   Check if all nodes have been counted
    */
    private boolean allCountRecordsCounted(List<BDDNodeCountRecord> countRecords)
    {
        for(BDDNodeCountRecord record : countRecords)
        {
            if(!record.hasBeenCounted)
            {
                return false;
            }
        }

        return true;
    }

    private int calculateCountOfLeaf(int v)
    {
        int count = 0;

        int numVarsSkipped = this.n - v;
        if(numVarsSkipped <= 0)
        {
            count = 1;
        }
        else
        {
            count = (int)Math.pow((double)2, (double)numVarsSkipped);
        }

        return count;
    }

    long countBDD ()
    {
        // FOR YOU TO CODE
        // Counts the number of solutions

        // initialize list of count records for each instruction
        List<BDDNodeCountRecord> countRecords = new ArrayList<BDDNodeCountRecord>();
        for (Instruction instr : instructionSet)
        {
            countRecords.add(new BDDNodeCountRecord(instr));
        }

        // make the list readonly since order matters
        countRecords = Collections.unmodifiableList(countRecords);

        // for each leaf instruction
        for(BDDNodeCountRecord c : countRecords)
        {
            // if c's LO and HI both equal 0 or 1 (is a leaf instruction)
            if(c.instruction.isLeafInstruction())
            {
                // c's count is sum of its HI and LO counts
                int HIcount = 0;
                if(c.instruction.HI == 1)
                {
                    HIcount = calculateCountOfLeaf(c.instruction.V);
                }

                int LOcount = 0;
                if(c.instruction.LO == 1)
                {
                    LOcount = calculateCountOfLeaf(c.instruction.V);
                }
                c.count = HIcount + LOcount;

                // mark c as counted and continue
                c.hasBeenCounted = true;
            }
        }

        // until all count records are marked as counted,
        while(!allCountRecordsCounted(countRecords))
        {
            // for each uncounted record
            for (BDDNodeCountRecord c : countRecords)
            {
                // ignore counted records
                if(c.hasBeenCounted)
                {
                    continue;
                }

                // get c's HI count
                int HIcount = 0;
                // if c's HI is a leaf node, calculate how many solutions it contributes
                if(c.instruction.HI == 1)
                {
                    HIcount = calculateCountOfLeaf(c.instruction.V);
                }
                // otherwise, get the count from c's HI record
                else
                {
                    BDDNodeCountRecord HIrec = countRecords.get(c.instruction.HI);
                    if(HIrec.hasBeenCounted)
                    {
                        HIcount = HIrec.count;
                    }
                }

                // get c's LO count
                int LOcount = 0;
                // if c's LO is a leaf node, calculate how many solutions it contributes
                if(c.instruction.LO == 1)
                {
                    calculateCountOfLeaf(c.instruction.V);
                }
                // otherwise, get the count from c's LO record
                else
                {
                    BDDNodeCountRecord LOrec = countRecords.get(c.instruction.LO);
                    if(LOrec.hasBeenCounted)
                    {
                        LOcount = LOrec.count;
                    }
                }

                // set c's count and mark it as counted
                c.count = HIcount + LOcount;
                c.hasBeenCounted = true;
            }
        }

        // return the count for the root of the entire BDD
        BDDNodeCountRecord rootRecord = countRecords.get(countRecords.size()-1);
        return rootRecord.count;

    }

    void listBDD ()
    {
        // FOR YOU TO CODE
        // Outputs all solutions, one per line, in format of example below
    }

    int[] polyBDD ()
    {
        // FOR YOU TO CODE
        // Outputs the coefficients of the generating function (a polynomial)
        // E.g., (19) in the text.
        return null;
    }

    int[] maxBDD ( int[] w )
    {
        // FOR YOU TO CODE
        // Maximizes a linear function, put the max value at index location 0.
        return null;
    }

}