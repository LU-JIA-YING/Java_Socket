package org.example;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.example.request.Request;
import org.example.service.CRUD;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    // Constructor
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    //  https://www.dotblogs.com.tw/Leon-Yang/2021/01/06/155519
    //  File -> Project Structure -> Modules 加入log4j-1.2.15.jar
    private static final Logger logger = Logger.getLogger(ClientHandler.class);

    public void run() {

        PrintWriter out = null;
        BufferedReader in = null;

        try {
            //當Socket持續在連接時，就做下面的事
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

            String requestStr = bufferedReader.readLine();
            System.out.println(requestStr);

            //  JSONObject vs JSONArray https://blog.csdn.net/sinat_31057219/article/details/71518123
            JSONObject jsonObject = new JSONObject(requestStr);
            String requestType = jsonObject.getString("requestType");
            JSONObject request = jsonObject.getJSONObject("request");

            Request request2 = new Gson().fromJson(requestStr, Request.class);

            switch (requestType) {
                case "1": {
                    logger.info("執行Cashi查詢");
                    bufferedWriter.write(CRUD.findAllCashi());
                    //  {"requestType":"1","request":{}}
                    break;
                }
                case "2": {
                    logger.info("執行Mgni查詢");
                    bufferedWriter.write(CRUD.findMgniById(request.getString("id")));
                    //  {"requestType":"2","request":{"id":"MGI20220929171333135"}}
                    break;
                }
                case "3": {
                    logger.info("執行Cashi查詢");
                    bufferedWriter.write(CRUD.findCashiById(request.getString("id")));
                    //  {"requestType":"3","request":{"id":"MGI20220929171333135"}}
                    break;
                }
                case "4": {
                    logger.info("Mgni動態查詢");
                    bufferedWriter.write(CRUD.dynamicQueryMgni(request));
                    //  {"requestType":"4","request":{"cmNo":"1111111","ccy":""}}
                    break;
                }
                case "5": {
                    logger.info("Mgni新增");
                    bufferedWriter.write(CRUD.createMgni(request2.getRequest()));
//                        bw.write(CRUD.createMgni2(request));
                    //  {"requestType":"5","request":{"cmNo":"9","kacType":"1","bankNo":"999","ccy":"TWD","pvType":"1","bicaccNo":"0000000","iType":"1","pReason":"money","clearingAccountList":[{"accNo":"1","amt":10},{"accNo":"2","amt":20}],"ctName":"Joey","ctTel":"12345678"}}
                    break;
                }
                case "6": {
                    logger.info("Mgni更新");
                    bufferedWriter.write(CRUD.updateMgni(request2.getRequest()));
                    //  {"requestType":"6","request":{"id":"MGI20221004222746688","cmNo":"3","kacType":"1","bankNo":"999","ccy":"TWD","pvType":"1","bicaccNo":"0000000","iType":"1","pReason":"deposit some money in the bank","clearingAccountList":[{"accNo":"1","amt":10000},{"accNo":"2","amt":200}],"ctName":"Joey","ctTel":"12345678"}}
                    break;
                }
                case "7": {
                    logger.info("資料刪除");
                    bufferedWriter.write(CRUD.deleteMgni(request.getString("id")));
                    //  {"requestType":"7","request":{"id":"MGI20221005091849492"}}
                    break;
                }
                default: {
                    bufferedWriter.write("請輸入 1~7");
                }
            }

            bufferedWriter.newLine();
            bufferedWriter.flush();

            clientSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Error Message: ", new SocketException("Socket is closed"));
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
            e.printStackTrace();

        } catch (JSONException | NullPointerException e) {
            try {
                OutputStream outputStream = clientSocket.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                System.out.println(e);
                bw.write("輸入Json格式資料有誤" + e);
                bw.newLine();
                bw.flush();

                bw.close();
                outputStream.close();

                logger.error("輸入Json格式資料有誤" + e);

            } catch (IOException ex) {
                System.out.println("Socket啟動有問題 !");
                System.out.println("IOException :" + e.toString());

            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();

        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

