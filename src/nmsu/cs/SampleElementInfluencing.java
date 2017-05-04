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
 * The element used to keep the sample (both LAIM and OAIM) for one influencing object's one position (s)
 * influencing = cited
 * 
 * @author Chuan Hu (chu@cs.nmsu.edu)
 *
 */
public class SampleElementInfluencing implements Comparable<SampleElementInfluencing>{
	protected int obj =0; //d
	protected int token =0; //w
	protected int z=0; //t
	
	public SampleElementInfluencing(){;}
	
	public SampleElementInfluencing(int _o, int _t, int _z)
	{
		obj = _o;
		token = _t;
		z = _z;
	}
	
	@Override
	public int compareTo(SampleElementInfluencing arg0) {
		if(obj < arg0.obj) return (-1);
		else if (obj > arg0.obj) return (1);
		else{
			if(token < arg0.token) return (-1);
			else if (token > arg0.token) return 1;
			else{
				if (z < arg0.z) return (-1);
				else if (z > arg0.z) return 1;
				else return 0;
			}
		}
	}
	
	public String toString()
	{
		String str = "[o"+obj+"][t"+token+"][z"+z+"]";
		return str;
	}
}
