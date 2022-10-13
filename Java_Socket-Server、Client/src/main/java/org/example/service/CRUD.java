package org.example.service;

import org.apache.log4j.Logger;
import org.example.MultiThreadServer;
import org.example.request.CashiAccAmt;
import org.example.request.MgniRequest;
import org.json.JSONObject;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class CRUD {

    private static final Logger logger = Logger.getLogger(CRUD.class);
    static Connection connection = DataBaseConnection.getConnection();

    //查詢Cashi全部資料
    public static String findAllCashi() throws SQLException {

        String result = "";
        System.out.println("查詢Cashi全部資料:");

        //  要執行的命令
        String sql = "SELECT * FROM mgn_cashi";
        //  通過連接對象，將命令傳給資料庫
        Statement st = connection.createStatement();
        //  執行
        ResultSet resultSet = st.executeQuery(sql);

        //STEP 5: Extract data from result set
        while (resultSet.next()) {

            result += (" 申請主檔 ID:" + resultSet.getString("CASHI_MGNI_ID") + "\n"
                    + " 存入結算帳戶帳號:" + resultSet.getString("CASHI_ACC_NO") + "\n"
                    + " 幣別:" + resultSet.getString("CASHI_CCY") + "\n"
                    + " 金額:" + resultSet.getBigDecimal("CASHI_AMT") + "\n"
                    + " ========================================================" + "\n");

            //Retrieve by column name
            String id = resultSet.getString("CASHI_MGNI_ID");
            String acc_no = resultSet.getString("CASHI_ACC_NO");
            String currency = resultSet.getString("CASHI_CCY");
            BigDecimal amt = resultSet.getBigDecimal("CASHI_AMT");

            //Display values
            System.out.print("申請主檔 ID: " + id);
            System.out.print(", 存入結算帳戶帳號: " + acc_no);
            System.out.println(", 幣別: " + currency);
            System.out.println(", 金額: " + amt);
            System.out.println("=============================================================");

        }
        resultSet.close();
        return result;
    }

//===================================================================================================

    //用Id查詢Mgni
    public static String findMgniById(String id) throws SQLException {

        String result = "";

        System.out.println("Mgni查詢" + id + "的資料");
        try {
            String SQL = "SELECT * FROM mgn_mgni WHERE MGNI_ID = ?";
            Statement st = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (checkIdExist(st, id) == false) {
                System.out.println("查無此Id");
                return "此Id不存在" + "\n" + "=======================================================";
            }

            while (resultSet.next()) {

                result = (" 申請ID:" + resultSet.getString("MGNI_ID") + "\n"
                        + " 存入日期:" + resultSet.getString("MGNI_TIME") + "\n"
                        + " 存入類型:" + resultSet.getString("MGNI_TYPE") + "\n"
                        + " 結算會員代號:" + resultSet.getBigDecimal("MGNI_CM_NO") + "\n"
                        + " 存入保管專戶別:" + resultSet.getString("MGNI_KAC_TYPE") + "\n"
                        + " 存入結算銀行代碼:" + resultSet.getString("MGNI_BANK_NO") + "\n"
                        + " 存入幣別:" + resultSet.getString("MGNI_CCY") + "\n"
                        + " 存入方式:" + resultSet.getString("MGNI_PV_TYPE") + "\n"
                        + " 實體帳號/虛擬帳號:" + resultSet.getString("MGNI_BICACC_NO") + "\n"
                        + " 存入帳號明細:" + "\n" + findCashiById(id)
                        + " 存入類別:" + resultSet.getString("MGNI_I_TYPE") + "\n"
                        + " 存入實體帳號原因:" + resultSet.getString("MGNI_P_REASON") + "\n"
                        + " 總存入金額:" + resultSet.getString("MGNI_AMT") + "\n"
                        + " 聯絡人姓名:" + resultSet.getString("MGNI_CT_NAME") + "\n"
                        + " 聯絡人電話:" + resultSet.getString("MGNI_CT_TEL") + "\n"
                        + " 申請狀態:" + resultSet.getString("MGNI_STATUS") + "\n"
                        + " 更新時間:" + resultSet.getString("MGNI_U_TIME") + "\n"
                        + " ========================================================" + "\n");
            }

        } catch (Exception e) {
            System.out.println("Exception :" + e.toString());

        }
        System.out.println("查詢結果：" + "\n" + result);
        return result;
    }

//===================================================================================================

    //用Id查詢Cashi
    public static String findCashiById(String id) {
        String result = "";

        System.out.println("Cashi查詢" + id + "的資料");

        try {
            String getCashiById = "SELECT * FROM mgn_cashi where CASHI_MGNI_ID = '" + id + "'";

            Statement st = connection.createStatement();
            if (checkIdExist(st, id) == false) {
                System.out.println("查無此Id");
                return "此Id不存在" + "\n" + "=======================================================";
            }

            ResultSet resultSet = st.executeQuery(getCashiById);
            while (resultSet.next()) {
                result += ("    存入結算帳戶帳號:" + resultSet.getString("CASHI_ACC_NO") + "\n"
                        + "    幣別:" + resultSet.getString("CASHI_CCY") + "\n"
                        + "    金額:" + resultSet.getBigDecimal("CASHI_AMT") + "\n" + "\n");
            }

        } catch (Exception e) {
            System.out.println("Exception :" + e.toString());

        }
        System.out.println("查詢結果：" + "\n" + result);
        return result;
    }

//===================================================================================================

    //Mgni動態查詢
    public static String dynamicQueryMgni(JSONObject request) {

        String result = "";
        String cmNo = request.getString("cmNo");
        String ccy = request.getString("ccy");

        //where 1=1 => 1=1表示true，即永真 https://www.gushiciku.cn/pl/peJ7/zh-tw
        String baseSQL = "SELECT * FROM mgn_mgni where 1=1";
        StringBuilder builder = new StringBuilder();// 用於拼接SQL語句
        builder.append(baseSQL);

        if (!(cmNo).isEmpty()) {
            builder.append(" and MGNI_CM_NO='" + cmNo + "'");
        }
        if (!(ccy).isEmpty()) {
            builder.append(" and MGNI_CCY='" + ccy + "'");
        }
        try {
            Statement st = connection.createStatement();
            ResultSet mgni = st.executeQuery(builder.toString());
            while (mgni.next()) {
                result += (" 申請ID:" + mgni.getString("MGNI_ID") + "\n"
                        + " 存入日期:" + mgni.getString("MGNI_TIME") + "\n"
                        + " 存入類型:" + mgni.getString("MGNI_TYPE") + "\n"
                        + " 結算會員代號:" + mgni.getBigDecimal("MGNI_CM_NO") + "\n"
                        + " 存入保管專戶別:" + mgni.getString("MGNI_KAC_TYPE") + "\n"
                        + " 存入結算銀行代碼:" + mgni.getString("MGNI_BANK_NO") + "\n"
                        + " 存入幣別:" + mgni.getString("MGNI_CCY") + "\n"
                        + " 存入方式:" + mgni.getString("MGNI_PV_TYPE") + "\n"
                        + " 實體帳號/虛擬帳號:" + mgni.getString("MGNI_BICACC_NO") + "\n"
                        + " 存入帳號明細:" + "\n" + findCashiById(mgni.getString("MGNI_ID"))
                        + " 存入類別:" + mgni.getString("MGNI_I_TYPE") + "\n"
                        + " 存入實體帳號原因:" + mgni.getString("MGNI_P_REASON") + "\n"
                        + " 總存入金額:" + mgni.getString("MGNI_AMT") + "\n"
                        + " 聯絡人姓名:" + mgni.getString("MGNI_CT_NAME") + "\n"
                        + " 聯絡人電話:" + mgni.getString("MGNI_CT_TEL") + "\n"
                        + " 申請狀態:" + mgni.getString("MGNI_STATUS") + "\n"
                        + " 更新時間:" + mgni.getString("MGNI_U_TIME") + "\n"
                        + " ========================================================" + "\n");
            }
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";

        } catch (Exception e) {
            System.out.println("Exception :" + e.toString());

        }
        return result;
    }

//===================================================================================================

    //Mgni新增
    public static String createMgni(MgniRequest request) throws SQLException {

        String id = "MGI" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());

        try {
            String err = error(request);
            if (err.length() == 0) {

                Statement st = connection.createStatement();
                BigDecimal totalAmt = new BigDecimal(0);

                String createCashi = "";
                for (CashiAccAmt s : request.getClearingAccountList()) {

                    createCashi = "INSERT INTO mgn_cashi VALUES ("
                            + "'" + id + "'" + ","
                            + "'" + s.getAccNo() + "'" + ","
                            + "'" + request.getCcy() + "'" + ","
                            + s.getAmt() + ")";
                    st.execute(createCashi);

                    totalAmt = totalAmt.add(s.getAmt());
                }
                String createMgni = "INSERT INTO mgn_mgni VALUES(" +
                        "'" + id + "'" + "," +
                        "'" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "'" + ","
                        + "1" + "," //  存入類型	1:入金
                        + "'" + request.getCmNo() + "'" + ","
                        + "'" + request.getKacType() + "'" + ","
                        + "'" + request.getBankNo() + "'" + ","
                        + "'" + request.getCcy() + "'" + ","
                        + "'" + request.getPvType() + "'" + ","
                        + "'" + request.getBicaccNo() + "'" + ","
                        + "'" + request.getIType() + "'" + ","
                        + "'" + request.getPReason() + "'" + ","
                        + "" + totalAmt + ","
                        + "'" + request.getCtName() + "'" + ","
                        + "'" + request.getCtTel() + "'" + ","
                        + "0" + ","   //  申請狀態	0:申請成功
                        + "'" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "'" + ")";

                st.execute(createMgni);
            }
            if (err.length() != 0) {
                return "新增失敗";
            }
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";

        } catch (Exception e) {
            System.out.println(e.toString());

        }
        System.out.println("Mgni新增" + id + "的資料");
        return "新增成功" + "\n" + findMgniById(id);
    }

