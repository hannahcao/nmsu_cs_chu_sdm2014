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
 * implement Runnable interface to let two sample chain run concurrently
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 *
 */
class SamplerRunnable implements Runnable{
	
	int chainNo;
	SamplerChain samplerChain;
	Set<Integer> testSet;

	/**
	 * Create a runnable sampler
	 * 
	 * @param _chainNo: the chain no of this runnable sampler
	 * @param _samplerChain: the sample chain this runnable belong to
	 */
	public SamplerRunnable(int _chainNo, SamplerChain _samplerChain)
	{
		chainNo = _chainNo;
		samplerChain = _samplerChain;
		this.testSet = _samplerChain.testSet;
	}


	@Override
	public void run() {
		
		boolean train = true;
		boolean test = false;
		
		boolean takeSamplesFromThisChain = (chainNo==samplerChain.allChainIds.get(0));
		System.out.println(Debugger.getCallerPosition()+"Chain: "+ chainNo);
		
		//2. create a sampler (set the parameters) 
		Sampler sampler = new Sampler(samplerChain.cmdOption,takeSamplesFromThisChain,
				samplerChain.parsedData,samplerChain.allChainIds,chainNo, testSet);
		
		//3. initialize all the initial structures for sampling 
		// (read and parse documents, create intermediate data structure, etc.) 
		//sampler.init(samplerChain.parsedData);
		
		//4. draw the train sample
		sampler.drawInitSample(train);

		sampler.doGibbs(train);


		//5. draw the test sample
        //do test only when test set is not empty.
		if (!sampler.getSampleData().testSet.isEmpty()){
            sampler.drawInitSample(test);
            sampler.doGibbs(test);
        }

		if (takeSamplesFromThisChain) {//explain SamplerChain line 92.  Why pass this?
			samplerChain.samplerToGetResult = sampler;
		}
		
	}
}


public class SamplerChain {
	
	DataParsed parsedData; 		//the data structure after parsing the original data
	CmdOption cmdOption;		//command options (with all the parameters)
	Sampler samplerToGetResult;	//from this sampler to derive the final results
	
	List<Integer> allChainIds;	//the total number of chains used to test the sampler convergence
	
	Set<Integer> testSet;
    //  operation -> iteration -> time
    Map<String, List<Long>> sampler_chain_map;


	/**
	 * Initialize sample chain, read sample data
	 * 
	 * Debugged & correct Aug. 17, 2012
	 * 
	 * @param _cmdOption
	 */
	public SamplerChain(CmdOption _cmdOption, Set<Integer> testSet)
	{
        sampler_chain_map = new TreeMap<String, List<Long>>();

        Util.beginRecordTime(sampler_chain_map, Constant.load_time);
		cmdOption = _cmdOption;
		Constant.zNum = cmdOption.znum;
		parsedData = new DataParsed();
		parsedData.init(cmdOption.paperfolder,cmdOption.graphfile, cmdOption.aspectfile);
		this.testSet = testSet;

        Util.endRecordTime(sampler_chain_map, Constant.load_time);

		allChainIds = new ArrayList<Integer>();
		
		for(int i=0;i<cmdOption.chainNum;i++){
			allChainIds.add(i);
		}

        System.out.println(Debugger.getCallerPosition()+" sampler chain initial time detail ");
        Util.printTimeMap(sampler_chain_map);
	}
	
