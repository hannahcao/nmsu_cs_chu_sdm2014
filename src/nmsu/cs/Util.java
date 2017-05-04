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

import java.util.*;


/**
 * utilities for array, nested map operation and time recording.
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 * @author  Chuan Hu (chu@cs.nmsu.edu)
 * @date Apr. 2, 2012
 */
public class Util{
	private static final int PRECISION = 2;
	
	//temporary file for gammasum in each iterations
	public static String getSummaryFileName(String samplerId, int chainId){
		return(Constant.DefaultResultOutputFolder+ samplerId + "-chain" + chainId +".gammasum.tmp");
	}
	
	public static String getStatFileName(String samplerId, int chainId)
	{
		String fname = Constant.DefaultResultOutputFolder+samplerId + "-chain" + chainId + ".stat.xls";
		return fname;
	}
	
	public static String getStatFileNameBinary(String samplerId, int chainId)
	{
		String fname = Constant.DefaultResultOutputFolder+
				samplerId + "-chain" + chainId+ ".stat.tmp"; //Constant.ResultStatisticsFileSuffix;
		return fname;
	}
	
	public static String getAllStatFileName(String samplerId)
	{
		String fname = Constant.DefaultResultOutputFolder+samplerId 
				+ "-allchain.stat.xls"; //Constant.ResultStatisticsFileAllChainSuffix;
		return fname;
	}
	
	public static String getFinishFileName(String samplerId){
		String finishFileName = Constant.DefaultResultOutputFolder+samplerId+".finished.txt";
		return finishFileName;
	}
	
	
	
    //////////////////////////////////////////////////////////////////
	
	/**
	 * A generic function to print an array
	 * @param array
	 */
	public static <E> void printArray(E[] array)
	{
		for(int i=0;i<array.length;i++)
			System.out.print(" "+array[i]);
		System.out.println();
	}
	/**
	 * print 2D array to string (matrix)
	 * @param array
	 * @return
	 */
	public static String Array2DToString(double[][] array)
	{
		final StringBuffer str = new StringBuffer();
		int i=0, j=0;  
        for(i=0;i<array.length;i++){
        	for(j=0;j<array[i].length;j++){
        		 str.append(" "+array[i][j]);
        	}
        	str.append("\n");
        }
        str.append("\n");
        
        return str.toString();
	}
	
	/**
	 * Convert a 2D array to string
	 * @param data
	 * @return
	 */
	public static String toString(double[][] data) {
        StringBuffer buf = new StringBuffer();
        
        //added by Huiping
        buf.append("row num = word num="+data.length+"\n");
        if(data.length>0) buf.append("col num = doc num = "+data[0].length+"\n");
        //end of adding
        
        for (int c0 = 0; c0 < data.length; c0++) {
            double[] row = data[c0];
            buf.append(Arrays.toString(row)).append("\n");
        }
        return buf.toString();
        //return Arrays.deepToString(data);
    }
	
	/**
	 * add the number of elements in one dimension together
	 * E.g., from 2D array
	 * [[1],[2]]
	 * [[3],[4]]
	 * [[5],[6]] dim0size = 3, dim1size=2
	 * sumArrayDim(array,0) should return array with size dim1size 2  
	 * [9=1+3+5, 12=2+4+6]
	 * sumArrayDim(array,1) should return array with size dim1size 3
	 * [3=1+2, 7=3+4, 11=5+6]
	 * @param array
	 * @param dimToSummarize
	 * @return
	 */
	public static double[] sumArrayDim(double [][] array,int dimToSummarize)
	{
		double[] sum = null; 
		int dim0size = array.length;
		int dim1size = array[0].length;
		
		if(dimToSummarize==0) sum = new double[dim1size];
		else 				sum = new double[dim0size];
	
		for(int i=0;i<dim0size;i++){
			for(int j=0;j<dim1size;j++){
				if(dimToSummarize==0) sum[j] += array[i][j];
				else 				sum[i] += array[i][j];
			}
		}
		return sum;
	}
	
	/**
	 * Add the number of the elements inside the array
	 * @param array
	 * @return
	 */
	public static double sumArray(double[] array)
	{
		double sum=0.0;
		for(int i=0;i<array.length;i++)
			sum+=array[i];
		return sum;
	}
	