//===================================================================================================

    //Mgni更新
    public static String updateMgni(MgniRequest request) throws SQLException {

        try {
            String err = error(request);
            if (err.length() == 0) {
                Statement st = connection.createStatement();

                //  先刪除Cashi資料(以免更新資料變少時，多的資料還留在資料庫中)
                String deleteCashi = "DELETE FROM mgn_cashi WHERE CASHI_MGNI_ID=" + "'" + request.getId() + "'";
                st.execute(deleteCashi);

                if (checkIdExist(st, request.getId()) == false) {
                    System.out.println("查無此Id");
                    return "此id不存在" + "\n" + "=======================================================";
                }

                BigDecimal price = BigDecimal.ZERO;

                String id = "'" + request.getId() + "'";
                String currency = "'" + request.getCcy() + "'";

                String cashi = new String();
                for (CashiAccAmt s : request.getClearingAccountList()) {
//                cashi = " UPDATE MGN_CASHI SET"
//                        + " CASHI_ACC_NO = " + "'" + s.getAccNo() + "'" + ","
//                        + " CASHI_CCY = " + currency + ","
//                        + " CASHI_AMT = " + s.getAmt()
//                        + " WHERE CASHI_MGNI_ID = " + id + "AND" + " CASHI_ACC_NO = " + "'" + s.getAccNo() + "'" + "AND" + " CASHI_CCY = " + currency + ";";

                    cashi = "INSERT INTO mgn_cashi VALUES ("
                            + id + ","
                            + "'" + s.getAccNo() + "'" + ","
                            + currency + ","
                            + s.getAmt() + ")";
                    st.execute(cashi);

                    price = price.add(s.getAmt());
                }

                String updateMgni = "UPDATE mgn_mgni SET "
                        + "MGNI_TYPE = " + "'" + "1" + "'" + ","
                        + "MGNI_CM_NO = " + "'" + request.getCmNo() + "'" + ","
                        + "MGNI_KAC_TYPE = " + "'" + request.getKacType() + "'" + ","
                        + "MGNI_BANK_NO = " + "'" + request.getBankNo() + "'" + ","
                        + "MGNI_CCY = " + "'" + request.getCcy() + "'" + ","
                        + "MGNI_PV_TYPE = " + "'" + request.getPvType() + "'" + ","
                        + "MGNI_BICACC_NO = " + "'" + request.getBicaccNo() + "'" + ","
                        + "MGNI_I_TYPE = " + "'" + request.getIType() + "'" + ","
                        + "MGNI_P_REASON = " + "'" + request.getPReason() + "'" + ","
                        + "MGNI_AMT = " + price + ","
                        + "MGNI_CT_NAME = " + "'" + request.getCtName() + "'" + ","
                        + "MGNI_CT_TEL = " + "'" + request.getCtTel() + "'" + ","
                        + "MGNI_STATUS = " + "'" + "0" + "'" + ","
                        + "MGNI_U_TIME=" + "'" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "'"
                        + " WHERE MGNI_ID =" + "'" + request.getId() + "'";

                st.execute(updateMgni);
            }
            if (err.length() != 0) {
                return "更新失敗";
            }
        } catch (SQLException e) {
            System.out.println("Exception :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";

        }
        System.out.println("Mgni更新" + request.getId() + "的資料成功");
        return "更新成功" + "\n" + findMgniById(request.getId());
    }

//===================================================================================================

    //Mgni刪除
    public static String deleteMgni(String id) {

        String deleteMgni = "DELETE FROM mgn_mgni WHERE MGNI_ID=" + "'" + id + "'";
        String deleteCashi = "DELETE FROM mgn_cashi WHERE CASHI_MGNI_ID=" + "'" + id + "'";

        try {
            Statement st = connection.createStatement();
            if (checkIdExist(st, id) == false) {
                System.out.println("查無此Id");
                return "此id不存在";
            }
            st.execute(deleteMgni);
            st.execute(deleteCashi);

        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";

        } catch (Exception e) {
            System.out.println("Exception :" + e.toString());

        }
        System.out.println("Mgni刪除" + id + "的資料成功");
        return "刪除成功";
    }

//===================================================================================================

    //檢查輸入資料格式
    //https://www.youtube.com/watch?v=FYMXu5lWnVA&t=184s
    public static String error(MgniRequest check) {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

        // 根據validatorFactory拿到一個Validator
        Validator validator = validatorFactory.getValidator();

        // 使用validator對結果進行校驗
        Set<ConstraintViolation<MgniRequest>> result = validator.validate(check);

        //把結果列印出來
        System.out.println("參數錯誤數量" + result.size());
        StringBuilder stringBuilder = new StringBuilder();
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + "，你填寫為: " + v.getInvalidValue())
                .forEach(System.out::println);
        for (ConstraintViolation<MgniRequest> e : result) {
            stringBuilder.append(e.getMessage()).append("\n");

        }
        return stringBuilder.toString();
    }

