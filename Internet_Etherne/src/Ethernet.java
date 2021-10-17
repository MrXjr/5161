import java.util.Random;

/**
 * @Author: Mrxjr
 * @Date: 2021/6/23 11:03
 * @Description:
 */
public class Ethernet extends Thread{
    private static int BUS = 0; //总线，0表示空闲
    private int ID;//主机号
    private int successCount = 0;//发送成功次数
    private int collisionCount = 0;//冲突次数
    private int collisionWindow = 5;//冲突窗口值为5毫秒
    @Override
    public void run() {
        Random random = new Random();//初始化随机数
        try {
            sleep(random.nextInt(100));//随机休眠一段时间，防止一开始就立刻发生冲突
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (successCount < 10 && collisionCount < 16) { //若发送成功达10次或发生冲突超过16次，则进程结束
            if (BUS == 0) {//监听总线是否空闲
                try {
                    sleep(collisionWindow);//休眠半个争用期时间，即单程端到端的时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BUS = BUS | ID;//代表这时，另外的进程已经可以检测到了总线的繁忙状态
                try {
                    sleep(collisionWindow);//再休眠半个争用期后才开始检测是否冲突
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (BUS == ID) {//无冲突
                    BUS = 0; //发送成功，释放总线
                    successCount++; //成功次数+1
                    collisionCount = 0;//初始化碰撞次数
                    System.out.println("主机" + ID + "发送成功,成功次数：" + successCount);
                } else { //有冲突
                    BUS = 0;//发生冲突，休眠一段时间，释放总线
                    System.out.println("主机" + ID + "检测到冲突，发送失败，等候重传");
                    collisionCount++;//碰撞次数+1
                    //截断二进制指数退避算法
                    int min = collisionCount > 10 ? 10 : collisionCount;
                    int R = random.nextInt(1000);
                    try {
                        sleep(R%(int)Math.pow(2,min)*collisionWindow);//休眠一段时间再重新发送数据帧
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(collisionCount >= 16) System.out.println("主机" + ID + "发送失败，线程结束");//碰撞次数超过16次，发送失败，进程结束
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public static void main(String[] args) {
            Ethernet t1 = new Ethernet();
            Ethernet t2 = new Ethernet();
            Ethernet t3 = new Ethernet();
            Ethernet t4 = new Ethernet();
            Ethernet t5 = new Ethernet();
            t1.setID(001);
            t2.setID(002);
            t3.setID(003);
            t4.setID(004);
            t5.setID(005);

            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
    }
}

