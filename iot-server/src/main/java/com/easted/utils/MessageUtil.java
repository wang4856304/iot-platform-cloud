package com.easted.utils;

import com.easted.entity.CarMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {

    public static final byte flag = 0x7e;//标志位,粘包分隔符

    /**
     * 粘包处理
     * @param msg
     * @param flag 粘包分隔符
     * @return
     */
    public static byte[][] parseBag(byte[] msg, byte flag) {
        if (msg == null || msg.length == 0) {
            return null;
        }
        List<byte[]> msgList = new ArrayList<>();
        List<Integer> flagPos = new ArrayList<>();
        byte[] tempMsg = null;

        //不以~开头或结尾，在中间分割
        if (msg[0] != flag && msg[msg.length-1] !=flag) {
            tempMsg = new byte[msg.length + 2];
            tempMsg[0] = flag;
            for (int i = 0; i < msg.length; i++) {
                tempMsg[i+1] = msg[i];
            }
            tempMsg[tempMsg.length - 1] = flag;
        }

        //不以~开头但以~结尾，在中间分割
        if (msg[0] != flag && msg[msg.length-1] == flag) {
            tempMsg = new byte[msg.length + 1];
            tempMsg[0] = flag;
            for (int i = 0; i < msg.length; i++) {
                tempMsg[i+1] = msg[i];
            }
        }

        //以~开头或结尾，在中间分割
        if (msg[0] == flag && msg[msg.length-1] ==flag) {
            tempMsg = msg;
        }

        //以~开头但不以~结尾，在中间分割
        if (msg[0] == flag && msg[msg.length-1] !=flag) {
            tempMsg = new byte[msg.length + 1];
            for (int i = 0; i < msg.length; i++) {
                tempMsg[i] = msg[i];
            }
            tempMsg[tempMsg.length - 1] = flag;
        }
        for (int i = 0; i < tempMsg.length; i++) {
            byte b = tempMsg[i];
            if (flag == b) {
                flagPos.add(i);
            }
        }

        for (int i = 0; i < flagPos.size(); i++) {
            if (i == flagPos.size() - 1) {
                break;
            }
            int preIndex = flagPos.get(i);
            int nextIndex = flagPos.get(i + 1);
            if (preIndex + 1 == nextIndex) {
                continue;
            }
            byte[] bytes = new byte[nextIndex - (preIndex + 1)];
            for (int j = preIndex + 1; j < nextIndex; j++) {
                bytes[j-preIndex -1] = tempMsg[j];
            }
            msgList.add(bytes);
        }
        byte[][] msgArr = new byte[msgList.size()][];
        msgList.toArray(msgArr);
        return msgArr;
    }

    /**
     * 获取消息id
     * @param msg
     * @return
     */
    public static byte[] getMessageId(byte[] msg) {
        byte[] bMsgId = new byte[2];
        bMsgId[0] = msg[0];
        bMsgId[1] = msg[1];
        return bMsgId;
    }

    /**
     * 获取消息流水号
     * @param msg
     * @return
     */
    public static byte[] getMessageNo(byte[] msg) {
        byte[] bMsgNo = new byte[2];
        bMsgNo[0] = msg[10];
        bMsgNo[1] = msg[11];
        return bMsgNo;
    }

    /**
     * 获取终端设备号
     * @param msg
     * @return
     */
    public static byte[] getDeviceNo(byte[] msg) {
        byte[] bDeviceNo = new byte[6];
        bDeviceNo[0] = msg[4];
        bDeviceNo[1] = msg[5];
        bDeviceNo[2] = msg[6];
        bDeviceNo[3] = msg[7];
        bDeviceNo[4] = msg[8];
        bDeviceNo[5] = msg[9];
        return bDeviceNo;
    }

    /**
     * 获取消息体属性
     * @param msg
     * @return
     */
    public static byte[] getMessageBodyProp(byte[] msg) {
        byte[] bMsgBodyProp = new byte[2];
        bMsgBodyProp[0] = msg[2];
        bMsgBodyProp[1] = msg[3];
        return bMsgBodyProp;
    }

    /**
     * 获取消息体
     * @param msg
     * @param msgPackage 是否存在消息包封装
     * @return
     */
    public static byte[] getMessageBody(byte[] msg, boolean msgPackage) {
        byte[] msgBody;
        if (!msgPackage) {
            msgBody = new byte[msg.length-(12+1)];
            System.arraycopy(msg, 12, msgBody, 0, msgBody.length);
        }
        else {
            msgBody = new byte[msg.length-(16+1)];
            System.arraycopy(msg, 16, msgBody, 0, msgBody.length);
        }
        return msgBody;
    }

    /**
     * 获取消息体
     * @param msg
     * @return
     */
    public static byte[] getMessageBody(byte[] msg) {
        //消息体属性
        byte[] bMsgBodyProp = MessageUtil.getMessageBodyProp(msg);
        //判断是否分包
        boolean exist = MessageUtil.existBags(bMsgBodyProp);

        return getMessageBody(msg, exist);
    }

    /**
     * 判断是否存在分包
     * @param bMsgBodyProp
     * @return
     */
    public static boolean existBags(byte[] bMsgBodyProp) {
        int m = 1<<5;
        int r = bMsgBodyProp[0]&m;
        //r等于0,表示不分包,则消息头中无"消息包封装项"
        return r == 0;
    }

    /**
     * 获取消息包封装项
     * @param msg
     * @return
     */
    public static byte[] getMessagePackage(byte[] msg) {
        //消息体属性
        byte[] bMsgBodyProp = MessageUtil.getMessageBodyProp(msg);
        //判断是否分包
        boolean exist = MessageUtil.existBags(bMsgBodyProp);
        if (exist) {
            byte[] bMsgPackage = new byte[4];
            bMsgPackage[0] = msg[12];
            bMsgPackage[1] = msg[13];
            bMsgPackage[2] = msg[14];
            bMsgPackage[3] = msg[15];
            return bMsgPackage;
        }
        return null;
    }

    /**
     * 校验验证
     * @param msg
     * @param checkFlag 校验码
     * @return
     */
    public static boolean checkMsg(byte[] msg, byte checkFlag) {

        int sign = 0;
        for (int i = 0; i < msg.length-1; i++) {
            sign = sign^msg[i];
        }
        byte signByte = (byte)sign;
        return checkFlag == signByte;
    }

    /**
     * byte数组转int
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes)
    {
        //如果不与0xff进行按位与操作，转换结果将出错，有兴趣的同学可以试一下。
        // 由低位到高位
        int int1=bytes[0]&0xff;
        int int2=(bytes[1]&0xff)<<8;
        int int3=(bytes[2]&0xff)<<16;
        int int4=(bytes[3]&0xff)<<24;

        return int1|int2|int3|int4;
    }

    /**
     * 平台通用应答
     * @param msgNo
     * @param msgId
     * @param response
     * @return
     */
    public static byte[] response(byte[] msgNo, byte[] msgId, byte[] response) {
        return merge(msgNo, msgId, response);
    }

    /**
     * 多个数组顺序合并
     * @param bytes
     * @return
     */
    public static byte[] merge(byte[]...bytes) {
        int length = 0;
        for (byte[] b: bytes) {
            length = length + b.length;
        }

        byte[] res = new byte[length];
        int tmpLen = 0;
        for (byte[] b: bytes) {
            System.arraycopy(b, 0, res, tmpLen, b.length);
            tmpLen = tmpLen + b.length;
        }
        return res;
    }

    /**
     * 获取封装消息
     * @param msg
     * @return
     */
    public static CarMessage getCarMessage(byte[] msg) {

        CarMessage carMessage = new CarMessage();
        //消息id
        byte[] msgId = getMessageId(msg);
        carMessage.setMsgId(msgId);

        //终端设备号
        byte[] deviceNo = getDeviceNo(msg);
        carMessage.setDeviceNo(deviceNo);

        //消息流水号
        byte[] msgNo = getMessageNo(msg);
        carMessage.setMsgNo(msgNo);

        //消息体
        byte[] msgBody = getMessageBody(msg);
        carMessage.setMsgBody(msgBody);

        //消息体属性
        byte[] msgBodyProp = getMessageBodyProp(msg);
        carMessage.setMsgBodyProp(msgBodyProp);

        //是否粘包
        boolean existBag = existBags(msgBodyProp);
        carMessage.setExistBag(existBag);

        //消息包封装项
        byte[] msgPackage = getMessagePackage(msg);
        carMessage.setMsgPackage(msgPackage);

        return carMessage;
    }

    public static void main(String args[]) {
        /*byte[] bytes = new byte[4];
        bytes[0] = 0;
        bytes[1] = 1;
        bytes[2] = 0;
        bytes[3] = 0;
        System.out.println(bytes2Int(bytes));*/
        byte b = 16;
        int m = 1<<5;
        int r = b&m;
        System.out.println(r);
    }

}
