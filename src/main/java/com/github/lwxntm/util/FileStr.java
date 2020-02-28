package com.github.lwxntm.util;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author laoma
 */
public class FileStr {

    /**
     * 复制Reader到Writer
     *
     * @param input  Reader
     * @param output Writer
     * @throws IOException IOException
     */
    public static void copy(final Reader input,
                            final Writer output) throws IOException {
        char[] buf = new char[4096];
        int charsRead = 0;
        while ((charsRead = input.read(buf)) != -1) {
            output.write(buf, 0, charsRead);
        }
    }

    /**
     * 读取文件，返回读取到的字符串
     *
     * @param fileName 要读取的文件名
     * @param encoding 要读取的文件名的编码方式 StandardCharsets.UTF_8
     * @return 返回读取到的字符串
     * @throws IOException IOException
     */
    public static String readFileToString(final String fileName, final Charset encoding) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));
            StringWriter writer = new StringWriter();
            copy(reader, writer);
            return writer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 把字符串写入文件
     *
     * @param fileName 文件名
     * @param data     字符串
     * @param encoding 编码方式: StandardCharsets.UTF_8
     * @throws IOException IOException
     */
    public static void writeStringToFile(final String fileName, final String data, final String encoding) throws IOException {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(
                    new FileOutputStream(fileName), encoding);
            writer.write(data);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 把多行数据写入文件
     *
     * @param fileName
     * @param encoding
     * @param lines
     * @throws IOException
     */
    public static void writeLines(final String fileName, final String encoding, final Collection<?> lines) throws IOException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, encoding);
            for (Object line : lines) {
                writer.println(line);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 从文件中读取多行数据
     *
     * @param fileName
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> readLines(final String fileName, final String encoding) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));
            List<String> list = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
            return list;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
