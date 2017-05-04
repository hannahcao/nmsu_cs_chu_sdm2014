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


/**
 * This class provide the probability calculation for each component if OAIM, LAIM.
 * a normal Gibbs sampling is implemented to draw z for influencing objects
 * and
 * a blocked Gibbs sampling is implemented to draw b, up, z, a for influenced objects
 *
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 * @author Chuan Hu (chu@cs.nmsu.edu)
 */
public class Probability {
	
	private static int averageBy = 1;
	
	/**
     * draw a new topic z for influencing objects
     * 
     * For obj's token, draw its new z
     * @param obj influencing obj id
     * @param token word token
     * @return
     */
    public static int getMAPInfluencing_z(CmdOption option,SampleData sampleData, int obj, int token, boolean trainOrTest)
    {
    	//mapTopicPosteriorDistr.clear();
    	MiniDistribution mapTopicPosteriorDistr = new MiniDistribution(option.znum + 1);
    	
    	//1. For each latent state, calculate its posterior probability p(z|others)
        for (int latentState = 0; latentState < option.znum; latentState++) {
            double prob = Probability.InfluencingPosterior_z(obj, token, latentState, sampleData,option);
            mapTopicPosteriorDistr.put(latentState, prob);
        }
        
        //2. make sure the posterior probablity is correct
        if (mapTopicPosteriorDistr.sum() == 0.0) {
            System.err.println("Posterior distribution.sum()==0.0. distr(10):" + mapTopicPosteriorDistr.toString());
            mapTopicPosteriorDistr.initializeEqualDistribution(option.znum);
        } //else {
        //    posteriorDistr.normalize();
        //}
        
        //3. Draw a new latent state
        int mapZ = mapTopicPosteriorDistr.draw();
        //System.out.println(Debugger.getCallerPosition()+"mapTopicPosteriorDistr="+mapTopicPosteriorDistr+",mapZ="+mapZ);
        
        //test
//        System.out.println(obj+" "+token+" normal prob sum "+mapTopicPosteriorDistr.sum());        
        return mapZ;
    }
    
