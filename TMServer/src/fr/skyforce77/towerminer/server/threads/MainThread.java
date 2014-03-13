package fr.skyforce77.towerminer.server.threads;

import fr.skyforce77.towerminer.server.match.MatchManager;


public class MainThread extends Thread{

	@Override
	public void run() {
		while(!isInterrupted()) {
			for(Integer i : MatchManager.getMatchs().keySet()) {
				MatchManager.getMatchs().get(i).onTick();
			}
			try {
				Thread.sleep(10l);
			} catch (InterruptedException e) {}
		}
	}
}
