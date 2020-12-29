package com.edwardlee.library.util;


import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.thrift.StreamRowTRDFPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 本体操作工具
 * @author EdwardLee
 * @version 0.0.1
 */
@Component
public class OntOperation {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String PREFIX = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                          "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                          "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                          "PREFIX lib: <http://www.semanticweb.org/edwardlee/library#>\n";


    public final String QUERY_BOOK_BY_AUTHOR = "select ?object where {lib:%s lib:write ?object}";
    public final String QUERY_AUTHOR_BY_BOOK = "select ?object where {lib:%s lib:writtenBy ?object}";

    public final String REG_AUTHOR_BY_BOOK = "(\\D+)的作者(是谁)?";

    /**
     * 配置文件读取器
     */
    private final PropertyReader propertyReader = new PropertyReader();;
    /**
     * 本体文件路径
     */
    private final String OWL_FILE_PATH = propertyReader.readProperty("owl-file");
    /**
     * 查询语句
     */
    private String sparql;
    /**
     * 本体模型
     */
    private OntModel model;
    /**
     * 结果集
     */
    private ResultSet resultSet;

    /**
     * 自动构造器
     * <p>
     *     根据配置文件确定 本体文件路径、本体模型
     * </p>
     */
    public OntOperation() {
        this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF, null);
        this.model.read(OWL_FILE_PATH, null);
        this.sparql = "";
    }

    public PropertyReader getPropertyReader() {
        return propertyReader;
    }

    /**
     * 执行查询语句。并将查询到的 ResultSet 保存到 resultSet 属性
     * @return
     */
    public ResultSet execQuery() {
        Query query = QueryFactory.create(this.sparql);
        QueryExecution execution = QueryExecutionFactory.create(query, this.model);
        this.resultSet = execution.execSelect();
        return this.resultSet;
    }

    public String getSparql() {
        return sparql;
    }

    public void setSparql(String sparql) {
        this.sparql = sparql;
    }



    /**
     * ResultSet 转 Iterator
     * @param rs 待转换的结果集
     * @return iterator - 转换后的 Iterator
     */
    public Iterator resultSet2Iterator(ResultSet rs){

        while (rs.hasNext()) {
            QuerySolution item = rs.next();
            RDFNode subject = item.get("subject");
            Resource predict = item.getResource("predict");
            RDFNode object = item.get("object");

            if (subject == null) {
                System.out.println("没有 subject");

            }
            if (predict == null) {
                System.out.println("没有 predict");
            }
            if (object == null) {
                System.out.println("没有 object");
            }

        }





        return null;
    }

    /**
     * 执行查询并转换成 JSON 格式
     * @throws UnsupportedEncodingException 转换时抛出的 编码不支持异常
     */
    // TODO
    public void execQueryJSON() throws UnsupportedEncodingException, JSONException {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(baout, this.resultSet);
        String json = new String(baout.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(json);
        //return json;
        JSONObject jsonObject = new JSONObject(json);
//        List<String> head = (List<String>) jsonObject.get("head");
//        for (String i : head) {
//            System.out.println(i);
//        }
        JSONArray bindings = jsonObject.getJSONObject("results").getJSONArray("bindings");
        String value = bindings.getJSONObject(0).getJSONObject("object").getString("value");
        System.out.println(value);

//        for (int i = 0; i < bindings.length(); i++) {
//            String predict = bindings.getJSONObject(i).getJSONObject("predict").getString("value");
//            String object = bindings.getJSONObject(i).getJSONObject("object").getString("value");
//            System.out.println(predict + "----" + object);
//        }

    }

    /**
     * 将 JSON 字符串解析成 Json对象
     * @param json 待解析json字符串
     * @return 解析出的 JSONObject
     */
    // TODO
    public JSONObject parseJSON(String json) {
        return null;
    }

    /**
     * 获取 Object 列表
     * @return Object 列表
     */
    public ArrayList<String> getObjects() {

        ArrayList<String> objects = new ArrayList<>();
        String s = null;
        while (this.resultSet.hasNext()) {

            QuerySolution item = this.resultSet.next();

            RDFNode object = item.get("object");
            if (object.isLiteral()) {
                s = object.toString();
            }
            else if (object.isResource()) {
                s = object.asResource().getLocalName();
            }
            objects.add(s);

        }
        return objects;
    }

    /**
     * 获取单个 object。当确定结果集中只有一个结果时使用此方法
     * @return Object 字符串
     */
    public String getObject() {
        String s = null;
        QuerySolution next = this.resultSet.next();
        RDFNode object = next.get("object");
        if (object.isLiteral()) {
            s = object.toString();
        }
        else if (object.isResource()) {
            s = object.asResource().getLocalName();
        }
        return s;
    }

    /**
     * 获取 Subject 列表
     * @return Subject 列表
     */
    public ArrayList<String> getSubjects() {

        ArrayList<String> subjects = new ArrayList<>();
        String s = null;
        while (this.resultSet.hasNext()) {

            QuerySolution item = this.resultSet.next();

            RDFNode subject = item.get("subject");
            if (subject.isLiteral()) {
                s = subject.toString();
            }
            else if (subject.isResource()) {
                s = subject.asResource().getLocalName();
            }
            subjects.add(s);

        }
        return subjects;
    }

}
