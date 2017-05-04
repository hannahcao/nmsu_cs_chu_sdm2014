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

import java.io.Serializable;
import java.util.Date;

/**
 * one record in the rHat calculation chain
 * @author  Huiping Cao (hcao@cs.nmsu.edu)
 */
public class ResultStatisticRecord implements Serializable {
	private static final long serialVersionUID = 1900491202369803233L;
	
	private int iteration;
    private double rHat;
    private long millisecoundsElapsed;
    private Date timestamp;

    public ResultStatisticRecord(int iteration, double rHat, long millisecoundsElapsed, Date timestamp) {
        this.iteration = iteration;
        this.rHat = rHat;
        this.millisecoundsElapsed = millisecoundsElapsed;
        this.timestamp = timestamp;
    }
    
    public int getIteration() {
        return iteration;
    }

    public double getRHat() {
        return rHat;
    }

    public long getMillisecoundsElapsed() {
        return millisecoundsElapsed;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

