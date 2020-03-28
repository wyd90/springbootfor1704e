package com.bawei.springbootfor1704e;

import com.bawei.springbootfor1704e.bean.Student;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class HBasetTest {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Test
    public void testCreateNsAndTab() throws IOException {
        Connection conn = ConnectionFactory.createConnection(hbaseTemplate.getConfiguration());
        Admin admin = conn.getAdmin();

        NamespaceDescriptor ns7 = NamespaceDescriptor.create("ns7").build();
        admin.createNamespace(ns7);

        TableName tableName = TableName.valueOf("ns7:t1");
        HTableDescriptor t1 = new HTableDescriptor(tableName);
        HColumnDescriptor f1 = new HColumnDescriptor("f1");
        t1.addFamily(f1);
        admin.createTable(t1);
    }

    @Test
    public void testPutData() {
        hbaseTemplate.put("ns7:t1","r00001","f1","name", Bytes.toBytes("zhangsan"));
        hbaseTemplate.put("ns7:t1","r00001","f1","age", Bytes.toBytes(18));
        hbaseTemplate.put("ns7:t1","r00001","f1","sex", Bytes.toBytes("man"));
        hbaseTemplate.put("ns7:t1","r00001","f1","score", Bytes.toBytes(78.5d));
    }

    @Test
    public void testCallBack() {
        hbaseTemplate.execute("ns7:t1", new TableCallback<Object>() {
            @Override
            public Object doInTable(HTableInterface table) throws Throwable {
                Put put = new Put(Bytes.toBytes("r00002"));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("name"),Bytes.toBytes("lisi"));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("age"),Bytes.toBytes(20));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("sex"),Bytes.toBytes("woman"));
                put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("score"),Bytes.toBytes(82.0d));
                table.put(put);
                return null;
            }
        });
    }

    @Test
    public void testScan() {
        Scan scan = new Scan();

        SingleColumnValueFilter filter1 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
                Bytes.toBytes("age"),
                CompareFilter.CompareOp.GREATER,
                Bytes.toBytes(18));

        SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("f1"),
                Bytes.toBytes("score"),
                CompareFilter.CompareOp.LESS,
                Bytes.toBytes(80d));

        FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ONE, filter1, filter2);
        scan.setFilter(fl);

        List<Student> students = hbaseTemplate.find("ns7:t1", scan, new RowMapper<Student>() {
            @Override
            public Student mapRow(Result result, int rowNum) throws Exception {
                byte[] row = result.getRow();
                Student student = new Student();
                student.setId(Bytes.toString(row));
                student.setName(Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))));
                student.setAge(Bytes.toInt(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("age"))));
                student.setSex(Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("sex"))));
                student.setScore(Bytes.toDouble(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("score"))));
                return student;
            }
        });

        for(Student student : students) {
            System.out.println(student);
        }
    }

    @Test
    public void testGet() {
        Student student = hbaseTemplate.get("ns7:t1", "r00002", new RowMapper<Student>() {
            @Override
            public Student mapRow(Result result, int rowNum) throws Exception {
                byte[] row = result.getRow();
                Student student = new Student();
                student.setId(Bytes.toString(row));
                student.setName(Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))));
                student.setAge(Bytes.toInt(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("age"))));
                student.setSex(Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("sex"))));
                student.setScore(Bytes.toDouble(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("score"))));
                return student;
            }
        });
        System.out.println(student);
    }

    @Test
    public void testDelete() {
        hbaseTemplate.delete("ns7:t1","r00001","f1");
        hbaseTemplate.delete("ns7:t1","r00002","f1","name");
    }
}
