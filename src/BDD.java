import java.util.*;

public class BDD
{
    private List<Instruction> instructionSet = new ArrayList<Instruction>();
    private List<String> solutionSet = new ArrayList<String>();

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
                    LOcount = calculateCountOfLeaf(c.instruction.V);
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

    private void printSolutionSet()
    {
        for(String solution : solutionSet)
        {
            System.out.println(solution);
        }
    }

    void listBDD ()
    {
        // FOR YOU TO CODE
        // Outputs all solutions, one per line, in format of example below

        // generate list of all possible solutions
        List<String> possibleSolutions = new ArrayList<String>();
        for (int i = 0; i < Math.pow(2, n); i++)
        {
            String bin = Integer.toBinaryString(i);

            while (bin.length() < n)
            {
                bin = "0" + bin;
            }

            possibleSolutions.add(bin);
        }

        // for each possible solution
        for(String solution : possibleSolutions)
        {
            // current instruction = BDD root instruction
            Instruction currentInstr = instructionSet.get(instructionSet.size() - 1);

            int i = 0;
            // while current route isn't at end of solution
            while (i < solution.length())
            {
                char currentEdge = solution.charAt(i);
                int targetInstr = 0;

                // determine if the current edge is HI or LO
                if (currentEdge == '1')
                {
                    targetInstr = currentInstr.HI;
                }
                else if (currentEdge == '0')
                {
                    targetInstr = currentInstr.LO;
                }
                // otherwise there was an error in the current solution format
                else
                {
                    System.out.println("Solution format error");
                    throw new NullPointerException();
                }

                // if target instruction is true leaf node
                if(targetInstr == 1)
                {
                    // add solution to set of actual solutions
                    solutionSet.add(solution);
                    break;
                }
                // if target instruction is false leaf node
                else if(targetInstr == 0)
                {
                    break;
                }
                // otherwise move the current instruction down the BDD
                else
                {
                    currentInstr = instructionSet.get(targetInstr);
                }

                // increment current edge
                i++;
            }
        }

        // output the solution set
        printSolutionSet();
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

        // ensure that there is exactly one w for each binary variable
        if(w.length != n)
        {
            System.out.println(String.format("There must be exactly one w coefficient for each of the %d binary variables (there are %d)", n, w.length));
            return new int[0];
        }

        // B1
        // initialize W set
        int[] W = new int[n+1];
        W[n] = 0;

        for(int i = n-1; i >= 0; i--)
        {
            W[i] = W[i+1] + Math.max(w[i], 0);
        }

        // B2
        // initialize m
        int[] m = new int[s-1];
        m[0] = 0;

        // initialize t
        int[] t = new int[s-1];
        t[0] = 0;

        // process k instructions
        for(int k = 1; k < s-1; k++)
        {
            // B3
            Instruction kthInstr = instructionSet.get(k+1);
            int v = kthInstr.V;
            int l = kthInstr.LO;
            int h = kthInstr.HI;

            if(l != 0)
            {
                m[k] = m[l-1] + W[v] - W[instructionSet.get(l).V-1];
            }

            int tempM = 0;
            if(h != 0)
            {
                tempM = m[h-1] + W[v] - W[instructionSet.get(h).V-1] + w[v-1];
            }

            if(l == 0 || tempM > m[k])
            {
                m[k] = tempM;
                t[k] = 1;
            }
        }

        // B4
        // initialize maximizing vector x
        int[] x = new int[n];
        int j = -1;
        int k = s-1;

        do {
            while(j < instructionSet.get(k).V - 2)
            {
                j++;
                x[j] = (w[j] > 0) ? 1 : 0;
            }

            if(k > 1)
            {
                j++;
                x[j] = t[k-1];
                k = (t[k-1] == 0) ? instructionSet.get(k).LO : instructionSet.get(k).HI;
            }
        } while(j < n-1);

        // include the sum of the maximizing binary vector in the first index of x
        int sum = 0;
        for(int i=0; i<x.length; i++)
        {
            sum += x[i] * w[i];
        }
        int[] maxX = new int[x.length+1];
        maxX[0] = sum;
        for (int i=0; i<x.length; i++)
        {
            maxX[i+1] = x[i];
        }

        // return maximizing binary vector with sum at beginning
        return maxX;
    }

}