//===================================================================================================

    private static boolean checkIdExist(Statement st, String id) {
        try {
            ResultSet exist = st.executeQuery("SELECT * FROM mgn_mgni WHERE MGNI_ID= '" + id + "'");
            if (exist.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");

        }
        return false;
    }

//===================================================================================================

//    //舊的Mgni新增
//    public static String createMgni2(JSONObject request) throws SQLException {
//
//        String id = "MGI" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
//
//        try {
//            Statement st = connection.createStatement();
//            BigDecimal totalAmt = new BigDecimal(0);
//
//            String createCashi = "";
//            for (Object s : request.getJSONArray("clearingAccountList")) {
//
//                JSONObject jsonObject = new JSONObject(s.toString());
//                createCashi = "INSERT INTO mgn_cashi VALUES (" +
//                        "'" + id + "'" + "," +
//                        "'" + jsonObject.getString("accNo") + "'" + "," +
//                        "'" + request.getString("ccy") + "'" + "," +
//                        "'" + jsonObject.getBigDecimal("amt") + "'" + ")";
//
//                totalAmt = totalAmt.add(jsonObject.getBigDecimal("amt"));
//
//                st.execute(createCashi);
//            }
//
//            String createMgni = "INSERT INTO mgn_mgni VALUES(" +
//                    "'" + id + "'" + "," +
//                    "'" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "'" + ","
//                    + "1" + "," //  存入類型	1:入金
//                    + "'" + request.getString("cmNo") + "'" + ","
//                    + "'" + request.getString("kacType") + "'" + ","
//                    + "'" + request.getString("bankNo") + "'" + ","
//                    + "'" + request.getString("ccy") + "'" + ","
//                    + "'" + request.getString("pvType") + "'" + ","
//                    + "'" + request.getString("bicaccNo") + "'" + ","
//                    + "'" + request.getString("iType") + "'" + ","
//                    + "'" + request.getString("pReason") + "'" + ","
//                    + "" + totalAmt + ","
//                    + "'" + request.getString("ctName") + "'" + ","
//                    + "'" + request.getString("ctTel") + "'" + ","
//                    + "0" + ","   //  申請狀態	0:申請成功
//                    + "'" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "'" + ")";
//
//            st.execute(createMgni);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//        System.out.println("Mgni新增" + id + "的資料");
//        return "新增成功" + "\n" + findMgniById(id);
//    }
}
