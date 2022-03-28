package simulator.view;

import java.awt.Component;

import simulator.control.Controller;

public class ControlPanel extends Component {
	Controller _ctrl;
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
	}
	//toolBarExample
	//ejemploJFileChooser
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
		try {
		_ctrl.run(1);
		} catch (Exception e) {
		// TODO show error message
		_stopped = true;
		return;
		}
		SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
		run_sim(n - 1);
		}
		});
		} else {
		enableToolBar(true);
		_stopped = true;
		}
		}
		private void stop() {
		_stopped = true;
		}


}
