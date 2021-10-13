
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        secret();
        decrypt();
    }
    //一次异或加密函数，铭文是图片：wrong.png，密钥是00000001
    public static void secret() throws IOException {
        File file = new File("wrong.png");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("wrong1.png"));
        int data = 0;
        while ((data=bufferedInputStream.read()) != -1){
            bufferedOutputStream.write(data^1);
        }
        bufferedInputStream.close();
        bufferedOutputStream.close();
    }
    //解密函数
    public static void decrypt() throws IOException {
        File file = new File("wrong1.png");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("wrong2.png"));
        int data = 0;
        while ((data=bufferedInputStream.read()) != -1){
            bufferedOutputStream.write(data^1);
        }
        bufferedInputStream.close();
        bufferedOutputStream.close();
    }
}
