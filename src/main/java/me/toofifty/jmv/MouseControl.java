package me.toofifty.jmv;

/**
 * Simple mouse follower
 * 
 * @author Toofifty
 *
 */
public class MouseControl {
	
	/* 'Last' positions */
	private int lx;
	private int ly;
	
	/* Current positions */
	private int mx;
	private int my;
	
	/**
	 * Update positions
	 * 
	 * @param mx
	 * @param my
	 */
	public void update(int mx, int my) {
		this.lx = this.mx;
		this.ly = this.my;
		this.mx = mx;
		this.my = my;
	}
	
	/**
	 * Return dx since last update
	 * 
	 * @return dx
	 */
	public float dx() {
		return mx - lx;
	}
	
	/**
	 * Return dy since last update
	 * 
	 * @return dy
	 */
	public float dy() {
		return my - ly;
	}

}
