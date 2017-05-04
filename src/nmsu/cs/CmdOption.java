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

import org.kohsuke.args4j.Option;


/**
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 * command line options
 */
public class CmdOption {

	//@Option(name="-est", usage="Specify whether we want to estimate model from scratch")
	//public boolean est = false;
	
	@Option(name="-help", usage="Print this help info")
	public boolean help = false;
	
	@Option(name="-graphfile", usage="Input graph file name (default: empty)")
	public String graphfile = "";
	//public String graphfile = "data/pubidcite.txt"; 	
	
	@Option(name="-paperfolder", usage="Input paper folder (default: empty)")
	public String paperfolder = "";
	//public String paperfolder = "data/paper/";
	
	@Option(name="-aspectfile", usage="Input aspect file path")
	public String aspectfile = "";
	
	@Option(name="-duplicate", usage=" whether allow duplicate objects (default: yes)")
	public String duplicate = "yes";
	//public String paperfolder = "data/paper/";
	
	@Option(name="-znum", usage="Number of latent states or topics (default: 10)")
	public int znum = 10;
	
	@Option(name="-anum", usage="Number of latent aspects (default: 5)")
	public int anum = 5; 	
	
	@Option(name="-model", usage="Sampling model: oaim OR laim")
	public String model = "oaim"; 	
	
	@Option(name="-numIter", usage="Number of Gibbs sampling iterations  (default: 1000)")
	int numIter = 10000;
	
	@Option(name="-burnin", usage="BURN IN iterations for Gibbs Sampling (default: 10)")
	int burnin = 10;
	
	@Option(name="-chainNum", usage="The number of chains used to judge convergence (default: 2)")
	int chainNum = 2;
	
	@Option(name="-rhat", usage="RHAT value for convergence (default: 1.01)")
	double R_HAT_THRESH = 1.01;

	@Option(name="-samplerId", usage="The sampler id string (default: Cao)")
	public String SAMPLER_ID = "Cao";
	
    //Parameters for distributions p(token|latent-state)
	@Option(name="-alphaPhi", usage="Dirichlet parameter alphaPhi for latent state variables (Default: 0.01)")
	public double alphaPhi=0.01;
	
	//Dirichlet for p(**|**)?
	@Option(name="-alphaPsi", usage="Dirichlet parameter alphaPsi for latent aspect variable (Default: 0.1)")
	public double alphaPsi=0.1;
	
	//Dirichlet for p(latent-state|object) 
	@Option(name="-alphaTheta", usage="Dirichlet parameter alphaTheta (Default: 0.1)")
	public double alphaTheta=0.1;
	
	@Option(name="-alphaGamma", usage="Dirichlet parameter alphaGamma for object interaction mixture (Default: 1.0)")
	public double alphaGamma=0.1;
	
	@Option(name="-alphaEta", usage="Dirichlet parameter alphaEta for aspect mixture (Default: 1.0)")
	public double alphaEta=0.1;
	
	@Option(name="-alphaLambdaInherit", usage="Dirichlet parameter inherit percentage (Default: 0.5)")
	public double alphaLambdaInherit=0.5;
	
	@Option(name="-alphaLambdaInnov", usage="Dirichlet parameter innovative percentage (Default: 0.5)")
	public double alphaLambdaInnov=0.5;
	
	@Option(name="-lambda", usage="Lambda value for baseline method")
	public double lambda = 0.5;

    @Option(name = "-sampleMethod", usage = "model sampling method, sparse or normal")
    public String sampleMethod = "normal";
}
