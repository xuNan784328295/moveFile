import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:XN
 * @create:2020/8/27
 */
public class TessMail {
    /**
     * Config路径地址
     */
    static String CONFIGPATHNAME = "config.txt";

    static Map<String, String> CONFIGMAP;


    public static void main(String[] args) {
        try {

            File directory = new File("");
            //获得当前目录
            String rootPath = directory.getCanonicalPath();
            //读取配置文件
            readConfigFile(rootPath);
            // 文件移动
            new TessMail().MoveFiles(rootPath);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取配置文件
     *
     * @param rootPath
     * @throws Exception
     */
    private static void readConfigFile(String rootPath) throws Exception {
        System.out.println(":::::::::"+rootPath);
        CONFIGMAP = new HashMap<>(13);
        File file = new File(rootPath + "/" + CONFIGPATHNAME);
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=SPLIT=");
                CONFIGMAP.put(split[0], split[1]);
            }
        } finally {
            if(br!=null){
                br.close();
            }
            if(fr!=null){
                fr.close();
            }
        }
    }

    /**
     * 文件移动
     *
     * @throws Exception
     */
    public void MoveFiles(String rootPath) throws Exception {
        System.out.println(":::::::::"+rootPath);
        File file = new File(rootPath + "/" + CONFIGMAP.get("FILEPATHNAME"));
        FileReader fr = null;
        BufferedReader br = null;
        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                //找到有效的url
                if (line.trim().length() > 0) {
                    //前端信息
                    if (line.trim().indexOf("/main/webapp/" + CONFIGMAP.get("HTMLDIRNAME") + "/") != -1) {
                        String[] split = line.trim().split("/main/webapp/" + CONFIGMAP.get("HTMLDIRNAME") + "/");
                        String newPath = rootPath+"/"+CONFIGMAP.get("SCREENPATH") + "/" + CONFIGMAP.get("HTMLDIRNAME") + "/" + split[1];
                        String oldPath = rootPath+"/"+CONFIGMAP.get("WARPATH") + "/" + CONFIGMAP.get("HTMLDIRNAME") + "/" + split[1];
                        if (newPath.endsWith(".html") || newPath.endsWith(".js") ||
                                newPath.endsWith(".css") ||newPath.endsWith(".png") ||
                                newPath.endsWith(".jpg") ||newPath.endsWith(".gif") ||
                                newPath.endsWith(".tpl")) {
                            String newileDirPath = newPath.substring(0, newPath.lastIndexOf("/"));
                            //生成文件夹
                            mkdirs(new File(newileDirPath));
                            //文件移动
                            new File(oldPath.trim()).renameTo(new File(newPath));
                        }
                    }
                    //class文件
                    if (line.trim().indexOf("/main/java/" + CONFIGMAP.get("CLASSDIRNAME") + "/") != -1) {
                        String[] split = line.trim().split("/main/java/" + CONFIGMAP.get("CLASSDIRNAME") + "/");
                        String newPath = rootPath+"/"+CONFIGMAP.get("SCREENPATH") + "/" + CONFIGMAP.get("CLASSDIRNAME") + "/" + split[1];
                        String oldPath = rootPath+"/"+CONFIGMAP.get("WARPATH") + "/" + CONFIGMAP.get("CLASSDIRNAME") + "/" + split[1];
                        if (newPath.endsWith(".java")) {
                            String newileDirPath = newPath.substring(0, newPath.lastIndexOf("/"));
                            //生成文件夹
                            mkdirs(new File(newileDirPath));
                            String newPathClass = newPath.replace(".java", ".class");
                            String oldPathClass = oldPath.replace(".java", ".class");
                            for (int i = 0; i < Integer.parseInt(CONFIGMAP.get("MAXNUM")); i++) {
                                if (i == 0) {
                                    //查找 .class
                                    if (new File(oldPathClass.trim()).isFile()) {
                                        new File(oldPathClass.trim()).renameTo(new File(newPathClass.trim()));
                                    }
                                } else {
                                    //查找$1.class
                                    oldPathClass = oldPath.trim().replace(".java", "");
                                    oldPathClass = oldPathClass + "$" + i + ".class";
                                    if (new File(oldPathClass.trim()).isFile()) {
                                        newPathClass = newPath.trim().replace(".java", "");
                                        newPathClass = newPathClass + "$" + i + ".class";
                                        new File(oldPathClass.trim()).renameTo(new File(newPathClass.trim()));
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            if(br!=null){
                br.close();
            }
            if(fr!=null){
                fr.close();
            }
        }
    }

    /**
     * 创建文件夹顺便创建父目录
     *
     * @param file file类
     */
    public static void mkdirs(File file) throws IOException {
        if (file.exists() && file.isDirectory()) {
            return;
        }
        file.mkdirs();
    }


}
