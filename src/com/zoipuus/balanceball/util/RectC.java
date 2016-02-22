package com.zoipuus.balanceball.util;

public class RectC {

	public int left;
	public int top;
	private int right;
	private int bottom;
	public int width;
	public int height;

	public RectC(int left, int top, int width, int height) {
		// TODO Auto-generated constructor stub
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height - 5;
		this.right = this.left + width;
		this.bottom = this.top + this.height;
	}

	public Boolean isInside(float x, float y) {
		if ((this.left <= x) && (x <= this.right) && (this.top <= y)
				&& (y <= this.bottom)) {
			return true;
		} else {
			return false;
		}
	}
}
