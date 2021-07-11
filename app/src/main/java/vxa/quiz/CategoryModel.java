package vxa.quiz;

public class CategoryModel {
    private String name ;
    private int Question ;
    private String url ;
    public CategoryModel(){

    }

    public CategoryModel(String name, int question, String url) {
        this.name = name;
        Question = question;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuestion() {
        return Question;
    }

    public void setQuestion(int question) {
        Question = question;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
