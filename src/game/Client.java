package game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import util.Drawable;
import util.KeyInput;
import weapon.BasicMissile;

public class Client implements Runnable {
	private String hostName;					//IP of host
//	private int port;							//Port of host
	private Socket socket;						//Socket of the connection
//	InputStream in;				//Input
//	OutputStream out;			//Output
	private ObjectInputStream clientIn;
	private ObjectOutputStream clientOut;
	private Thread clientThread;		//Thread for reading
	private HashSet<Drawable> drawables;
	private int index;
	
	public Client(String hostname, int port){
		this.hostName = hostname;
		drawables = new HashSet<Drawable>();
//		this.port = port;
		//Socketing
		InetAddress hostIP = null;
		try {
			hostIP = InetAddress.getByName(hostName);
			System.out.println("Creating socket");
			socket = new Socket(hostIP, port);
			socket.setTcpNoDelay(true);
			socket.setPerformancePreferences(0, 1, 0);
			System.out.println("Creating output");
			clientOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			clientOut.flush();
			System.out.println(clientOut);
			System.out.println("creating input");
			clientIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			System.out.println("Created everything");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientThread = new Thread(this);
	}

	@Override
	public void run() {
		while (true) {
			this.read();
		}
	}
	
	public void sendInputs(KeyInput keyinputs){
		try {
			clientOut.writeObject(keyinputs);
			clientOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void read(){
		try {
			Object o = clientIn.readObject();
			int i = clientIn.readInt();
			index = i;
			drawables = (HashSet<Drawable>) o;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startThread(){
		clientThread.start();
	}
	
	public HashSet<Drawable> getGame() {
		return drawables;
	}
	
	public int getIndex() {
		return index;
	}
}
