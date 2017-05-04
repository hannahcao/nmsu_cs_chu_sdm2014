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
 * The element used to keep the sample (OAIM) for one influenced (citing)
 * object's one position (s) 
 * 
 * @author  Chuan Hu (chu@cs.nmsu.edu)
 *
 */
public class SampleElementInfluencedOAIM implements Comparable<SampleElementInfluencedOAIM>, Cloneable {
	
	protected int obj; //d
	protected int token; //w
	protected int ta; //ta
	protected int z; //t
	protected int b; //binary value, s
	protected int oprime; //c
	
	public SampleElementInfluencedOAIM(){;}
	public SampleElementInfluencedOAIM(int _obj, int _t, int _ta, int _z, int _b, int _oprime)
	{
		obj = _obj;
		token = _t;
		z = _z;
		b = _b;
		ta = _ta;
		oprime = _oprime; //this is only useful when b = 1-contant.INNOVATION
	}
	
	@Override
	public int compareTo(SampleElementInfluencedOAIM arg0) {
		
		if(obj < arg0.obj) return (-1);
		else if (obj > arg0.obj) return (1);
		else{
			if(ta < arg0.ta)	return (-1);
			else if(ta > arg0.ta) return 1;
			else{
				if(token < arg0.token) return (-1);
				else if (token > arg0.token) return 1;
				else{
					if (z < arg0.z) return (-1);
					else if (z > arg0.z) return 1;
					else {
						if( b < arg0.b) return (-1);
						else if (b > arg0.b) return 1;
						else {
								if(oprime < arg0.oprime) return (-1);
								else if (oprime > arg0.oprime) return (1);
								else return 0;
						}
					}
				}
			}
		}
	}
	
	public String toString()
	{
		String str = "[o"+obj+"][t"+token+"][ta"+ta+"][z"+z+"]"+"[b"+b+"[op"+oprime+"]";
		return str;
	}
	
	/**
	 * @return the obj
	 */
	public int getObj() {
		return obj;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setObj(int obj) {
		this.obj = obj;
	}

	/**
	 * @return the token
	 */
	public int getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(int token) {
		this.token = token;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return b;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(int b) {
		this.b = b;
	}

	/**
	 * @return the oprime
	 */
	public int getOprime() {
		return oprime;
	}

	/**
	 * @param oprime the oprime to set
	 */
	public void setOprime(int oprime) {
		this.oprime = oprime;
	}
	public int getTa() {
		return ta;
	}
	public void setTa(int ta) {
		this.ta = ta;
	}


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
