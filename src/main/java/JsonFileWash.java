import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.collections.MappingChange;

import java.io.*;
import java.util.*;

/**
 * author: Nikola Dai;
 * basic idea: convert the json file to the neo4j commands. have to create the nodes and edges info
 * from json file.
 */
public class JsonFileWash {
    public static void main(String[] args) throws IOException {
        //read the source file
        File file = new File("test.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        File fileCategory = new File("category.txt");
        BufferedReader bufferedReaderCategory = new BufferedReader(new FileReader(fileCategory));
        StringBuilder categoryJson = new StringBuilder();
        String tempData;
        while ((tempData = bufferedReaderCategory.readLine()) != null) {
            categoryJson.append(tempData);
        }
        String jsonCategoryString = categoryJson.toString().replace("}{", ",");
        JSONObject jsonObjCategory = JSONObject.parseObject(jsonCategoryString);

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
            if(valueEditors != null){
                valueEditors = valueEditors.replaceAll("版面编辑／","").replaceAll("版条#+\\d{4}","")
                        .replaceAll("席严峰任旭","席严峰;任旭");
            }
            if (valueAuthor != null) {
                valueAuthor = valueAuthor.replaceAll("、",";");
                valueAuthor = valueAuthor.replaceAll("·",";");

                String[] authors = valueAuthor.split(";");
                if (authors.length == 1) {
                    if (!authorMap.containsKey(valueAuthor)) {
                        AuthorData authorData = new AuthorData();
                        //去除名字后的等字样
                        authorData.authorName = valueAuthor.replaceAll("等", "");

                        if (valueEditors != null) {
                            String[] editors = valueEditors.split(";");
                            for(int i = 0; i < editors.length; i++) {
                                authorData.editors.add(editors[i]);
                            }
                        }
                        authorData.articleTitles.add(jsonObj.getString("标题"));
                        String categoryID = jsonObj.getString("分类");
                        String categoryName = null;
                        if(categoryID != null)
                            categoryName  = jsonObjCategory.getString(categoryID.toUpperCase());

                        if(categoryName != null) authorData.category.add(categoryName);

                        authorData.type.add(jsonObj.getString("体裁"));
                        authorData.pageName.add(jsonObj.getString("版名"));
                        authorMap.put( authorData.authorName, authorData);
                    } else {
                        AuthorData authorData = authorMap.get(valueAuthor);
                        if (valueEditors != null) {
                            String[] editors = valueEditors.split(";");
                            for(int i = 0; i < editors.length; i++) {

                                authorData.editors.add(editors[i]);
                            }
                        }
                        authorData.articleTitles.add(jsonObj.getString("标题"));

                        String categoryID = jsonObj.getString("分类");
                        String categoryName = null;
                        if(categoryID != null)
                            categoryName  = jsonObjCategory.getString(categoryID.toUpperCase());

                        if(categoryName != null) authorData.category.add(categoryName);

                        authorData.type.add(jsonObj.getString("体裁"));
                        authorData.pageName.add(jsonObj.getString("版名"));
                        authorMap.put(authorData.authorName, authorData);
                    }
                } else {
                    for(int i=0; i < authors.length; i++){
                        if(!authorMap.containsKey(authors[i])) {
                            AuthorData authorData01 = new AuthorData();
                            //去除名字后的等字样
                            authorData01.authorName = authors[i].replaceAll("等", "");

                            if (valueEditors != null) {
                                String[] editors = valueEditors.split(";");
                                for(int j = 0; j < editors.length; j++) {
                                  authorData01.editors.add(editors[j]);
                                }
                            }

                            authorData01.articleTitles.add(jsonObj.getString("标题"));
                            String categoryID = jsonObj.getString("分类");
                            String categoryName = null;
                            if(categoryID != null)
                                categoryName  = jsonObjCategory.getString(categoryID.toUpperCase());

                            if(categoryName != null) authorData01.category.add(categoryName);

                            authorData01.type.add(jsonObj.getString("体裁"));
                            authorData01.pageName.add(jsonObj.getString("版名"));

                            String[] coauthors = jsonObj.getString("作者").split(";");
                            for(int k = 0; k < coauthors.length; k++) {
                                authorData01.coauthors.add(coauthors[k]);
                            }

                            authorMap.put( authorData01.authorName, authorData01);
                        }else{
                            AuthorData authorData01 = authorMap.get(authors[i]);
                            if (valueEditors != null) {
                                String[] editors = valueEditors.split(";");
                                for(int k = 0; k < editors.length; k++) {
                                   authorData01.editors.add(editors[k]);
                                }
                            }
                            authorData01.articleTitles.add(jsonObj.getString("标题"));

                            String categoryID = jsonObj.getString("分类");
                            String categoryName = null;
                            if(categoryID != null)
                                categoryName  = jsonObjCategory.getString(categoryID.toUpperCase());

                            if(categoryName != null) authorData01.category.add(categoryName);

                            authorData01.type.add(jsonObj.getString("体裁"));
                            authorData01.pageName.add(jsonObj.getString("版名"));

                            String[] coauthors = jsonObj.getString("作者").split(";");
                            for(int k = 0; k < coauthors.length; k++) {
                                authorData01.coauthors.add(coauthors[k]);
                            }

                            authorMap.put(authorData01.authorName, authorData01);
                        }
                    }
                }


            }

        }

        BufferedWriter tempFile = new BufferedWriter(new FileWriter(new File("temp.txt")));

        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> edgeList = new ArrayList<String>();
        int ik=0;
    for(String akey : authorMap.keySet()){
        System.out.println(ik++);
        AuthorData aAuthorData = authorMap.get(akey);
        String tempAuthorName = aAuthorData.authorName.replaceAll(" ", "");
        if(!nameList.contains(tempAuthorName)){
            tempFile.write("CREATE ("+ tempAuthorName +":Person {name:'"+ tempAuthorName +"'})"+"\n");
            nameList.add(tempAuthorName);
        }
        ArrayList<String> editorsOfAuthor = aAuthorData.editors;
        for(int ii = 0; ii < editorsOfAuthor.size(); ii++){
            String tempEditorName = editorsOfAuthor.get(ii).replaceAll(" ", "");
            if(!nameList.contains(tempEditorName)) {
                tempFile.write("CREATE (" + tempEditorName + ":Person {name:'" + tempEditorName + "'})" + "\n");
                nameList.add(tempEditorName);
            }
            String tempCommand = "CREATE ("+tempEditorName+")-[:EditorOf]->("+tempAuthorName+")"+"\n";
            if(!edgeList.contains(tempCommand)){
            tempFile.write("CREATE ("+tempEditorName+")-[:EditorOf]->("+tempAuthorName+")"+"\n");
            edgeList.add(tempCommand);
            }
        }
    }
    tempFile.close();
    }
}

