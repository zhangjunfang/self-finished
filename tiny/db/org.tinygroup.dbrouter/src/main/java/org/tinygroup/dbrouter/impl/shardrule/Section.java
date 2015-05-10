package org.tinygroup.dbrouter.impl.shardrule;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("section")
public class Section {

    private long start;
    private long end;

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (end ^ (end >>> 32));
        result = prime * result + (int) (start ^ (start >>> 32));
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Section other = (Section) obj;
        if (end == other.end && start == other.start) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format("Section [start=%s, end=%s]", start, end);
    }


}
