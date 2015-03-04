package com.jerry.math;


public class MyBigInteger {
	int sign = 1;
	short[] values;
	MyBigInteger(){}
	MyBigInteger(int k){
		sign = 1;
		values = new short[k];
	}
	MyBigInteger(MyBigInteger old){
		sign = old.sign;
		values = new short[old.values.length];
		values =(short[]) old.values.clone();
		removeZero(this);
	}
	MyBigInteger(short[] valuesIn, int start, int end)
	{
		sign = 1;
		if(valuesIn.length < end+1){
			throw new BigIntegerException("end is out of bound of values in constructor of " + this.getClass().toString());
		}
		if(start >= end){
			throw new BigIntegerException("start is bigger than end");
		}
		
		values = new short[end-start+1];
		for(int i = start; i<=end;i++){
			values[i-start]=valuesIn[i];
		}
		removeZero(this);
	}
	MyBigInteger(String val){
		int len1 = val.length();
		int len = len1;
		boolean isZero = true;
		if("-".equals(val.substring(0,1))){
			len -=1;
			sign = -1;
		}else{
			if("0".equals(val.substring(0, 1))){
				len-=1;
			}else{
				isZero = false;
			}
		}
		
		int t = 1;
		while(t<len1 & isZero){
			String tmp;
			if(t ==len1-1){
				tmp = val.substring(t);
			}else{
				tmp = val.substring(t,t+1);
			}
			if("0".equals(tmp)){
				len-=1;
			}else{
				isZero = false;
			break;
			}
			t++;
		}
		
		if(isZero){
			values = new short[1];
			values[0]=0;
			sign = 1;
			return;
		}// zero
		
		values = new short[len];
		
		for (int i = 0; i < len; i++) {
			String tmp_val;
			if (i == 0) {
			tmp_val = new String(val.substring(len1 - i - 1));
			} else {
				tmp_val = new String(val.substring(len1 - i - 1, len1 - i));
			}

			if (i < len - 1) {
				if (!tmp_val.matches("[0-9]")) {
					values = null;
					sign = -2;
					throw new BigIntegerException(
							"The "
									+ i
									+ " digit of the val is not a number ranging from 0 - 9!");
				}
				values[i] = (short) Integer.parseInt(tmp_val);
			} else {
				if (tmp_val.matches("[1-9]")) {
					values[i] = (short) Integer.parseInt(tmp_val);
				} else {
					if ("0".equals(tmp_val)) {

					} else {
						throw new BigIntegerException(
								"The first bit of the val is not a number between 1 and 9!");
					}
				}
			}
		}
		removeZero(this);
	}
	
