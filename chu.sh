 java -Xmx2G -ea -cp infdetection.jar nmsu.cs.MainInfDetection -chainNum 2 -graphfile  ./data/twitter1000/cite.txt -paperfolder ./data/twitter1000/tweet/ -aspectfile ./data/twitter1000/aspect.txt -samplerId twitter_1000_oaim_z_10_numThread_$n -znum 10  -burnin 100 -duplicate yes -model oaim -sampleMethod normal 
#-concurrent n
#> serial.txt 2>&1
#-Xmx10G 
#for n in 4
# 4 6 8 10 12 14 16 18 20
# 22 24 26 28 30 32 34 36 38 40 42 44 46 48 50 52 54 56 58 60
#do
    # echo "$n" ;
    # java -version
#    java -Xmx10G -ea -cp infdetection.jar nmsu.cs.MainInfDetection -chainNum 2 -graphfile  ./data/twitter500/cite.txt -paperfolder ./data/twitter500/tweet/ -aspectfile ./data/twitter500/aspect.txt -samplerId twitter_500_oaim_z_10_numThread_$n -znum 10  -burnin 5 -duplicate yes -model oaim -sampleMethod normal -numThread $n -checkConsistence n -version v2 -lockType method -printThread y -debug n -summary ./multithread_result/test2.xls
    # 
    #  -printThread y
    # > test$n.txt 2>&1
#done

# for i in {1..5}
# do
#    echo "$i"
# done
