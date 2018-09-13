import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Neo4jCommandGenerator {
    public void commandGenerator(Map<String, AuthorData> authorMap) throws IOException {
        BufferedWriter tempFile = new BufferedWriter(new FileWriter(new File("temp.txt")));

        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> edgeList = new ArrayList<String>();
        int ik = 0;
        for (String akey : authorMap.keySet()) {
            System.out.println(ik++);
            AuthorData aAuthorData = authorMap.get(akey);
            String tempAuthorName = aAuthorData.authorName.replaceAll(" ", "");
            if (!nameList.contains(tempAuthorName)) {
                tempFile.write("CREATE (" + tempAuthorName + ":Person {name:'" + tempAuthorName + "'})" + "\n");
                nameList.add(tempAuthorName);
            }
            ArrayList<String> editorsOfAuthor = aAuthorData.editors;
            for (int ii = 0; ii < editorsOfAuthor.size(); ii++) {
                String tempEditorName = editorsOfAuthor.get(ii).replaceAll(" ", "");
                if (!nameList.contains(tempEditorName)) {
                    tempFile.write("CREATE (" + tempEditorName + ":Person {name:'" + tempEditorName + "'})" + "\n");
                    nameList.add(tempEditorName);
                }
                String tempCommand = "CREATE (" + tempEditorName + ")-[:EditorOf]->(" + tempAuthorName + ")" + "\n";
                if (!edgeList.contains(tempCommand)) {
                    tempFile.write("CREATE (" + tempEditorName + ")-[:EditorOf]->(" + tempAuthorName + ")" + "\n");
                    edgeList.add(tempCommand);
                }
            }
        }
        tempFile.close();
    }
}
