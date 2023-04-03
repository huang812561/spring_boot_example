package com.hgq.util;

import com.github.shyiko.mysql.binlog.BinaryLogFileReader;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.deserialization.ChecksumType;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

import java.io.File;
import java.io.IOException;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-06-10 18:04
 * @since 1.0
 **/
public class MysqlBinlogReader {

    public static void main(String[] args) throws IOException {
        String filePath="D:\\DATA\\mysql-bin.000987";
        File binlogFile = new File(filePath);
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setChecksumType(ChecksumType.CRC32);
        BinaryLogFileReader reader = new BinaryLogFileReader(binlogFile, eventDeserializer);
        try {
            for (Event event; (event = reader.readEvent()) != null; ) {
                System.out.println(event.toString());
            }
        } finally {
            reader.close();
        }
    }
}
