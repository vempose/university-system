package university.academic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class School implements Serializable {
    private String name;
    private List<Major> majors;
    public School(String name){
        this.name = name;
        this.majors = new ArrayList<>();
    }
    public void  addMajor(Major major){ 
    	majors.add(major); }
    public String getName(){ 
    	return name; }
    public List<Major> getMajors(){ 
    	return new ArrayList<>(majors); }

    @Override
    public String toString() {
        return String.format("School{ name='%s', majors=%d }", name, majors.size());
    }
}
