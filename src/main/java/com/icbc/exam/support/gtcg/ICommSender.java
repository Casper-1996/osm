package com.icbc.exam.support.gtcg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import com.sun.istack.internal.logging.Logger;
import org.jdom.JDOMException;

/**
 *
 *
 * <pre>
 * 外联前置不定长通讯组件通讯
 * 继承XML，所有操作规范统一
 * </pre>
 *
 * <pre>
 * modify by dlfh-yuc02 on 2017年9月12日
 *    fix->1.2017-11-08 编码问题BUG修改
 *         2.
 * </pre>
 */
public class ICommSender extends XmlDocument {

    private String hostName;
    private int portNum;
    private int timeOut = 30 * 1000;
    private Logger log = Logger.getLogger(this.getClass());
    private static long num = 0;

    /**
     * 通过CTP的ENV配置文件配置相应的接口技术信息
     *
     * @author dlfh-yuc02
     * @time 2017年7月13日 上午9:13:08
     */
    public void setConfigByEnv() {
        this.hostName = "104.6.189.201";
        this.portNum = 20126;
        this.timeOut = 30000;
    }

    public ICommSender() {
    }

    public ICommSender(String xmlFilePath) throws JDOMException, IOException {
        super(xmlFilePath);
        try {
            this.hostName = getValue("/PRIVATE/HOSTNAME");
            this.portNum = Integer.parseInt(getValue("/PRIVATE/PORTNUM"));
            this.timeOut = Integer.parseInt(getValue("/PRIVATE/TIMEOUT"));
        } catch (Exception e) {
            // 如果报错则全部置为默认
            this.hostName = null;
            this.portNum = 0;
            this.timeOut = 30 * 1000;
        }
    }

    public ICommSender(String hostIp, int portNum, int timeOut, String xmlFilePath) throws Exception {
        super(xmlFilePath);
        this.hostName = hostIp;
        this.portNum = portNum;
        this.timeOut = timeOut;
    }

    /**
     *
     * @param hostIp
     * @param portNum
     * @param timeOut
     *            单位毫秒
     * @throws Exception
     */
    public ICommSender(String hostIp, int portNum, int timeOut) throws Exception {
        super();
        this.hostName = hostIp;
        this.portNum = portNum;
        this.timeOut = timeOut;
    }

    private synchronized long getserno() {
        long s = ++num;
        if (num >= Integer.MAX_VALUE){
            num = 0;
        }
        return s;
    }

    /**
     *
     * <pre>
     * 同步发送报文
     * </pre>
     *
     * @author dlfh-yuc02
     * @time 2017年9月12日 下午1:40:52
     * @throws IOException
     * @throws JDOMException
     */
    public void send() throws IOException, JDOMException {
        long serno = getserno();
        String getXmlStr = getXmlStr();
        log.info("GTCG_IC[" + serno + "]发送报文-->\n" + getXmlStr);
        String rcv = sendMessage(getXmlStr);
        log.info("GTCG_IC[" + serno + "]返回报文-->\n" + rcv + "\n");
        setDocument(rcv);
    }

    /**
     *
     * <pre>
     * 与GTCG不定长通讯实现方法
     * </pre>
     *
     * @author dlfh-yuc02
     * @time 2017年9月12日 下午1:41:04
     * @param strMessage
     * @return
     * @throws IOException
     */
    public String sendMessage(String strMessage) throws IOException {
        String serverString = null;
        Socket socket = null;
        PrintStream out = null;
        BufferedReader in = null;
        try {
            socket = creatSocket();
            out = new PrintStream(socket.getOutputStream(), true, this.encoding);
            out.print(strMessage);
            out.flush();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), this.encoding));
            long sendTime = System.currentTimeMillis();
            long receiveTime;// = System.currentTimeMillis();
            boolean received = false; // 成功接收报文
            boolean delayTooLong = false;
            while (!received && !delayTooLong) {// 循环等待
                if (socket.getInputStream().available() > 0) {
                    StringBuffer sbuff = new StringBuffer();
                    String line = null;
                    while (null != (line = in.readLine())) {
                        sbuff.append(line);
                    }
                    serverString = sbuff.toString();
                }
                receiveTime = System.currentTimeMillis();
                if (serverString != null){
                    received = true; // 字符串不为空，接收成功
                }
                if ((receiveTime - sendTime) > this.timeOut){
                    delayTooLong = true; // 接收等待时间过长，超时
                }
            } // while
            if (delayTooLong) {
                throw new RuntimeException("communication time out");
            }
            if (!received) {
                throw new RuntimeException("no data received");
            }
        } finally {
            if (null != in){
                in.close();
            }
            if (null != out){
                out.close();
            }
            if (null != socket){
                socket.close();
            }
        }
        socket = null;
        return serverString.trim();
    }

    /**
     *
     * @author dlfh-yuc02
     * @time 2016-11-18 下午05:09:47
     * @return
     * @throws IOException
     */
    private Socket creatSocket() throws IOException {
        Socket s = null;
        s = new Socket();
        s.connect(new InetSocketAddress(hostName, portNum), timeOut);
        s.setSoTimeout(timeOut);
        return s;
    }

    /**
     * @return Returns the hostName.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            The hostName to set.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return Returns the portNum.
     */
    public int getPortNum() {
        return portNum;
    }

    /**
     * @param portNum
     *            The portNum to set.
     */
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    /**
     * @return Returns the timeOut.
     */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * @param timeOut
     *            The timeOut to set.
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}