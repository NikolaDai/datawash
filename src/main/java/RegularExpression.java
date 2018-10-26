import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class RegularExpression {
    public static void main(String[] args)  throws IOException {
        File file = new File("test.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        File nameFile = new File("newTest.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nameFile));

        String recordData = null;
        while(( recordData = bufferedReader.readLine()) != null){
            String matchString = null;
            String newRecordData = null;
            JSONObject jsonObj = JSONObject.parseObject(recordData);
            String valueAuthor = jsonObj.getString("作者");

            if (valueAuthor != null) {
                valueAuthor = valueAuthor.replaceAll(" \\s+", "").replaceAll("等","");

                String[] authors = valueAuthor.split(";");

                if (authors.length == 1) {
                    if(valueAuthor.length() == 2) {
                        matchString = valueAuthor.charAt(0) + "\\s{1,4}" + valueAuthor.charAt(1);
                        newRecordData = recordData.replaceAll(matchString, valueAuthor);
                        if (!newRecordData.equals(recordData)) {
                            System.out.println(recordData);
                            System.out.println(newRecordData);
                        }
                    }
                }
                else{
                    for (int i = 0; i < authors.length; i++) {
                        if(authors[i].length() == 2){
                            matchString = authors[i].charAt(0) + "\\s{1,4}" + authors[i].charAt(1);
                            if(newRecordData != null)
                                newRecordData = newRecordData.replaceAll(matchString, authors[i]);
                            else
                                newRecordData = recordData.replaceAll(matchString, authors[i]);

                            if (!newRecordData.equals(recordData)) {
                                System.out.println(recordData);
                                System.out.println(newRecordData);
                            }
                        }
                    }
                }

            }

            if(newRecordData != null){
                newRecordData = newRecordData.replaceAll("\"体裁\"", "\"paperType\"")
                        .replaceAll("\"作者\"", "\"authorsName\"")
                        .replaceAll("\"分类\"", "\"paperCategory\"")
                        .replaceAll("\"副题\"", "\"subTitle\"")
                        .replaceAll("\"引题\"", "\"eyebrowTitle\"")
                        .replaceAll("\"日期\"", "\"publishDate\"")
                        .replaceAll("\"标题\"", "\"mainTitle\"")
                        .replaceAll("\"栏目\"", "\"columnName\"")
                        .replaceAll("\"正文\"", "\"articleText\"")
                        .replaceAll("\"版名\"", "\"pageName\"")
                        .replaceAll("\"责编\"", "\"editorsName\"");
                bufferedWriter.write(newRecordData + "\n");}
            else {
                recordData = recordData.replaceAll("\"体裁\"", "\"paperType\"")
                        .replaceAll("\"作者\"", "\"authorsName\"")
                        .replaceAll("\"分类\"", "\"paperCategory\"")
                        .replaceAll("\"副题\"", "\"subTitle\"")
                        .replaceAll("\"引题\"", "\"eyebrowTitle\"")
                        .replaceAll("\"日期\"", "\"publishDate\"")
                        .replaceAll("\"标题\"", "\"mainTitle\"")
                        .replaceAll("\"栏目\"", "\"columnName\"")
                        .replaceAll("\"正文\"", "\"articleText\"")
                        .replaceAll("\"版名\"", "\"pageName\"")
                        .replaceAll("\"责编\"", "\"editorsName\"");
                bufferedWriter.write(recordData + "\n");
            }
        }

        bufferedReader.close();
        bufferedWriter.close();
    }
}