	public MyBigInteger add(MyBigInteger addVal){
		int upforward = 0;
		if(addVal.values==null){
			System.out.print("at"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			throw new BigIntegerException("null of parameter!");
		}
		if (addVal.sign*this.sign <0 ){
			//call 减法
			MyBigInteger newAddVal = new MyBigInteger(addVal);// new constructor
			newAddVal.sign = this.sign;
			return sub(newAddVal);
		}
		
		int len1 = addVal.values.length;
		int len2 = this.values.length;
		int maxLen = Math.max(len1, len2);
		MyBigInteger results = new MyBigInteger(maxLen);
		results.sign = this.sign;
		int minLen = Math.min(len1,len2);
		
		for(int i = 0; i< minLen; i++){
			int tmp = (this.values[i] + addVal.values[i]) + upforward;
			if(tmp >9){
				upforward = 1;
				tmp -=10;
			}else{
				upforward = 0;
			}
			results.values[i] =(short)tmp;
		}
		
		for(int i = minLen; i< maxLen; i++){
			if(minLen == len1){
				results.values[i] =(short)(this.values[i] + upforward);
				
			}else{
				results.values[i] =(short)(addVal.values[i] + upforward);
			}
			upforward = 0;
		}
			return results;
	}
	
	public MyBigInteger sub(MyBigInteger subVal){
		if(subVal.values==null){
			System.out.print("at"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			throw new BigIntegerException("null of parameter!");
		}
		if(subVal.sign*this.sign<0)
		{
			MyBigInteger newSubVal = new MyBigInteger(subVal);
			newSubVal.sign = this.sign;
			return add(newSubVal);
		}

		if(this.sign<0){// (-) - (-) =>  (+) - (+) change to subVal - this
			return subOperation(subVal,this);	
		}else{// + - (+) ,change to this - subVal
			
			return subOperation(this,subVal);
		}
	}
	
	
	private MyBigInteger subOperation(MyBigInteger sub1, MyBigInteger sub2){// ignore the sign, the input
		int len1 = sub1.values.length;
		int len2 = sub2.values.length;
		int minLen = Math.min(len1,len2);
		int maxLen = Math.max(len1, len2);
		MyBigInteger results = new MyBigInteger(maxLen);
		int downforward = 0;
		for(int i = 0; i < minLen; i++){
			int tmp =sub1.values[i]-sub2.values[i] - downforward;
			if(tmp <0){
				downforward = 1;
				tmp += 10;
			}else{
				downforward = 0;
			}
			results.values[i]=(short)tmp;
		}
		int tmp = 0;
		for(int i = minLen; i< maxLen; i++){
			if(maxLen == len1){
				tmp = sub1.values[i] - downforward;
				if(tmp<0){
					downforward=1;
					tmp+=10;
				}else
				{
					downforward = 0;
				}
			}else
			{
				tmp = 0 - sub2.values[i]-downforward;
				if(tmp<0)
				{
					downforward = 1;
					tmp+= 10;
				}else{
					downforward = 0;
				}
			}
			results.values[i] = (short)tmp;
		}
		
		if (downforward > 0){
			int tmp_downforward = 0;
			for(int i = 0; i < maxLen; i++){
				tmp = 0 -results.values[i] - tmp_downforward;
				if(tmp < 0){
					tmp_downforward=1;
					tmp+=10;
				}else{
					tmp_downforward = 0;
				}
				results.values[i] = (short)tmp;
			}
			results.sign = -1;
		}else
		{
			results.sign = 1;
		}
		
		results = removeZero(results);
		return results;
	}
	
	
	public MyBigInteger multiply(MyBigInteger val){
		if(val.values == null){
			System.out.print("at"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			throw new BigIntegerException("null of parameter!");
		}
		int len1 = this.values.length;
		int len2 = val.values.length;
		int upforward = 0;
		int tmp = 0;
		MyBigInteger results = new MyBigInteger(len1+len2); //to figures multiplied, maximum bit is sum of two -1.
		for(int i = 0; i < len2; i++){
			for(int j = 0; j < len1; j++){
				tmp = this.values[j]*val.values[i] + upforward;
				if(tmp >9){
					upforward = tmp/10;
					tmp  =  tmp % 10;
				}
				else{
					upforward = 0;
				}
				results.values[i+j]+=tmp;
			}
		}

		if(upforward>0)// Highest bit operation
		{
			results.values[len1+len2-1]=(short)upforward;
		}
		
		results.sign = this.sign*val.sign;//乘法符号规则
		return removeZero(results);

	}
	
	boolean BigIntegerCompare(MyBigInteger num1, MyBigInteger num2){
		if(num1.values==null || num2.values==null){
			System.out.print("at"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			throw new BigIntegerException("null of parameter!");
		}
		
		return num1.sub(num2).sign == 1;
	}
	/**
	 * @author 80102098
	 * @param val:denuminator,
	 * @return the quotient, MyBigInteger
	 */
	
	public MyBigInteger divid(MyBigInteger val){
		// Exception handling
		if(val.values==null){
			System.out.print("at"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			throw new BigIntegerException("null of parameter!");
		}
		
		if(val.isZero()){
			throw new BigIntegerException("denominator is zero!!");
		}
		int tmp = 0;
		int len1 = this.values.length;
		int len2 = val.values.length;
		MyBigInteger results = new MyBigInteger(len1);
		//int resultIndex = len1-1;
		// num process;
		
		if(len1 < len2){//被除数的位数少于除数，直接得0
			results.values=new short[1];
			results.values[0]=0;
		}else{
			//除数与被除数修正
			MyBigInteger num = new MyBigInteger(this);
			num.sign = 1;
			MyBigInteger denum = new MyBigInteger(val);
			denum.sign = 1;
			MyBigInteger bigTmp ;//计算当前商数的临时被除数
			int i =len1-len2;//计数器，计算当前计算结果的位置
			//resultIndex = len1 - len2;
			// 计算商的数值，暂不考虑符号
			while(i>=0){
				bigTmp = new MyBigInteger(num.values,i,num.values.length-1);
				if(!BigIntegerCompare(bigTmp,denum)){
					i--;
					continue;
				}else{
					tmp = 0;
					//bigTmp = new BigIntegerTest(num);
					while(BigIntegerCompare(bigTmp,denum)){
						tmp++;
						bigTmp = bigTmp.sub(denum);
					}
					results.values[i]=(short)tmp;//存储商
					
					short[] tmpshort = new short[i+bigTmp.values.length];//结合商和原始被除数，生成新被除数
					for(int j = 0; j<tmpshort.length;j++){
						if(j<i){
							tmpshort[j]=num.values[j];//低位补齐
						}else{
							tmpshort[j]=bigTmp.values[j-i];//加入玉树
						}
					}
					num.values= tmpshort;
					i--;
				}
			}
		}
		
		
		// sign process处理符号
		if(this.sign*val.sign<0){
			//BigIntegerTest tmp_div = new BigIntegerTest("-1");
			results.sign = -1;
			//results = results.add(tmp_div);
		}else{
			results.sign = 1;
		}
		
		
		return removeZero(results);
	}
	/**
	 * @author 80102098
	 * @param null
	 * 
	 * @return if the num is zero. return true;
	 */
	
	boolean isZero(){
		for(int i = 0; i<this.values.length;i++){
			if(this.values[i]!=0){
				return false;
			}
		}
		return true;
	}
	/**
	 * @author 80102098
	 * @param null
	 * turn the results to String.
	 */
	public String toString(){
		StringBuffer results = new StringBuffer();
		int len= this.values.length;
		if(this.sign<0)
			results.append("-");
		// Note, the bit representation is reversed to the String representation.
		for(int i = len; i>0;i--){
			results.append(Integer.toString((int)this.values[i-1]));
		}
		return results.toString();
	}
	
	public MyBigInteger rem(MyBigInteger divVal){
		if(divVal.values==null){
			System.out.print("at"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			throw new BigIntegerException("null of parameter!");
		}
		MyBigInteger results = this.divid(divVal);
		MyBigInteger dd = results.multiply(divVal);
		return this.sub(dd);
	}
/**
 *@author 80102098
 *@param values to be dealed
 *@return MyBigInteger, the results that the 0sof the high bits are removed
 */

	private MyBigInteger removeZero(MyBigInteger val){
		int len = val.values.length;
		int tmp = len;
		while(val.values[tmp-1]==0){
			tmp--;
			if(tmp==0){
				val.values=new short[1];
				val.values[0]=0;
				val.sign = 1;
				return val;
			}
		}
		if(tmp < len){
			MyBigInteger newResults = new MyBigInteger();
			newResults.values = new short[tmp];
			
			for(int i = 0; i< tmp;i++){
				newResults.values[i]=val.values[i];
			}
			newResults.sign = val.sign;
			return newResults;
		}
		return val;
	}

}
