package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import controller.BuildModeButtonListener;
import controller.BuildModeMouseListener;
import controller.LoadFileListener;
import controller.PlayModeKeyListener;
import controller.SaveFileListener;
import model.IMainEngine;

public class GameWindow implements IGameWindow {

	/* GUI components */
	private JFrame gameWindow;
	private JPanel sidebarPanel;
	private BuildMenu buildmenu;
	private PlayMenu playmenu;
	private GameBoard board;
	
	private JLabel coords;
	private JTextArea actionTipsTextArea;

	/* Controllers */
	/** Just there for New Board command **/
	private BuildModeButtonListener buildModeAL;
	private LoadFileListener loadFileAL;
	private SaveFileListener saveFileAL;

	/* Model */
	private IMainEngine model;

	/**
	 * Initialize the contents of the frame.
	 */
	public GameWindow(IMainEngine m) {
		model = m;
		loadFileAL = new LoadFileListener(this, model);
		saveFileAL = new SaveFileListener(this, model);

		initialiseBuildWindow();
	}

	private void initialiseBuildWindow() {
		gameWindow = new JFrame("Build Mode");
		gameWindow.setBounds(100, 100, 750, 500);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		IBuildMenu iBuildMenu = buildmenu;			// must be passed as interface instead to hide implementation
		buildModeAL = new BuildModeButtonListener(model, this, iBuildMenu);

		// start of drop menu
		JMenuBar menuBar = new JMenuBar();
		gameWindow.setJMenuBar(menuBar);

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
		
		JMenuItem mntmReset = new JMenuItem("New Board");
		mntmReset.setActionCommand("resetBoard");
		mntmReset.addActionListener(buildModeAL);
		mnFolio.add(mntmReset);


		JSeparator separator = new JSeparator();
		mnFolio.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFolio.add(mntmExit);
		gameWindow.getContentPane().setLayout(new FlowLayout());
		// end of menu bar

		JSeparator separator1 = new JSeparator();
		gameWindow.add(separator1);


		sidebarPanel = new JPanel(new CardLayout());

		buildmenu = new BuildMenu(model, this);
		playmenu = new PlayMenu(model, this);

		sidebarPanel.add(buildmenu.getMenu(), "Build Mode");
		sidebarPanel.add(playmenu.getMenu(), "Play Mode");


		board = new GameBoard(model, buildmenu, this);
		BuildModeMouseListener l = new BuildModeMouseListener(board, model, buildmenu, this);
		board.addMouseListener(l);
		board.addMouseMotionListener(l);
		
		board.addKeyListener(new PlayModeKeyListener(model));

		gameWindow.add(sidebarPanel);
		gameWindow.add(new JSeparator());
		gameWindow.add(board);

		JLabel tips = new JLabel("Action Tip:");
		tips.setFont(new Font("Arial", 1, 12));
		tips.setForeground(Color.BLUE);
		gameWindow.add(tips);

		actionTipsTextArea = new JTextArea(1, 45);
		actionTipsTextArea.setBackground(Color.WHITE);
		actionTipsTextArea.setEditable(false);
		actionTipsTextArea.setFocusable(false);
		gameWindow.add(actionTipsTextArea);

		coords = new JLabel("X: 100 (10), Y: 100 (10)");
		gameWindow.add(coords);

		gameWindow.setVisible(true);
	}

	@Override
	public void setMode(String mode) {
		CardLayout cl = (CardLayout) sidebarPanel.getLayout();
		cl.show(sidebarPanel, mode);

		gameWindow.setTitle(mode);
		updateCoordsLabel(0, 0);
	}

	public void updateCoordsLabel(int x, int y) {
		if (gameWindow.getTitle().equals("Build Mode")) {
			String xP = String.format("%03d", x);
			String yP = String.format("%03d", y);
			String xG = String.format("%02d", x / model.getLInPixels());
			String yG = String.format("%02d", y / model.getLInPixels());

			coords.setText("X: " + xG + " (" + xP + "), Y: " + yG + " (" + yP + ")");
		} else {
			coords.setText("");
		}
	}

	@Override
	public Point getCoords() {
		Point p = new Point();
		return p;
	}

	@Override
	public String getFile(String buttonText, String lastLocation) {
		JFileChooser f = new JFileChooser();
		
		// set JFileChooser to user's last location, for quickness
		if(lastLocation != null){
			File fileLoc = new File(lastLocation);
			f.setCurrentDirectory(fileLoc);
		}
		f.showDialog(gameWindow, buttonText);

		File file = f.getSelectedFile();

		if (file != null) {
			return file.getAbsolutePath();
		} else {
			return null;
		}
	}

	@Override
	public void setActionTipsTextArea(String message){
		actionTipsTextArea.setText(message);
	}
}