	/**
	 * randomly generate the the latent state
	 * @param token
	 * @param object
	 * @param freq
	 * @param numLatentState
	 * @return
	 */
	public static int initialLatentState(int token, int object, int freq, int numLatentState) {
        return (int) Math.floor(Math.random() * numLatentState);
    } 
	
	public static int initiaInfluencing(int w, int d, int freq, List<Integer> bibliography) {
        assert (!bibliography.isEmpty()) : "d=" + d + " w=" + w;
        int index = (int) Math.floor(Math.random() * bibliography.size());
        
        return bibliography.get(index);
    }
    
    public static String cut(double value) {
        double tenPower = Math.pow(10, PRECISION);
        double scaledInt = Math.rint(value * tenPower);
        double cuttedVal = scaledInt / tenPower;
        return "" + cuttedVal;
    }

    public static String cut(double value, int precision) {
        double tenPower = Math.pow(10, precision);
        double scaledInt = Math.rint(value * tenPower);
        double cuttedVal = scaledInt / tenPower;
        return "" + cuttedVal;

    }
   
    /**
     * update 3 dimension Hash map
     * @param map
     * @param key1
     * @param key2
     * @param key3
     * @param value
     */
    public static void update3Map(Map<Integer, Map<Integer, Map<Integer, Double>>> map, int key1, int key2, int key3, double value){
    	if(value==0 && get3Map(map, key1, key2, key3)==0)
    		return ;

    	Map<Integer, Map<Integer, Double>> map1 = map.get(key1);
    	if(map1==null){
    		if(map instanceof HashMap){
    			map1 = new HashMap<Integer, Map<Integer, Double>>();
    			map.put(key1, map1);
    		}
    		else if(map instanceof TreeMap){
    			map1 = new TreeMap<Integer, Map<Integer, Double>>();
    			map.put(key1, map1);
    		}
    	}
    	Map<Integer, Double> map2 = map1.get(key2);
    	if(map2==null){
    		if(map instanceof HashMap){
    			map2 = new HashMap<Integer, Double>();
    			map1.put(key2, map2);
    		}
    		else if(map instanceof TreeMap){
    			map2 = new TreeMap<Integer, Double>();
    			map1.put(key2, map2);
    		}
    	}
    	if (value==0)
    		map2.remove(key3);
    	else
    		map2.put(key3, value);

    	if (map2.isEmpty())
    		map1.remove(key2);

    	if(map1.isEmpty())
    		map.remove(key1);
    }

    /**
     * read value from 3 dimension map
     * @param map
     * @param key1
     * @param key2
     * @param key3
     * @return
     */
    public static double get3Map(Map<Integer, Map<Integer, Map<Integer, Double>>> map, int key1, int key2, int key3){
    	Map<Integer, Map<Integer, Double>> map1 = map.get(key1);
    	if(map1==null)
    		return 0;
    	else{
    		Map<Integer, Double> map2 = map1.get(key2);
    		if(map2==null)
    			return 0;
    		else{
    			Double value = map2.get(key3);
    			if(value==null)
    				return 0;
    			else
    				return value;
    		}
    	}
    }
    
