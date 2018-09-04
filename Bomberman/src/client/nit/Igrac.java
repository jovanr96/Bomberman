package client.nit;

public class Igrac {
	private String username;
	private int pozX, pozY, pixX, pixY;
	private boolean ziv;
	private boolean mozeBomba;
	private int brPobeda;
	public Igrac(String username, int pozX, int pozY, int pixX, int pixY, boolean ziv, boolean mozeBomba, boolean dodatnaBomba) {
		this.username = username;
		this.pozX = pozX;
		this.pozY = pozY;
		this.pixX = pixX;
		this.pixY = pixY;
		this.ziv = ziv;
		this.mozeBomba = mozeBomba;
		this.dodatnaBomba = dodatnaBomba;
		brPobeda = 0;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getBrPobeda() {
		return brPobeda;
	}

	public void setBrPobeda(int brPobeda) {
		this.brPobeda = brPobeda;
	}

	private boolean dodatnaBomba;

	public int getPozX() {
		return pozX;
	}

	public void setPozX(int pozX) {
		this.pozX = pozX;
	}

	public int getPozY() {
		return pozY;
	}

	public void setPozY(int pozY) {
		this.pozY = pozY;
	}

	public int getPixX() {
		return pixX;
	}

	public void setPixX(int pixX) {
		this.pixX = pixX;
	}

	public int getPixY() {
		return pixY;
	}

	public void setPixY(int pixY) {
		this.pixY = pixY;
	}

	public boolean isZiv() {
		return ziv;
	}

	public void setZiv(boolean ziv) {
		this.ziv = ziv;
	}

	public boolean isMozeBomba() {
		return mozeBomba;
	}

	public void setMozeBomba(boolean mozeBomba) {
		this.mozeBomba = mozeBomba;
	}

	public boolean isDodatnaBomba() {
		return dodatnaBomba;
	}

	public void setDodatnaBomba(boolean dodatnaBomba) {
		this.dodatnaBomba = dodatnaBomba;
	}

}
