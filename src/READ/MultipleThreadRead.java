package READ;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class MultipleThreadRead {
    public static void main(String[] args)  {
        String srcpath = "C:\\coding\\a.txt";
        String destpath = "C:\\coding\\b35.txt";
        Scanner cin = new Scanner(System.in);// input the number of thread
        int num;// spilt the file by the number of thread
        num=cin.nextInt();

        MultipleThreadCopy(srcpath,destpath,num);

    }


    public static void MultipleThreadCopy(String srcPath, String destPath, Integer ThreadNumber){
        if(ThreadNumber < 1){
            return;
        }

        File OriginalFile = new File(srcPath);
        long length = OriginalFile.length();
        long len = length/ThreadNumber;

        int sum = 0;
        for (int i = 0; i < ThreadNumber-1; i++) {
            SubThread subThread = new SubThread(srcPath, destPath, i * len + sum, (i + 1) * len + sum, i, len);
            subThread.start();
            String add = "\n--"  + " Thread "+ i+ " read:\n";
            sum += add.getBytes().length;
        }
        if (ThreadNumber > 1) {
            SubThread subThread = new SubThread(srcPath, destPath, (ThreadNumber-1) * len + sum, OriginalFile.length() + sum,ThreadNumber-1, len);
            subThread.start();
        }
    }
}
class SubThread extends Thread{
    private String srcPath;
    private String destPath;
    private long startIndex;
    private long endIndex;
    private int id;
    private int len;
    public SubThread(String srcPath,String destPath,long startIndex,long endIndex, int id, long len){
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.id = id;
        this.len = (int)len;
    }


    @Override
    public void run() {
        try {
            RandomAccessFile srcFile = new RandomAccessFile(srcPath,"r");
            RandomAccessFile destFile = new RandomAccessFile(destPath, "rw");
            srcFile.seek(id * len);
            destFile.seek(startIndex);

            long index = startIndex;

            byte[] bytes = new byte[1010];

            int  n;
            String add = "\n--"  + " Thread "+ id+ " read:\n";
            int length = add.getBytes().length;
            destFile.write(add.getBytes(),0, length);
            index += length;
            while ((n = srcFile.read(bytes)) != -1){
                index+=len;

                destFile.write(bytes,0,len);

                if(index >= endIndex+length){
                    break;
                }
            }
            srcFile.close();
            destFile.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}