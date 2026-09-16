package com.itheima.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class GameFrame extends JFrame implements KeyListener ,ActionListener{
	JMenuItem replayItem=new JMenuItem("重新开始");
	JMenuItem reLoginItem=new JMenuItem("重新登录");
	JMenuItem exitItem=new JMenuItem("退出");
	JMenuItem accountItem=new JMenuItem("公众号");
	//全局步数统计：
	int step=0;
	//路径,方便后续的改动
	String path="/image/";
	//打乱图片，找到空白位置去移动
	int x=0;
	int y=0;
	//成员变量，多个成员方法需要
	int[][] win={
		{1,2,3,4},
		{5,6,7,8},
		{9,10,11,12},
		{13,14,15,0},
	};
	int[][] data=new int[4][4];
	public GameFrame() {
		//初始化界面
		initJFrame();

		//初始化菜单
		initJMenu();

		//初始化数据(打乱数据)
		initData();

		//初始化图片
		initImage();

		//监听器只能添加一遍，不然按一次，几个监听器同时响应

		//给界面对象添加键盘事件
		this.addKeyListener(this);

		//给对象专门添加动作监听
		replayItem.addActionListener(this);
		reLoginItem.addActionListener(this);
		exitItem.addActionListener(this);
		accountItem.addActionListener(this);
		//最后设置界面可以显示
		this.setVisible(true);
	}

	private void initData() {
		Random r = new Random();
		//一维数组0~15->4个一维数组添加到二维数组，打乱图片顺序
		int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		for (int i = 0; i < arr.length; i++) {
			int index = r.nextInt(arr.length);
			int tmp = arr[i];
			arr[i] = arr[index];
			arr[index] = tmp;
		}

		//解法一：遍历一维数组
		for (int i = 0; i < arr.length; i++) {
			if(arr[i]==0){
				x=i/4;
				y=i%4;
			}
			data[i / 4][i % 4] = arr[i];
		}

	}
	//按照二维数组的方式，添加图片到界面中，而不是自己手动的算x,y的坐标
	private void initImage() {
		//JLabel一旦创建内容，内容不会自动刷新，需要手动刷新

		//清除容器所有组件，只清理可视化部分，keyListener不会被移除
		this.getContentPane().removeAll();

		if(win()){
			//添加胜利图片：原图934x503，等比缩放到306x165，避免被拉伸变形
			//居中叠在完整图片上层：x=160+(306-306)/2=160，y=240+(306-165)/2≈310

			JLabel winLabel=new JLabel(changeImage(getClass().getResource(path+"win.jpg"),306,165));
			winLabel.setBounds(160,310,306,165);
			this.getContentPane().add(winLabel);
		}
		//在容器中添加组件
		JLabel jstepLabel=new JLabel("步数"+step);
		jstepLabel.setBounds(10,10,40,20);
		this.getContentPane().add(jstepLabel);

		//根据data[][]数组放置图片
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				//计算当前格子的编号：0~15，共16格
				//编号0的格子作为空白格，不添加图片
				if(data[i][j]==0){
					continue;
				}
				//创建图片对象，获取图片路径
				//绝对路径：从盘符开始,C:\ D:\
				//相对路径：相对当前项目而言，在当前项目下一层层寻找,aaa\\bbb

				//把图片等比缩放成与背景图灰色区域网格匹配的76x76，再创建新的图片对象
				//Image是抽象类，通过ImageIcon获取图片对象，存放图像数据，调用getScaledInstance()方法

				//创建图片管理容器
				//每一个图片管理容器对应一个图片
				//ImageIcon ,default class，方便给Java swing组件设置图片
				JLabel jLabel=new JLabel(changeImage(getClass().getResource(path+data[i][j]+".jpg"),76,76));

				//设置宽高，位置：起点(158,240)为背景图灰色区域内网格的左上角，每格76x76
				jLabel.setBounds(158+j*76,240+i*76,76,76);

				//给图片加边框,凸起来，凹下去
				//jLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
				jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));

				//添加到界面中
				this.getContentPane().add(jLabel);
				//JFrame是窗口框 getContentPane()是存放内容面板，add()是添加组件到容器中
			}
		}


		//先添加的显示在最上方

		//创建图片管理容器
		JLabel background=new JLabel(changeImage(getClass().getResource(path+"16.png"),580,580));
		//设置宽高，位置
		background.setBounds(20,40,580,580);
		//添加到界面中
		this.getContentPane().add(background);


		//刷新容器
		this.getContentPane().repaint();

	}

	private void initJMenu() {
		JMenuBar jMenuBar=new JMenuBar();

		//菜单条目
		JMenu functionJMenu=new JMenu("功能");
		JMenu aboutJMenu=new JMenu("关于我们");

		//菜单细则
		//多个方法需要，添加到成员变量中

		//添加条目到选项中
		functionJMenu.add(replayItem);
		functionJMenu.add(reLoginItem);
		functionJMenu.add(exitItem);
		aboutJMenu.add(accountItem);

		//添加选项到菜单条中
		jMenuBar.add(functionJMenu);
		jMenuBar.add(aboutJMenu);

		//菜单条添加到界面中
		this.setJMenuBar(jMenuBar);
	}


	private void initJFrame() {
		//设置界面的宽高
		this.setSize(620, 700);
		//设置界面标题
		this.setTitle("拼图游戏 v1.0");
		//设置界面置顶
		this.setAlwaysOnTop(true);
		//设置界面居中
		this.setLocationRelativeTo(null);
		//设置界面关闭模式
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//获取隐藏容器,取消默认的居中方式，JLabel也在容器中
		this.setLayout(null);
		//完整：this.getContentPane().setLayout(null);先获取再设置，设置背景颜色必须这样
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		//快捷键，一定要用英文格式下的，否则没用
		int keyCode = e.getKeyCode();
		if(keyCode==65){
			//先删除所有图片
			this.getContentPane().removeAll();
			//添加图片
			JLabel jLabel=new JLabel(changeImage(getClass().getResource(path+"all.jpg"),306,306));
			jLabel.setBounds(160,240,306,306);
			this.getContentPane().add(jLabel);


			//创建图片管理容器
			JLabel background=new JLabel(changeImage(getClass().getResource(path+"16.png"),580,580));
			//设置宽高，位置
			background.setBounds(20,40,580,580);
			//添加到界面中
			this.getContentPane().add(background);

			//刷新图片
			this.getContentPane().repaint();      // 再重新绘制
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(win()){
			//否则仍要执行keyReleased
			//用处：结束当前方法
			return;
		}
	//释放即移动
		int keyCode = e.getKeyCode();
		if(keyCode==38) {
			System.out.println("上");
			if(x==3){
				return;
			}
			data[x][y] = data[x + 1][y];
			data[++x][y] = 0;
			step++;
			//改变完data[][]数组后要去调用initImage()方法，重新生成图片
			initImage();
//			通过二维数组里面的内容去移动界面，二维数组的位置做移动
		}else if(keyCode==40){
			System.out.println("下");
			if(x==0){
				return;
			}
			data[x][y]=data[x-1][y];
			data[--x][y]=0;
			step++;
			initImage();
		}else if(keyCode==37){
			System.out.println("左");
			if(y==3){
				return;
			}
			data[x][y] = data[x][y + 1];
			data[x][++y] = 0;
			step++;
			initImage();
		}else if(keyCode==39){
			System.out.println("右");
			if(y==0){
				return;
			}
			data[x][y] = data[x][y - 1];
			data[x][--y] = 0;
			step++;
			initImage();
		}else if(keyCode==65){
			initImage();
		}else if(keyCode==87){
			//win();
			//更好的方法：呈现完整图片，不要再清理，正确的二维数组产生对应图片
			data= new int[][]{
				{1, 2, 3, 4},
				{5, 6, 7, 8},
				{9, 10, 11, 12},
				{13, 14, 15, 0},
			};
			initImage();
	}
	}

	private boolean win(){
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if(data[i][j]!=win[i][j])
					return false;
			}
		}
					return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source==replayItem){
			//重新开始
			step=0;
			//要重新打乱数据
			initData();
			initImage();
		}else if(source==reLoginItem){
			//重新登录
			this.setVisible(false);//关闭当前界面
			new LoginFrame();//创建新的登录界面
		}else if(source==exitItem){
			//退出游戏
			System.exit(0);
		}else if(source==accountItem){
			//新建一个弹窗
			JDialog jDialog=new JDialog();
			//新建图片
			JLabel jLabel=new JLabel(changeImage(getClass().getResource(path+"aboutus.jpg"),200,200));
			//先后不用管，因为在哪里决定x,y轴从哪儿算
			//设置图片位置大小
			jLabel.setBounds(0,0,200,200);
			//添加到弹窗中
			jDialog.add(jLabel);
			//设置弹窗大小
			jDialog.setSize(344,344);
			//设置弹窗居中
			jDialog.setLocationRelativeTo(null);
			//设置弹窗置顶
			jDialog.setAlwaysOnTop(true);
			//设置弹窗显示后，下面不许动
			jDialog.setModal(true);
			//设置弹窗可见
			jDialog.setVisible(true);
		}
	}
	private ImageIcon changeImage(URL path, int width, int height){
		ImageIcon icon=new ImageIcon(path);
		Image image=icon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH);
		icon.setImage(image);
		return icon;
	}
}