    /**
     * TA influence model block sampling for one position.
     * For object that is influenced by others, 
     * obj's token, draw its new b, new o', and new z
     * 
     * @param obj, an influenced object
     * @param token, a token in the profile of obj
     * @return [mapZ, mapB, mapA, mapOprime]
     */
    public static int[] getMAPInfluenced_zbaop(CmdOption option, SampleData sampleData, int obj, int token, int ta, boolean trainOrTest)
    {
    	//MiniDistribution mapTopicPosteriorDistr = new MiniDistribution(option.znum * (option.oprimeNum + 1)*option.anum);
    	int distrSize = option.model.equals(Constant.oaim)?option.znum * (sampleData.in_refPubIndexIdList.get(obj).size() + 1) : option.znum * (sampleData.in_refPubIndexIdList.get(obj).size() + 1)*option.anum;
    	
    	MiniDistribution mapTopicPosteriorDistr = new MiniDistribution(distrSize);
    	//calculate the probability of p(z=*,oprime=*|b= NOT innovative) when b is NOT innovative

        double sum0 = 0;
        double sum1 = 0;

        for (int z = 0; z < option.znum; z++) {
            double probGj = Probability.influencedPosterior_zbopa(obj,token, ta, z, Constant.INNOTVATION, -1, -1, sampleData, option);
            sum0 += probGj;

            mapTopicPosteriorDistr.put(z, Constant.INNOTVATION, -1, -1, probGj);
        }

//    	mapTopicPosteriorDistr.sum0 = mapTopicPosteriorDistr.sum();

        //calculate the probability of p(z=*,oprime=*|b=innovative) when b is innovative
        for (int z = 0; z < option.znum; z++) {//for each latent state
            for (int oprime : sampleData.in_refPubIndexIdList.get(obj)) {
                if((oprime==obj)||(sampleData.testSet.contains(oprime)&&trainOrTest))
                    continue;

                if(option.model.equals(Constant.oaim)){//oaim
                    double probGj = Probability.influencedPosterior_zbopa(obj, token, ta, z, Constant.INHERITANCE,  -1 , oprime,  sampleData, option);
                    sum1 += probGj;
                    mapTopicPosteriorDistr.put(z, Constant.INHERITANCE, oprime, -1, probGj);
                }
                else{//laim
                    for (int a=0; a < option.anum; a++){
                        double probGj = Probability.influencedPosterior_zbopa(obj, token, ta, z, Constant.INHERITANCE, a, oprime, sampleData, option);
                        sum1 += probGj;
                        mapTopicPosteriorDistr.put(z, Constant.INHERITANCE, oprime, a, probGj);
                    }
                }
            }
        }
//        if (Math.random()*100 > 99) //check the difference between sum p(b=0) and sum p(b=1).  It seems something wrong here
//            System.out.println(Debugger.getCallerPosition()+" normal sum p(b=0)="+(sum0)+" sum p(b=1)="+(sum1));

//        System.out.println(Debugger.getCallerPosition()+" real sum probability "+mapTopicPosteriorDistr.sum());

//    	mapTopicPosteriorDistr.sum1 = mapTopicPosteriorDistr.sum() - mapTopicPosteriorDistr.sum0;
//    	System.out.println(Debugger.getCallerPosition()+" object "+obj+", token "+token+", ta"+ta+" 's prob "+mapTopicPosteriorDistr.toString());

        if (mapTopicPosteriorDistr.sum() == 0.0) {
            System.err.println("Posterior distribution.sum()==0.0. distr(10):" + mapTopicPosteriorDistr.toString());
            mapTopicPosteriorDistr.initializeEqualDistribution(option.znum);
        }
        //randomly draw new z, new b, new o' which follow the joint distribution calculated above
        int mapZ = mapTopicPosteriorDistr.draw(); //mapZ
        int mapB = mapTopicPosteriorDistr.getKey2Draw(); //mapB
        int mapOprime = mapTopicPosteriorDistr.getKey3Draw();//mapOp
        int mapA = mapTopicPosteriorDistr.getKey4Draw();
        
        return new int[]{mapZ, mapB, mapOprime, mapA};
	}
    
    //p(z|...)= N1 *N2
	public static double InfluencingPosterior_z(int obj, int token, int latentState,
			final SampleData data,final CmdOption option) {//CitinfSampler.citedPosteriorT
		
//		double lambda = beta(Constant.INNOTVATION, obj, data, option); //p(b|.)
		double N3 = N3Influencing(latentState, obj, data,option); //N3
		double N2 = N2(token, latentState, data,option);//N2
//		double prob =  N3 * N3 * N2; //should it be (N2*N3)*(N3*lambda)? TODO
		double prob =  N3 * N2; //should it be (N2*N3)*(N3*lambda)? TODO
        assert (prob >= 0.0) : "probG0 must be positive but is " + prob + 
        ". obj=" + obj + " token=" + token + " latentState=" + latentState;
        assert (!Double.isNaN(prob));
        assert (!Double.isInfinite(prob));
        
        return prob;
    }
	
	//p(o'|., b=1) = gamma * N1
	//p(zi|...,b=0) = N2 * N3
	//p(zi|...,b=1) = N2 * N1
	//p(bi=0|...) = lambda * N3
	//p(bi=1|...) = lambda * N1
	//citingPosteriorTSC

