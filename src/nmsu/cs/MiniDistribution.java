/*
Copyright (c) 2014 cs.nmsu.edu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package nmsu.cs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 * Minimal implementation of a Distribution. Designed for reuse and high performance.
 * Chuan add keys5 to hold aspect
 */
public class MiniDistribution implements Serializable {
	//Invariants: (added by Huiping Cao June 5, 2012)
	//(1) keys.length = keys2.length = keys3.length = keys4.length = vals.length 
	//(2) Number of real values in keys, keys2, keys3, keys4, vals is "endposition"
	//(3) vals[i] = probability (key1 = keys[i], key2 = keys2[i], ...)
	private static final long serialVersionUID = 4487678948145657551L;
	
	
	/**
	 * latent state
	 */
	private int[] keys1;
	/**
	 * b
	 */
	private int[] keys2;
	/**
	 * oprime
	 */
	private int[] keys3;
	/**
	 * aspect
	 */
	private int[] keys4;
	private int[] keys5;//useless now
//	sum of distributions
	private double[] vals;
	
//	/**
//	 * beginning index mark for a interval
//	 */
//	private int[] intervalMark;
//	/**
//	 * sum of p() in this interval
//	 */
//	private double[] interval;
//	/**
//	 * next interval index
//	 */
//	private int intervalIndex = 0 ; 
//	
//	private double tmpSum = 0;
	
	int endposition = 0;
	private int key2Draw = 0;
	private int key3Draw = 0;
	private int key4Draw = 0;
	private int key5Draw = 0;
	
	public double totalSum = 0;
	public double sum0 = 0;
	public double sum1 = 0;
	
	public MiniDistribution(int maxEntries) {
		keys1 = new int[maxEntries];
		keys2 = new int[maxEntries];
		keys3 = new int[maxEntries];
		keys4 = new int[maxEntries];
		keys5 = new int[maxEntries];
		vals = new double[maxEntries];
		
	}

	public String toString(){
//		String str = "keys="+Arrays.toString(keys1) +"\n"
//				+ "keys2="+Arrays.toString(keys2) +"\n"
//				+ "keys3="+Arrays.toString(keys3) +"\n"
//				+ "keys4="+Arrays.toString(keys4) +"\n"
//				+ "keys5="+Arrays.toString(keys5) +"\n"
//				+ "vals="+Arrays.toString(vals) +"\n"
//				+ "endposition = "+endposition;
		
		String str = " sum of b=0 is "+String.valueOf(sum0)+" sum of b=1 is "+String.valueOf(sum1);
		return str;
	}

	public void add(int key1, double val) {
		add(key1, 0, val);
	}
	
	public void add(int key1, int key2, double val) {
		add(key1, key2, 0, val);
	}

	public void add(int key1, int key2, int key3, double val) {
		add(key1, key2, key3,0, val);
	}
	
	public void add(int key1, int key2, int key3, int key4, double val){
		add(key1, key2, key3, key4, 0, val);
	}
	
	public void add(int key1, int key2, int key3, int key4,  int key5, double val) {
		assert (!Double.isNaN(val)) : key1 + " " + key2 + " " + key3 + " " + key4+" "+key5;
		totalSum+=val;
		keys1[endposition] = key1;
		keys2[endposition] = key2;
		keys3[endposition] = key3;
		keys4[endposition] = key4;
		keys5[endposition] = key5;
		vals[endposition] = totalSum;
//		tmpSum+=val;
//		
//		int numOfInterval = this.intervalMark.length-1;
//		double lengthOfInterval = MiniDistribution.priSum/numOfInterval;
//		if (tmpSum > lengthOfInterval && intervalIndex<interval.length){
//			this.intervalMark[intervalIndex] =  endposition;
//			this.interval[intervalIndex] = totalSum;
//			this.intervalIndex++;
//			tmpSum = 0.0;
//		}

		endposition++;
	}

	public void put(int key1, int key2, double val) {
		put(key1, key2, 0, val);
	}

	public void put(int key1, int key2, int key3, double val) {
		add(key1, key2, key3, 0, val);
	}
	
	public void put(int key1, int key2, int key3, int key4, double val) {
		add(key1, key2, key3, key4, val);
	}
	
