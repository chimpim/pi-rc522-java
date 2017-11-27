package com.chimpim.rc522;

import com.chimpim.rc522.api.Convert;
import com.chimpim.rc522.api.RC522SimpleAPI;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author liuyonghui
 */
public class Main {
    private static byte sector = 2, block = 1;
    private static byte[] data = new byte[16];

    public static void main(String[] args) {
        begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);
        boolean isRead = true;
        System.out.println("1 - read card , 2 - write card ");
        System.out.println("Current operation : read card");
        while (true) {
            switch (sc.nextLine()) {
                case "1":
                    System.out.println("Handover success, current operation : read card");
                    isRead = true;
                    read();
                    break;
                case "2":
                    System.out.println("Handover success, current operation : write card");
                    isRead = false;
                    write(sc);
                    break;
                case "exit":
                    System.exit(0);
                default:
                    if (isRead) read();
                    else write(sc);
                    break;
            }
        }
    }

    private static void write(Scanner sc) {
        System.out.println("Input 16 hex characters (range: 00 to FF)，Use \",\" separate：");
        boolean isContinue = true;
        while (isContinue) {
            String s = sc.nextLine();
            if (s.equals("exit")) {
                System.out.println("Quit write operation");
                return;
            }
            String[] split = s.split(",");
            if (split.length == 16) {
                try {
                    for (int i = 0; i < split.length; i++) {
                        data[i] = (byte) Integer.parseInt(split[i], 16);
                    }
                    isContinue = false;
                } catch (Exception e) {
                    System.out.println("Input error re input !");
                }
            } else {
                System.out.println("Input error re input !");
            }

        }
        try {
            RC522SimpleAPI.getInstance().findCards().operate().writeCard(sector, block, data);
        } catch (RC522SimpleAPI.SimpleAPIException e) {
            System.out.println(e.toString());
        }
        System.out.println("Write card success");
    }

    private static void read() {
        byte[] uid = new byte[5];
        byte[] bytes;
        try {
            bytes = RC522SimpleAPI.getInstance().findCards().getUid(uid).operate().readCard(sector, block);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            saveRecord(timestamp, uid, bytes);
            //uid
            System.out.println("uid = " + Arrays.toString(uid));
            String hexUid = Convert.bytesToHex(uid);
            StringBuilder hexUidSb = new StringBuilder();
            for (int i = 0; i < hexUid.length() / 2; i++) {
                hexUidSb.append(hexUid.substring(i * 2, (i + 1) * 2));
                if (i < hexUid.length() / 2 - 1)
                    hexUidSb.append(",");
            }
            System.out.println("hex uid = " + hexUidSb.toString());
            //data
            System.out.println("byte data = " + Arrays.toString(bytes));
            String hexData = Convert.bytesToHex(bytes);
            StringBuilder hexDataSb = new StringBuilder();
            for (int i = 0; i < hexData.length() / 2; i++) {
                hexDataSb.append(hexData.substring(i * 2, (i + 1) * 2));
                if (i < hexData.length() / 2 - 1)
                    hexDataSb.append(",");
            }
            System.out.println("hex data = " + hexDataSb.toString());
        } catch (RC522SimpleAPI.SimpleAPIException e) {
            System.out.println(e.toString());
        }

    }

    private static void saveRecord(String timestamp, byte[] uid, byte[] data) {
        String sUid = Convert.bytesToHex(uid);
        String sData = Convert.bytesToHex(data);
        FileUtil.fileAppendWrite("data.txt", String.format("timestamp=%s;uid=%s;data=%s;", timestamp, sUid, sData));
    }
}
