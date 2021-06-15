package server;

import javax.imageio.IIOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static ArrayList<Files> files = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        int fileNumber=0;

        JFrame jFrame=new JFrame("Server");
        jFrame.setSize(600,400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel=new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
        JScrollPane jScrollPane=new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel title=new JLabel("Received files");
        title.setBorder(new EmptyBorder(20,0,10,0));
        title.setFont(new Font("Arial",Font.BOLD,25));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jPanel);
        jFrame.add(title);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        ServerSocket serverSocket=new ServerSocket(1234);

        while (true){
            try {
                Socket socket=serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                int fileNameLength = dataInputStream.readInt();
                if(fileNameLength>0){
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes,0,fileNameBytes.length);
                    String fileName=new String(fileNameBytes);

                    int fileContentLength = dataInputStream.readInt();
                    if(fileContentLength>0){
                        byte[] fileContentBytes=new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes,0,fileContentBytes.length);

                        JPanel jpfile=new JPanel();
                        jpfile.setLayout(new BoxLayout(jpfile,BoxLayout.Y_AXIS));

                        JLabel jlfile=new JLabel(fileName);
                        jlfile.setFont(new Font("Arial",Font.BOLD,20));
                        jlfile.setBorder(new EmptyBorder(10,0,10,0));

                        if(getFileExtension(fileName).equalsIgnoreCase("text here")){
                            jpfile.setName(String.valueOf(fileNumber));
                            jpfile.addMouseListener(getMouseListener());
                            jpfile.add(jlfile);
                            jPanel.add(jpfile);
                            jFrame.validate();
                        }
                        else {
                            jpfile.setName(String.valueOf(fileNumber));
                            jpfile.addMouseListener(getMouseListener());

                            jpfile.add(jpfile);
                            jPanel.add(jpfile);
                            jFrame.validate();
                        }
                        files.add(new Files(fileNumber,fileName,fileContentBytes,getFileExtension(fileName)));
                    }
                }
            }catch (IIOException e2){
                e2.printStackTrace();
            }
        }
    }

    private static MouseListener getMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                int fileNumber =Integer.parseInt(jPanel.getName());
                for (Files files: files){
                    if(files.getFileNumber()==fileNumber){
                        JFrame jFrame2= creatFrame(files.getFilenames(),files.getFileData(),files.getFileExtension());
                        jFrame2.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    public static JFrame creatFrame(String filename,byte[] fileData,String fileExtension){
        JFrame jFrame = new JFrame("file downloader");
        jFrame.setSize(600,400);

        JPanel jPanel=new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));

        JLabel title2 = new JLabel("File Downloader");
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setFont(new Font("Arial",Font.BOLD,30));
        title2.setBorder(new EmptyBorder(20,0,10,0));

        JLabel message = new JLabel("Are you sure download the file:  "+filename);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setFont(new Font("Arial",Font.BOLD,25));
        message.setBorder(new EmptyBorder(20,0,10,0));

        JButton jButton=new JButton("YES");
        jButton.setPreferredSize(new Dimension(140,50));
        jButton.setFont(new Font("Arial",Font.BOLD,20));

        JButton jButton2=new JButton("NO");
        jButton2.setPreferredSize(new Dimension(140,50));
        jButton2.setFont(new Font("Arial",Font.BOLD,20));

        JLabel jLabel=new JLabel();//file contend
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton=new JPanel();
        jpButton.setBorder(new EmptyBorder(20,0,10,0));
        jpButton.add(jButton);
        jpButton.add(jButton2);

        if(fileExtension.equalsIgnoreCase("text")){
            jLabel.setText("<html>"+new String(fileData)+"</html>");
        }
        else {
            jLabel.setIcon(new ImageIcon(fileData));
        }

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileDownload =new File(filename);
                try{
                    FileOutputStream fileOutputStream=new FileOutputStream(fileDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();
                    jFrame.dispose();
                }catch (IIOException | FileNotFoundException e1){
                    e1.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
            }
        });
        jPanel.add(title2);
        jPanel.add(message);
        jPanel.add(jLabel);
        jPanel.add(jpButton);

        jFrame.add(jPanel);

        return jFrame;
    }

    public static String getFileExtension(String filename){
        int li=filename.lastIndexOf('.');
        if(li>0){
            return filename.substring(li+1);
        }
        else {
            return "Has no Extension";
        }
    }

}
