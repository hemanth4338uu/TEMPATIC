import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class TemperatureController {
    private SerialPort serialPort;

    public void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.err.println("Port is currently in use");
        } else {
            serialPort = (SerialPort) portIdentifier.open(this.getClass().getName(), 2000);

            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            InputStream input = serialPort.getInputStream();
            OutputStream output = serialPort.getOutputStream();

            (new Thread(new SerialReader(input))).start();
        }
    }

    public void setTargetTemperature(float temperature) throws IOException {
        serialPort.getOutputStream().write(("SET_TEMP " + temperature + "\n").getBytes());
    }

    public static void main(String[] args) {
        TemperatureController controller = new TemperatureController();

        try {
            controller.connect("/dev/ttyACM0"); // Replace with your Arduino's serial port
            controller.setTargetTemperature(25.0); // Set your desired temperature
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SerialReader implements Runnable {
        private InputStream input;

        public SerialReader(InputStream input) {
            this.input = input;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int len;

            try {
                while ((len = this.input.read(buffer)) > -1) {
                    System.out.print(new String(buffer, 0, len));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
