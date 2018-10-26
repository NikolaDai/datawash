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
            JSONObject jsonObj = JSONObject.parseObject(recordData);
            String valueAuthor = jsonObj.getString("作者");

            if (valueAuthor != null) {
                valueAuthor = valueAuthor.replaceAll(" \\s+", "").replaceAll("等","");

                String[] authors = valueAuthor.split(";");

                if (authors.length == 1) {
                    System.out.println(valueAuthor);
                    System.out.println(recordData);
                    String matchString = valueAuthor.charAt(0) + "\\s{1,4}" + valueAuthor.charAt(1);
                    System.out.println(matchString);
                }

            }
        }
    }
}
