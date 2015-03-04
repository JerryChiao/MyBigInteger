package com.jerry.math;


public class BigIntegerTest {

	public static void main(String[] args){
		MyBigInteger big = new MyBigInteger("110000");
		MyBigInteger big2 = new MyBigInteger("-200000000000000");
		MyBigInteger results;
		int a = -1100;
		int b = -2000;
		System.out.println(a/b+":"+a%b);
		System.out.println("Begin to test:");
		System.out.println("First argument is " + big.toString());
		System.out.println("Second argument is " + big2.toString());
		results = big.add(big2);
		System.out.println("Add: results = " + results);
		
		results = big.sub(big2);
		System.out.println("Sub: results = " + results);
		
		
		results = big.multiply(big2);
		System.out.println("Muplily: results = "+ results);
		
		
		results = big.divid(big2);
		System.out.println("Divide: results = " + results);
		
		results = big.rem(big2);
		System.out.println("Rem: results = " + results);
		
		
		System.out.println("To Test the Exception:");
		try{
			big = new MyBigInteger("-82934kl");
		}
		catch(BigIntegerException e){
			e.printStackTrace();
		}
		
		try{
		big = new MyBigInteger("a93874");
		}catch(BigIntegerException e){
			e.printStackTrace();
		}
		try{
		big = new MyBigInteger("093874");
		}catch(BigIntegerException e){
			e.printStackTrace();
		}
		
		results = big.divid(new MyBigInteger("0"));
	}

}
