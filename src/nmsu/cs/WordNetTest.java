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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import edu.smu.tspell.wordnet.AdjectiveSynset;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * word net test program.
 * @author Chuan Hu (chu@cs.nmsu.edu)
 */
public class WordNetTest {
	public static void main(String[] args){
		System.setProperty("wordnet.database.dir", "/opt/local/share/WordNet-3.0/dict");
		System.out.println(System.getProperty("wordnet.database.dir"));

		FileReader fr;
		BufferedReader br;
		File target = new File("./data/twitter3/aspect_ext.txt");
		WordNetDatabase database = WordNetDatabase.getFileInstance(); 

		try {
			fr = new FileReader(target);
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line!=null){
				StringTokenizer tool = new StringTokenizer(line, ",./<>?[]\\{}|+=-_&/!@#$%^&*() ");
				
				String lineExtentsion = "";
				while(tool.hasMoreTokens()){
					String word = tool.nextToken();
					String wordExtension = word;
					
					NounSynset nounSynset; 
					NounSynset[] nounHyponyms; 

					Synset[] synsets = database.getSynsets(word, SynsetType.NOUN, true); 

					for (int i = 0; i < synsets.length; i++) {
						nounSynset = (NounSynset)(synsets[i]); 
						nounHyponyms = nounSynset.getHypernyms();
						for(Synset set : nounHyponyms){
//							System.out.print("noun: ");
							String[] forms = set.getWordForms();
							for(String form : forms){
//								System.out.print(form + ", ");
								wordExtension+=" "+form;
							}
//							System.out.println("");
						}
					}

					VerbSynset verbSynset; 
					VerbSynset[] verbHyponyms; 
					synsets = database.getSynsets(word, SynsetType.VERB, true); 

					for (int i = 0; i < synsets.length; i++) {
						verbSynset = (VerbSynset)synsets[i];
						verbHyponyms = verbSynset.getHypernyms();
						for(Synset set : verbHyponyms){
//							System.out.print("verb: ");
							String[] forms = set.getWordForms();
							for(String form : forms){
//								System.out.print(form + ", ");
								wordExtension+=" "+form;
							}
//							System.out.println("");
						}
					}

					AdjectiveSynset adjSynset;
					AdjectiveSynset[] adjSet;
					synsets = database.getSynsets(word, SynsetType.ADJECTIVE, true); 
					for (int i = 0; i < synsets.length; i++) {
						adjSynset = (AdjectiveSynset)synsets[i];
						adjSet = adjSynset.getRelated();
						for(Synset set : adjSet){
//							System.out.print("adj: ");
							String[] forms = set.getWordForms();
							for(String form : forms){
//								System.out.print(form + ", ");
								wordExtension+=" "+form;
							}
//							System.out.println("");
						}
					}
					lineExtentsion+=" "+wordExtension;
				}
				System.out.println("\""+line.trim()+"\",\""+lineExtentsion.trim()+"\"");
				line = br.readLine();
			}
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
