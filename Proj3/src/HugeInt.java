//
// Names: Omaid Khan && Jose Hernandez
// Course: CS 342 Spring 2016 - Troy UIC
// Assignment: RSA Encryption and Decryption
// Date: 15 March 2016
//

import java.util.Scanner;

//import java.util.*;
//import java.io.*;


public class HugeInt {

    public int []array;
    public int numDig;
	/*
	private int prime1;
	private int prime2;
	private int n;
	private int phi;
	private int result3;
	private int e;
	private int d;
	private int counter;
	
	

	*/

    //private int dtemp;

    public HugeInt(int target){

        int counter=0;
        int toCount= target;

        for(int i=0; target>0; i++){

            counter++;
            target= target/10;
        }
        numDig= counter;
        array= new int [numDig];

        for(int j=0;j<numDig; j++){

            array[j]=toCount%10;
            toCount= toCount/10;
        }

    }


    public HugeInt(String s){

        numDig=s.length();
        array= new int [numDig];

        for(int i=0; i<numDig;i++){

            int tempint= s.charAt(numDig-1-i)-'0';
            array[i]= tempint;
        }
    }


    public HugeInt(int []array2){



        numDig= array2.length;

        array= new int [numDig];

        for(int j=0; j< numDig; j++){

            array[j]= array2[j];
            //if(array2[i]==0)
            //	counter++;
        }
		/*
			if(counter== numDig){
				numDig=1;
				array= new int[1];
				array[0]=0;
			}
			else{
				
				while(array2[numDig-1]==0) 
					
				numDig--;
				array= new int[numDig];	
				
				for(int x=0; x< numDig; x++){
					
					array[x]= array2[x];
				}
			}
			*/
    }
    public HugeInt add(int i){

        return this.add(new HugeInt(i));
    }

    public HugeInt add(HugeInt target){

        int newsize=0;

        if( target.numDig> numDig){

            newsize= target.numDig+1;

        }
        else{

            newsize= numDig+1;

        }
        int [] array3= new int [newsize];

        int transfer=0;

        for(int i=0; i< newsize-1; i++){

            int tmp=0;
            if(i<numDig){

                tmp+=array[i];

            }
            if(i<target.numDig){

                tmp+=target.array[i];

            }

            tmp+= transfer;

            array3[i]= tmp%10;
            transfer= tmp/10;

        }

        if(transfer==1)
            array3[newsize-1]=1;
        else
            array3[newsize-1]=0;

        return new HugeInt(array3);
    }
    public HugeInt sub(int i){

        return this.sub(new HugeInt(i));

    }

    public HugeInt sub(HugeInt target){

        int []array3= new int[numDig];

        int result=0;
        int transfer=0;
        int i=0;
        while(i<numDig){

            int tmp=array[i];

            if(i<target.numDig)
                tmp-= target.array[i];

            tmp-=transfer;
            transfer=0;

            if(tmp<0){

                tmp=tmp+10;
                transfer=1;

            }
            array3[i]= tmp;
            i++;
        }
        return new HugeInt(array3);
    }

    public HugeInt mult(int i){

        return this.mult(new HugeInt(i));

    }

    public HugeInt power(HugeInt exp)
    {
        HugeInt i = exp;
        HugeInt ret = new HugeInt("1");
        while(i.isGreater(new HugeInt("0")))
        {
            ret.mult(this);
            i.sub(1);
        }

        return ret;
    }

    public HugeInt mult( HugeInt target)
    {

        int [] array3= new int [numDig+ target.numDig];

        int transfer=0;

        for(int i=0; i< target.numDig; i++){

            int tmp=0;
            for(int j=0; j< numDig; j++){

                array3[j + i] += transfer + array[j] * target.array[i];
                transfer = array3[j+i] / 10;
                array3[j+i] = array3[j+i] % 10;
            }
            array3[i+numDig ] +=transfer;
        }

        return new HugeInt(array3);

    }



    public boolean isGreater(HugeInt target){
        if( numDig > target.numDig){
            return true;
        }
        else if(numDig < target.numDig){
            return false;
        }
        else{
            for( int i = numDig-1; i >= 0; i--){
                if(array[i] < target.array[i]){
                    return false;
                }
                else if( array[i]  > target.array[i]){
                    return true;
                }
            }
            return false;
            // this means they r equal
        }

    }

    public boolean isEqual(HugeInt target){

        if(numDig!=target.numDig){
            return false;

        }
        else{
            for(int i=numDig-1;i>=0;i--){
                if(array[i]!= target.array[i]){
                    return false;
                }
            }
            return true;
        }
    }


