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
 * Constant global variables used in OAIM and LAIM model
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 */
public class Constant {
    /**
     * b=0 or 1 in the OAIM LAIM model
     */
    public final static int INNOTVATION= 0;
	public final static int INHERITANCE= 1;
	
	public final static boolean DeleteTemporaryFile = false;
	public final static String DefaultResultOutputFolder = "./output/";
	
	public final static String citeseerIndexDir = "./index/citeseer";
	
	public final static String twitterIndexDir = "./index/twitter";

	public final static String oaim = "oaim";

    /**
     * number of observed aspect
     */
	public static int aspectNum;

    /**
     * number of influenced objects
     */
	public static int oNum;

    /**
     * number of influencing objects
     */
	public static int oprimeNum;

    /**
     * number of total distinct tokens
     */
	public static int tokenNum;

    /**
     * number of topics
     */
	public static int zNum;

    public final static String load_time = "load time";

    public final  static String init_sample_time = "initial sample time";

    public  final  static  String sample_time = "sample time";

    public final static String sample_sum_time = "sample sum time";

    public  final static String converge_check_time = "converge check time";



}
