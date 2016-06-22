package team5818.robot.modules;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.AutoAim;

public class Track {

    public double blobCount = 0;
    public double blobLocX = 0;
    public double blobLocY = 0;
    public NetworkTable RoboData;
    public DatagramSocket socket;
    private int portNum = 5808;
    private boolean hasReportedError;


    /**
     * This class retrieves data from our off-board vision processing. Data can be retrieved using a UDP socket (more reliable)
     * or over a network table (less reliable).
     */
    public Track() {

        try {
            socket = new DatagramSocket(portNum);
            InetSocketAddress address =
                    new InetSocketAddress("10.58.18.191", portNum);
            socket.setSoTimeout(100);
            socket.bind(address);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        try {
            RoboData = NetworkTable.getTable("Targeting");
            NetworkTable.setUpdateRate(.2);
        } catch (Exception e) {
            DriverStation.reportError("no data table", false);
        }
    }

    /**
     * Receive Information from RoboRealm
     */
    public void GetData() {
        if (AutoAim.UDP) { //Receive a packet from the Datagram Socket if in UDP mode
            try {
                socket = new DatagramSocket(portNum);
                InetSocketAddress address =
                        new InetSocketAddress("10.58.18.191", portNum);
                socket.setSoTimeout(100);
                socket.bind(address);
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                byte[] buff = new byte[16];
                DatagramPacket packet = new DatagramPacket(buff, 16);
                
                socket.receive(packet);

                String s = new String(buff);


                String[] string_array = s.split(",");


                blobCount = Integer.parseInt(string_array[0]);
                if(blobCount>0){
                    blobLocX = Integer.parseInt(string_array[1]);
                    blobLocY = Integer.parseInt(string_array[2]);
                }
                else{
                    blobLocX = -2;
                    blobLocY = -2;
                }
                if(hasReportedError) {
                    DriverStation.reportError("Recieving Socket Data", false);
                }
                hasReportedError = false;

            } catch (IOException e) {
                //e.printStackTrace();
                if(!hasReportedError) {
                    DriverStation.reportError("Not Recieving Data", false);
                }
                hasReportedError = true;
                blobCount = -2;
                blobLocX = -2;
                blobLocY = -2;

            }

        } else {//Fetch data from network tables if not using UDP
            blobCount = RoboData.getNumber("BLOB_COUNT", -1.0);

            blobLocX = RoboData.getNumber("COG_X", 0.0);
            blobLocY = RoboData.getNumber("COG_Y", 0.0);
        }

    }
    
    
}