    public boolean isGreaterthanEqual(HugeInt target){
        return this.isGreater(target) || this.isEqual(target);
    }


    public boolean isLessthan(HugeInt target){

        return !this.isGreater(target) && !this.isGreater(target);
    }


    HugeInt leftover=null;

    public HugeInt div(HugeInt target){


        HugeInt dividend= this;//new HugeInt(0);
        HugeInt c= new HugeInt(0);


        while(dividend.isGreaterthanEqual(target)||dividend.isEqual(target)){
            dividend=dividend.sub(target);
            c=c.add(new HugeInt(1));
        }
        if(!dividend.isGreaterthanEqual(target)){
            leftover= dividend;
        }


        //int [] result= new int [numDig+ target.numDig];

        //HugeInt [] array1= new HugeInt[numDig];
        //HugeInt [] finalResult= new HugeInt[numDig];

        //array1[0]= target.add(0);
        //finalResult[0]= new HugeInt(1);

        //int lenght=0;
	    /*
	    for(int i=0; i<numDig; i++){
	    	
	    	finalResult[i]= finalResult[i-1].mult(y);
	    	array1[i]= array1[i-1].mult(y);
	    	
	    	if(array1[i].isGreater(this)){
	    		
	    		lenght=i;
	    		i=numDig;
	    		
	    	}
	    	
	    	
	    }
	    
	    for(int j=0; j< lenght; j++){
	    	
	    	
	    }
	    
	    for(int z=lenght-1; z>=0; z--){
	    	
	    	while(remainder.isGreaterthanEqual(array1[z])){
	    		
	    		remainder=remainder.sub(array1[z]);
	    		dividend=dividend.add(finalResult[z]);
	    		System.out.println(remainder);
	    		
	    	}
	    }
	    */

        return c;







    }

    public HugeInt mod(HugeInt target){

        this.div(target);
        return leftover;
    }

    public String converter(){

        String result= "";

        int lenght= numDig;

        if(lenght >1){

            while(array[lenght-1]==0 && lenght >1 ) lenght--;
        }
        while(lenght !=0){

            result+=array[lenght-1];
            lenght--;
        }
        return result;

    }
    public String toString() {
        String ans = "";

        int length = numDig;

        if(length > 1){
            while(array[length-1]==0 && length > 1)length--;
        }
        while (length !=0){
            ans += array[length-1];
            length--;
        }
        return ans;
    }
    public HugeInt modinverse(HugeInt target){

        HugeInt x = this;
        x=x.mod(target);

        for(HugeInt i= new HugeInt(1);i.isLessthan(target);i=i.add(1)){
            if((x.mult(i)).mod(target).equals(1));
            return i;
        }
        return new HugeInt(0);



    }
    public int Compare(HugeInt target){

        if(this.numDig<target.numDig)
            return -1;
        else if (this.numDig>target.numDig)
            return 1;
        else{
            for(int i=numDig-1; i>=0;i--){

                if(this.array[i]>target.array[i])
                    return 1;
                else if(this.array[i]<target.array[i])
                    return -1;
            }
            return 0;
        }

    }
	/*
	public static String chartoString(char x){
		
		char[] tmp= new char[1];
		tmp[0]=x;
		return new String(tmp);
	}
		
	*/	
		
	
			
			
		
		
		/*
		
		
		Scanner reader = new Scanner(System.in);
		
		System.out.println("Please enter a number \n");
		
		
		prime1= reader.nextInt();
		
		System.out.println("Please enter ANOTER   number \n");
		
		prime2= reader.nextInt();
		
		n= prime1*prime2;
		
		System.out.println( "value of n:"+ n);
		
		phi= (prime1-1)*(prime2-1);
		
		System.out.println("value of phi:"+phi);
		
		result3=phi;
		
		
		
		while(gcd(result3,phi)!= 1){
			
			result3--;
			System.out.println("testing:"+result3);
		}
		
		e= result3;
		System.out.println("final result e:"+e);
		
		dtemp= mod(1,phi);
		
		counter=phi;
		System.out.println("temp d:"+dtemp);
		
		while(e*counter != dtemp ){
			
			counter--;
			System.out.println(counter);
		}
		
		System.out.println("final d:"+counter);
		
		
		//System.out.println("mode test:"+mod(-4,11));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	//}// huge int first contructor
	public int gcd(int a, int b){
		
		int c;
		while(a != 0){
			
			c=a;
			a=b%a;
			b=c;
		}
		
		return b;
	}
	private int mod(int x, int y)
	{
	    int result = x % y;
	    if (result < 0)
	    {
	        result += y;
	    }
	    return result;
	}
	
	*/








}