    /**
     *  b=1 inheritance
     * p(z,b=1=inhe,op|.) = p(z|.b=1)*p(b=1|.)*p(op|)= (N2 *N1) (N1*lambda)*(N1*gamma)
     *
     * b=0 innovation
     * p(z,b=0=inno) = p(z|.) * p(b=0|.)= (N2 *N3) (N3*lambda)
     * @param obj
     * @param token
     * @param ta
     * @param z
     * @param b
     * @param a
     * @param oprime
     * @param data
     * @param DistributionParam
     * @return
     */
	public static double influencedPosterior_zbopa(int obj, int token, int ta, int z, int b, int a, int oprime, 
		 final SampleData data,final CmdOption DistributionParam) 
	{
		double prob = 0.0;
		if (b == Constant.INNOTVATION) {//for b=0, innovation 
			double lambda = beta(b, obj, data, DistributionParam); //p(b|.)
			double N3 = N3Influenced(z, obj, data,DistributionParam); //N3
			double N2 = N2(token, z, data,DistributionParam);//N2
//			prob = lambda * N3 * N3 * N2; //should it be (N2*N3)*(N3*lambda)? TODO
			prob = lambda * N3 * N2; //should it be (N2*N3)*(N3*lambda)? TODO
		} else {//b=1, 
			double lambda = beta(b, obj, data, DistributionParam);//p(b|.)
			double theta = theta(z, oprime, data,DistributionParam);//N1
			double N2 = N2(token, z, data,DistributionParam);
			double gamma = gamma(obj,
              data.cmdOption.model.equals(Constant.oaim)? ta : a,
              oprime, data, DistributionParam);
			//oaim
			prob = lambda * theta * N2 * gamma ; //should it be (N1*N2)*(N1*lambda)*(N1*gamma) TODO 
			if(!data.cmdOption.model.equals(Constant.oaim)){//laim
				double eta = eta(obj, a, oprime,  data,  DistributionParam);//aspect
				prob *= eta;
			}
		}
		
		assert (prob >= 0.0) : "probG0 must be positive but is " + prob + ". o=" + obj + " t=" + token + " z=" + z;
		assert (!Double.isNaN(prob));
		assert (!Double.isInfinite(prob));
		return prob;
    }
	 
	 /*
	  * Calculate the right part of formula (16) and (17)
	  * p(b|...)
	  */
	 private static double beta(int b, int obj, final SampleData data,final CmdOption DistributionParam) {
        double num;
        
        //TODO: why not minus 1?
        if (b == Constant.INNOTVATION) {//b=0, p(bi=innovation| b not i, t, o', z), alpha1 = alphaLambdaInnov
            //num = data.count_ob_influenced(obj, b) + DistributionParam.alphaLambdaInnov;
        	num = Util.get2Map(data.N_ob_influenced, obj, b)/ (double)averageBy  + DistributionParam.alphaLambdaInnov;
        } else {//b=1,p(bi=inherit| b not i, t, o', z), alpha2 = alphaLambdaInherit
            //num = data.count_ob_influenced(obj, b) + DistributionParam.alphaLambdaInherit;
        	num = Util.get2Map(data.N_ob_influenced, obj, b)/ (double)averageBy + DistributionParam.alphaLambdaInherit;
        }
        
        //double den = data.countObjIncluenced(obj) + DistributionParam.alphaLambdaInherit + DistributionParam.alphaLambdaInnov;
//        double den = data.N_o_influenced[obj] / (double)averageBy
//        		+ DistributionParam.alphaLambdaInherit + DistributionParam.alphaLambdaInnov;

         double den = 1;
         return num / den;
    }
	 
	 /**
	  * full condition probability for latent state part.
	  * @param z
	  * @param oprime
	  * @param data
	  * @param option
	  * @return
	  */
	 private static double theta(int z, int oprime, final SampleData data,final CmdOption option) {
//        assert (z != -1) : z + " " + oprime;
//        assert (oprime != -1) : z + " " + oprime;
        //TODO: why not minus 1
        
		 double num = 1.0;
		 double den = option.znum;
//
//		 if(oprime!=-1){
         num = Util.get2Map(data.N_oz_influencing, oprime, z)/ (double)averageBy
           + Util.get2Map(data.NN_opz_bold_influencing, oprime, z)/ (double)averageBy
           + option.alphaTheta;
         den = data.N_o_influencing[oprime] / (double)averageBy
           + data.NN_op_bold_influencing[oprime]/ (double)averageBy
           + option.znum * option.alphaTheta;
//		 }
		 return num / den;
    }
	
