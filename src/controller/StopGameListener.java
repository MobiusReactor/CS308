package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.IMainEngine;

public class StopGameListener implements ActionListener {
	private IMainEngine model;

	public StopGameListener(IMainEngine m) {
		model = m;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
