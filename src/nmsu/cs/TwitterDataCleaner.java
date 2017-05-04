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

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import com.google.common.collect.Sets;

/**
 * extract data from Twitter crawler
 * @author Chuan Hu (chu@cs.nmsu.edu)
 */
public class TwitterDataCleaner {
    private static String data_path = "./TwitterData";

    /**
     * Randomly choose dataset of size from the total data.
     * before extraction manually remove and mkdir the ./twitterSIZE directory
     * @param size
     */
    public static void extract_dataset(int size){
        File dataset = new File(data_path+"/twitter"+size);

        Set<File> friend_list_set = new HashSet<File>();

        File[] friend_list_dirs = new File(data_path+"/friendList").listFiles();
        for (File friend_list_dir : friend_list_dirs){
            File[] friend_list_json_files = friend_list_dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");  //To change body of implemented methods use File | Settings | File Templates.
                }
            });

            friend_list_set = Sets.union(new HashSet<File>(Arrays.asList(friend_list_json_files)), friend_list_set);
        }

        List<File> friend_list_arr = new ArrayList<File>(friend_list_set);

        //get map and tweets in String first, the convert to integer.

        Map<String, List<String>> inf_graph = new HashMap<String, List<String>>();
        Set<Set<String>> partition = new HashSet<Set<String>>();
        Set<String> aspect_set = new HashSet<String>();
        Set<String> objSet = new HashSet<String>();
    }


    public static void main(String[] args){
        TwitterDataCleaner.extract_dataset(100);
    }

}
