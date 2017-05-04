
for l in 0.1 
do
    java -Xmx10G  -cp infdetection.jar nmsu.cs.BaseLineMethod -chainNum 2 -graphfile "./data/citeseerx_data/pubidcite.txt" -paperfolder "./data/citeseerx_data/paper_chu/" -aspectfile "./data/citeseerx_data/aspect.txt/" -samplerId "citeseerx" -znum 50 -burnin 100 -duplicate yes -model oaim -lambda $l
done
