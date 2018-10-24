import java.util.ArrayList;

public class AuthorData {
    public String authorName; //作者
    public ArrayList<String> editors = new ArrayList(); //责编
    public ArrayList<String> coauthors = new ArrayList(); //合作者
    public ArrayList<String> articleTitles = new ArrayList(); //标题
    public ArrayList<String> category = new ArrayList(); //分类
    public ArrayList<String> pageName = new ArrayList(); //版名
    public ArrayList<String> type = new ArrayList(); //体裁
    public ArrayList<String> columnName = new ArrayList(); //栏目名
}

/**
 * the author entity definition in AIAS
 *     private String authorName;
 *     private String cellNumber;
 *     private String phoneNumber;
 *     private String QQ;
 *     private String weChat;
 *     private String emailAddress;
 *     private String organizationName;
 *     private String mailAddress;
 *     private String zipCode;
 *     private String workCityName;
 *     private String workProvinceName;
 *
 *
 * the article entity definition in AIAS
 *     private String    eyebrowTitle;
 *     private String    mainTitle;
 *     private String    subTitle;
 *     private String    authorsName;
 *     private String    editorsName;
 *     private String    pageName;
 *     private String    paperCategory;
 *     private String    publishDate;
 *     private String    paperType;
 *     private String    articleText;
 * **/
