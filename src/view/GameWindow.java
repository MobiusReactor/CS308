package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import controller.BuildModeMouseListener;
import controller.LoadFileListener;
import controller.SaveFileListener;
import model.IMainEngine;
import model.ISaveDataEngine;

public class GameWindow implements IGameWindow {
	private JFrame window1, window2;

	private IMenu buildmenu, playmenu;
	private BuildBoard buildboard;
	// , playboard;
	private LoadFileListener loadFileAL;
	private SaveFileListener saveFileAL;
	private IMainEngine model;
	/* other GUI components */
	private PlayBoard gameBoard;
	private BuildBoard buildBoard;

	/**
	 * Initialize the contents of the frame.
	 */
	public GameWindow(IMainEngine m, ISaveDataEngine s) {
		model = m;

		loadFileAL = new LoadFileListener(this, s);
		saveFileAL = new SaveFileListener(m);
		window1 = new JFrame("Play Mode");
		window1.setBounds(100, 100, 720, 600);
		window1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// start of drop menu
		JMenuBar menuBar = new JMenuBar();
		window1.setJMenuBar(menuBar);

		JMenu mnFolio = new JMenu("Game");
		menuBar.add(mnFolio);

		JMenuItem mntmOpen = new JMenuItem("Load");
		mntmOpen.setActionCommand("load");
		mntmOpen.addActionListener(loadFileAL);
		mnFolio.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setActionCommand("save");
		mntmSave.addActionListener(saveFileAL);
		mnFolio.add(mntmSave);

		JSeparator separator = new JSeparator();
		mnFolio.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFolio.add(mntmExit);
		window1.getContentPane().setLayout(new FlowLayout());
		// end of menu bar

		JSeparator separator1 = new JSeparator();
		window1.getContentPane().add(separator1);



		// TODO
		buildmenu = new BuildMenu();
		buildboard = new BuildBoard(m);

		BuildModeMouseListener l = new BuildModeMouseListener(buildboard, model);

		buildboard.addMouseListener(l);
//		buildboard.addMouseMotionListener(l);

		window1.add(buildmenu.getMenu());
		window1.add(buildboard);

		JLabel tips = new JLabel("Action Tip:");
		tips.setFont(new Font("Arial", 1, 12));
		tips.setForeground(Color.BLUE);
		window1.add(tips);

		JTextArea textarea = new JTextArea(1, 50);
		textarea.setBackground(Color.WHITE);
		textarea.setEditable(false);
		window1.add(textarea);

		window1.setVisible(true);
	}

	@Override
	public String getFile() {
		JFileChooser f = new JFileChooser();
		f.showOpenDialog(window1);

		File file = f.getSelectedFile();

		if (file != null) {
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}
}