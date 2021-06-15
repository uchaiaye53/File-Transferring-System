package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        JFrame jFrame = new JFrame("Client");
        jFrame.setSize(600, 400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Send Box");
        title.setFont(new Font("Arial",Font.BOLD,30));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        jFrame.add(title);

        JLabel message = new JLabel("Select a file,,,");
        message.setFont(new Font("Aeial", Font.BOLD, 25));
        message.setBorder(new EmptyBorder(50, 0, 0, 0));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        jFrame.add(message);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton sendButton = new JButton("Upload");
        sendButton.setPreferredSize(new Dimension(140, 50));
        sendButton.setFont(new Font("Arial", Font.BOLD, 20));

        JButton choseButton = new JButton("Select File");
        choseButton.setPreferredSize(new Dimension(140, 50));
        choseButton.setFont(new Font("Arial", Font.BOLD, 20));

        jpButton.add(sendButton);
        jpButton.add(choseButton);
        jFrame.add(jpButton);

        final File fileToSend[] = new File[1];

        choseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JFileChooser jFileChooser=new JFileChooser();
               jFileChooser.setDialogTitle("All files which can be selected,,,");

               if(jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                   fileToSend[0]=jFileChooser.getSelectedFile();
                   message.setText("The file want to upload is:  "+"\n"+fileToSend[0].getName());
               }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileToSend[0]==null){
                    message.setText("Choose a file first,,,");
                }
                else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 1234);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String filename = fileToSend[0].getName();
                        byte[] fileNameByte = filename.getBytes();

                        byte[] fileContentByte = new byte[(int) fileToSend[0].length()];
                        fileInputStream.read(fileContentByte);

                        dataOutputStream.writeInt(fileNameByte.length);
                        dataOutputStream.write(fileNameByte);

                        dataOutputStream.writeInt(fileContentByte.length);
                        dataOutputStream.write(fileContentByte);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        jFrame.setVisible(true);

    }
}