	 //N3 = psi
	 private static double N3Influenced(int z, int obj, SampleData data, final CmdOption cmdOption) {
		 double num = Util.get2Map(data.N_oz_innov_influenced, obj, z)/ (double)averageBy + cmdOption.alphaTheta;
		 double den = Util.get2Map(data.N_ob_influenced, obj, Constant.INNOTVATION) / (double)averageBy 
				 + cmdOption.znum * cmdOption.alphaTheta;

		 return num / den;
    }
	 
	 private static double N3Influencing(int z, int obj, SampleData data, final CmdOption cmdOption) {
		 double num = Util.get2Map(data.N_oz_influencing, obj, z)/ (double)averageBy + cmdOption.alphaTheta;

         //actually this the denominator is constant.
         //To compute likelihood we need this.
		 double den = data.N_o_influencing[obj]/ (double)averageBy
				 + cmdOption.znum * cmdOption.alphaTheta;
//		 double den = 1.0;

		 return num / den;
	    }
	 
	//gamma: left side of p(o_i'|.)  formula
    public static double gamma(int objIdx, int aspectOrta, int oprimeIdx, SampleData data,final CmdOption cmdOption) {
    	double num = 1.0;
    	double den = data.getBibliographySize(objIdx) ;
    	
//    	if(oprimeIdx!=-1 && aspectOrta!=-1){
        if(cmdOption.model.equals(Constant.oaim)){//oaim
            num = Util.get3Map(data.N_otaopb1_influenced, objIdx, aspectOrta, oprimeIdx) / (double) averageBy
              + cmdOption.alphaGamma;
            den = Util.get2Map(data.N_otab1_influenced, objIdx, aspectOrta)/ (double)averageBy
              + data.getBibliographySize(objIdx) * cmdOption.alphaGamma;
        }
        else{//laim
            num = Util.get3Map(data.N_oaop_influenced, objIdx, aspectOrta, oprimeIdx) / (double) averageBy
              + cmdOption.alphaGamma;
            den = Util.get2Map(data.N_oa_influenced, objIdx, aspectOrta)/ (double)averageBy
              + data.getBibliographySize(objIdx) * cmdOption.alphaGamma;
        }
//    	}
    	
        return num / den;
    }
    /**
     * 
     * @param objIdx
     * @param aspect
     * @param oprimeIdx
     * @param data
     * @param cmdOption
     * @return
     */
    private static double eta(int objIdx, int aspect, int oprimeIdx, SampleData data,final CmdOption cmdOption){
    	double num = 1.0;
    	double den = cmdOption.anum ;
    	
    	if(oprimeIdx!=-1 && aspect!=-1){
    		num = Util.get2Map(data.N_oa_influenced, objIdx, aspect) /(double) averageBy + 
    				cmdOption.alphaEta;
    		den = Util.get2Map(data.N_ob_influenced, objIdx, Constant.INHERITANCE)  + 
    				cmdOption.anum*cmdOption.alphaEta;
    	}
    	 
    	return num/den;
    }
    /**
     * Full conditional probability for token part.
     * @param token
     * @param z
     * @param data
     * @param option
     * @return
     */
    private static double N2(int token, int z, SampleData data,final CmdOption option) {
    	//count_ZT_All(z, token) = N_{z,t}(z,token) + N'_{z,t}(z,token)
    	//count_Z_All(z) = N_z(z) + N'_z(z)
        //double num = data.count_ZT_All(z, token) + option.alphaPhi;
    	//double den = data.count_Z_All(z) + data.getNumTokens() * option.alphaPhi;
    	double num = Util.get2Map(data.N_zt_all, z, token)/(double)averageBy + option.alphaPhi;
    	double den = data.N_z_all[z]/(double)averageBy + data.getNumTokens() * option.alphaPhi;
        
        return num / den;
    }
    
    public static void main(String[] args){
    	double a = 1;
    	double b = 8;
    	System.out.println(a/b);
    }
    
}
