package utills;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AccessoryUtil {

//    public static List<File> getAccessoryFiles(List<String> cardIds, byte category, String folder) {
//        List<File> files = new ArrayList<>();
//        for(String stuId:cardIds){
//            if((category & Student.MATERIAL_PHOTO)!=0){
//                String fileName = String.format("%s/p_%s.jpg",folder,stuId);
//                File file = new File(fileName);
//                if(file.exists()){
//                    files.add(file);
//                }
//            }
//            if((category & Student.MATERIAL_MEDICAL)!=0){
//                String fileName = String.format("%s/m_%s.jpg",folder,stuId);
//                File file = new File(fileName);
//                if(file.exists()){
//                    files.add(file);
//                }
//            }
//            if((category & Student.MATERIAL_DEGREE)!=0){
//                String fileName = String.format("%s/d_%s.jpg",folder,stuId);
//                File file = new File(fileName);
//                if(file.exists()){
//                    files.add(file);
//                }
//            }
//            if((category & Student.MATERIAL_CARD1)!=0){
//                String fileName = String.format("%s/i1_%s.jpg",folder,stuId);
//                File file = new File(fileName);
//                if(file.exists()){
//                    files.add(file);
//                }
//            }
//            if((category & Student.MATERIAL_CARD2)!=0){
//                String fileName = String.format("%s/i2_%s.jpg",folder,stuId);
//                File file = new File(fileName);
//                if(file.exists()){
//                    files.add(file);
//                }
//            }
//        }
//        return files;
//    }

    /**
     * 文件打包下载
     * @param response
     * @param files 待打包的文件
     * @throws IOException
     */
    public static void zipDownload(HttpServletResponse response, List<File> files) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=contract.zip");
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        byte bt[] = new byte[4*1024];
        for(File file:files){
            FileInputStream is = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(URLEncoder.encode(file.getName() , "UTF-8"));
            zos.putNextEntry(entry);

            int len = -1;
            while((len = is.read(bt)) != -1){
                if(bt!=null){
                    zos.write(bt, 0, len);
                }
            }
            zos.closeEntry();
            is.close();
        }
        zos.flush();
        zos.close();
    }

    /**
     * 判断学员附件是否完整
     * @param path 附件存储路径
     * @param cardId 身份证号码
     * @return 是否完整
     */
    public static boolean isStudentAccessoryCompleted(String path, String cardId){
        boolean completed = true;//学员附件是否完整
        String[] category = {"p", "i1", "i2", "m", "d"};
        String fileName = "";
        for (String c : category) {
            fileName = String.format("%s/%s_%s.jpg", path, c, cardId);
            File file = new File(fileName);
            if (!file.exists()) {
                completed = false;
                break;
            }
        }
        return completed;
    }

    /**
     * 判断证件附件是否完整
     * @param path 附件存储路径
     * @param cardId 身份证号码
     * @param item 准操项目
     * @return 是否完整
     */
    public static boolean isCertAccessoryCompleted(String path, String cardId, String item){
        boolean completed = true;//学员附件是否完整
        String[] category = {"c1", "c2"};
        String fileName = "";
        for (String c : category) {
            fileName = String.format("%s/%s_%s_%s.jpg", path, c, item, cardId);
            File file = new File(fileName);
            if (!file.exists()) {
                completed = false;
                break;
            }
        }
        return completed;
    }

}
