package com.ctosb.study.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Dao {

    public static void main(String[] args) {
        MongoClient mongo = new MongoClient("172.88.88.206", 27017);
        MongoDatabase database = mongo.getDatabase("dbtest");
        MongoCollection<Document> collection = database.getCollection("cc");
        Dao dao = new Dao();
        dao.insert(collection);
        dao.update(collection);
//		dao.delete(collection);
//		dao.find(collection);
    }


    private static String name = "name";
    private static String age = "age";
    private static String high = "high";

    /**
     * 插入数据
     *
     * @param collection
     * @author Alan
     * @time 2015-10-27 下午03:22:47
     */
    public void insert(MongoCollection<Document> collection) {
        List<Document> list = new ArrayList<Document>();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Document document = new Document();
            document.put(name, "zhangsan" + i);
            document.put(age, new Random().nextInt(100));
            document.put(high, new Random().nextInt(200));
            list.add(document);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("------------构建数据完成,耗时" + (t2 - t1) + "ms-----------------");
        collection.insertMany(list);
        long t3 = System.currentTimeMillis();
        System.out.println("------------数据插入完成,耗时" + (t3 - t2) + "ms-----------------");
        System.out.println("------------插入了" + list.size() + "条记录-----------");
    }

    /**
     * 删除数据
     *
     * @param collection
     * @author Alan
     * @time 2015-10-27 下午03:22:31
     */
    public void delete(MongoCollection<Document> collection) {
        long t2 = System.currentTimeMillis();
        DeleteResult result = collection.deleteMany(Filters.eq("name", "zhangsan1"));
        long t3 = System.currentTimeMillis();
        System.out.println("------------删除数据完成，耗时" + (t3 - t2) + "ms-----------------");
        System.out.println("------------删除了" + result.getDeletedCount() + "条记录-----------");
    }

    /**
     * 更新数据
     *
     * @param collection
     * @author Alan
     * @time 2015-10-27 下午03:22:23
     */
    public void update(MongoCollection<Document> collection) {
        long t2 = System.currentTimeMillis();
        UpdateResult result = collection.updateMany(Filters.eq("name", "zhangsan1"), new Document("$set", new Document("age1", "12s")));
        long t3 = System.currentTimeMillis();
        System.out.println("------------更新数据耗时" + (t3 - t2) + "ms-----------------");
        System.out.println("------------更新了" + result.getModifiedCount() + "条记录-----------");
        System.out.println("------------总数据量为" + collection.count());
    }

    /**
     * 查找数据
     *
     * @param collection
     * @author Alan
     * @time 2015-10-27 下午03:22:13
     */
    public void find(MongoCollection<Document> collection) {
        FindIterable<Document> iterable = collection.find();
        for (Document document : iterable) {
            Set<String> keySet = document.keySet();
            for (String key : keySet) {
//				System.out.print(key+":"+document.get(key) + " ");
            }
            System.out.println();
        }
        System.out.println("---------数据搜索完成----------");
    }
}
