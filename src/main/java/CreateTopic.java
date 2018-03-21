public class CreateTopic {
    private String number;
    private String title;
    private String description;
    private String narrative;

    public CreateTopic(String number, String title, String desc, String narr){
        this.description = desc;
        this.narrative = narr;
        this.number = number;
        this.title = title;
    }

    public String getQueryNumber(){
        return this.number;
    }

    public String getQueryTitle(){
        return this.title;
    }

    public String getQueryDesc(){
        return this.description;
    }

    public String getQueryNarr(){
        return this.narrative;
    }
}
