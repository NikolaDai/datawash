import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: Nikola Dai;
 * basic idea: convert the json file to the neo4j commands. have to create the nodes and edges info
 * from json file.
 */

public class JsonFileWash {
    public static void main(String[] args) throws IOException {
        //Read the original data source with json format
        File file = new File("dataJson.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        File nameFile = new File("authorList.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nameFile));

        //Generate the category mapping data
        /*
        File fileCategory = new File("category.txt");
        BufferedReader bufferedReaderCategory = new BufferedReader(new FileReader(fileCategory));
        StringBuilder categoryJson = new StringBuilder();
        String tempData;
        while ((tempData = bufferedReaderCategory.readLine()) != null) {
            categoryJson.append(tempData);
        }
        String jsonCategoryString = categoryJson.toString().replace("}{", ",");
        JSONObject jsonObjCategory = JSONObject.parseObject(jsonCategoryString);
        */

        //each line of the file is a record including all the information
        String recordData;

        Map<String, AuthorData> authorMap = new HashMap<String, AuthorData>();
        /**
         *         build one type of data structure
         *         {author, editors, {title, category, types, page, content}}
         */

        while ((recordData = bufferedReader.readLine()) != null) {
            JSONObject jsonObj = JSONObject.parseObject(recordData);
            String valueAuthor = jsonObj.getString("作者");

            String valueEditors = jsonObj.getString("责编");

            //If the author is blank which means no author listed, the related article will be removed.
            if (valueAuthor != null) {
                //editorDataCheck(valueAuthor);
                //valueAuthor = valueAuthor.replaceAll("、", ";").replaceAll("·", ";").replaceAll(" \\s+", ";");
                //make the space disappear
                valueAuthor = valueAuthor.replaceAll(" \\s+", "");
                if(valueEditors != null)
                    valueEditors = valueEditors.replaceAll(" \\s+", "");

                String[] authors = valueAuthor.split(";");

                String[] editors = null;
                if (valueEditors != null) {
                    editors = valueEditors.split(";");
                }

                if (authors.length == 1) {
                    //if the author name isn't existed in our map
                    if (!authorMap.containsKey(valueAuthor)) {
                        AuthorData authorData = new AuthorData();
                        //去除名字后的等字样
                        //authorData.authorName = valueAuthor.replaceAll("等", "");
                        authorData.authorName = valueAuthor;
                        if (editors != null)
                            for (int i = 0; i < editors.length; i++) {
                                if (!authorData.editors.contains(editors[i]))
                                    authorData.editors.add(editors[i]);
                            }

                        authorData.articleTitles.add(jsonObj.getString("标题"));
                        String categoryName = jsonObj.getString("分类");

                        if (categoryName != null) authorData.category.add(categoryName);

                        authorData.type.add(jsonObj.getString("体裁"));
                        authorData.pageName.add(jsonObj.getString("版名"));

                        if (jsonObj.getString("体裁") != "广告")
                            authorMap.put(authorData.authorName, authorData);
                    } else {
                        AuthorData authorData = authorMap.get(valueAuthor);

                        if (editors != null)
                            for (int i = 0; i < editors.length; i++) {
                                if (!authorData.editors.contains(editors[i]))
                                    authorData.editors.add(editors[i]);
                            }

                        authorData.articleTitles.add(jsonObj.getString("标题"));

                        String categoryName = jsonObj.getString("分类");

                        if (categoryName != null) authorData.category.add(categoryName);

                        authorData.type.add(jsonObj.getString("体裁"));
                        authorData.pageName.add(jsonObj.getString("版名"));

                        if (jsonObj.getString("体裁") != "广告")
                            authorMap.put(authorData.authorName, authorData);
                    }
                } else {
                    for (int i = 0; i < authors.length; i++) {
                        if (!authorMap.containsKey(authors[i])) {
                            AuthorData authorData01 = new AuthorData();
                            //去除名字后的等字样
                            authorData01.authorName = authors[i];

                            if (editors != null)
                                for (int j = 0; j < editors.length; j++) {
                                    if (!authorData01.editors.contains(editors[j]))
                                        authorData01.editors.add(editors[j]);
                                }

                            authorData01.articleTitles.add(jsonObj.getString("标题"));
                            String categoryName = jsonObj.getString("分类");
                            if (categoryName != null) authorData01.category.add(categoryName);

                            authorData01.type.add(jsonObj.getString("体裁"));
                            authorData01.pageName.add(jsonObj.getString("版名"));

                            String[] coauthors = jsonObj.getString("作者").split(";");
                            for (int k = 0; k < coauthors.length; k++) {
                                if(!authorData01.coauthors.contains(coauthors[k])&& !coauthors[k].equals(authors[i]))
                                    authorData01.coauthors.add(coauthors[k]);
                            }

                            if (jsonObj.getString("体裁") != "广告")
                                authorMap.put(authorData01.authorName, authorData01);
                        } else {
                            AuthorData authorData01 = authorMap.get(authors[i]);

                            if (editors != null)
                                for (int j = 0; j < editors.length; j++) {
                                    if (!authorData01.editors.contains(editors[j]))
                                        authorData01.editors.add(editors[j]);
                                }

                            authorData01.articleTitles.add(jsonObj.getString("标题"));

                            String categoryName = jsonObj.getString("分类");

                            if (categoryName != null) authorData01.category.add(categoryName);

                            authorData01.type.add(jsonObj.getString("体裁"));
                            authorData01.pageName.add(jsonObj.getString("版名"));

                            String[] coauthors = jsonObj.getString("作者").split(";");

                            for (int k = 0; k < coauthors.length; k++) {
                                if(!authorData01.coauthors.contains(coauthors[k])&& !coauthors[k].equals(authors[i]))
                                    authorData01.coauthors.add(coauthors[k]);
                            }

                            if (jsonObj.getString("体裁") != "广告")
                                authorMap.put(authorData01.authorName, authorData01);
                        }
                    }
                }
                }

            }
            AtomicInteger counter_integer = new AtomicInteger();

            for (String authorKey : authorMap.keySet()) {
                //System.out.println(authorKey + "#" + authorMap.get(authorKey).editors + "#" + authorMap.get(authorKey).coauthors
                //       + "#" + authorMap.get(authorKey).articleTitles);
                bufferedWriter.write(authorKey+"\n");
            }

            bufferedWriter.close();
            bufferedReader.close();

        }
    public static void editorDataCheck(String editorNames){
        if(editorNames.matches("^([\\u4E00-\\u9FA5]|;)+")) {
            if(editorNames.contains("等"))
                System.out.println(editorNames);
            if(editorNames.contains("、"))
                System.out.println(editorNames);
            if(editorNames.contains(" "))
                System.out.println(editorNames);
            String[] editorNameArray = editorNames.split(";");
            for(int i = 0; i < editorNameArray.length; i++) {
                if(editorNameArray[i].length() > 3 && !editorNameArray[i].equals("本报评论员")&& !editorNameArray[i].equals("人民日报评论员")){
                    System.out.println(editorNameArray[i]);
                    //System.out.println(editorNames)
                }
            }
        }
        else{
            System.out.println(editorNames);
        }
    }
}


