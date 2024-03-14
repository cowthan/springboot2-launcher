package com.ddy.dyy.web.lang;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public class Lang {

    private static final int READ_BUFFER_LEN = 1024;
    private static final int INVALID_LEN = -1;

    public static void setLogLevel() {
        setLogLevel(ch.qos.logback.classic.Level.INFO);
    }

    public static void setLogLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.LoggerContext loggerContext =
                (ch.qos.logback.classic.LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        loggerList.forEach(logger -> {
            logger.setLevel(level);
        });
    }

    public static String loadResourceFile(String pathInClasspath) {
        Resource resource = new ClassPathResource(pathInClasspath);
        String ret = null;
        try {
            ret = Lang.fileGetContent(resource.getInputStream());
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getNow
     *
     * @param
     * @return result
     */
    public static String getNow() {
        return getNow(false);
    }

    public static String getNow(boolean withMillseconds) {
        String format = "yyyy-MM-dd HH:mm:ss";
        if (withMillseconds) {
            format += ".SSS";
        }
        return new SimpleDateFormat(format).format(new Date());
    }

    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * sleep，为了省略catch
     *
     * @param milliSeconds 毫秒
     */
    public static void sleep(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    public static boolean isEmpty(Map<?, ?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> c) {
        return !isEmpty(c);
    }

    public static <T> boolean isEmpty(T[] c) {
        return c == null || c.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] c) {
        return !isEmpty(c);
    }

    public static boolean isEmpty(CharSequence c) {
        return c == null || c.toString().isEmpty();
    }

    public static boolean isNotEmpty(String c) {
        return !isEmpty((CharSequence) c);
    }

    public static int count(Collection<?> c) {
        return c == null ? 0 : c.size();
    }

    public static int count(Map<?, ?> c) {
        return c == null ? 0 : c.size();
    }

    public static <T> int count(T[] c) {
        return c == null ? 0 : c.length;
    }

    public static long toLong(String strInt) {
        try {
            return Long.parseLong(strInt);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static int toInt(String strInt) {
        try {
            return Integer.parseInt(strInt);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <T> ArrayList<T> newArrayList(T... t) {
        ArrayList<T> list = new ArrayList();
        if (t != null && t.length != 0) {
            for (int i = 0; i < t.length; ++i) {
                list.add(t[i]);
            }

            return list;
        } else {
            return list;
        }
    }

    public static <T> T firstElement(Collection<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.stream().findFirst().get();
    }

    public static <T> T lastElement(Collection<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        T ret = null;
        for (T t : list) {
            ret = t;
        }
        return ret;
    }

    public static String[] split(String s, String delemeter) {
        return s == null ? null : s.split(delemeter);
    }

    public static List<String> splitToList(String s, String delemeter) {
        if (isEmpty((CharSequence) s)) {
            return new ArrayList();
        } else {
            String[] parts = split(s, delemeter);
            List<String> list = new ArrayList();

            for (int i = 0; i < count((Object[]) parts); ++i) {
                list.add(parts[i]);
            }

            return list;
        }
    }

    public static List<Long> splitToLongList(String s, String delemeter) {
        if (isEmpty((CharSequence) s)) {
            return new ArrayList();
        } else {
            String[] parts = split(s, delemeter);
            List<Long> list = new ArrayList();

            for (int i = 0; i < count((Object[]) parts); ++i) {
                list.add(toLong(parts[i]));
            }

            return list;
        }
    }

    public static void filePutContent(String file, String content) {
        filePutContent(file, content.getBytes(StandardCharsets.UTF_8));
    }

    public static void filePutContent(String file, byte[] content) {
        if (content != null) {
            createFileIfNotExists(file);
            FileOutputStream fs = null;

            try {
                fs = new FileOutputStream(file);
                fs.write(content);
            } catch (Exception var11) {
                throw new RuntimeException(var11);
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException var10) {
                    }
                }

            }

        }
    }

    /**
     * file_get_content
     *
     * @param file .
     * @return .
     */
    public static String fileGetContent(File file) {
        try {
            return fileGetContent(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fileGetContentFromResource(String path) {
        Resource resource = new ClassPathResource(path);
        String json = null;
        try {
            json = Lang.fileGetContent(resource.getInputStream());
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * file_get_content
     *
     * @param inputStream inputStream
     * @return result
     */
    public static String fileGetContent(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                //ignore
            }
            try {
                baos.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }


    /**
     * 文件写入：追加模式
     *
     * @param file    file
     * @param content content
     */
    public static void fileAppendContent(String file, String content) {
        if (content == null) {
            return;
        }
        createFileIfNotExists(file);
        PrintWriter pw = null;
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(file, true);
            pw = new PrintWriter(fs);
            pw.append(content);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * 创建文件及目录
     *
     * @param dirPath .
     * @return result
     */
    public static void createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    public static boolean createFileIfNotExists(String file) {
        File f = new File(file);
        File dir = f.getParentFile();
        if (dir != null) {
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    public static String snull(Object maybeNullOrEmpty, String replaceNull) {
        return (maybeNullOrEmpty == null || "".equals(maybeNullOrEmpty)) ? replaceNull : maybeNullOrEmpty.toString();
    }

    public static <K, V> Map<K, V> newHashMap(Object... args) {
        Map<K, V> m = new HashMap<K, V>();

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i += 2) {
                int ki = i;
                int vi = i + 1;
                if (ki < args.length && vi < args.length) {
                    K k = (K) args[ki];
                    V v = (V) args[vi];
                    m.put(k, v);
                }
            }
        }
        return m;
    }

    public static <K> Set<K> newHashSet(K... args) {
        Set<K> set = new HashSet<>();

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                set.add(args[i]);
            }
        }
        return set;
    }

    public static String fastUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static String urlencode(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    public static String urldecode(String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    public static String getFileNameWithoutSuffix(String path) {
        String fileName = new File(path).getName();
        if (fileName != null && fileName.contains(".")) {
            return fileName.split("\\.")[0];
        }
        return Lang.snull(fileName, "");
    }

    public static String getFileSuffix(String path) {
        String fileName = new File(path).getName();
        if (fileName != null && fileName.contains(".")) {
            return fileName.split("\\.")[1];
        }
        return "";
    }

    // -----------------------------------------------------
    // 日期转换系列
    // -----------------------------------------------------
    public static String toDate(String pattern, String seconds) {
        return toDate(pattern, toInt(seconds));
    }

    public static Date strtodate(String str) {
        return new Date(strtotime(str) * 1000L);
    }

    // str = 2020-10-10 10:10:10
    public static long strtotime(String str) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (str == null) return new Date().getTime() / 1000L;

        SimpleDateFormat sdf = null;
        if (str.contains("-")) {
            if (str.contains(" ")) sdf = sdf2;
            else sdf = sdf1;
        } else if (str.contains("/")) {
            if (str.contains(" ")) sdf = sdf4;
            else sdf = sdf3;
        }
        if (sdf != null) {
            try {
                return sdf.parse(str).getTime() / 1000L;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("无法匹配时间格式: " + str);
        }
    }

    public static String date(String format, long seconds) {
        return new SimpleDateFormat(format).format(new Date(seconds * 1000L));
    }

    public static String toDate(String pattern, long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(new Date(seconds * 1000));
    }

    public static String toDate(String pattern, Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(date);
    }

    public static long fromDate(String pattern, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试把str的时间格式转为format的格式，通过new Date(str)来转，只能尽量转，失败则返回原来字符串
     *
     * @param str    类似Tue May 31 17:46:55 +0800 2011的字符串
     * @param format
     */
    public static String tryToDate(String str, String format) {
        try {
            Date date = new Date(str);
            DateFormat df = new SimpleDateFormat(format);
            String s = df.format(date);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }


    public static String join(Collection<?> list, String delemeter) {
        return join(list, delemeter, "");
    }

    public static <T> String join(T[] list, String delemeter) {
        return join(list, delemeter, "");
    }

    public static <T> String join(T[] list, String delemeter, String wrapper) {
        if (Lang.isEmpty(list)) {
            return "";
        } else {
            String res = "";

            int i = -1;
            for (Object s : list) {
                i++;
                String item = wrapper + s + wrapper;
                res = res + Lang.snull(item)
                        + (i == list.length - 1 ? "" : delemeter);
            }

            return res;
        }
    }

    public static String join(Collection<?> list, String delemeter, String wrapper) {
        if (Lang.isEmpty(list)) {
            return "";
        } else {
            String res = "";

            int i = -1;
            for (Object s : list) {
                i++;
                String item = wrapper + s + wrapper;
                res = res + Lang.snull(item)
                        + (i == list.size() - 1 ? "" : delemeter);
            }

            return res;
        }
    }

    public static String snull(Object maybeNull) {
        return snull(maybeNull, "");
    }

    public static class NetworkEth {
        private String name;
        private String displayName;
        private String host;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    public static List<NetworkEth> getIp() {
        List<NetworkEth> ret = new ArrayList<>();
        try {
//            String host = Inet4Address.getLocalHost().getHostAddress();
//            System.out.println(host); // 只能得到eth序号最大的网卡

            /*
            网卡：eth1, 是否虚拟：false, 是否Loopback：false, 是否up：true, 网卡名字：Intel(R) Ethernet Connection (11) I219-LM
            10.139.106.41，false，false，false，false
            网卡：eth5, 是否虚拟：false, 是否Loopback：false, 是否up：true, 网卡名字：VirtualBox Host-Only Ethernet Adapter
            192.168.56.1，false，false，false，false
            网卡：eth11, 是否虚拟：false, 是否Loopback：false, 是否up：true, 网卡名字：Hyper-V Virtual Ethernet Adapter
            172.28.16.1，false，false，false，false
             */
            NetworkInterface.networkInterfaces().forEach(e -> {
                try {
                    if (e.isUp() && !e.isLoopback()) {
//                        System.out.println("网卡：" + e.getName()
//                                + ", 是否虚拟：" + e.isVirtual()
//                                + ", 是否Loopback：" + e.isLoopback()
//                                + ", 是否up：" + e.isUp()
//                                + ", 网卡名字：" + e.getDisplayName()
//                        );
                        e.inetAddresses().forEach(f -> {
                            if (f.getHostAddress().contains(".")) {
                                NetworkEth eth = new NetworkEth();
                                eth.setHost(f.getHostAddress());
                                eth.setName(e.getName());
                                eth.setDisplayName(e.getDisplayName());
                                ret.add(eth);
//                                System.out.println(f.getHostAddress() +
//                                        "，" + f.isAnyLocalAddress() +
//                                        "，" + f.isLinkLocalAddress() +
//                                        "，" + f.isLoopbackAddress() +
//                                        "，" + f.isMCGlobal());
                            }
                        });
                    }
                } catch (SocketException ex) {
                    throw new RuntimeException(ex);
                }

            });
            return ret;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
