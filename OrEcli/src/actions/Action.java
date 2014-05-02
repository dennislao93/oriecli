package actions;

import game.Game;

public abstract class Action {

	protected String[] args;
	private String argsCSV;

	public Action(String[] args) {
		this.args = args;
		argsCSV = "";
		for (int i = 0; i < args.length; i++) {
			argsCSV += args[i] + ",";
		}
	}

	public abstract void processAction(Game game);

	public abstract String getHeader();

	public String toString() {
		return getHeader() + ":" + argsCSV;
	}

	public static Action instantiate(String header, String[] args) {
		args[args.length - 1] = args[args.length - 1].trim();
		if (header.equals("pse")) {
			return new Pause(args);
		} else if (header.equals("rsm")) {
			return new Resume(args);
		} else if (header.equals("pseC")) {
			return new PauseClient(args);
		} else if (header.equals("rsmC")) {
			return new ResumeClient(args);
		} else if (header.equals("fft")) {
			return new Forfeit(args);
		} else if (header.equals("fftC")) {
			return new ForfeitClient(args);
		} else if (header.equals("bdr")) {
			return new BuildRoad(args);
		} else if (header.equals("rmr")) {
			return new RemoveRoad(args);
		}
		return null;
	}

}
