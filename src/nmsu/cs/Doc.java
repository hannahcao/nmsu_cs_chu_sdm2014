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

import java.util.List;



/**
 * Class represents a document
 * @author Huiping Cao (hcao@cs.nmsu.edu)
 */
public class Doc {
	private final int id;
	private final String title;
	/**
	 * follow the order aspect list file
	 */
	private final List<String> text;
	
	public Doc(int id, String title, List<String> text){
		this.id = id;
		this.title = title;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	//	public String getDescription() {
	//	    return text;
	//	}

	public List<String> getText() {
		return text;
	}
	
	public String getFullText(){
		String fullText = "";
		for(String section : text)
			fullText+=" "+section;
		return fullText;
	}

	public String toString(){
		return ("id:"+id+",title:"+title+",desc:omitted");
	}
}
