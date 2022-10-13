package org.example;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

//  Socket連線Server-Client
//  => http://hsingjungchen.blogspot.com/2017/07/javaserver-clientsokcet.html
//  OR https://www.it145.com/9/72695.html
public class Client {

    public static void main(String[] args) throws Exception {
        boolean close = false;
        try {

            while (!close) {

                //  創建 Socket 類對象並初始化 Socket
                Socket socket = new Socket("localhost", 5000);
                System.out.println("已連線Server");

//        String temp = JOptionPane.showInputDialog("Write Your Message");    //  JOptionPane彈跳視窗:https://peimei0808.pixnet.net/blog/post/327725823-%5Bjava%5D-joptionpane%E5%BD%88%E8%B7%B3%E8%A6%96%E7%AA%97%E4%B9%8B%E5%B8%B8%E7%94%A8%E9%A1%9E%E5%9E%8B%E4%BB%8B%E7%B4%B9%E2%80%93showin

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));   //  利用sk來取得輸出串流

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));  //   透過輸入串流來取得Client的輸入
                System.out.println("------------------------------Client------------------------------");
                System.out.println("requestType請輸入執行代碼 1~7" + "\n"
                        + "1: 查詢Cashi全部資料" + "\n"
                        + "2: 用Id查詢Mgni" + "\n"
                        + "3: 用Id查詢Cashi" + "\n"
                        + "4: Mgni動態查詢" + "\n"
                        + "5: Mgni新增" + "\n"
                        + "6: Mgni更新" + "\n"
                        + "7: Mgni刪除" + "\n"
                        + "Client: ");

                String requestStr = bufferedReader.readLine();
                if ("exit".equalsIgnoreCase(requestStr)) {
                    close = true;
                    break;
                }

                bufferedWriter.write(requestStr + "\n"); //  寫入輸出串流(使用 readline函數以'\n'為結尾當作一條消息)
                bufferedWriter.flush(); //  立即傳送

                //  查詢結果印出
                BufferedReader response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = "";
                System.out.println("查詢結果：");
                while ((str = response.readLine()) != null) {
                    System.out.println(str);
                }
                socket.close();
            }

        } catch (SocketException e){
            throw new SocketException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();    //  TODO Auto-generated catch block
        }
    }
}