    /**
     * update 2 dimension map
     * @param map
     * @param key1
     * @param key2
     * @param value
     */
    public static void update2Map(Map<Integer, Map<Integer, Double>> map, int key1, int key2,  double value){
    	if(value==0 && get2Map(map, key1, key2)==0)
    		return;

    	Map<Integer, Double> map1 = map.get(key1);
    	if(map1==null){
    		if(map instanceof HashMap){
    			map1 = new HashMap<Integer, Double>();//
    			map.put(key1, map1);
    		}
    		else if(map instanceof TreeMap){
    			map1 = new TreeMap<Integer, Double>();//
    			map.put(key1, map1);
    		}
    	}
    	
    	if(value==0)
    		map1.remove(key2);
    	else
    		map1.put(key2, value);

    	if(map1.isEmpty())
    		map.remove(key1);

    }
    /**
     * read value from 2 dimension map
     * @param map
     * @param key1
     * @param key2
     * @return
     */
    public static double get2Map(Map<Integer, Map<Integer, Double>> map, int key1, int key2){
    	Map<Integer, Double> map1 = map.get(key1);
    	if(map1==null)
    		return 0;
    	else{
    		Double value = map1.get(key2);
    		if(value==null)
    			return 0;
    		else{
    			return value;
    		}
    	}
    }
    /**
     * print 2 dimension map to screeen
     * @param map
     */
    public static void print2Map(Map<Integer, Map<Integer, Double>> map){
    	for(Map.Entry<Integer, Map<Integer, Double>> entry : map.entrySet())
    		for(Map.Entry<Integer, Double> entry2 : entry.getValue().entrySet())
    			System.out.println(entry.getKey()+"\t"+entry2.getKey()+"\t"+entry2.getValue());
    	
    }
    /**
     * Add by Chuan
     * binary search
     * @param arr sorted array to be searched
     * @param value target search value
     * @param p begin index
     * @param q end index
     * @return the biggest index b such that arr[b]<value; -1 if value < arr[0]
     */
    public static int binarySearch(double[] arr, double value, int p, int q){
    	if(p>q)
    		return p-1;
    	
    	int r = (p+q)/2;
    	if(arr[r] == value)
    		return r;
    	else if(arr[r] < value)
    		return binarySearch(arr, value, r+1, q);
    	else
    		return binarySearch(arr, value, p, r-1);
    }
    
    public static void main(String args[]){
//    	double a = Math.random();
//    	int b = 4;
//    	System.out.println((int)Math.floor(a*b));
//    	
//    	JSONArray arr = new JSONArray();
//    	arr.put(1);
//    	arr.put(2);
//    	
//    	System.out.println(arr.get(0));
    	
//    	double[] arr = new double[100];
//    	for(int i=0; i<100; i++)
//    		arr[i] = i+0.4;
//    	double value = 1.5;
//    	int returnIndex = binarySearch(arr, value, 0, 99);
//    	
//    	if(returnIndex!=-1)
//    		System.out.println("target value: "+value+"\r\nreturn index "+returnIndex+"\r\nreturn value "+arr[returnIndex]);
//    	else
//    		System.out.println("-1");
    
    	Map<Integer, Map<Integer, Double>> map = new HashMap<Integer, Map<Integer, Double>>();
    	
    	Map<Integer, Double> map1 = new HashMap<Integer, Double>();
    	map1.put(1, 1.1);
    	map1.put(2, 2.2);
        double b = 44;
        map1.put(3, b) ;
        b = 55;

        System.out.println(map1.get(3));

    	Map<Integer, Double> map2 = new HashMap<Integer, Double>();
    	map2.put(1, 1.2);
    	map2.put(2, 2.3);
    	
    	map.put(1, map1);
    	map.put(2, map2);
    	
    	System.out.println(map1.size());
    	map1.remove(1);
    	map1.remove(2);
    	System.out.println(map1.isEmpty());
    	System.out.println(map1.size());
    	
    	map1.put(1, null);
    	map1.put(2, null);
    	System.out.println(map1.isEmpty());
    	System.out.println(map1.size());

    }
    
    public static void printMsTimeBetween(String text, Date beginPoint, Date endPoint){
    	System.out.println(text+"\t time(ms) = "+ ( endPoint.getTime()-beginPoint.getTime() ) ); 
    }



    /**
     * begin record time for operation at iter iteration
     * @param operation
     */
    public static void beginRecordTime(Map<String, List<Long>> map, String operation){
        Date date = new Date();
        List<Long> list = map.get(operation);
        if (list==null) {
            list = new ArrayList<Long>();
            map.put(operation, list);
        }

        list.add(date.getTime());
    }
    /**
     * end record time for operation at iter iteration
     * @param operation
     */
    public static void endRecordTime(Map<String, List<Long>> map, String operation){
        Date date = new Date();

        List<Long> list = map.get(operation);

        long beginTime = list.get(list.size()-1);
        long endTime = date.getTime();

        list.set(list.size()-1, endTime - beginTime);
    }

    public static void printTimeMap(Map<String, List<Long>> map) {
//        System.out.println(Debugger.getCallerPosition()+" print running time detail");
        for (Map.Entry<String, List<Long>> entry : map.entrySet()){
            String operation = entry.getKey();
            List<Long> list = entry.getValue();

            for (int i=0; i<list.size(); i++)
                System.out.println(operation+" at iteration "+i+" use time "+list.get(i));
        }
    }

}
