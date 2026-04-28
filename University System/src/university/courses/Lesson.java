package university.courses;

import university.enums.LessonType;

import java.io.Serializable;

public class Lesson implements Serializable {
    private String topic;
    private LessonType type;
    public Lesson(String topic, LessonType type){
        this.topic = topic;
        this.type = type;
    }

    public String getTopic() { 
    	return topic; }
    public LessonType getType() { 
    	return type; }
    public void setTopic(String topic) { 
    	this.topic = topic; }
    public void setType(LessonType type) { 
    	this.type = type; }

    @Override
    public String toString() {
        return String.format(
            "Lesson { topic='%s', type=%s }",
            topic, type.getDisplayName()
        );
    }
}
