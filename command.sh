
# Print the help information
#java -cp infdetection.jar nmsu.cs.MainInfDetection -help

# run oaim model and normal blocked Gibbs sampling on Twitter500 dataset.  The model parameter setting is as below:
# Z=10, number of burn-in iteration=100 allow one object to be both influencing and influenced.
java   -cp infdetection.jar nmsu.cs.MainInfDetection -chainNum 2 -graphfile  ./data/twitter500/cite.txt -paperfolder ./data/twitter500/tweet/ -aspectfile ./data/twitter500/aspect.txt -samplerId twitter_500_oaim_z_10 -znum 10  -burnin 100 -duplicate yes -model oaim -sampleMethod normal

