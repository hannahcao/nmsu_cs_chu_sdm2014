package nmsu.cs;

import java.util.*;

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

/**
 * the raw data read in from files
 *  @author Huiping Cao (hcao@cs.nmsu.edu)
 */
public class DataRaw {
	/**
	 * citation relation map.  dataset id
	 */
	public HashMap<Integer, List<Integer>> pubId2CiteIds;
	/**
	 * document id to document content map.
	 */
	public HashMap<Integer, Doc> id2Docs;
	
	public Map<Integer, String> id2Aspect;

	public DataRaw(){
		pubId2CiteIds = new HashMap<Integer, List<Integer>>(); //object id: object id list cited 
		id2Docs = new HashMap<Integer, Doc>();//object id: object document
		id2Aspect = new TreeMap<Integer, String>();
	}
	
	/**
	 * Manually set a set of data
	 */
	public void getManualData()
	{
		pubId2CiteIds.put(10, Arrays.asList(1,2,3)); //10 cites 1, 2, 3
        pubId2CiteIds.put(11, Arrays.asList(3,4,5)); //11 cites 3, 4, 5
        pubId2CiteIds.put(12, Arrays.asList(6,7,8,9));
        pubId2CiteIds.put(13, Arrays.asList(1,2,8,9));
        
        id2Docs.put(1, new Doc(1, "Document number one", Collections.singletonList("American football, known in the United States and Canada simply as football,[1] is a competitive team sport known for mixing strategy with physical play. The objective of the game is to score points by advancing the ball[2] into the opposing team's end zone. The ball can be advanced by carrying it (a running play) or by throwing it to a teammate (a passing play). Points can be scored in a variety of ways, including carrying the ball over the opponent's goal line; catching a pass from beyond that goal line; kicking the ball through the goal posts at the opponent's end zone; and tackling an opposing ballcarrier within his end zone. The winner is the team with the most points when the time expires.")));
        id2Docs.put(2, new Doc(2, "Document number two", Collections.singletonList("The sport is also played outside the United States. National leagues exist in Germany, Italy, Switzerland, Finland, Sweden, Japan, Mexico, Israel, Spain, Austria and several Pacific Island nations.")));
        id2Docs.put(3, new Doc(3, "Document number three", Collections.singletonList("The National Football League, the largest professional American football league in the world, ran a developmental league in Europe from 1991�1992 and then from 1995�2006.")));
        id2Docs.put(4, new Doc(4, "Document number four", Collections.singletonList("American football is closely related to Canadian football, but with significant differences. Both sports originated from rugby football.")));
        id2Docs.put(5, new Doc(5, "Document number five", Collections.singletonList("The history of American football can be traced to early versions of rugby football and soccer. Both games have their origins in varieties of football played in the United Kingdom in the mid-19th century, in which a ball is kicked at a goal and/or run over a line.")));
        id2Docs.put(6, new Doc(6, "Document number six", Collections.singletonList("Also like soccer, American football has twenty two players on the field of play. Furthermore, some player position references from soccer are used, such as the term \"halfback\" and \"fullback\".")));
        id2Docs.put(7, new Doc(7, "Document number seven",Collections.singletonList("American football resulted from several major divergences from rugby football, most notably the rule changes instituted by Walter Camp, considered the \"Father of American Football\".")));
        id2Docs.put(8, new Doc(8, "Document number eight",Collections.singletonList("Among these important changes were the introduction of the line of scrimmage and of down-and-distance rules. In the late 19th and early 20th centuries, gameplay developments by college coaches such as Eddie Cochems, Amos Alonzo Stagg, Knute Rockne, and Glenn \"Pop\" Warner helped take advantage of the newly introduced forward pass.")));
        id2Docs.put(9, new Doc(9, "Document number nine",Collections.singletonList("The popularity of collegiate football grew as it became the dominant version of the sport for the first half of the twentieth century.")));
        id2Docs.put(10, new Doc(10, "Document number ten",Collections.singletonList("Bowl games, a college football tradition, attracted a national audience for collegiate teams. Bolstered by fierce rivalries, college football still holds widespread appeal in the US")));
        id2Docs.put(11, new Doc(11, "Document number eleven", Collections.singletonList("The origin of professional football can be traced back to 1892, with William \"Pudge\" Heffelfinger's $500 contract to play in a game for the Allegheny Athletic Association against the Pittsburgh Athletic Club.")));
        id2Docs.put(12, new Doc(12, "Document number twelve", Collections.singletonList("In 1920 the American Professional Football Association was formed. The first game was played in Dayton, Ohio on October 3rd, 1920 with the host Triangles defeating the Columbus Panhandles 14-0.")));
        id2Docs.put(13, new Doc(13, "Document number thirteen", Collections.singletonList("The league changed its name to the National Football League (NFL) two years later, and eventually became the major league of American football. Initially a sport of Midwestern, industrial towns in the United States, professional football eventually became a national phenomenon.")));

	}
}
