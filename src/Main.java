import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
    public static void main (String[] args)
    {
        Scanner in;

        // if an explicit bdd file was passed, build scanner from it
        if(args.length == 1)
        {
            ClassLoader classLoader = Main.class.getClassLoader();
            File file = new File(classLoader.getResource(args[0]).getFile());
            try
            {
                in = new Scanner(file);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
                return;
            }
        }
        // otherwise, bdd data is streamed through stdin
        else
        {
            in = new Scanner( System.in );
        }

        BDD bdd = new BDD( in );

        System.out.println( "count = "+bdd.countBDD() );

        int[] w = {0,1,-2,-3,4};
        int[] x = bdd.maxBDD( w );
        System.out.print( "max = "+x[0]+" soln = " );
        for (int i=1; i<=bdd.n; ++i) System.out.print( x[i]+" " );

        System.out.println( "\nlisting: " );
        bdd.listBDD();

        System.out.print( "polynomial: " );
        int[] a = bdd.polyBDD();
        for (int i=0; i<=bdd.n; ++i) System.out.print( a[i]+" " );
        System.out.println();
    }
}
