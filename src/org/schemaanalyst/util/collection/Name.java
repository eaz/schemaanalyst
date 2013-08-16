package org.schemaanalyst.util.collection;

import java.io.Serializable;

public class Name implements Serializable {

    private static final long serialVersionUID = -6918681791248562084L;

    private String name;
    private String caseInsensitiveName;
    
    public Name(String name) {
        setName(name);
    }
    
    public String get() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        this.caseInsensitiveName = name.toLowerCase();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((caseInsensitiveName == null) ? 0 : caseInsensitiveName
                        .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Name other = (Name) obj;
        if (caseInsensitiveName == null) {
            if (other.caseInsensitiveName != null)
                return false;
        } else if (!caseInsensitiveName.equals(other.caseInsensitiveName))
            return false;
        return true;
    }
    
    public String toString() {
        return name;
    }    
}