	public void put(int key1, int key2, int key3, int key4, int key5, double val) {
		add(key1, key2, key3, key4, key5, val);
	}


	public void put(int key1, double val) {
		put(key1, 0, val);
	}

	public void clear() {
		endposition = 0;
	}

	public boolean isEmpty() {
		return endposition == 0;
	}

	public double sum() {
//		double result = 0.0;
//		for (int i = 0; i < endposition; i++) {
//			assert (!Double.isNaN(vals[i]));
//			result += vals[i];
//		}
//		return result;
		return totalSum;
	}

	public void normalize() {
		double sum = sum();
		for (int i = 0; i < endposition; i++) {
			assert (!Double.isNaN(vals[i]));
			vals[i] = vals[i] / sum;
			assert (!Double.isNaN(vals[i]));
		}
	}

	/**
	 * Draw a key from the distribution
	 * The corresponding key2 can be retrieved by a subsequent call to getKey2Draw().
	 *
	 * @return drawn key
	 */
	 public int draw() {
		 if (isEmpty()) {
			 throw new RuntimeException(Debugger.getCallerPosition()+" distribution is Empty!");
		 }
//		 double sum = sum();
		 double sum = totalSum;
		 //next time prior sum
//		 MiniDistribution.priSum = sum;
		 
//		 System.out.println(Debugger.getCallerPosition()+"sum= "+sum+" size "+endposition);
		 
		 assert (sum > 0) : sum + " \n" + Arrays.toString(keys1) + " \n" + Arrays.toString(vals);
		 assert (!Double.isNaN(sum));
		 if (Double.isInfinite(sum)) {
			 System.err.println("Too large values. Sums to infinity. " + Arrays.toString(vals));
		 }
		 
		 double rnd = Math.random(); //a random number in [0.0, 1.0)
		 double rdmValue = rnd * sum;
		 
		 //binary search here
		 int startPoint = Util.binarySearch(vals, rdmValue, 0, vals.length-1);
		 if(startPoint > vals.length)
			 throw new RuntimeException(Debugger.getCallerPosition()+"something went wrong here.. remaining seed=" + rdmValue+" total sum "+sum);
		 else if(startPoint == vals.length-1){
			 key2Draw = keys2[startPoint];
			 key3Draw = keys3[startPoint];
			 key4Draw = keys4[startPoint];
			 key5Draw = keys5[startPoint];
			 return keys1[startPoint];
		 }
		 else{
			 key2Draw = keys2[startPoint+1];
			 key3Draw = keys3[startPoint+1];
			 key4Draw = keys4[startPoint+1];
			 key5Draw = keys5[startPoint+1];
			 return keys1[startPoint+1];
		 }
		 
	 }
	 
	 public int getKey2Draw() {
		 return key2Draw;
	 }


	 public int getKey3Draw() {
		 return key3Draw;
	 }


	 public int getKey4Draw() {
		 return key4Draw;
	 }

	 public int getKey5Draw() {
		 return key5Draw;
	 }
	 
	 public void initializeEqualDistribution(Collection<Integer> keys) {
		 clear();
		 for (int key : keys) {
			 add(key, 0, 0, 0, 1.0);
		 }
	 }

	 public void initializeEqualDistribution(int maxKey) {
		 clear();
		 for (int key = 0; key < maxKey; key++) {
			 add(key, 0, 0, 1.0);
		 }
	 }

	 public int size() {
		 return keys1.length;
	 }

	 public double get(int key1) {
		 int index = Arrays.binarySearch(keys1, key1);
		 assert (index >= 0) : "Search failed: " + index;
		 return vals[index];
	 }

	 public void overlay(MiniDistribution other) {
		 assert (other.vals.length == vals.length) : other.vals.length + "!=" + vals.length;
		 for (int i = 0; i < other.vals.length; i++) {
			 int key1 = other.keys1[i];
			 int key2 = other.keys2[i];
			 int key3 = other.keys3[i];
			 int key4 = other.keys4[i];
			 int key5 = other.keys5[i];
			 double val = other.vals[i];

			 add(key1, key2, key3, key4, key5, val);
		 }
	 }
}