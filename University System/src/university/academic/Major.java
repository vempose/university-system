package university.academic;

import java.io.Serializable;

public class Major implements Serializable {
    private String name;
    public Major(String name) {
        this.name = name;
    }

    public String getName() {
    	return name; }
    public void   setName(String n){ 
    	this.name = n; }

    @Override
    public String toString() {
        return String.format("Major { name='%s' }", name);
    }
}
