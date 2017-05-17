This project is the code package of model OAIM, LAIM from the paper "Detecting
Influence Relationships from Graphs, SDM2014".  

@author Chuan Hu (chu@cs.nmsu.edu)
@author Huiping Cao (hcao@cs.nmsu.edu)

1.  Build the project

    1.1. change the javac version in "build.xml" to the version of your machine.
    We have tested the code on JDK 1.6 and JDK 1.7, this program runs well on
    these 2 versions of java.  
    1.2. $ ant
    1.3. infdetection.jar is the runnable jar file for influence detection.

2.  Command shell file
    command.sh is the example shell file to run influence detection.  For the meaning of each math symbol, please refer to Table 1 in our paper.

    2.1. Help information: java -cp infdetection.jar nmsu.cs.MainInfDetection -help

         -alphaEta N : Dirichlet distribution parameter alphaEta for aspect mixture $\alpha_eta$ (Default: 1.0)

         -alphaGamma N : Dirichlet distribution parameter alphaGamma for object interaction mixture $\alpha_gamma$ (Default: 1.0)

         -alphaLambdaInherit N : Dirichlet distribution parameter inherit percentage $\alpha_{\lambda_1}$ (Default: 0.5)
	 
		-alphaLambdaInnov N : Dirichlet distribution parameter innovative percentage $\alpha_{\lambda_0}$ (Default: 0.5)

         -alphaPhi N : Dirichlet distribution parameter alphaPhi for latent state variables $\alpha_phi$ (Default: 0.01)
	 
		 -alphaPsi N : Dirichlet distribution parameter alphaPsi for latent aspect variable $\alpha_psi$ (Default: 0.1)

         -alphaTheta N : Dirichlet distribution parameter alphaTheta $\alpha_theta$ (Default: 0.1)

         -anum N : Number of latent aspects used only in LAIM model $A$ (default: 5)

         -aspectfile VAL : Input aspect file path (MUST specify)

         -burnin N : BURN IN iterations for Gibbs Sampling (default: 10)

         -chainNum N : The number of chains used to judge convergence (default : 2)

         -duplicate VAL : whether allow duplicate objects (default: yes)

         -graphfile VAL : Input graph file name (MUST specify)

         -help : Print this help info

         -lambda N : Lambda value for baseline method

         -model VAL : Sampling model: oaim OR laim

         -numIter N : Number of Gibbs sampling iterations (default: 1000)

         -paperfolder VAL : Input paper or user profile folder (MUST specify)

         -rhat N : RHAT value for convergence $\hat{r}$ (default: 1.01)

         -sampleMethod VAL : model sampling method [sparse|normal]

         -samplerId VAL : The sampler id string (default: Cao)

         -znum N : Number of latent states or topics $Z$ (default: 10)
    
    2.2. An example shell script that run OAIM model with $Z=10$ and blocked Gibbs sampling
    on Twitter500 dataset:

    java -cp infdetection.jar nmsu.cs.MainInfDetection -chainNum 2 -graphfile  ./data/twitter500/cite.txt -paperfolder ./data/twitter500/tweet/ -aspectfile ./data/twitter500/aspect.txt -samplerId twitter_500_oaim_z_10 -znum 10  -burnin 100 -duplicate yes -model oaim -sampleMethod normal

3. Dataset
   In this package we share the two datasets we used in our paper, Citeseer and Twitter. Link: ask for download link.

3.1  CiteMisc (data/citeseerx_data)

   The CiteMisc dataset contains forty six research articles.  The details of
   the research articles we picked is in "data/citeseerx_data/readme.txt".

   Aspect file: aspect.txt
   Graph edge file: pubidcite.txt
   Paper json format files: paper_chu/paper_id.json: 
   Aspect level related reference label: rank_aspect.txt.  This rank file is used to calculate P@3 in Figure 5.
   
3.2  Twitter (data/twitterXXX)

   We crawl data from Twitter in a BFS way.  Our dataset contains user
   relationships, user follow list, user recent 400 tweets and user profile.
   For the experiment purpose, we only extract a subset (XXX is the number of users
   in this subset) of data from our crawled Twitter data.
   
   Aspect file: aspect.txt
   Graph edge file: cite.txt
   User profile json format files: tweet/uid.json, uid means the user id in this dataset
   
3.3  Raw data format

     It is easy to run OAIM or LAIM on your own dataset.

     1. First number your graph nodes and create a graph edge file for example
     "edge.txt".  The graph file format should be

     ----------------------------
     influenced_id influencing_id
     ...
     ----------------------------

     2. Convert the graph objects or nodes profile as the following json format:
     {
     "user_name":"...",
     "user_timeline":
	{
	     "aspect":"...",
	     "tweet":"..."
	},{},...
     }
     Use the "node_id.json" to name each graph node and put them under the same folder say "tweets"
     
     3. Put all the aspect name into one aspect file with one aspect on each row say "aspect.txt".
      
     4. To run OAIM model on your data, an example command is as below:
        ----------------------
	java -cp infdetection.jar nmsu.cs.MainInfDetection -chainNum 2 -graphfile  PATH_TO/edge.txt -paperfolder PATH_TO/tweets -aspectfile PATH_TO/aspect.txt  -znum XX  -burnin XX -duplicate yes -model oaim -sampleMethod normal
	----------------------

	To run LAIM model on your data, an example command is as below:
	----------------------
	java -cp infdetection.jar nmsu.cs.MainInfDetection -chainNum 2 -graphfile  PATH_TO/edge.txt -paperfolder PATH_TO/tweets -aspectfile PATH_TO/aspect.txt  -znum XX -anum XX  -burnin XX -duplicate yes -model laim -sampleMethod normal
	----------------------


   
