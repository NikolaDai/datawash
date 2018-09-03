# From the json file, we make some basic statistics.
we work form a json file, mainly use fastjson from alibaba and also take one json file as a dictionary. Our final objective is to generate the Neo4J commands.

## some faults
CREATE (商婧、何梦舒:Person {name:'商婧、何梦舒'})
CREATE (陈占杰、刘咏秋:Person {name:'陈占杰、刘咏秋'})
CREATE (林茂发、欧阳大名:Person {name:'林茂发、欧阳大名'})

CREATE (杨祖荣)-[:EditorOf]->(商婧、何梦舒)
CREATE (庞清杰)-[:EditorOf]->(商婧、何梦舒)
CREATE (陈  明:Person {name:'陈  明'})
(席严峰任旭:Person {name:'席严峰任旭'})
CREATE (席严峰任旭)-[:EditorOf]->(陈千学)
CREATE (严珊版条###0504:Person {name:'严珊版条###0504'})
CREATE (严珊版条###0504)-[:EditorOf]->(张以忠)
CREATE (巴哈尔古丽哈力克:Person {name:'巴哈尔古丽哈力克'})
CREATE (邱明全)-[:EditorOf]->(巴哈尔古丽哈力克)
"CREATE (版面编辑／林飞)-[:EditorOf]->(商志勇)"

# most popular authors
丁雅涵

# common Neo4j commands
MATCH (丁雅涵:Person {name:"丁雅涵"})-[:EditorOf]->(person)<-[:EditorOf]-(Person)
RETURN Person.name;

MATCH (丁雅涵:Person {name:"丁雅涵"})<--(n)
return 丁雅涵,n

match(n)
return(n)
