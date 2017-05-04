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

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * sparse influenced LAIM class
 * we take a similar idea from paper
 * "Efficient Methods for Topic Model Inference on Streaming Document Collections, KDD2009"
 * to speed up sampling procedure
 * @author Chuan Hu (chu@cs.nmsu.edu)
 */
public class SparseInfluencedLAIM implements SparseInfluencedPro {
    /**
     * maintaining sum for constant term for both influencing and influenced obj
     */
    double sum_s;

    /**
     * maintaining one count constant term for influencing obj
     */
    double sum_r_0;

    /**
     * maintaining sum one count term constant for each oprime
     */
    Map<Integer, Double> sum_r_1;

    /**
     * object index
     */
    int oIndex;

    /**
     * oprime list
     */
    List<Integer> oprimeList;

    public SparseInfluencedLAIM(int oIndex) {
        this.oIndex = oIndex;
        this.sum_r_0 = 0;
        this.sum_s = 0;
        this.sum_r_1 = new HashMap<Integer, Double>();
    }

    @Override
    public void buildSum(final SampleData sampleData){
        this.oprimeList = sampleData.getReferencePubIndexIdList().get(oIndex);
        sum_r_0 = 0;
        sum_s = 0;
        sum_r_1 = new HashMap<Integer, Double>();

        double[] N_z_all = sampleData.N_z_all;
        Map<Integer, Map<Integer, Double>> N_oz_influenced = sampleData.N_oz_innov_influenced;
        Map<Integer, Map<Integer, Double>> NN_opz_influenced = sampleData.NN_opz_bold_influencing;
        Map<Integer, Map<Integer, Double>> N_oz_influencing = sampleData.N_oz_influencing;
        CmdOption cmdOption = sampleData.cmdOption;

        //compute sum_s
        for(double z_count : N_z_all){
            double num = cmdOption.alphaTheta*cmdOption.alphaPhi;
            double den = (z_count + sampleData.getNumTokens()*cmdOption.alphaPhi);
            sum_s +=  num/den ;
        }

        //iterate through each non zero topic in this object
        //compute sum_r_0
        Map<Integer, Double> N_o_z = N_oz_influenced.get(oIndex);
        if (N_o_z==null) {
            sum_r_0 = 0;
            System.out.println("empty objects "+oIndex+"\t");
        }
        else {
            for (Map.Entry<Integer, Double> entry : N_o_z.entrySet()) {// there are empty objects
                int zIndex = entry.getKey();

                double num = cmdOption.alphaPhi*entry.getValue();
                double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);

                sum_r_0 += num/den;
            }
        }

        //compute sum_r_1
        for(int oprime : oprimeList){
            double sum_r_1_oprime = 0;

            for(int zIndex = 0; zIndex < cmdOption.znum ; zIndex++){
                double num = cmdOption.alphaPhi
                  * (Util.get2Map(NN_opz_influenced, oprime, zIndex) +
                     Util.get2Map(N_oz_influencing, oprime, zIndex) );
                double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);

                sum_r_1_oprime += (num/den) ;
            }

//            System.out.println(Debugger.getCallerPosition()+" obj "+oIndex+" op "+oprime+" r_1_op "+sum_r_1_oprime);

            sum_r_1.put(oprime, sum_r_1_oprime);
        }

//        double tmp = 0;
//        for (double r_1 : sum_r_1.values())
//            tmp += r_1;

