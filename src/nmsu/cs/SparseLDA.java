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

import java.util.HashMap;
import java.util.Map;

/**
 * implement the sparse lda in paper
 * "efficient method for topic model inference on streaming document collections KDD2009"
 * @author Chuan Hu (chu@cs.nmsu.edu)
 *
 */
public class SparseLDA {
	/**
	 * maintaining sum for constant term
	 */
	 double sum_s;
	/**
	 * maintaining sum one count term
	 */
	 double sum_r;
	/**
	 * maintaining the two count term sum
	 */
	 double sum_q;
	 /**
	  * object index
	  */
	 int oIndex;
	 
	 public SparseLDA(int oIndex){
		 this.sum_s = 0;
		 this.sum_r = 0;
		 this.sum_q = 0;
		 this.oIndex = oIndex;
	 }
	 
	/**
	 * build initial sum
	 * @param sampleData
	 */
	public void buildSum(SampleData sampleData){
		double[] N_z_all = sampleData.N_z_all;
		Map<Integer, Map<Integer, Double>> N_oz_influencing = sampleData.N_oz_influencing;
		CmdOption cmdOption = sampleData.cmdOption;

		//compute sum_s
		for(int zIndex=0; zIndex<N_z_all.length; zIndex++){
			double num = cmdOption.alphaTheta*cmdOption.alphaPhi;
			double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
			sum_s +=  num/den ;
		}
		
		//iterate through each non zero topic in this object
		Map<Integer, Double> N_o_z = N_oz_influencing.get(oIndex);
        if (N_o_z == null) {
            N_o_z = new HashMap<Integer, Double>();
            System.out.println(Debugger.getCallerPosition()+" debug error "+this.oIndex);
        }

		for(Map.Entry<Integer, Double> entry : N_o_z.entrySet()){
			int zIndex = entry.getKey();

			double num = cmdOption.alphaPhi*entry.getValue();
			double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
			sum_r += num/den;
		}
		//don't compute sum_q in prior
	}
	/**
	 * during sampling process update each prior sum
	 * !!!! do this before updCount
	 * @param obj
	 * @param token
	 * @param z
	 * @param freq
	 */
	public void updSum(SampleData sampleData, int obj, int token, int z, int freq){
		double[] N_z_all = sampleData.N_z_all;
		Map<Integer, Map<Integer, Double>> N_oz_influencing = sampleData.N_oz_influencing;
		
		CmdOption cmdOption = sampleData.cmdOption;
		
		//update sum_s
		double num = cmdOption.alphaTheta*cmdOption.alphaPhi;
		double den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
		sum_s -= (num/den);
		
		num = cmdOption.alphaTheta*cmdOption.alphaPhi;
		den = (N_z_all[z]+freq + sampleData.getNumTokens()*cmdOption.alphaPhi); 
		sum_s += (num/den);
		
		//update sum_r
		num = cmdOption.alphaPhi*Util.get2Map(N_oz_influencing, oIndex, z);
		den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
		sum_r -= (num/den);


		num = cmdOption.alphaPhi * (Util.get2Map(N_oz_influencing, oIndex, z) + freq);
		den = (N_z_all[z]+freq + sampleData.getNumTokens()*cmdOption.alphaPhi); 
		sum_r += (num/den);
		
	}
	
	public int drawMAPInfluencing_z(SampleData sampleData, int obj, int token){
		
		double[] N_z_all = sampleData.N_z_all;
		Map<Integer, Map<Integer, Double>> N_oz_influencing = sampleData.N_oz_influencing;
		Map<Integer, Map<Integer, Double>> N_tz_all = sampleData.N_tz_all;
		CmdOption cmdOption = sampleData.cmdOption;


		double s = sum_s;
		double r = sum_r;
		double q = sum_q;
		
		
		//compute q
		Map<Integer, Double> N_t_z = N_tz_all.get(token);
		
		double[] q_arr = new double[N_t_z.size()];
		int[] z = new int[N_t_z.size()];
		int i = 0;
		
		for(Map.Entry<Integer, Double> entry : N_t_z.entrySet()){
			int zIndex = entry.getKey();
			double N_t_z_value = entry.getValue();
			
			
			double num = N_t_z_value * ( Util.get2Map(N_oz_influencing, obj, zIndex) + cmdOption.alphaTheta);
			double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
			q += (num/den);

			z[i] = zIndex;
			q_arr[i] = (q+s+r);
			i++;
		}
		
		//test
//		System.out.println(obj+" "+token+" sparse prob sum "+(s+r+q));
		
		double x = Math.random()*(s+r+q);
		double temSum = 0;
		
		if(x<=s){
			//iterate through each topic
			for(int zIndex=0; zIndex<N_z_all.length; zIndex++) {
				double num = cmdOption.alphaTheta*cmdOption.alphaPhi;
				double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
				temSum += (num/den);
				if(temSum>=x)
					return zIndex;
			}
		}
		else if(x<=s+r){
			temSum += s;

			//iterate through non zero N_oz
			Map<Integer, Double> N_o_z = N_oz_influencing.get(obj);
			for(Map.Entry<Integer, Double> entry : N_o_z.entrySet()){
				int zIndex = entry.getKey();
				
				double num = cmdOption.alphaPhi*entry.getValue();
				double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
				temSum += (num/den);
				if(temSum>=x)
					return zIndex;
			}
		}
		// r+s < x <=r+s+q
		else {
			temSum += (s+r);
			//iterate through non zero N_t_z
			N_t_z = N_tz_all.get(token);
			for(Map.Entry<Integer, Double> entry : N_t_z.entrySet()){
				int zIndex = entry.getKey();
				double N_t_z_value = entry.getValue();
				
				double num = N_t_z_value * ( Util.get2Map(N_oz_influencing, obj, zIndex) + cmdOption.alphaTheta);
				double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi); 
				temSum += (num/den);
				if(temSum>=x)
					return zIndex;
			}
//			return z[Util.binarySearch(q_arr, x-(s+r), 0, q_arr.length-1)];
		}
		System.out.println(Debugger.getCallerPosition()+"\t s="+s+" t="+r+" q="+q+" x="+x+" tmpSum="+temSum+" sum="+(s+r+q));
		
		return (int) (sampleData.cmdOption.znum*Math.random());
		
	}
}
