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

public class Track implements Module {

    public double blobCount = 0;
    public double blobWidth = 0;
    public double blobHeight = 0;
    public double blobOffsetFar = 0;
    public double blobOffsetClose = 0;
    public double imageWidth = 0;
    public double imageHeight = 0;
    public double blobLocX = 0;
    public double blobLocY = 0;
    public NetworkTable RoboData;
    public DatagramSocket socket;
    private int portNum = 5808;

    public Track() {

        try {
            socket = new DatagramSocket(null);
            InetSocketAddress address = new InetSocketAddress("192.168.43.36", portNum);
            socket.setSoTimeout(100);
            socket.connect(address);
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

    public void GetData() {
        if (AutoAim.udp) {
            try {
                byte[] buff = new byte[256];
                DatagramPacket packet = new DatagramPacket(buff, 256);
                socket.receive(packet);

                String s = new String(buff);

                DriverStation.reportError(s, false);
                DriverStation.reportError((Arrays.toString(buff)),false);

                String[] string_array = s.split(",");//might be "/"

                blobCount = Integer.parseInt(string_array[0]);
                blobLocX = Integer.parseInt(string_array[1]);
                blobLocY = Integer.parseInt(string_array[2]);

                DriverStation.reportError("" + blobCount, false);

            } catch (IOException e) {
                e.printStackTrace();
                DriverStation.reportError("NOPE" + " " + socket.getPort() +" " + socket.getInetAddress(), false);
                blobCount = -2;
                blobLocX = -2;
                blobLocY = -2;

            }

        } else {

            blobCount = RoboData.getNumber("BLOB_COUNT", -1.0);
            imageWidth = RoboData.getNumber("IMAGE_WIDTH", 0.0);
            imageHeight = RoboData.getNumber("IMAGE_HEIGHT", 0.0);
            blobWidth = RoboData.getNumber("WIDTH", 320.0);
            blobHeight = RoboData.getNumber("HEIGHT", 240.0);
            blobLocX = RoboData.getNumber("COG_X", 0.0);
            blobLocY = RoboData.getNumber("COG_Y", 0.0);
        }

    }

    @Override
    public void teleopPeriodicModule() {
        SmartDashboard.putNumber("Blob Count", blobCount);
        SmartDashboard.putNumber("Image Width", imageWidth);
        SmartDashboard.putNumber("Image Height", imageHeight);
        SmartDashboard.putNumber("Blob Width", blobWidth);
        SmartDashboard.putNumber("Blob Height", blobHeight);
        SmartDashboard.putNumber("blobLocX", blobLocX);
        SmartDashboard.putNumber("blobLocY", blobLocY);

    }

    @Override
    public void initModule() {

    }
}
