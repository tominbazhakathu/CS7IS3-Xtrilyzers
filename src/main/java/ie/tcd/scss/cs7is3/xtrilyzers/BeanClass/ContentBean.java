package ie.tcd.scss.cs7is3.xtrilyzers.BeanClass;

public class ContentBean {

    String newsPaperName , documentTitle,aticleContent, documentDate,documentID;

    public ContentBean(String newsPaperName, String documentTitle, String aticleContent, String documentDate, String documentID) {
        this.newsPaperName = newsPaperName;
        this.documentTitle = documentTitle;
        this.aticleContent = aticleContent;
        this.documentDate = documentDate;
        this.documentID = documentID;
    }

    public String getNewsPaperName() {
        return newsPaperName;
    }

    public void setNewsPaperName(String newsPaperName) {
        this.newsPaperName = newsPaperName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getAticleContent() {
        return aticleContent;
    }

    public void setAticleContent(String aticleContent) {
        this.aticleContent = aticleContent;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
