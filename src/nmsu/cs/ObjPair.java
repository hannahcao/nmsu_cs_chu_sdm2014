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
 * Not used now, defined for object pairs
 * 
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 *
 */
public class ObjPair implements Comparable<ObjPair>{

	int objInfluenced;
	int objInfluencing;
	
	@Override
	public int compareTo(ObjPair arg0) {
		
		if(objInfluenced<arg0.objInfluenced){
			return (-1);
		}else if (objInfluenced > arg0.objInfluenced){
			return 1;
		}else{
			if(objInfluencing < arg0.objInfluencing) return (-1);
			else if (objInfluencing > arg0.objInfluencing) return (1);
			else return 0; 
		}
	}

	public String toString(){
		return "("+objInfluenced+" <- " +objInfluencing+")"; 
	}
}
