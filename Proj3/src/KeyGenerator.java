//
// Names: Omaid Khan && Jose Hernandez
// Course: CS 342 Spring 2016 - Troy UIC
// Assignment: RSA Encryption and Decryption
// Date: 15 March 2016
//

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

public class KeyGenerator
{
    private BigInteger p;
    private BigInteger q;


    // Constructor
    public KeyGenerator(BigInteger pVal, BigInteger qVal)
    {
        p = pVal;
        q = qVal;

    }

    public boolean isPrime(BigInteger n )
    {

        if(( Integer.parseInt(n.toString()) <= 0) || Objects.equals(n.toString(), "1"))
            return false;

        if(Objects.equals(n.mod(new BigInteger("2")), new BigInteger("0")))
            return false;
        // non-even numbers below 8 are all prime: 1 3 5 7
        if(Objects.equals(n.toString(), "3") || Objects.equals(n.toString(), "5") || Objects.equals(n.toString(), "7")) {
            return true;
        }
        // search for possible factors
        for( int j = 3; j < Integer.parseInt(n.divide(new BigInteger("2")).toString()); j += 2 )
        {
            // is j a factor?
            //if( n % j == 0 )
            if ( (Objects.equals(n.divide(new BigInteger(String.valueOf(j))).toString(), "0")))
            {
                JOptionPane.showMessageDialog(null, n.toString() + " is not prime. Try Again");
                return false;
            }
        }

        return true;

    }

    public boolean create() throws IOException
    {
        if(!isPrime(p))
        {
            JOptionPane.showMessageDialog(null, p.toString() + " is not Prime. Try Again");
            return false;
        }
        if(!isPrime(q))
        {
            JOptionPane.showMessageDialog(null, q.toString() + " is not Prime. Try Again");
            return false;
        }

        // both numbers are prime.
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        if(phi.compareTo(new BigInteger("127")) <= 0)
        {
            JOptionPane.showMessageDialog(null, "The values for p and q used do not have a product greater than 127. Try Again");
            return false;
        }
       // System.out.println("1 CheckPoint");
        // find largest suitable e
        BigInteger w = n.subtract(BigInteger.ONE);
        while(Objects.equals(w, new BigInteger(phi.toString())) || !areRelativelyPrime(w, phi) )
        {
            if(Objects.equals(w.toString(), "1"))
            {
                JOptionPane.showMessageDialog(null, "Unable to find valid value for e.");
                return false;
            }
            w = w.subtract(new BigInteger("1"));
        }
        BigInteger e = w;
//        System.out.println("2 CheckPoint");

//        // search (inefficiently) for d
//        int d = 3;
//        while( (M_e*d) % M_phi != 1 )
//        {
//            // TODO: Try to find out if this can ever happen
//            if( d == M_n || d == M_phi ) throw new TException( "Unable to find valid value for d" );
//            d++;
//        }
//        M_d = d;

        BigInteger dTemp = new BigInteger("3");

        while(((e.multiply(dTemp)).mod(phi)).compareTo(BigInteger.ONE) != 0)
        //while(!Objects.equals(((e.multiply(dTemp)).mod(phi)).toString(), "1"))
        {
            if( dTemp.compareTo(n) == 0 || dTemp.compareTo(phi) == 0)
            {
                JOptionPane.showMessageDialog(null, "Unable to find valid value for d.");
                return false;
            }
            dTemp = dTemp.add(BigInteger.ONE);
          //  System.out.println(dTemp + "; 2.1 CheckPoint");

        }

        BigInteger d = dTemp;

        String pubFileName = JOptionPane.showInputDialog(null, "Enter a file name for the public key.");
        while(pubFileName.equals(""))
        {
            pubFileName = JOptionPane.showInputDialog(null, "Try Again... Enter a file name for the public key.");
        }
//        System.out.println("4 CheckPoint");

        FileWriter pubKeyFile = new FileWriter(pubFileName+".xml");
        pubKeyFile.write(
                "<rsakey>\r\n" +
                        "\t<evalue>"+ e.toString() +"</evalue>\r\n" +
                        "\t<nvalue>"+ n.toString() +"</nvalue>\r\n" +
                        "</rsakey>\r\n");
        pubKeyFile.close();

        String privFileName = JOptionPane.showInputDialog(null, "Enter a file name for the private key.");
        while(privFileName.equals(""))
        {
            privFileName = JOptionPane.showInputDialog(null, "Try Again... Enter a file name for the public key.");
        }

        FileWriter privKeyFile = new FileWriter( privFileName+".xml");
        privKeyFile.write(
                "<rsakey>\r\n" +
                        "\t<dvalue>" + d.toString() + "</dvalue>\r\n" +
                        "\t<nvalue>" + n.toString() + "</nvalue>\r\n" +
                        "</rsakey>\r\n");
        privKeyFile.close();

        return true;
    }

    boolean areRelativelyPrime( BigInteger a, BigInteger b )
    {
        while( !b.equals(new BigInteger("0") ))
        {
            BigInteger t = b;
            b = a.mod(b);
            a = t;
        }
        return a.equals(new BigInteger("1"));
    }
}