	/**
	 * Run two threads and do gibbs sample
	 */
	public void doGibbs()
    {
		System.out.println(Debugger.getCallerPosition()+"allChainids size="+ allChainIds.size()+":" +allChainIds);
		
		//Create the sampler threads 
		final List<Thread> threadlist = new ArrayList<Thread>();
		for (int chain : allChainIds) {
			Runnable runnableSampler = new SamplerRunnable(chain, this);//why pass this?  See line 45
			Thread thread = new Thread(runnableSampler,"train-" + chain);
			
			//if(chain.trim().equals("1")) break;//Huiping added, just use one chain
			threadlist.add(thread);
		}
     
		//Start the sampler threads
		for (Thread thread : threadlist) {
			System.out.println(Debugger.getCallerPosition()+"start thread" + thread.getId()+":" + thread.getName());
			thread.start();
		}
		
		for (Thread thread : threadlist) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.err.println(Debugger.getCallerPosition()+"interrupted");
			}
		} 
    }
	
	/**
	 * Get the gamma results
	 * @return map of (object: (influencing object, influence))
	 */
	public Map<Integer, Map<Integer, Map<Integer, Double>>>  getGamma(){ 
		return (samplerToGetResult.getGamma());
	}
	
	public Map<Integer, Map<Integer, Double>> getOTAInfluenced(){
        System.out.println("samplerToGetResult is null? "+samplerToGetResult==null );
        System.out.println("avg sample data is null? "+ (samplerToGetResult.getAvgSampleData()==null) );
        System.out.println("N_otab1_influenced is null? "+(samplerToGetResult.getAvgSampleData().N_otab1_influenced ==null));

        return samplerToGetResult.getAvgSampleData().N_otab1_influenced;
	}
	
	public Map<Integer, Map<Integer, Double>> getATAInfluenced(){
		return samplerToGetResult.getAvgSampleData().N_ata_influenced;
	}
	
	public double[] getOInfluenced(){
		return samplerToGetResult.getAvgSampleData().N_o_influenced;
	}
	
	public Map<Integer, Map<Integer, Double>> getOOP(){
		return samplerToGetResult.getAvgSampleData().N_oop_bold_influenced;
	}
	
	public Map<Integer, Map<Integer, Double>> getOB(){
		return samplerToGetResult.getAvgSampleData().N_ob_influenced;
	}
	
	public Map<Integer, Map<Integer, Map<Integer, Double>>> getOTAOPInfluenced(){
		return samplerToGetResult.getAvgSampleData().N_otaopb1_influenced;
	}
	/**
	 * get the Topic-Token mixture in the average SampleData.
	 * @return
	 */
	public double[][] getPhi(){
		return samplerToGetResult.getAvgSampleData().getPhi();
	}
	/**
	 * get topic distribution of influenced documents in the average SampleData.
	 * @return
	 */
	public double[][] getTheta(){
		return samplerToGetResult.getAvgSampleData().getTheta();
	}
	/**
	 * get topic distribution for influencing documents in the average SampleData.
	 * @return
	 */
	public double[][] getThetaPrime(){
		return samplerToGetResult.getAvgSampleData().getThetaPrime();
	}
	/**
	 * calculate the likelihood for test documents of OAIM.
	 * @return
	 */
	public double getLogLikelihoodAll(){
		double llh = 0;
		
		int znum = cmdOption.znum;
		int anum = cmdOption.anum;
		
		SampleData sample = samplerToGetResult.getAvgSampleData();
		CmdOption option = samplerToGetResult.getAvgSampleData().cmdOption;
		
		for(SampleElementInfluencing e : samplerToGetResult.getSampleData().getSampleInfluencing_otz()){
			int o = e.obj;
			if(!this.testSet.contains(o) && !this.testSet.isEmpty())//calculate likelihood only for test data set
				continue;
			int t = e.token;
			double prob = 0;
			for(int z=0; z<znum; z++)
				prob += Probability.InfluencingPosterior_z(o, t, z, sample, option);
			
			llh+=Math.log(prob);
		}
		if(cmdOption.model.equals(Constant.oaim))//oaim
			for(SampleElementInfluencedOAIM e : samplerToGetResult.getSampleData().getSampleInfluenced_otzbtaop()){
				int o = e.obj;
				if(!this.testSet.contains(o) && !this.testSet.isEmpty())//calculate likelihood only for test data set
					continue;
				int t = e.token;
				int ta = e.ta;

				double prob = 0;

				for(int op : sample.in_refPubIndexIdList.get(o))
					for(int z=0; z<znum; z++)
						prob += Probability.influencedPosterior_zbopa(o, t, ta, z, Constant.INHERITANCE, -1, op, sample, option);

				for(int z=0; z<znum; z++)
					prob += Probability.influencedPosterior_zbopa(o, t, ta, z, Constant.INNOTVATION, -1, -1, sample, option);

				llh+=Math.log(prob);
			}
		else{//laim
			for(SampleElementInfluencedLAIM e : samplerToGetResult.getSampleData().getSampleInfluenced_otzbataop()){
				int o = e.obj;
				if(!this.testSet.contains(o) && !this.testSet.isEmpty())//calculate likelihood only for test data set
					continue;
				int t = e.token;
				int ta = e.ta;
				
				double prob = 0;
				
				for(int a=0; a<anum; a++)
					for(int op : sample.in_refPubIndexIdList.get(o))
						for(int z=0; z<znum; z++)
							prob += Probability.influencedPosterior_zbopa(o, t, ta, z, Constant.INHERITANCE, a, op, sample, option);
				
				for(int z=0; z<znum; z++)
					prob += Probability.influencedPosterior_zbopa(o, t, ta, z, Constant.INNOTVATION, -1, -1, sample, option);
				
				llh+=Math.log(prob);
			}
		}
		return llh;
	}

}
