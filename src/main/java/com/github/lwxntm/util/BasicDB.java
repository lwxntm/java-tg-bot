package com.github.lwxntm.util;

import java.io.*;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @author lei
 */
public class BasicDB {
    private static final int MAX_DATA_LENGTH = 1020;
    //补白字节
    private static final byte[] ZERO_BYTES = new byte[MAX_DATA_LENGTH];
    //数据文件扩展名
    private static final String DATA_SUFFIX = ".data";
    //元数据文件扩展名，包括索引和空白空间数据
    private static final String META_SUFFIX = ".meta";

    //索引信息，键->值在.data文件中的位置
    Map<String, Long> indexMap;
    //空白空间，值为在.data文件中的位置
    Queue<Long> gaps;

    //值数据文件
    RandomAccessFile db;
    //元数据文件
    File metaFile;


    //  元数据文件存在时，会调用loadMeta将元数据加载到内存
    public BasicDB(String path, String name) throws IOException {
        File dataFile = new File(path + name + DATA_SUFFIX);
        metaFile = new File(path + name + META_SUFFIX);
        db = new RandomAccessFile(dataFile, "rw");
        if (metaFile.exists()) {
            loadMeta();
        } else {
            indexMap = new HashMap<>();
            gaps = new ArrayDeque<>();
        }
    }

    /**
     * 先通过索引查找键是否存在，如果不存在，调用nextAvailablePos方法为值找一个存储位置，
     * 并将键和存储位置保存到索引中，最后，调用writeData方法将值写到数据文件中
     */
    public void put(String key, byte[] value) throws IOException {
        Long index = indexMap.get(key);
        if (index == null) {
            index = nextAvailablePos();
            indexMap.put(key, index);
        }
        writeData(index, value);
    }

    //它先检查长度，长度满足的情况下，定位到指定位置，写实际数据的长度、写内容、最后补白
    private void writeData(Long pos, byte[] data) throws IOException {
        if (data.length > MAX_DATA_LENGTH) {
            throw new IllegalArgumentException("maximum allowed length is " + MAX_DATA_LENGTH + ", data length is " + data.length);
        }
        db.seek(pos);
        db.writeInt(data.length);
        db.write(data);
        db.write(ZERO_BYTES, 0, MAX_DATA_LENGTH - data.length);
    }

    //首先查找空白空间，如果有，则重用，否则定位到文件末尾
    private long nextAvailablePos() throws IOException {
        if (!gaps.isEmpty()) {
            return gaps.poll();
        } else {
            return db.length();
        }
    }

    public byte[] get(String key) throws IOException {
        Long index = indexMap.get(key);
        if (index != null) {
            return getData(index);
        }
        return null;
    }

    //定位到指定位置，读取实际长度，然后调用readFully方法读够内容
    private byte[] getData(long index) throws IOException {
        db.seek(index);
        int len = db.readInt();
        byte[] data = new byte[len];
        db.readFully(data);
        return data;
    }

    //从索引结构中删除，并添加到空白空间队列中
    public void remove(String key) {
        Long remove = indexMap.remove(key);
        if (remove != null) {
            gaps.offer(remove);
        }
    }

    //getFD方法会返回文件描述符，其sync方法会确保文件内容保存到设备上
    public void flush() throws IOException {
        saveMeta();
        db.getFD().sync();
    }

    private void saveMeta() throws IOException {
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(metaFile)));
        try {
            saveIndex(out);
            saveGaps(out);
        } finally {
            out.close();
        }
    }

    //先保存键值对个数，然后针对每条索引信息，保存键及值在.data文件中的位置
    private void saveIndex(DataOutputStream out) throws IOException {
        out.writeInt(indexMap.size());
        for (Map.Entry<String, Long> stringLongEntry : indexMap.entrySet()) {
            out.writeUTF(stringLongEntry.getKey());
            out.writeLong(stringLongEntry.getValue());
        }
    }

    private void saveGaps(DataOutputStream out) throws IOException {
        out.writeInt(gaps.size());
        for (Long gap : gaps) {
            out.writeLong(gap);
        }
    }

    private void loadMeta() throws IOException {
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(metaFile)))) {
            loadIndex(in);
            loadGaps(in);
        }
    }

    private void loadIndex(DataInputStream in) throws IOException {
        int size = in.readInt();
        indexMap = new HashMap<String, Long>((int) (size / 0.75f) + 1, 0.75f);
        for (int i = 0; i < size; i++) {
            String key = in.readUTF();
            long index = in.readLong();
            indexMap.put(key, index);
        }
    }

    private void loadGaps(DataInputStream in) throws IOException {
        int size = in.readInt();
        gaps = new ArrayDeque<>(size);
        for (int i = 0; i < size; i++) {
            long index = in.readLong();
            gaps.add(index);
        }
    }

    public void close() throws IOException {
        flush();
        db.close();
    }


}
