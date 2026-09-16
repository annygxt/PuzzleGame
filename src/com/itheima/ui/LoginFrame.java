package com.itheima.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class LoginFrame extends JFrame implements ActionListener, MouseListener {
	String tip="";
	private static final int USER_EMPTY=0;
	private static final int USER_NOT_EXIST=1;
	private static final int RIGHT_USER=2;
	private static final int USER_ERROR=3;
	//相对路径：相对于运行程序的工作目录(项目根目录D:\develop\IdeaProjects)，所以要带模块名PuzzleGame，与GameFrame保持一致
	String path="/image1/";

	//必须放到成员位置，才能被所有方法共享
	ArrayList<User> users=new ArrayList<>();
	JTextField userJTextField=new JTextField();
	JPasswordField passwordJPasswordField=new JPasswordField();

	//生成的验证码只有一个，方便上下文比对
	String code=generateCode();
	JTextField codeJTextField=new JTextField();
	JButton codeButton =new JButton();


	JButton LoginButton=new JButton();
	JButton registerButton=new JButton();
	public LoginFrame(){
		initFrame();
		initImage();
		initUser();

		//不要把设置界面可视化写在initFrame()中，否则很多都没有显示
		//最后设置界面可见：等所有组件都添加完毕再显示，避免窗口先显示、后添加的组件来不及绘制
		this.setVisible(true);
	}

	//******
	//初始化默认用户：类体中不能直接写执行语句，所以要包成方法，由构造方法调用
	private void initUser() {
		users.add(new User("heima","123"));
	}

	private int  isRightUser(String username,String password) {
		boolean exist=false;
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			if(username.equals("")|password.equals("")) {
				tip = "用户名或密码不能为空";
				return USER_EMPTY;
			}
			if(username.equals(user.getName())){
				exist=true;
			}
		}
		if(!exist) {
			tip = "用户不存在，请先注册";
			return USER_NOT_EXIST;
		}
		for (int i = 0; i < users.size(); i++) {
			if(!(users.get(i).getName().equals(username)&&users.get(i).getPassword().equals(password))){
				tip="用户名或密码错误";
				return USER_ERROR;
			}
		}
				return RIGHT_USER;

	}

	private void initImage() {

		//用户名
		JLabel userJLabel=new JLabel("用户名");
		userJLabel.setBounds(100, 130, 100, 30);
		this.getContentPane().add(userJLabel);

		//密码
		JLabel passwordJLabel=new JLabel("密码");
		passwordJLabel.setBounds(100, 170, 100, 30);
		this.getContentPane().add(passwordJLabel);

		//验证码
		JLabel codeJLabel=new JLabel("验证码");
		codeJLabel.setBounds(100, 210, 100, 30);
		this.getContentPane().add(codeJLabel);

		//用户名输入框
		userJTextField.setBounds(150, 130, 200, 30);
		this.getContentPane().add(userJTextField);

		//密码输入框
		passwordJPasswordField.setBounds(150, 170, 200, 30);
		this.getContentPane().add(passwordJPasswordField);

		//验证码输入框
		codeJTextField.setBounds(150, 210, 200, 30);
		this.getContentPane().add(codeJTextField);

		//随机验证码
		codeButton.setText(code);
		codeButton.addActionListener(this);
		codeButton.setBounds(360, 210, 80, 30);
		this.getContentPane().add(codeButton);

		//登录
		LoginButton.setIcon(changeImage(getClass().getResource(path+"2.jpg"),128,47));
		LoginButton.setBounds(90, 300, 128, 47);
		//JLabel无法使用addActionListener，需要使用按钮
		LoginButton.addActionListener(this);
		LoginButton.addMouseListener(this);
		this.getContentPane().add(LoginButton);

		//注册
		registerButton.setIcon(changeImage(getClass().getResource(path+"3.jpg"),128,47));
		registerButton.setBounds(240, 300, 128, 47);
		registerButton.addMouseListener(this);
		registerButton.addActionListener(this);
		this.getContentPane().add(registerButton);

		//添加背景图片
		JLabel background=new JLabel(changeImage(getClass().getResource(path+"1.jpg"),470,390));
		background.setBounds(0,0,470,390);
		this.getContentPane().add(background);

	}

	private void initFrame() {
		//设置界面大小
		this.setSize(488,430);
		//设置界面标题
		this.setTitle("拼图 登录");
		//设置界面置顶
		this.setAlwaysOnTop(true);
		//设置页面居中
		this.setLocationRelativeTo(null);
		//设置界面关闭模式
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//取消默认居中
		this.setLayout(null);
	}
	private ImageIcon changeImage(URL path, int width, int height) {
		ImageIcon icon=new ImageIcon(path);
		Image image=icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH);
		icon.setImage(image);
		return icon;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source==registerButton){
			System.out.println("注册");
		}else if(source==LoginButton) {
			if(!codeJTextField.getText().equalsIgnoreCase(code)){
				//验证码错误，出现弹窗提示
				showjDialog("验证码错误");
			}else{
				if(isRightUser(userJTextField.getText(), passwordJPasswordField.getText())==2){
					this.setVisible(false);
					new GameFrame();
				}else {
					showjDialog(tip);
				}
			}
		}else if(source==codeButton){
			//生成新的验证码
			code=generateCode();
			codeButton.setText(code);
		}
	}
	private String generateCode(){
		StringBuilder sb=new StringBuilder();
		Random r=new Random();
		char[] c=new char[52];
		for (int i = 0; i < 26; i++) {
			c[i]=(char)('a'+i);
		}
		for (int i = 26; i < c.length; i++) {
			c[i]=(char)('A'+i-26);
		}
		for (int i = 0; i <4; i++) {
			int index=r.nextInt(c.length);
			sb.append(c[index]);
		}
		sb.append(r.nextInt(10));
		char[] code=sb.toString().toCharArray();
		for (int i = 0; i < code.length; i++) {
			int index=r.nextInt(code.length);
			char tmp=code[index];
			code[index]=code[i];
			code[i]=tmp;
		}
		return new String(code);
	}
	private void showjDialog(String message){
		//JDialog没有只接收一个String的构造方法：JDialog构造方法里的String参数代表标题，不是弹窗内容
		//所以先用无参构造创建弹窗，再把提示文字放进JLabel中显示
		JDialog jDialog=new JDialog();
		//设置弹窗大小
		jDialog.setSize(200,150);
		//设置弹窗置顶
		jDialog.setAlwaysOnTop(true);
		//设置弹窗居中显示
		jDialog.setLocationRelativeTo(null);
		//设置弹窗为模态：不关闭弹窗就无法操作下面的界面
		jDialog.setModal(true);
		//创建JLabel承载提示文字，并添加到弹窗的内容面板中
		JLabel messageJLabel=new JLabel(message);
		messageJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageJLabel.setBounds(0,0,200,150);
		jDialog.getContentPane().add(messageJLabel);
		//等所有组件添加完毕后再显示弹窗，避免内容来不及绘制
		jDialog.setVisible(true);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Object source = e.getSource();
		if(source==registerButton){
			registerButton.setIcon(changeImage(getClass().getResource(path+"5.jpg"),128,47));
		}else if(source==LoginButton) {
			//图片变换
			LoginButton.setIcon(changeImage(getClass().getResource(path+"4.jpg"),128,47));
		}


	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Object source = e.getSource();
		if(source==registerButton){
			registerButton.setIcon(changeImage(getClass().getResource(path+"3.jpg"),128,47));
		}else if(source==LoginButton) {
			//图片变换
			LoginButton.setIcon(changeImage(getClass().getResource(path+"2.jpg"),128,47));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
