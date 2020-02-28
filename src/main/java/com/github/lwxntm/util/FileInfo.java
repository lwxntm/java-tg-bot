package com.github.lwxntm.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author lei
 */
public class FileInfo {
    public static long sizeOfDirectory(File dir) {
        long size = 0;
        if (dir.isFile()) {
            return dir.length();
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size += sizeOfDirectory(file);
            }
        }
        return size;
    }

    public static String sizeOfDirFriendly(File dir) {
        String r = "";
        long l = sizeOfDirectory(dir);
        if (l > 2 << 27) {
            r = d2S(l / 1024 / 1024 / 1024.0) + " GB";
        } else if (l > 2 << 18) {
            r = d2S(l / 1024 / 1024.0) + " MB";
        } else if (l > 2 << 9) {
            r = d2S(l / 1024.0) + " KB";
        } else {
            r = l + " Byte";
        }
        return r;
    }

    public static String d2S(double f) {
        return new DecimalFormat("#.000").format(f);
    }

    public static Collection<File> findFile(final File directory, final String fileName) {
        List<File> files = new ArrayList<>();
        for (File f : Objects.requireNonNull(directory.listFiles())) {
            if (f.isFile() && f.getName().equals(fileName)) {
                files.add(f);
            } else if (f.isDirectory()) {
                files.addAll(findFile(f, fileName));
            }
        }
        return files;
    }

    /**
     * 高危
     */
    public static void rmrf(File dir) throws IOException {
        if (dir.isFile()) {
            if (!dir.delete()) {
                //典型路径？。。
                throw new IOException("Fail to delete file:" + dir.getCanonicalPath());
            } else {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (!file.delete()) {
                        throw new IOException("Fail to delete file:" + file.getCanonicalPath());
                    }
                }
            }
        }
    }
}