//        System.out.println(Debugger.getCallerPosition()+" obj "+oIndex+" r_1 "+tmp);

    }


    @Override
    public void updSum(final SampleData sampleData, int b, int a, int op, int z, int freq){
        double[] N_z_all = sampleData.N_z_all;
        Map<Integer, Map<Integer, Double>> N_oz_influenced = sampleData.N_oz_innov_influenced;
        Map<Integer, Map<Integer, Double>> N_opz_influenced = sampleData.NN_opz_bold_influencing;
        Map<Integer, Map<Integer, Double>> N_oz_influencing = sampleData.N_oz_influencing;

        CmdOption cmdOption = sampleData.cmdOption;

        //update sum_s
        double num = cmdOption.alphaTheta * cmdOption.alphaPhi;
        double den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi);
        sum_s -= (num/den);

        num = cmdOption.alphaTheta * cmdOption.alphaPhi;
        den = (N_z_all[z] + freq + sampleData.getNumTokens()*cmdOption.alphaPhi);
        sum_s += (num/den);

        //update sum_r_0
        //update numerator only when b==0
        int numFreq = b==0?freq:0;
        int denFreq = freq;

        num = cmdOption.alphaPhi* Util.get2Map(N_oz_influenced, oIndex, z);
        den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi);
        sum_r_0 -= (num/den);

        num = cmdOption.alphaPhi * (Util.get2Map(N_oz_influenced, oIndex, z) + numFreq);
        den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi + denFreq);
        sum_r_0 += (num/den);

        //update sum_r_1
        //update numerator only when b==1
        //update for each oprime
        numFreq = b==1?freq:0;
        denFreq = freq;
        for (int oprime : oprimeList) {
            double sum_r_1_op_value = sum_r_1.get(oprime);

            double sum_r_1_op_value_copy =  sum_r_1_op_value;

            num = cmdOption.alphaPhi*
              ( Util.get2Map(N_opz_influenced, oprime, z)
              + Util.get2Map(N_oz_influencing, oprime, z) );
            den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi);

            sum_r_1_op_value -= (num/den);

            num = cmdOption.alphaPhi*
              ( Util.get2Map(N_opz_influenced, oprime, z) +
                Util.get2Map(N_oz_influencing, oprime, z) + oprime==op?numFreq:0 );
            den = (N_z_all[z] + sampleData.getNumTokens()*cmdOption.alphaPhi + denFreq );

            sum_r_1_op_value += (num/den);

            if ( sum_r_1_op_value < 0) {
//                System.out.println(Debugger.getCallerPosition()+" obj "+oIndex+" b "+ b +" op "+op+" z "+z+" freq "+freq+" r_1 "+sum_r_1_op_value);
//                System.exit(1);
                sum_r_1_op_value = 0;
            }

            sum_r_1.put(oprime, sum_r_1_op_value);
        }
    }

    @Override
    public int[] drawMAPInfluenced_baopz(final SampleData sampleData, int ta, int token){
        // z related
        double[] N_z_all = sampleData.N_z_all;
        Map<Integer, Map<Integer, Double>> N_oz_influenced = sampleData.N_oz_innov_influenced;
        Map<Integer, Map<Integer, Double>> N_tz_all = sampleData.N_tz_all;
        Map<Integer, Map<Integer, Double>> NN_opz = sampleData.NN_opz_bold_influencing;
        Map<Integer, Map<Integer, Double>> N_oz_influencing = sampleData.N_oz_influencing;
        //op related
        double[] NN_op = sampleData.NN_op_bold_influencing;
        double[] N_op = sampleData.N_o_influencing;
        //a related
        Map<Integer,  Map<Integer, Map<Integer, Double>>> N_oaop = sampleData.N_oaop_influenced;
        double[] N_ab1 = sampleData.N_a_all;
        Map<Integer, Map<Integer, Double>> N_atab1 = sampleData.N_ata_influenced;
        Map<Integer, Map<Integer, Double>> N_oab1 = sampleData.N_oa_influenced;

        CmdOption cmdOption = sampleData.cmdOption;

        double b0Constant =
          ( Util.get2Map(sampleData.N_ob_influenced, oIndex, Constant.INNOTVATION) + cmdOption.alphaLambdaInnov)
          / ( Util.get2Map(sampleData.N_ob_influenced, oIndex, Constant.INNOTVATION) + cmdOption.znum*cmdOption.alphaTheta ) ;

        double b1Constant =
          ( Util.get2Map(sampleData.N_ob_influenced, oIndex, Constant.INHERITANCE) + cmdOption.alphaLambdaInherit)
          / (Util.get2Map(sampleData.N_ob_influenced, oIndex, Constant.INHERITANCE) + cmdOption.anum * cmdOption.alphaEta) ;

        //compute s_0
        double s_0 = sum_s * b0Constant;
        //compute r_0
        double r_0 = sum_r_0 * b0Constant;

        //compute q_0
        Map<Integer, Double> N_t_z = N_tz_all.get(token);
        double q_0 = 0;
        for(Map.Entry<Integer, Double> entry : N_t_z.entrySet()){
            int zIndex = entry.getKey();
            double N_t_z_value = entry.getValue();
            double num = N_t_z_value
              *
              ( Util.get2Map(N_oz_influenced, oIndex, zIndex) + cmdOption.alphaTheta);
            double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);
            q_0 += b0Constant * (num/den);
        }

        //compute s_1
        //compute r_1 : sum over each op and a
        double s_1 = 0;
        double r_1 = 0;
        double q_1 = 0;

        //loop over a
        for (int aIndex = 0; aIndex < cmdOption.anum; aIndex++){
            double num1 = (Util.get2Map(N_oab1, oIndex, aIndex) + cmdOption.alphaEta)
              * (Util.get2Map(N_atab1, aIndex, ta) + cmdOption.alphaPsi);
            double den1 = (Util.get2Map(N_oab1, oIndex, aIndex) + oprimeList.size()*cmdOption.alphaGamma)
              * (N_ab1[aIndex] + sampleData.numTa * cmdOption.alphaPsi);

            //loop over oprime
            for (int opIndex : oprimeList){

                double num2 = Util.get3Map(N_oaop, oIndex, aIndex, opIndex) + cmdOption.alphaGamma;
                double den2 = NN_op[opIndex] + N_op[opIndex] + cmdOption.znum*cmdOption.alphaTheta;

                s_1 += b1Constant * (num1/den1) * (num2/den2) * sum_s;
                r_1 += b1Constant * (num1/den1) * (num2/den2) * sum_r_1.get(opIndex);

                //q1 compute
                for(Map.Entry<Integer, Double> entry : N_t_z.entrySet()){
                    int zIndex = entry.getKey();
                    double N_t_z_value = entry.getValue();

                    double num3 = N_t_z_value *
                      ( Util.get2Map(N_oz_influencing, opIndex, zIndex)
                      + Util.get2Map(NN_opz, opIndex, zIndex)
                      + cmdOption.alphaTheta);
                    double den3 = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);
                    q_1 += b1Constant * (num1/den1) * (num2/den2) * (num3/den3);
                }
            }
        }

        double totalSum = s_0 + r_0 + q_0 + s_1 + r_1 + q_1;

        double x = Math.random() * totalSum;

        if (cmdOption.sampleMethod.equals("compare"))
            if (Math.random()*100 > 99.99){                             //check the difference between sum p(b=0) and sum p(b=1).  It seems something wrong here
                System.out.println(Debugger.getCallerPosition()+" Sparse sum p(b=0)="+(s_0+r_0+q_0)+" sum p(b=1)="+(s_1+r_1+q_1));
                System.out.println(Debugger.getCallerPosition()+" obj "+oIndex+" ta "+ta+" token "+token +
                  "\r\n s_0 "+s_0+" s_1 "+s_1+" r_0 "+r_0+" r_1 "+r_1+" q_0 "+q_0+" q_1 "+q_1+
                  " x "+x+" total sum "+totalSum);
            }

        double tmpSum = 0;
        if (x <= s_0){// sample falls in b=0 constant interval
            for(int zIndex=0; zIndex<N_z_all.length; zIndex++){
                double num = cmdOption.alphaTheta*cmdOption.alphaPhi;
                double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);
                tmpSum += b0Constant * (num / den);
                if (tmpSum >= x){
                    return new int[]{Constant.INNOTVATION, -1, -1, zIndex} ;
                }
            }
        }
        else if (x <= s_0+r_0){// sample falls in b=0 one term interval
            tmpSum = s_0;
            //iterate through each non zero topic in this object
            Map<Integer, Double> N_o_z = N_oz_influenced.get(oIndex);
            for(Map.Entry<Integer, Double> entry : N_o_z.entrySet()){
                int zIndex = entry.getKey();

                double num = cmdOption.alphaPhi*entry.getValue();
                double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);

                tmpSum += b0Constant * (num / den);
                if (tmpSum >= x)
                    return new int[]{Constant.INNOTVATION, -1, -1, zIndex};
            }
            System.out.println(Debugger.getCallerPosition()+" x here ");
        }
        else if (x <= s_0 + r_0 + q_0){// sample falls in b=0 two term interval
            tmpSum = s_0 + r_0;
            for(Map.Entry<Integer, Double> entry : N_t_z.entrySet()){
                int zIndex = entry.getKey();
                double N_t_z_value = entry.getValue();
                double num = N_t_z_value
                  *
                  (  Util.get2Map(N_oz_influenced, oIndex, zIndex) + cmdOption.alphaTheta);
                double den = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);
                tmpSum += b0Constant * (num/den);
                if (tmpSum >= x)
                    return new int[]{Constant.INNOTVATION, -1, -1, zIndex};
            }
            System.out.println(Debugger.getCallerPosition()+" x here ");
        }
        else  if (x <= s_0 + r_0 + q_0 + s_1){// sample falls in b=1 constant interval
            tmpSum = s_0 + r_0 + q_0;

            for (int aIndex = 0; aIndex < cmdOption.anum; aIndex++){
                double num1 = (Util.get2Map(N_oab1, oIndex, aIndex) + cmdOption.alphaEta)
                  * (Util.get2Map(N_atab1, aIndex, ta) + cmdOption.alphaPsi);
                double den1 = (Util.get2Map(N_oab1, oIndex, aIndex) + oprimeList.size()*cmdOption.alphaGamma)
                  * (N_ab1[aIndex] + sampleData.numTa * cmdOption.alphaPsi);

                for (int opIndex : oprimeList){

                    double num2 = Util.get3Map(N_oaop, oIndex, aIndex, opIndex) + cmdOption.alphaGamma;
                    double den2 = NN_op[opIndex] + N_op[opIndex] + cmdOption.znum*cmdOption.alphaTheta;

                    tmpSum += b1Constant * (num1/den1) * (num2/den2) * sum_s;

                    if (tmpSum >= x) {
                        tmpSum -= b1Constant * (num1/den1) * (num2/den2) * sum_s;
                        for(int zIndex = 0; zIndex<cmdOption.znum; zIndex++){
                            double z_count = N_z_all[zIndex];
                            double num3 = cmdOption.alphaTheta*cmdOption.alphaPhi;
                            double den3 = (z_count + sampleData.getNumTokens()*cmdOption.alphaPhi);
                            tmpSum +=  b1Constant * (num1/den1) * (num2/den2) * (num3/den3);
                            if (tmpSum >= x) {
                                return new int[]{Constant.INHERITANCE, aIndex, opIndex, zIndex};
                            }
                        }
                    }
                }
            }
            System.out.println(Debugger.getCallerPosition()+" x here ");
        }
        else if (x <= s_0 + r_0 + q_0 + s_1 + r_1){// sample falls in b=1 one term interval
            tmpSum = s_0 + r_0 + q_0 + s_1;

            for (int aIndex = 0; aIndex < cmdOption.anum; aIndex++){
                double num1 = (Util.get2Map(N_oab1, oIndex, aIndex) + cmdOption.alphaEta)
                  * (Util.get2Map(N_atab1, aIndex, ta) + cmdOption.alphaPsi);
                double den1 = (Util.get2Map(N_oab1, oIndex, aIndex) + oprimeList.size()*cmdOption.alphaGamma)
                  * (N_ab1[aIndex] + sampleData.numTa * cmdOption.alphaPsi);

                for (int opIndex : oprimeList){

                    Map<Integer, Double> N_op_z_influenced = NN_opz.get(opIndex);
                    Map<Integer, Double> N_op_z_influencing = N_oz_influencing.get(opIndex);

                    N_op_z_influenced = N_op_z_influenced==null? new HashMap<Integer, Double>() : N_op_z_influenced;
                    N_op_z_influencing = N_op_z_influencing==null? new HashMap<Integer, Double>() : N_op_z_influencing;

                    double num2 = Util.get3Map(N_oaop, oIndex, aIndex, opIndex) + cmdOption.alphaGamma;
                    double den2 = NN_op[opIndex] + N_op[opIndex] + cmdOption.znum*cmdOption.alphaTheta;

                    double sum_r_1_value = sum_r_1.containsKey(opIndex)?sum_r_1.get(opIndex) : 0;

                    tmpSum += b1Constant * (num1/den1) * (num2/den2) * sum_r_1_value;

                    if (tmpSum >= x){
                        tmpSum -=  b1Constant * (num1/den1) * (num2/den2) * sum_r_1_value;

                        Set<Integer> nonZero_op_z = Sets.union(N_op_z_influenced.keySet(), N_op_z_influencing.keySet());

                        for (int zIndex : nonZero_op_z){
                            double num3 = (Util.get2Map(NN_opz, opIndex, zIndex) + Util.get2Map(N_oz_influencing, opIndex, zIndex))
                              * cmdOption.alphaPhi;
                            double den3 = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);
                            tmpSum += b1Constant * (num1/den1) * (num2/den2) * (num3/den3);

                            if (tmpSum >= x)
                                return new int[]{Constant.INHERITANCE, aIndex, opIndex, zIndex};
                        }
                    }

                }
            }
            System.out.println(Debugger.getCallerPosition()+" x here ");
        }
        else{//s_0 + r_0 + q_0 + s_1 + r_1 <= x <= s_0 + r_0 + q_0 + s_1 + r_1 + q_1
            // sample falls in b=1 two term interval
            tmpSum = s_0 + r_0 + q_0 + s_1 + r_1;

            for (int aIndex = 0; aIndex < cmdOption.anum; aIndex++){
                double num1 = (Util.get2Map(N_oab1, oIndex, aIndex) + cmdOption.alphaEta)
                  * (Util.get2Map(N_atab1, aIndex, ta) + cmdOption.alphaPsi);
                double den1 = (Util.get2Map(N_oab1, oIndex, aIndex) + oprimeList.size()*cmdOption.alphaGamma)
                  * (N_ab1[aIndex] + sampleData.numTa * cmdOption.alphaPsi);

                for (int opIndex : oprimeList){

                    double num2 = Util.get3Map(N_oaop, oIndex, aIndex, opIndex) + cmdOption.alphaGamma;
                    double den2 = NN_op[opIndex] + N_op[opIndex] + cmdOption.znum*cmdOption.alphaTheta;

                    //q1 compute
                    for(Map.Entry<Integer, Double> entry : N_t_z.entrySet()){
                        int zIndex = entry.getKey();
                        double N_t_z_value = entry.getValue();

                        double num3 = N_t_z_value *
                          ( Util.get2Map(N_oz_influencing, opIndex, zIndex)
                            + Util.get2Map(NN_opz, opIndex, zIndex)
                            + cmdOption.alphaTheta);
                        double den3 = (N_z_all[zIndex] + sampleData.getNumTokens()*cmdOption.alphaPhi);
                        tmpSum += b1Constant * (num1/den1) * (num2/den2) * (num3/den3);
                        if (tmpSum >= x)
                            return new int[]{Constant.INHERITANCE, aIndex, opIndex, zIndex};
                    }
                }
            }
            System.out.println(Debugger.getCallerPosition()+" x here ");
        }

        System.out.println(Debugger.getCallerPosition()+" obj "+oIndex+" ta "+ta+" token "+token +
          "\r\n s_0 "+s_0+" s_1 "+s_1+" r_0 "+r_0+" r_1 "+r_1+" q_0 "+q_0+" q_1 "+q_1+
          " x "+x+" total sum "+totalSum);
//        System.out.println(Debugger.getCallerPosition()+" sparse sum probability "+totalSum);

        return new int[]{-1, -1, -1, -1};
    }

}
